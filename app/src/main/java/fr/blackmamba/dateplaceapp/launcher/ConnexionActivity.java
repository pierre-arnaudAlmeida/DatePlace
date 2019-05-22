package fr.blackmamba.dateplaceapp.launcher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
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
import fr.blackmamba.dateplaceapp.MapActivity;
import fr.blackmamba.dateplaceapp.R;
import fr.blackmamba.dateplaceapp.backgroundtask.ConnexionInternet;
import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;
import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;

public class ConnexionActivity extends AppCompatActivity {

    private EditText email, password;
    private String last_name;
    private String name;
    private String birthday;
    private String but;
    private String password_user;
    private int success, user_id;
    ConnexionActivity.GetDataAsyncTask GetData;
    DatabaseHelper user_connected = null;


    /**
     * Affiche l'activité Connexion
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Resources resources=getResources();
        user_connected = new DatabaseHelper(this);

        email = findViewById(R.id.connexion_identifier);
        password = findViewById(R.id.connexion_password);

        Button button_goback = findViewById(R.id.button_goback);
        button_goback.setOnClickListener(new View.OnClickListener() {

            /**
             * Si l'utilisateur clique sur le bouton retour, il sera redirigé vers l'activité
             * RunAppActivity qui est la première activité de l'application
             */
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(goback);
                finish();
            }
        });

        Button button_connect = findViewById(R.id.button_connect);
        button_connect.setOnClickListener(new View.OnClickListener() {
            /**
             * Apres le clic sur le bouton
             * Si les champs sont vide ou bien que l'adresse mail ne contient pas de @
             * alors une popup de dialogue apparait pour prevenir l'utilisateur
             * Sinon on execute la methode pour transmettre les informations au serveur
             */
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                    builder.setMessage(resources.getString(R.string.mail_or_password_missing))
                            .setNegativeButton(resources.getString(R.string.retry_message), null)
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
                            builder.setMessage(resources.getString(R.string.no_connection_message))
                                    .setNegativeButton(resources.getString(R.string.retry_message), null)
                                    .create()
                                    .show();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                        builder.setMessage(resources.getString(R.string.not_an_mail))
                                .setNegativeButton(resources.getString(R.string.retry_message), null)
                                .create()
                                .show();
                    }
                }
            }
        });

        Button button_go_inscription = findViewById(R.id.button_go_inscription);
        button_go_inscription.setOnClickListener(v -> {
            Intent inscription = new Intent(getApplicationContext(), InscriptionActivity.class);
            startActivity(inscription);
            finish();
        });


    }

    @SuppressLint("StaticFieldLeak")
    public class GetDataAsyncTask extends AsyncTask<Void, Void, Void> {
        /**
         * Methode qui convertit les différentes informations a transmettre au serveur dans un tableau
         * les transmets grace a la methode makeSerciveCall
         * et ensuite récupere un objet JSON au format String
         * on récupere les valeurs présente au format JSON et on les stock dans des varaibles
         */
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<>(1);

            nameValuePair.add(new BasicNameValuePair("email", email.getText().toString().toLowerCase()));
            nameValuePair.add(new BasicNameValuePair("password", password.getText().toString()));

            String urlConnect = "https://dateplaceapp.000webhostapp.com/login.php";
            String jsonStr = sh.makeServiceCall(urlConnect, ServiceHandler.POST, nameValuePair);

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
            Resources resources = getResources();
            super.onPostExecute(result);
            int nb_added = 0;
            if (success == 1) {
                //Ajout dans la BDD User Connected si la personne possède le bon identifiant et mot de passe
                user_connected.addDataUserConnected(user_id, last_name, name, email.getText().toString(), password_user, birthday, but);

                Cursor data1 = user_connected.getDataUser();
                while (data1.moveToNext()) {
                    nb_added++;
                }
                data1.close();
                //Si personne n'as été ajouté au préalable dans la BDD
                if (nb_added == 0) {
                    user_connected.addDataUser(user_id, last_name, name, email.getText().toString(), password_user, birthday, but);
                } else {
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
                builder.setMessage(resources.getString(R.string.connection_failed));
                builder.setNegativeButton(resources.getString(R.string.retry_message), null);
                builder.show();
            }
        }
    }
}