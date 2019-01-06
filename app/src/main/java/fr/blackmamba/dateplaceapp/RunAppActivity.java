package fr.blackmamba.dateplaceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class RunAppActivity extends AppCompatActivity {
    private Button button_connexion;
    private Button button_inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_app);
        this.button_connexion=findViewById(R.id.button_connexion);
        this.button_inscription= findViewById(R.id.button_inscription);
        button_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent connexion_activity = new Intent(getApplicationContext(),ConnexionActivity.class);
                startActivity(connexion_activity);
                finish();
            }
        });

        button_inscription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent inscription_activity = new Intent(getApplicationContext(),InscriptionActivity.class);
                startActivity(inscription_activity);
                finish();
            }
        });
    }
}
