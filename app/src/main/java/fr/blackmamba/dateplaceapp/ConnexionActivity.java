package fr.blackmamba.dateplaceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;


public class ConnexionActivity extends AppCompatActivity {
    private Button button_goback;
    private Button button_connect;
    EditText email,password;
    String urlAdd = "https://dateplaceapp.000webhostapp.com/login.php";
    ConnexionActivity.GetDataAsyncTask GetData;
    String message;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        email = (EditText) findViewById(R.id.connexion_identifier);
        password = (EditText) findViewById(R.id.connexion_password);

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
                if ( email.getText().toString().equals("") || password.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                    builder.setMessage("Il manque mot de passe/email")
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }else{
                    int testemail = email.getText().toString().indexOf("@");
                    if (testemail != -1) {
                        GetData = new GetDataAsyncTask();
                        GetData.execute();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                        builder.setMessage("Vous n'avez pas entrer une adresse email :)")
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                    }
                }
            }
        });
    }

    public class GetDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");

            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<>(1);

            nameValuePair.add(new BasicNameValuePair("email", email.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("password", password.getText().toString()));

            String jsonStr = sh.makeServiceCall(urlAdd, ServiceHandler.POST, nameValuePair);

            Log.d("Response: ", jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getInt("success");
                    message = jsonObj.getString("message");
                    Log.i("success", String.valueOf(success));
                    Log.i("message", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("add", " end doInBackground");
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if (success==1){
                Intent gobefore = new Intent(ConnexionActivity.this, MapActivity.class);
                startActivityForResult(gobefore,100);
                finish();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                builder.setMessage("Connexion Failed");
                builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goback = new Intent(ConnexionActivity.this, ConnexionActivity.class);
                        startActivityForResult(goback,100);
                        finish();
                    }
                });
                builder.show();
            }
        }
    }
}
