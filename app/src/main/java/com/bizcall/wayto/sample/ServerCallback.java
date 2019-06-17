package com.bizcall.wayto.sample;

import org.json.JSONException;

public interface ServerCallback {
    void onSuccess(String response) throws JSONException;
}
