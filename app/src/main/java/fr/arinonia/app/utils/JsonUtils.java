package fr.arinonia.app.utils;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {

    public static Gson gson;
    public static Type stringObjectMap;
    private static final Map<String, Object> AGENT = new LinkedHashMap<String, Object>();

    static {
        gson = new Gson();
        stringObjectMap = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> agentValues = new LinkedHashMap<String, Object>();
        agentValues.put("name", "FFVL");
        agentValues.put("version", 1);
        AGENT.put("agent", agentValues);
    }

    public static String credentialsToJson(String prenom, String nom, String email, String password) {
        Map<String, Object> jsonData = new LinkedHashMap<String, Object>();
        jsonData.putAll(AGENT);
        jsonData.put("prenom", prenom);
        jsonData.put("nom", nom);
        jsonData.put("email", email);
        jsonData.put("password", password);
        return gson.toJson(jsonData);
    }

    public static String baliseFavToJson(String idBalise, String idUser) {
        Map<String, Object> jsonData = new LinkedHashMap<String, Object>();
        jsonData.put("idBalise", idBalise);
        jsonData.put("idUser", idUser);
        System.out.println(gson.toJson(jsonData));
        return gson.toJson(jsonData);
    }

}
