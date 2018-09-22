package net.pkhapps.idispatch.client.v3.util;

import com.google.gson.Gson;

/**
 * Base class for objects that serialize to JSON using GSON.
 */
@SuppressWarnings("WeakerAccess")
public abstract class JsonObjectTest {

    private Gson gson;

    public JsonObjectTest() {
        gson = new Gson();
    }

    protected Gson getGson() {
        return gson;
    }
}
