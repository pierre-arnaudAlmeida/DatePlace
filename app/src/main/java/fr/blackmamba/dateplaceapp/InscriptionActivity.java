package fr.blackmamba.dateplaceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import fr.blackmamba.dateplaceapp.backgroundtask.InscriptionRequest;

public class InscriptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button button_goback;
    private Button button_inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Spinner goal = findViewById(R.id.newgoal);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.goal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goal.setAdapter(adapter);
        goal.setOnItemSelectedListener(this);

        final EditText name = (EditText) findViewById(R.id.new_prenom);
        final EditText last_name = (EditText) findViewById(R.id.new_nom);
        final EditText password = (EditText) findViewById(R.id.newmot_de_passe);
        final EditText email = (EditText) findViewById(R.id.newadressemail);
        final EditText date_de_naissance = (EditText) findViewById(R.id.newdate_de_naissance);
//        @SuppressLint("WrongViewCast") final EditText but = (EditText) findViewById(R.id.newgoal);


        setTitle("Inscription");
        this.button_goback=findViewById(R.id.button_goback);
        button_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(goback);
                finish();
            }
        });

        this.button_inscription=findViewById(R.id.button_inscrip);
        button_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String new_name = name.getText().toString();
                final String new_last_name = last_name.getText().toString();
                final String new_password = password.getText().toString();
                final String new_email = email.getText().toString();
                final String new_birthday = date_de_naissance.getText().toString();
                //final String new_goal = but.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success  = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent inscription = new Intent(getApplicationContext(),MapActivity.class);
                                startActivity(inscription);
                                finish();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);
                                builder.setMessage("Inscription Failed")
                                .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                InscriptionRequest inscriptionRequest = new InscriptionRequest(new_name, new_last_name,new_password,new_email,new_birthday, responseListener);
                RequestQueue queue = Volley.newRequestQueue(InscriptionActivity.this);
                queue.add(inscriptionRequest);

                //Intent inscription = new Intent(getApplicationContext(),MapActivity.class);
                //startActivity(inscription);
                //finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
