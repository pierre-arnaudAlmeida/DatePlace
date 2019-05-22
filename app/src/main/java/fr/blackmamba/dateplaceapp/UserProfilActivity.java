package fr.blackmamba.dateplaceapp;

import android.app.DatePickerDialog;
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
import android.widget.Button;
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
    private TextView button_deconnection;
    private TextView user_title_name;
    private TextView textViewUserName;
    private TextView textViewUserLastname;
    private TextView textViewUserEmail;
    private TextView textViewUserPassword;
    private TextView textViewUserBirthday;
    private TextView textViewUserGoal;
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
    private String new_birthday2;
    private String new_goal;
    private String action;
    private DatePickerDialog.OnDateSetListener user_birthday_Listener;
    private String urlUpdate = "https://dateplaceapp.000webhostapp.com/update_datas.php";
    UpdateDataAsyncTask UpdateData;
    DatabaseHelper user_connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        this.user_title_name = findViewById(R.id.user_name);
        this.textViewUserEmail = findViewById(R.id.user_email);
        this.textViewUserName = findViewById(R.id.user_firstname);
        this.textViewUserLastname = findViewById(R.id.user_lastname);
        this.textViewUserPassword = findViewById(R.id.user_password);
        this.textViewUserBirthday = findViewById(R.id.user_birthday);
        this.textViewUserGoal = findViewById(R.id.user_goal);

        user_connected = new DatabaseHelper(this);
        Cursor data_user_connected = user_connected.getDataUserConnected();
        while (data_user_connected.moveToNext()) {
            if (!data_user_connected.getString(0).equals("")) {
                user_id = data_user_connected.getString(0);
                user_lastname = data_user_connected.getString(1);
                user_firstname = data_user_connected.getString(2);
                textViewUserName.setText(user_firstname);
                textViewUserLastname.setText(user_lastname);
                user_title_name.setText(user_lastname+ " " + user_firstname);
                textViewUserEmail.setText(data_user_connected.getString(3));
                textViewUserBirthday.setText(data_user_connected.getString(5));
                textViewUserGoal.setText(data_user_connected.getString(6));
            }
        }
        data_user_connected.close();
        user_connected.close();

        this.button_deconnection = findViewById(R.id.button_deconnexion);
        button_deconnection.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage("Voulez-vous vraiment vous deconnecter ?");

            builder.setNegativeButton("Non", null);
            builder.setPositiveButton("Oui", (dialogInterface, i) -> {
                user_connected.deleteUserCOnnected();
                Intent deconnexion = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(deconnexion);
                finish();
            })
                    .create()
                    .show();
        });

        this.button_parameter = findViewById(R.id.button_parametre);
        button_parameter.setOnClickListener(v -> {
            Intent parameter = new Intent(getApplicationContext(), UserSettingActivity.class);
            startActivity(parameter);
            finish();
        });

        this.button_map = findViewById(R.id.button_map);
        button_map.setOnClickListener(v -> {
            Intent go_map = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(go_map);
            finish();
        });

        textViewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText_new_name = new EditText(UserProfilActivity.this);
                editText_new_name.setHint("Nouveau prenom ");

                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Modification du prenom ");
                builder.setView(editText_new_name);

                builder.setNegativeButton("Annuler", null);
                builder.setPositiveButton("Modifier", (dialogInterface, i) -> {
                    new_name = editText_new_name.getText().toString();
                    new_lastname = textViewUserLastname.getText().toString();
                    action = "update_name";
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                })
                        .create()
                        .show();
            }
        });

        textViewUserLastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText_new_lastname = new EditText(UserProfilActivity.this);
                editText_new_lastname.setHint("Nouveau nom ");

                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage("Modification du nom ");
                builder.setView(editText_new_lastname);

                builder.setNegativeButton("Annuler", null);
                builder.setPositiveButton("Modifier", (dialogInterface, i) -> {
                    new_lastname = editText_new_lastname.getText().toString();
                    new_name = textViewUserName.getText().toString();
                    action = "update_name";
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                })
                        .create()
                        .show();
            }
        });

//        user_title_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout layout = new LinearLayout(UserProfilActivity.this);
//                layout.setOrientation(LinearLayout.VERTICAL);
//
//                EditText editText_lastname = new EditText(UserProfilActivity.this);
//                editText_lastname.setHint("Modifiez votre nom");
//                layout.addView(editText_lastname);
//
//                EditText editText_name = new EditText(UserProfilActivity.this);
//                editText_name.setHint("Modifiez votre prénom");
//                layout.addView(editText_name);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
//                builder.setMessage("Modifiez vos informations ");
//                builder.setView(layout);
//
//                builder.setNegativeButton("Annuler", null);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        new_name = editText_name.getText().toString();
//                        new_lastname = editText_lastname.getText().toString();
//                        if((new_name.equals("") && (!new_lastname.equals(""))) || ((!new_name.equals("")) && new_lastname.equals("")) || ((!new_name.equals("")) && (!new_lastname.equals(""))) ){
//                            action = "update_name";
//                            UpdateData = new UpdateDataAsyncTask();
//                            UpdateData.execute();
//                        } else {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
//                            builder.setMessage("La modification à échoué");
//                            builder.setPositiveButton("J'ai compris", null);
//                            builder.show();
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });


        textViewUserPassword.setOnClickListener(view -> {
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
            builder.setPositiveButton("Modifier", (dialogInterface, i) -> {
                actual_password = editText_actual_password.getText().toString();
                new_password = editText_new_password.getText().toString();
                if((!actual_password.equals("")) && (!new_password.equals(""))){
                    action = "update_password";
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder1.setMessage("Les champs ne sont pas tous renseigné");
                    builder1.setPositiveButton("J'ai compris", null);
                    builder1.show();
                }
            })
                    .create()
                    .show();
        });

        textViewUserEmail.setOnClickListener(view -> {
            EditText editText_new_email = new EditText(UserProfilActivity.this);
            editText_new_email.setHint("Nouvelle adresse mail ");

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage("Modification de l'adresse mail ");
            builder.setView(editText_new_email);

            builder.setNegativeButton("Annuler", null);
            builder.setPositiveButton("Modifier", (dialogInterface, i) -> {
                new_email = editText_new_email.getText().toString();
                action = "update_adressmail";
                UpdateData = new UpdateDataAsyncTask();
                UpdateData.execute();
            })
                    .create()
                    .show();
        });

        textViewUserBirthday.setOnClickListener(new View.OnClickListener() {
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
                if(month <10 && day <10) {
                    new_birthday2 = "" + year + "-0" + month + "-0" + day;
                } else if (month<10 && day>9){
                    new_birthday2 = "" + year + "-0" + month + "-" + day;
                } else if (month>9 && day<10){
                    new_birthday2 = "" + year + "-" + month + "-0" + day;
                } else if (month>9 && day>9){
                    new_birthday2 = "" + year + "-" + month + "-" + day;
                }
                action = "update_birthday";
                UpdateData = new UpdateDataAsyncTask();
                UpdateData.execute();
            }
        };

        textViewUserGoal.setOnClickListener(view -> {
            LinearLayout layout = new LinearLayout(UserProfilActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            Button textView_1 = new Button(UserProfilActivity.this);
            textView_1.setText("Date Place");
            layout.addView(textView_1);

            Button textView_2 = new Button(UserProfilActivity.this);
            textView_2.setText("Un lieu pour entre potes");
            layout.addView(textView_2);

            Button textView_3 = new Button(UserProfilActivity.this);
            textView_3.setText("Un lieu pour sortir avec mes parents");
            layout.addView(textView_3);

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage("Modification du but ");
            builder.setView(layout);

            builder.setNegativeButton("Annuler", null);

            textView_1.setOnClickListener(v -> {
                action = "update_goal";
                new_goal = "Date Place";
                if(new_goal.equals(textViewUserGoal.getText().toString())){
                    AlertDialog.Builder builder12 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder12.setMessage("Vous avez deja ce but");
                    builder12.setPositiveButton("J'ai compris", null);
                    builder12.show();
                } else {
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                }
            });
            textView_2.setOnClickListener(v -> {
                action = "update_goal";
                new_goal = "Un lieu pour entre potes";
                if(new_goal.equals(textViewUserGoal.getText().toString())){
                    AlertDialog.Builder builder13 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder13.setMessage("Vous avez deja ce but");
                    builder13.setPositiveButton("J'ai compris", null);
                    builder13.show();
                } else {
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                }
            });
            textView_3.setOnClickListener(v -> {
                action = "update_goal";
                new_goal = "Un lieu pour sortir avec mes parents";
                if(new_goal.equals(textViewUserGoal.getText().toString())){
                    AlertDialog.Builder builder14 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder14.setMessage("Vous avez deja ce but");
                    builder14.setPositiveButton("J'ai compris", null);
                    builder14.show();
                } else {
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                }
            });
            builder.show();
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
            if (action.equals("update_name")){
                nameValuePair.add(new BasicNameValuePair("new_name", new_name));
                nameValuePair.add(new BasicNameValuePair("new_lastname", new_lastname.toUpperCase()));
            } else if (action.equals("update_birthday")){
                nameValuePair.add(new BasicNameValuePair("new_birthday", new_birthday));
            } else if (action.equals("update_adressmail")){
                nameValuePair.add(new BasicNameValuePair("new_adressmail", new_email.toLowerCase()));
            } else if (action.equals("update_password")){
                nameValuePair.add(new BasicNameValuePair("actual_password", actual_password));
                nameValuePair.add(new BasicNameValuePair("new_password", new_password));
            } else if (action.equals("update_goal")){
                nameValuePair.add(new BasicNameValuePair("new_goal", new_goal));
            }

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
                    if (new_lastname.equals("")){
                        new_lastname = user_lastname;
                    } else if (new_name.equals("")){
                        new_name = user_firstname;
                    }
                    user_connected.updateDataUserConnected(Integer.parseInt(user_id),new_lastname.toUpperCase(),new_name, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                    user_connected.updateDataUser(Integer.parseInt(user_id),new_lastname.toUpperCase(),new_name, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                    user_title_name.setText(new_lastname.toUpperCase() + " " + new_name);
                    textViewUserName.setText(new_name);
                    textViewUserLastname.setText(new_lastname.toUpperCase());
                } else if (action.equals("update_birthday")){
                    user_connected.updateDataUserConnected(Integer.parseInt(user_id),user_lastname,user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(),new_birthday2, textViewUserGoal.getText().toString());
                    user_connected.updateDataUser(Integer.parseInt(user_id),user_lastname,user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(),new_birthday2, textViewUserGoal.getText().toString());
                    textViewUserBirthday.setText(new_birthday2);
                } else if (action.equals("update_adressmail")){
                    user_connected.updateDataUserConnected(Integer.parseInt(user_id),user_lastname,user_firstname,new_email.toLowerCase(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                    user_connected.updateDataUser(Integer.parseInt(user_id),user_lastname,user_firstname,new_email.toLowerCase(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                    textViewUserEmail.setText(new_email.toLowerCase());
                } else if (action.equals("update_password")){
                    user_connected.updateDataUserConnected(Integer.parseInt(user_id),user_lastname,user_firstname, textViewUserEmail.getText().toString(),new_password, textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                    user_connected.updateDataUser(Integer.parseInt(user_id),user_lastname,user_firstname, textViewUserEmail.getText().toString(),new_password, textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                } else if (action.equals("update_goal")){
                    user_connected.updateDataUserConnected(Integer.parseInt(user_id),user_lastname,user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(),new_goal);
                    user_connected.updateDataUser(Integer.parseInt(user_id),user_lastname,user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(),new_goal);
                    textViewUserGoal.setText(new_goal);
                    Intent go_before = new Intent(getApplicationContext(), UserProfilActivity.class);
                    startActivity(go_before);
                    finish();
                }
            }
        }
    }
}
