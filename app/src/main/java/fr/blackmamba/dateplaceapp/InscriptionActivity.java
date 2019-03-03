package fr.blackmamba.dateplaceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.blackmamba.dateplaceapp.backgroundtask.ConnexionInternet;
import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;
import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;

public class InscriptionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button button_goback;
    private Button button_inscription;
    private DatePickerDialog.OnDateSetListener date_de_naissance_Listener;
    private EditText name;
    private EditText last_name;
    private EditText password;
    private EditText email;
    private Button date_de_naissance;
    private Spinner but;
    private String urlAdd = "https://dateplaceapp.000webhostapp.com/insert_user.php";
    private String message,birthday,password_user;
    private int success;
    private int user_id;
    private int year;
    private int month;
    private int day;
    AddDataAsyncTask AddData;
    DatabaseHelper users =null;

    /**
     * Affiche l'activité Inscription
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        users = new DatabaseHelper(this);
        //Affectation de la liste déroulante
        Spinner goal = findViewById(R.id.newgoal);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.goal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goal.setAdapter(adapter);
        goal.setOnItemSelectedListener(this);

        //Affectation des champs à des variables
        name = (EditText) findViewById(R.id.new_prenom);
        last_name = (EditText) findViewById(R.id.new_nom);
        password = (EditText) findViewById(R.id.newmot_de_passe);
        email = (EditText) findViewById(R.id.newadressemail);
        but = (Spinner) findViewById(R.id.newgoal);

        //Ajout d'un écouteur au bouton retour
        this.button_goback = findViewById(R.id.button_goback);
        button_goback.setOnClickListener(new View.OnClickListener() {
            /**
             * Si l'utilisateur clique sur le bouton retour, il sera redirigé vers l'activité
             * RunAppActivity
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(goback);
                finish();
            }
        });

        //Ajout d'un écouteur au bouton date_de_naissance
        this.date_de_naissance = (Button) findViewById(R.id.newdate_de_naissance);
        date_de_naissance.setOnClickListener(new View.OnClickListener() {
                /**
                 * Quand l'utilisateur clique sur le bouton Calendrier il y a une popup qui s'ouvre lui permettant
                 * de choisir une date de naissance.
                 * @param v
                 */
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datedialog = new DatePickerDialog(
                        InscriptionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        date_de_naissance_Listener,
                        year,month,day);
                datedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datedialog.show();
            }
        });
        date_de_naissance_Listener = new DatePickerDialog.OnDateSetListener(){
            /**
             * Recuperation des valeurs saisie par l'utilisateur dans le calendrier
             * pour ensuite les stockés dans un string
             * @param datePicker
             * @param year  année
             * @param month mois
             * @param day   jour
             */
            @Override
            public void onDateSet(DatePicker datePicker,int year,int month,int day){
                month=month+1;
                birthday = ""+year+"/"+month+"/"+day;
            }
        };

        //Ajout d'un écouteur au bouton inscription
        this.button_inscription = findViewById(R.id.button_inscrip);
        button_inscription.setOnClickListener(new View.OnClickListener() {
            /**
             * Au moment du clic sur le bouton si l'un des champs n"est pas renseigné ou bien
             * que l'adresse mail ne contient pas de @, cela génère une fenetre popup avec un message d'erreur
             * Si le téléphone n'as pas de connexion internet une fenetre de popup s'ouvrira
             * Et si l'ensemble des condition sont respecter on execute la methode pour communiquer avec le serveur
             * @param v
             */
            @Override
            public void onClick(View v) {
                if ( name.getText().toString().equals("") || last_name.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);
                    builder.setMessage("Les champs ne sont pas tous renseigner")
                           .setNegativeButton("Recommencer",null)
                           .create()
                           .show();
                }else{
                    int testemail = email.getText().toString().indexOf("@");
                    if (testemail != -1) {
                        if(ConnexionInternet.isConnectedInternet(InscriptionActivity.this)) {
                            if (birthday==null){
                                birthday= "1970/1/1";
                            }
                            AddData = new AddDataAsyncTask();
                            AddData.execute();

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);
                            builder.setMessage("Vous n'avez pas de connexion internet")
                                    .setNegativeButton("Recommencer",null)
                                    .create()
                                    .show();
                        }
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);
                        builder.setMessage("Vous n'avez pas entrer une adresse email :)")
                                .setNegativeButton("Recommencer",null)
                                .create()
                                .show();
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class AddDataAsyncTask extends AsyncTask<Void, Void, Void> {

        /**
         * Methode qui convertit les différentes informations a transmettre au serveur dans un tableau
         * les transmets grace a la methode makeSerciveCall
         * et ensuite récupere un objet JSON au format String
         * on récupere les valeurs présente au format JSON et on les stock dans des varaibles
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");

            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<>(1);

            nameValuePair.add(new BasicNameValuePair("name", name.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("last_name", last_name.getText().toString().toUpperCase()));
            nameValuePair.add(new BasicNameValuePair("password", password.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("email", email.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("date_de_naissance", birthday));
            nameValuePair.add(new BasicNameValuePair("goal", but.getSelectedItem().toString()));

            String jsonStr = sh.makeServiceCall(urlAdd, ServiceHandler.POST, nameValuePair);

            Log.d("Response: ", jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getInt("success");
                    user_id = jsonObj.getInt("user_id");
                    password_user = jsonObj.getString("password");
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
                   users.addDataUser(user_id,last_name.getText().toString().toUpperCase(),name.getText().toString(),email.getText().toString(),password_user,birthday,but.getSelectedItem().toString());

                   Intent gobefore = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                   startActivityForResult(gobefore,100);
                   finish();
               }else {
                   AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);
                   builder.setMessage("L'inscription à échoué");
                   builder.setNegativeButton("Recommencer", null);
                   builder.show();
               }
        }
    }
}