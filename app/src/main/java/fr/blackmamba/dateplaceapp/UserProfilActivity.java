package fr.blackmamba.dateplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;

public class UserProfilActivity extends AppCompatActivity {

    private TextView button_deconnexion;
    private ImageButton button_parameter;
    DatabaseHelper user_connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);
        user_connected = new DatabaseHelper(this);

        this.button_deconnexion = findViewById(R.id.button_deconnexion);
        button_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_connected.deleteUserCOnnected();
                Intent deconnexion = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(deconnexion);
                finish();
            }
        });
        this.button_parameter = findViewById(R.id.button_parametre);
        button_parameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parameter = new Intent(getApplicationContext(), UserSettingActivity.class);
                startActivity(parameter);
                finish();
            }
        });
    }
}
