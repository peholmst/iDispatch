package net.pkhapps.idispatch.client.v3.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.pkhapps.idispatch.client.v3.base.gson.DomainObjectIdJsonTypeAdapter;
import net.pkhapps.idispatch.client.v3.type.ResourceId;
import net.pkhapps.idispatch.client.v3.type.ResourceTypeId;
import net.pkhapps.idispatch.client.v3.type.StationId;
import net.pkhapps.idispatch.client.v3.util.Color;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;
import net.pkhapps.idispatch.client.v3.util.PhoneNumber;
import net.pkhapps.idispatch.client.v3.util.gson.ColorJsonTypeAdapter;
import net.pkhapps.idispatch.client.v3.util.gson.MultilingualStringTypeAdapter;
import net.pkhapps.idispatch.client.v3.util.gson.PhoneNumberJsonTypeAdapter;

/**
 * Base class for objects that serialize to JSON using GSON.
 */
@SuppressWarnings("WeakerAccess")
public abstract class JsonObjectTest {

    private Gson gson;

    public JsonObjectTest() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorJsonTypeAdapter())
                .registerTypeAdapter(MultilingualString.class, new MultilingualStringTypeAdapter())
                .registerTypeAdapter(PhoneNumber.class, new PhoneNumberJsonTypeAdapter())
                .registerTypeAdapter(ResourceId.class, new DomainObjectIdJsonTypeAdapter<>(ResourceId::new))
                .registerTypeAdapter(ResourceTypeId.class, new DomainObjectIdJsonTypeAdapter<>(ResourceTypeId::new))
                .registerTypeAdapter(StationId.class, new DomainObjectIdJsonTypeAdapter<>(StationId::new))
                .setPrettyPrinting()
                .create();
    }

    protected Gson getGson() {
        return gson;
    }
}
