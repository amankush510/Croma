package in.co.leaf.Croma.connection;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPRequestHandler {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String KEY_API_VERSION = "X-API-VERSION";
    private final String KEY_USER_AGENT = "X-USER-AGENT";
    private final String KEY_APP_VERSION = "X-APP-VERSION";
    private final String KEY_IMEI = "IMEI";

    private boolean httpSuccess = true;
    public JSONObject responseObject = null;
    String response = "";
    private OkHttpClient client = new OkHttpClient();

    private static final int AUTH_FAILED = 401;
    public static final int CANNOT_CONNECT_SERVER = 11;
    public static final int ERROR_PARSING_DATA = 12;

    public JSONObject getJSONFromUrlPOST(String url, HashMap<String, Object> params) {
        return getJSONFromUrlPOST(url , params , "1");
    }

    public JSONObject getJSONFromUrlPOST(String url, HashMap<String, Object> params , String apiVersion) {
        httpSuccess = true;
        try {
            String bodyData = getPostDataString(params);
            RequestBody body = RequestBody.create(JSON, bodyData);
            Request.Builder requestBuilder = getRequestBuilder(apiVersion);
            requestBuilder.url(url);
            requestBuilder.post(body);
            Request request = requestBuilder.build();
            executeRequest(request, bodyData);
        } catch (Exception e) {
            e.printStackTrace();
            httpSuccess = false;
        }
        parseResponse();
        return responseObject;
    }

    public JSONObject getJSONFromUrlGET(String url, String params) {
        return getJSONFromUrlGET(url , params , "1");
    }

    public JSONObject getJSONFromUrlGET(String url, String params , String apiVersion) {
        httpSuccess = true;
        try {
            url += "?" + params;
            Request.Builder requestBuilder = getRequestBuilder(apiVersion);
            requestBuilder.url(url);
            Request request = requestBuilder.build();
            executeRequest(request, params);
        } catch (Exception e) {
            e.printStackTrace();
            httpSuccess = false;
        }
        parseResponse();
        return responseObject;
    }

    public byte[] getCBORFromUrlGET(String url, String params) {
        int bufSize = 4096;
        byte[] buffer = new byte[bufSize];
        httpSuccess = true;
        try {
            url += "?" + params;

            URL targetURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) targetURL.openConnection();
            conn.setRequestProperty(KEY_API_VERSION, "1");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            is.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            httpSuccess = false;
        }
        return buffer;
    }

    private String getPostDataString(HashMap<String, Object> params) throws Exception {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject.toString();
    }

    private Request.Builder getRequestBuilder(String apiVersion) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader(KEY_API_VERSION, "1");
        //requestBuilder.addHeader(KEY_USER_AGENT, AppConstant.USER_AGENT);
        requestBuilder.addHeader(KEY_APP_VERSION, apiVersion);
        requestBuilder.addHeader(KEY_IMEI, "2545a54d5a45d4a");
        return requestBuilder;
    }

    private void executeRequest(Request request, String params) {
        try {
            Log.d("ajay", "paramList:" + params.toString());
            Log.d("ajay", "request url:" + request.url());
            Response response = client.newCall(request).execute();
            this.response = response.body().string();
            checkForAuth(this.response);
        } catch (Exception e) {
            httpSuccess = false;
            e.printStackTrace();
        }
    }

    private void checkForAuth(String response) {
        try {
            responseObject = new JSONObject(response);
            if (responseObject.getInt("code") == AUTH_FAILED) {
                onAuthenticationFailed();
            }
        } catch (JSONException e) {
            try {
                onDataParsingError(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void parseResponse() {
        if (httpSuccess) {
            try {
                Log.d("ajay ", "row response:" + response);
                responseObject = new JSONObject(response);
            } catch (JSONException e) {
                try {
                    onDataParsingError(e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            try {
                serverConnectionFailure();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onDataParsingError(JSONException e) throws Exception {
        responseObject = new JSONObject();
        responseObject.put("code", ERROR_PARSING_DATA);
        responseObject.put("status", "false");
        responseObject.put("message", "Could not connect to server .Please try again later.");
    }

    private void serverConnectionFailure() throws Exception {
        responseObject = new JSONObject();
        responseObject.put("code", CANNOT_CONNECT_SERVER);
        responseObject.put("status", "false");
        responseObject.put("message", "Could not connect to server. Please try again later.");
    }

    private void onAuthenticationFailed() {
        //NEED TO WRITE CODE HERE
    }
}