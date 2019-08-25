package net.pkhapps.idispatch.core.mongodb.common;

import com.mongodb.client.MongoCollection;
import net.pkhapps.idispatch.core.domain.common.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;
import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 *
 * @param <ID>
 * @param <T>
 * @param <E>
 */
public abstract class MongoRepository<ID extends WrappedIdentifier, T extends AggregateRoot<ID>,
        E extends AggregateRoot.Essence<ID>> implements Repository<ID, T>, IdFactory<ID> {

    private static final String KEY_ID = "_id";
    private static final String KEY_VERSION = "_version";
    private static final String KEY_CREATED_ON = "_createdOn";
    private static final String KEY_LAST_MODIFIED_ON = "_lastModifiedOn";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Class<ID> idClass;
    private final Class<T> aggregateClass;
    private final Class<E> essenceClass;
    private final MongoCollection<Document> collection;

    /**
     * @param idClass
     * @param aggregateClass
     * @param essenceClass
     * @param collection
     */
    public MongoRepository(@NotNull Class<ID> idClass,
                           @NotNull Class<T> aggregateClass,
                           @NotNull Class<E> essenceClass,
                           @NotNull MongoCollection<Document> collection) {
        this.idClass = requireNonNull(idClass);
        this.aggregateClass = requireNonNull(aggregateClass);
        this.essenceClass = requireNonNull(essenceClass);
        this.collection = requireNonNull(collection);
    }

    /**
     * @return
     */
    protected final @NotNull MongoCollection<Document> getCollection() {
        return collection;
    }

    /**
     * @return
     */
    protected @NotNull E createEssence() {
        try {
            return essenceClass.getConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Could not create new essence object - please override createEssence()", ex);
        }
    }

    /**
     * @param source
     * @return
     */
    protected @NotNull E createEssence(@NotNull T source) {
        requireNonNull(source);
        try {
            return essenceClass.getConstructor(aggregateClass).newInstance(source);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create new essence object - please override createEssence(T)", ex);
        }
    }

    /**
     * @param essence
     * @return
     */
    protected @NotNull T createAggregate(@NotNull E essence) {
        requireNonNull(essence);
        try {
            return aggregateClass.getConstructor(essenceClass).newInstance(essence);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create new aggregate - please override createAggregate(E)");
        }
    }

    private void populateDocument(@NotNull Document document, @NotNull E essence) {
        document.append(KEY_ID, essence.getId().unwrap(ObjectId.class));
        document.append(KEY_VERSION, essence.getOptLockVersion());
        document.append(KEY_CREATED_ON, essence.getCreatedOn());
        document.append(KEY_LAST_MODIFIED_ON, essence.getLastModifiedOn());
        // Add additional metadata here in the future
        copyToDocument(essence, document);
    }

    /**
     * @param source
     * @param destination
     */
    protected abstract void copyToDocument(@NotNull E source, @NotNull Document destination);

    protected final @NotNull T createPojo(@NotNull Document document) {
        final var essence = createEssence();
        essence.setId(WrappedIdentifier.wrap(getIdClass(), document.getObjectId(KEY_ID)));
        essence.setOptLockVersion(document.getLong(KEY_VERSION));
        essence.setCreatedOn(document.get(KEY_CREATED_ON, Instant.class));
        essence.setLastModifiedOn(document.get(KEY_LAST_MODIFIED_ON, Instant.class));
        copyToEssence(document, essence);
        return createAggregate(essence);
    }

    /**
     * @param source
     * @param destination
     */
    protected abstract void copyToEssence(@NotNull Document source, @NotNull E destination);

    private @NotNull Class<ID> getIdClass() {
        return idClass;
    }

    @Override
    public final @NotNull Stream<T> findByIds(@NotNull Iterable<ID> ids) {
        var result = collection.find(in("_id", ids));
        return StreamSupport.stream(result.spliterator(), false).map(this::createPojo);
    }

    @NotNull
    @Override
    public final T save(@NotNull T aggregateRoot) {
        final var now = DomainContextHolder.get().clock().instant();
        final var insertingNew = aggregateRoot.optLockVersion() == 0;
        final var essence = createEssence(aggregateRoot);
        // Since we create the essence from the aggregate root and the aggregate root is always in a consistent
        // state, we don't need to perform any validation of the contents of the essence. We can safely assume
        // it is valid.
        essence.incrementOptLockVersion();
        essence.setLastModifiedOn(now);
        if (essence.getCreatedOn() == null) {
            essence.setCreatedOn(now);
        }
        final var document = new Document();
        populateDocument(document, essence);
        if (insertingNew) {
            // Insert new
            collection.insertOne(document);
            logger.debug("Inserted {} into collection {}", aggregateRoot, collection);
        } else {
            // Update existing
            var result = collection.replaceOne(
                    and(
                            eq("_id", aggregateRoot.id().unwrap()),
                            eq("_version", aggregateRoot.optLockVersion())
                    ), document);
            if (result.getModifiedCount() == 0) {
                throw new OptimisticLockingFailureException();
            }
            logger.debug("Updated {} in collection {}", aggregateRoot, collection);
        }
        return createPojo(document);
    }

    @Override
    public final void deleteById(@NotNull ID id) {
        collection.deleteOne(eq("_id", id.unwrap()));
        logger.debug("Deleted aggregate with ID {} from collection {}", id, collection);
    }

    @NotNull
    @Override
    public final ID createId() {
        return WrappedIdentifier.wrap(getIdClass(), new ObjectId());
    }
}
