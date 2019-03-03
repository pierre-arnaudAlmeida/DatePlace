package fr.blackmamba.dateplaceapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;
import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;

public class UserProfilActivity extends AppCompatActivity {

    private ImageView button_parameter;
    private ImageView button_map;
    private TextView button_deconnexion;
    private TextView user_name;
    private TextView user_email;
    private TextView user_password;
    private TextView user_birthday;
    private TextView user_but;
    private int year;
    private int month;
    private int day;
    private int success;
    private String user_id;
    private String user_lastname;
    private String user_firstname;
    private String new_name;
    private String new_lastname;
    private String actual_password;
    private String new_password;
    private String new_email;
    private String new_birthday;
    private String action;
    private DatePickerDialog.OnDateSetListener user_birthday_Listener;
    private String urlUpdate = "https://dateplaceapp.000webhostapp.com/update_datas.php";
    UpdateDataAsyncTask UpdateData;
    DatabaseHelper user_connected;

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
                user_id = data_user_connected.getString(0);
                user_lastname = data_user_connected.getString(1);
                user_firstname = data_user_connected.getString(2);
                user_name.setText(user_lastname+ " " + user_firstname);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Voulez-vous vraiment vous deconnecter ?");

                builder.setNegativeButton("Non", null);
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user_connected.deleteUserCOnnected();
                        Intent deconnexion = new Intent(getApplicationContext(), RunAppActivity.class);
                        startActivity(deconnexion);
                        finish();
                    }
                })
                        .create()
                        .show();
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

                builder.setNegativeButton("Annuler", null);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new_name = editText_name.getText().toString();
                        new_lastname = editText_lastname.getText().toString();
                        if((new_name.equals("") && (!new_lastname.equals(""))) || ((!new_name.equals("")) && new_lastname.equals("")) || ((!new_name.equals("")) && (!new_lastname.equals(""))) ){
                            action = "update_name";
                            UpdateData = new UpdateDataAsyncTask();
                            UpdateData.execute();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                            builder.setMessage("La modification à échoué");
                            builder.setPositiveButton("J'ai compris", null);
                            builder.show();
                        }
                    }
                });
                builder.show();
            }
        });

        user_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(UserProfilActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                EditText editText_actual_password = new EditText(UserProfilActivity.this);
                editText_actual_password.setHint("Ancien mot de passe");
                layout.addView(editText_actual_password);

                EditText editText_new_password = new EditText(UserProfilActivity.this);
                editText_new_password.setHint("Nouveau mot de passe");
                layout.addView(editText_new_password);

                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Modification du mot de passe ");
                builder.setView(layout);

                builder.setNegativeButton("Annuler", null);
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        actual_password = editText_actual_password.getText().toString();
                        new_password = editText_new_password.getText().toString();
                        // TODO faire la maj mot de passe
                        user_password.setText(new_password);
                    }
                })
                        .create()
                        .show();
            }
        });

        user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText_new_email = new EditText(UserProfilActivity.this);
                editText_new_email.setHint("Nouvelle adresse mail ");

                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Modification de l'adresse mail ");
                builder.setView(editText_new_email);

                builder.setNegativeButton("Annuler", null);
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new_email = editText_new_email.getText().toString();
                        // TODO faire la maj mot de passe
                        user_email.setText(new_email);
                    }
                })
                        .create()
                        .show();
            }
        });

        user_birthday.setOnClickListener(new View.OnClickListener() {
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
                        UserProfilActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        user_birthday_Listener,
                        year, month, day);
                datedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datedialog.show();
            }
        });

        user_birthday_Listener = new DatePickerDialog.OnDateSetListener() {
            /**
             * Recuperation des valeurs saisie par l'utilisateur dans le calendrier
             * pour ensuite les stockés dans un string
             * @param datePicker
             * @param year  année
             * @param month mois
             * @param day   jour
             */
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                new_birthday = "" + year + "/" + month + "/" + day;
                // TODO fait la maj sur la date
                user_birthday.setText(new_birthday);
            }
        };

        user_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Modification du but ");

                builder.setNegativeButton("Annuler", null);
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO faire une liste a cocher pour les trois cas possible et permettre d'en choisir q'un
                    }
                })
                        .create()
                        .show();
            }
        });
    }

    public class UpdateDataAsyncTask extends AsyncTask<Void, Void, Void> {

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

            nameValuePair.add(new BasicNameValuePair("action", action));
            nameValuePair.add(new BasicNameValuePair("user_id", user_id));
            nameValuePair.add(new BasicNameValuePair("new_name", new_name));
            nameValuePair.add(new BasicNameValuePair("new_lastname", new_lastname.toUpperCase()));

            String jsonStr = sh.makeServiceCall(urlUpdate, ServiceHandler.POST, nameValuePair);

            Log.d("Response: ", jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getInt("success");
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
            if (success!=1){
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("La modification à échoué");
                builder.setPositiveButton("J'ai compris", null);
                builder.show();
            } else {
                if (action.equals("update_name")){
                    //TODO actualisation de la bd local
                    if (new_lastname.equals("")){
                        new_lastname = user_lastname;
                    } else if (new_name.equals("")){
                        new_name = user_firstname;
                    }
                    user_connected.updateDataUserConnected(Integer.parseInt(user_id),new_lastname.toUpperCase(),new_name,user_email.getText().toString(),user_password.getText().toString(),user_birthday.getText().toString(),user_but.getText().toString());
                    user_connected.updateDataUser(Integer.parseInt(user_id),new_lastname.toUpperCase(),new_name,user_email.getText().toString(),user_password.getText().toString(),user_birthday.getText().toString(),user_but.getText().toString());
                    user_name.setText(new_lastname.toUpperCase() + " " + new_name);
                }
            }
        }
    }
}
