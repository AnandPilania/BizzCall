package com.bizcall.wayto.mentebit13;

import org.json.JSONException;

public interface ServerCallback {
    void onSuccess(String response) throws JSONException;
}
