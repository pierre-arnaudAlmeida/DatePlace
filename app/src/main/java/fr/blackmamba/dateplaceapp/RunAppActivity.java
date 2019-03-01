package fr.blackmamba.dateplaceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;
import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;

public class RunAppActivity extends AppCompatActivity {
    private Button button_connexion;
    private Button button_inscription;
    String urlAdd = "https://dateplaceapp.000webhostapp.com/update_db.php";
    DatabaseHelper count_connected;
    int nb_connected = 0;
    int user_id, success;
    String last_name, name, birthday, but, password,email;
    GetDataAsyncTask GetData;

    /**
     * Affiche l'activité RunApp
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_app);

        count_connected = new DatabaseHelper(this);
        Cursor count_connectedDataUserConnected = count_connected.getDataUserConnected();
        while (count_connectedDataUserConnected.moveToNext()) {
            nb_connected++;
            if(nb_connected==1){
                user_id = Integer.parseInt(count_connectedDataUserConnected.getString(0));
                GetData = new GetDataAsyncTask();
                GetData.execute();
            }
        }
        count_connectedDataUserConnected.close();

        if (nb_connected == 1) {
            Intent gotomap = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(gotomap);
            this.finish();
        } else {

            //Affectation des boutons a des variables
            this.button_connexion = findViewById(R.id.button_connexion);
            this.button_inscription = findViewById(R.id.button_inscription);

            //Ajout d'un écouteur au bouton connexion
            button_connexion.setOnClickListener(new View.OnClickListener() {
                /**
                 * Quand l'utilisateur clique sur le bouton connexion il est redirigé vers
                 * l'activité Connexion
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    Intent connexion_activity = new Intent(getApplicationContext(), ConnexionActivity.class);
                    startActivity(connexion_activity);
                    finish();
                }
            });

            //Ajout d'un écouteur au bouton inscription
            button_inscription.setOnClickListener(new View.OnClickListener() {
                /**
                 * Quand l'utilisateur clique sur le bouton inscription il est redirigé vers
                 * l'activité Inscription
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    Intent inscription_activity = new Intent(getApplicationContext(), InscriptionActivity.class);
                    startActivity(inscription_activity);
                    finish();
                }
            });
        }
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

            nameValuePair.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));

            String jsonStr = sh.makeServiceCall(urlAdd, ServiceHandler.POST, nameValuePair);
            Log.d("Response: ", jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getInt("success");
                    user_id = jsonObj.getInt("user_id");
                    last_name = jsonObj.getString("last_name");
                    name = jsonObj.getString("name");
                    email = jsonObj.getString("adress_mail");
                    birthday = jsonObj.getString("birthday");
                    but = jsonObj.getString("goal");
                    password = jsonObj.getString("password");
                    Log.i("success", String.valueOf(success));
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
            if (success == 1) {
                Cursor data = count_connected.getDataUserConnected();
                while(data.moveToNext()){
                    if(data.getString(0).equals(Integer.toString(user_id)) && (!data.getString(4).equals(password))){
                        count_connected.updateDataUser(user_id, last_name, name, email, password, birthday, but);
                        count_connected.updateDataUserConnected(user_id, last_name, name, email, password, birthday, but);
                        Intent gobefore = new Intent(RunAppActivity.this, ConnexionActivity.class);
                        startActivityForResult(gobefore, 100);
                        finish();
                    } else {
                        count_connected.updateDataUser(user_id, last_name, name, email, password, birthday, but);
                        count_connected.updateDataUserConnected(user_id, last_name, name, email, password, birthday, but);
                    }
                }
                data.close();
            }
        }
    }
}