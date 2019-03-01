package fr.blackmamba.dateplaceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;

public class UserProfilActivity extends AppCompatActivity {

    private TextView button_deconnexion;
    private ImageView button_parameter;
    private ImageView button_map;
    DatabaseHelper user_connected;
    private TextView user_name;
    private TextView user_email;
    private TextView user_password;
    private TextView user_birthday;
    private TextView user_but;

    private String new_name;
    private String new_lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        this.user_name = findViewById(R.id.user_name);
        this.user_email = findViewById(R.id.user_email);
        this.user_password = findViewById(R.id.user_password);
        this.user_birthday = findViewById(R.id.user_birthday);
        this.user_but = findViewById(R.id.user_goal);

        user_connected = new DatabaseHelper(this);
        Cursor data_user_connected = user_connected.getDataUserConnected();
        while (data_user_connected.moveToNext()) {
            if (!data_user_connected.getString(0).equals("")) {
                user_name.setText("" + data_user_connected.getString(1) + " " + data_user_connected.getString(2));
                user_email.setText(data_user_connected.getString(3));
                user_birthday.setText(data_user_connected.getString(5));
                user_but.setText(data_user_connected.getString(6));
            }
        }
        data_user_connected.close();
        user_connected.close();

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

        this.button_map = findViewById(R.id.button_map);
        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_map = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(go_map);
                finish();
            }
        });

        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(UserProfilActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                EditText editText_lastname = new EditText(UserProfilActivity.this);
                editText_lastname.setHint("Modifiez votre nom");
                layout.addView(editText_lastname);

                EditText editText_name = new EditText(UserProfilActivity.this);
                editText_name.setHint("Modifiez votre prénom");
                layout.addView(editText_name);

                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Modifiez vos informations ");
                builder.setView(layout);

                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goback = new Intent(UserProfilActivity.this, UserProfilActivity.class);
                        startActivityForResult(goback,100);
                        finish();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new_name = editText_name.getText().toString();
                        new_lastname = editText_lastname.getText().toString();

                        Intent goafter = new Intent(UserProfilActivity.this, UserProfilActivity.class);
                        startActivityForResult(goafter,100);
                        finish();
                    }
                });
                builder.show();
            }
        });

        user_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Voulez-vous modifer le mot de passe ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //faire la maj mot de passe
                            }
                        })
                        .setNegativeButton("Non", null)
                        .create()
                        .show();
            }
        });

    }
}
