package fr.blackmamba.dateplaceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.blackmamba.dateplaceapp.backgroundtask.ConnexionInternet;
import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;
import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;

public class ConnexionActivity extends AppCompatActivity {

    private Button button_goback;
    private Button button_connect;
    EditText email, password;
    String urlAdd = "https://dateplaceapp.000webhostapp.com/login.php";
    String message, last_name, name, birthday, but,password_user;
    ConnexionActivity.GetDataAsyncTask GetData;
    int success, user_id;
    DatabaseHelper user_connected = null;

    /**
     * Affiche l'activité Connexion
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        user_connected = new DatabaseHelper(this);

        //Affectation des champs à des variables
        email = (EditText) findViewById(R.id.connexion_identifier);
        password = (EditText) findViewById(R.id.connexion_password);

        //Ajout d'un écouteur sur le bouton de retour
        this.button_goback = findViewById(R.id.button_goback);
        button_goback.setOnClickListener(new View.OnClickListener() {

            /**
             * Si l'utilisateur clique sur le bouton retour, il sera redirigé vers l'activité
             * RunAppActivity qui est la première activité de l'application
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(goback);
                finish();
            }
        });

        //Ajout d'un écouteur sur le bouton de conection
        this.button_connect = findViewById(R.id.button_connect);
        button_connect.setOnClickListener(new View.OnClickListener() {
            /**
             * Apres le clic sur le bouton
             * Si les champs sont vide ou bien que l'adresse mail ne contient pas de @
             * alors une popup de dialogue apparait pour prevenir l'utilisateur
             * Sinon on execute la methode pour transmettre les informations au serveur
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                    builder.setMessage("Il manque mot de passe/email")
                            .setNegativeButton("Recommencer", null)
                            .create()
                            .show();
                } else {
                    int testemail = email.getText().toString().indexOf("@");
                    if (testemail != -1) {
                        if (ConnexionInternet.isConnectedInternet(ConnexionActivity.this)) {
                            GetData = new GetDataAsyncTask();
                            GetData.execute();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                            builder.setMessage("Vous n'avez pas de connexion internet")
                                    .setNegativeButton("Recommencer", null)
                                    .create()
                                    .show();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                        builder.setMessage("Vous n'avez pas entrer une adresse email :)")
                                .setNegativeButton("Recommencer", null)
                                .create()
                                .show();
                    }
                }
            }
        });
    }

    public class GetDataAsyncTask extends AsyncTask<Void, Void, Void> {
        /**
         * Methode qui convertit les différentes informations a transmettre au serveur dans un tableau
         * les transmets grace a la methode makeSerciveCall
         * et ensuite récupere un objet JSON au format String
         * on récupere les valeurs présente au format JSON et on les stock dans des varaibles
         *
         * @param params
         * @return
         */
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
                    password_user = jsonObj.getString("password");
                    user_id = jsonObj.getInt("user_id");
                    last_name = jsonObj.getString("last_name");
                    name = jsonObj.getString("name");
                    birthday = jsonObj.getString("birthday");
                    but = jsonObj.getString("but");
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

        /**
         * Methode qui s'execute apres la methode doInBackground et permet en fonction du résultat obtenu suite a la requete
         * de redirigé l'utilisateur vers la Carte (l'activité suivante) ou bien de generer une fenetre de dialogue
         * pour signifier l'echec de la connection et donc la redirection vers la page connection
         *
         * @param result resultat de la methode doInBackground
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            int nb_added = 0;
            if (success == 1) {
                //Ajout dans la BDD User Connected si la personne possède le bon identifiant et mot de passe
                user_connected.addDataUserConnected(user_id, last_name, name, email.getText().toString(), password_user, birthday, but);

                Cursor data1 = user_connected.getDataUser();
                while(data1.moveToNext()){
                    nb_added++;
                }
                data1.close();
                //Si personne n'as été ajouté au préalable dans la BDD
                if (nb_added==0) {
                    user_connected.addDataUser(user_id,last_name,name,email.getText().toString(),password_user,birthday,but);
                }else {
                    //on verifie que les infos de la base en ligne sont identique a la base en local et on les modifie si necessaire
                    Cursor data = user_connected.getDataUser();
                    int x = 0;
                    while (data.moveToNext()) {
                        if (data.getString(1).equals(Integer.toString(user_id))) {
                            if ((!data.getString(2).equals(last_name)) || (!data.getString(3).equals(name)) || (!data.getString(4).equals(email.getText().toString())) || (!data.getString(5).equals(password_user)) || (!data.getString(6).equals(birthday)) || (!data.getString(7).equals(but))) {
                                user_connected.updateDataUser(user_id, last_name, name, email.getText().toString(), password.getText().toString(), birthday, but);
                            }
                            x++;
                        }
                    }
                    data.close();
                    //Si la table User n'est pas vide et que la personne n'existe pas dasn celle-ci
                    if (x == 0) {
                        user_connected.addDataUser(user_id, last_name, name, email.getText().toString(), password_user, birthday, but);
                    }
                }
                Intent gobefore = new Intent(ConnexionActivity.this, MapActivity.class);
                startActivityForResult(gobefore, 100);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                builder.setMessage("La Connection à échoué");
                builder.setNegativeButton("Recommencer", null);
                builder.show();
            }
        }
    }
}