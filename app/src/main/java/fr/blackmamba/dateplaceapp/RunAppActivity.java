package fr.blackmamba.dateplaceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RunAppActivity extends AppCompatActivity {
    private Button button_connexion;
    private Button button_inscription;

    /**
     * Affiche l'activité RunApp
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_app);

        //Affectation des boutons a des variables
        this.button_connexion=findViewById(R.id.button_connexion);
        this.button_inscription= findViewById(R.id.button_inscription);

        //Ajout d'un écouteur au bouton connexion
        button_connexion.setOnClickListener(new View.OnClickListener() {
            /**
             * Quand l'utilisateur clique sur le bouton connexion il est redirigé vers
             * l'activité Connexion
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent connexion_activity = new Intent(getApplicationContext(),ConnexionActivity.class);
                startActivity(connexion_activity);
                finish();
            }
        });

        //Ajout d'un écouteur au bouton inscription
        button_inscription.setOnClickListener(new View.OnClickListener(){
            /**
             * Quand l'utilisateur clique sur le bouton inscription il est redirigé vers
             * l'activité Inscription
             * @param v
             */
            @Override
            public void onClick(View v){
                Intent inscription_activity = new Intent(getApplicationContext(),InscriptionActivity.class);
                startActivity(inscription_activity);
                finish();
            }
        });
    }
}