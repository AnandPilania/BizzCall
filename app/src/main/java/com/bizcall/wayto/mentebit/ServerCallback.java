package com.bizcall.wayto.mentebit;

import org.json.JSONException;

public interface ServerCallback {
    void onSuccess(String response) throws JSONException;
}
