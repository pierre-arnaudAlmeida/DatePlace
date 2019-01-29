package fr.blackmamba.dateplaceapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;

public class InscriptionActivity extends AppCompatActivity {
    private Button button_goback;
    private Button button_inscription;
    EditText name, last_name, password, email, date_de_naissance;
    String urlAdd = "https://dateplaceapp.000webhostapp.com/insert.php";
    AddDataAsyncTask AddData;
    String message;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Spinner goal = findViewById(R.id.newgoal);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.goal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goal.setAdapter(adapter);
        //goal.setOnItemSelectedListener(this);

        name = (EditText) findViewById(R.id.new_prenom);
        last_name = (EditText) findViewById(R.id.new_nom);
        password = (EditText) findViewById(R.id.newmot_de_passe);
        email = (EditText) findViewById(R.id.newadressemail);
        date_de_naissance = (EditText) findViewById(R.id.newdate_de_naissance);
//        @SuppressLint("WrongViewCast") final EditText but = (EditText) findViewById(R.id.newgoal);
        AddData = new AddDataAsyncTask();

        this.button_goback = findViewById(R.id.button_goback);
        button_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(goback);
                finish();
            }
        });

        this.button_inscription = findViewById(R.id.button_inscrip);
        button_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData.execute();
                Intent goback = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(goback);
                finish();
            }
        });
    }

    private class AddDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<>(1);

            nameValuePair.add(new BasicNameValuePair("user_id", name.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("password", password.getText().toString()));

            // Making a request to url and getting response
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
    }
}

