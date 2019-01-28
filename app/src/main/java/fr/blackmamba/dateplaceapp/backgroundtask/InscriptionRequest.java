package fr.blackmamba.dateplaceapp.backgroundtask;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InscriptionRequest extends StringRequest {
    private static final String INSCRIPTION_REQUEST_URL = "https://dateplaceapp.000webhostapp.com/Inscription.php";
    private Map<String, String> params;

    public InscriptionRequest(String name, String last_name, String password, String email, String birthday, Response.Listener<String> listener){
        super(Method.POST, INSCRIPTION_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("new_last_name", last_name);
        params.put("new_name", name);
        params.put("new_password",password);
        params.put("new_email", email);
        params.put("new_birthday", birthday + "");
        params.put("new_goal", "zero");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
