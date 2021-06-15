package fr.arinonia.app.utils;

import java.util.Map;

public class RequestResponse {
    private int responseCode = -1;
    private Map<String, Object> data;

    public RequestResponse(int responseCode, Map<String, Object> data) {
        this.responseCode = responseCode;
        this.data = data;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
    public boolean isSuccessful(){
        return this.responseCode == 200 || this.responseCode == 204;
    }
    public Map<String, Object> getData() {
        return this.data;
    }
}
