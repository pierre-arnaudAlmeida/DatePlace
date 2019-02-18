package fr.blackmamba.dateplaceapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;

public class RunAppActivity extends AppCompatActivity {
    private Button button_connexion;
    private Button button_inscription;
    DatabaseHelper user_connected;
    int nb_connected = 0;

    /**
     * Affiche l'activité RunApp
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_app);

        user_connected = new DatabaseHelper(this);
        Cursor data = user_connected.getDataUserConnected();
        while(data.moveToNext()){
            nb_connected++;
        }
        if (nb_connected==1) {
            Intent gotomap = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(gotomap);
            finish();
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
}