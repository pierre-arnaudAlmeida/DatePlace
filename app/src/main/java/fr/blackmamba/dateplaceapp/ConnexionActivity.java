package fr.blackmamba.dateplaceapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;


public class ConnexionActivity extends AppCompatActivity {
    private Button button_goback;
    private Button button_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        setTitle("Connexion");

        final EditText new_email = (EditText) findViewById(R.id.connexion_identifier);
        final EditText new_password = (EditText) findViewById(R.id.connexion_password);

        this.button_goback=findViewById(R.id.button_goback);
        button_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(goback);
                finish();
            }
        });
        this.button_connect=findViewById(R.id.button_connect);
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = new_email.getText().toString();
                final String password = new_password.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success){
                                Intent connect = new Intent(getApplicationContext(),UserProfilActivity.class);
                                startActivity(connect);
                                finish();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginResuqest = new LoginRequest(email, password, responseListener);
                //Intent connect = new Intent(getApplicationContext(),UserProfilActivity.class);
                //startActivity(connect);
                //finish();
            }
        });
    }
}
