package in.co.leaf.Croma.connection;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AirConfigServerApi {

    public JSONObject attendanceMarker(HashMap<String, Object> params){
        params = validate(params);
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlPOST("url" , params);
        return response;
    }


    //Utility
    public static String getUrlParamString(HashMap<String, Object> paramMap) {
        StringBuffer urlParam = new StringBuffer();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            urlParam.append(key + "=" + value + "&");
        }
        if (urlParam.length() > 0) {
            urlParam.deleteCharAt(urlParam.length() - 1);
        }
        return urlParam.toString();
    }

    private HashMap<String, Object> validate(HashMap<String, Object> paramMap) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        return paramMap;
    }
}
