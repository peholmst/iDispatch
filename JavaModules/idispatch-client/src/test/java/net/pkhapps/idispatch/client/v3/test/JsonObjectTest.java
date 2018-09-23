package net.pkhapps.idispatch.client.v3.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.pkhapps.idispatch.client.v3.gson.GsonConfigurer;

/**
 * Base class for objects that serialize to JSON using GSON.
 */
@SuppressWarnings("WeakerAccess")
public abstract class JsonObjectTest {

    private Gson gson;

    public JsonObjectTest() {
        gson = new GsonConfigurer().configure(new GsonBuilder().setPrettyPrinting()).create();
    }

    protected Gson getGson() {
        return gson;
    }
}
