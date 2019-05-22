package fr.blackmamba.dateplaceapp.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
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
import fr.blackmamba.dateplaceapp.MapActivity;
import fr.blackmamba.dateplaceapp.R;
import fr.blackmamba.dateplaceapp.backgroundtask.DatabaseHelper;
import fr.blackmamba.dateplaceapp.backgroundtask.ServiceHandler;
import fr.blackmamba.dateplaceapp.launcher.RunAppActivity;

public class UserProfilActivity extends AppCompatActivity {

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
    UpdateDataAsyncTask UpdateData = new UpdateDataAsyncTask();
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
        Resources resources = getResources();

        user_connected = new DatabaseHelper(this);
        Cursor data_user_connected = user_connected.getDataUserConnected();
        while (data_user_connected.moveToNext()) if (!data_user_connected.getString(0).equals("")) {
            user_id = data_user_connected.getString(0);
            user_lastname = data_user_connected.getString(1);
            user_firstname = data_user_connected.getString(2);
            textViewUserName.setText(user_firstname);
            textViewUserLastname.setText(user_lastname);
            user_title_name.setText(user_lastname + " " + user_firstname);
            textViewUserEmail.setText(data_user_connected.getString(3));
            textViewUserBirthday.setText(data_user_connected.getString(5));
            textViewUserGoal.setText(data_user_connected.getString(6));
        }
        data_user_connected.close();
        user_connected.close();

        TextView button_deconnection = findViewById(R.id.button_deconnexion);
        button_deconnection.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage(resources.getString(R.string.disconnection_message));

            builder.setNegativeButton(resources.getString(R.string.negative_response), null);
            builder.setPositiveButton(resources.getString(R.string.positive_response), (dialogInterface, i) -> {
                user_connected.deleteUserCOnnected();
                Intent deconnexion = new Intent(getApplicationContext(), RunAppActivity.class);
                startActivity(deconnexion);
                finish();
            })
                    .create()
                    .show();
        });

        ImageView button_parameter = findViewById(R.id.button_parametre);
        button_parameter.setOnClickListener(v -> {
            Intent parameter = new Intent(getApplicationContext(), UserSettingActivity.class);
            startActivity(parameter);
            finish();
        });

        ImageView button_map = findViewById(R.id.button_map);
        button_map.setOnClickListener(v -> {
            Intent go_map = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(go_map);
            finish();
        });

        textViewUserName.setOnClickListener(view -> {
            EditText editText_new_name = new EditText(UserProfilActivity.this);
            editText_new_name.setHint(resources.getString(R.string.new_firstname_message));

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage(resources.getString(R.string.new_firstname_title_message));
            builder.setView(editText_new_name);

            builder.setNegativeButton(resources.getString(R.string.cancel_message), null);
            builder.setPositiveButton(resources.getString(R.string.modify_message), (dialogInterface, i) -> {
                new_name = editText_new_name.getText().toString();
                new_lastname = textViewUserLastname.getText().toString();
                action = "update_name";
                UpdateData = new UpdateDataAsyncTask();
                UpdateData.execute();
            })
                    .create()
                    .show();
        });

        textViewUserLastname.setOnClickListener(view -> {
            EditText editText_new_lastname = new EditText(UserProfilActivity.this);
            editText_new_lastname.setHint(resources.getString(R.string.new_lastname_message));

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage(resources.getString(R.string.new_lastname_title_message));
            builder.setView(editText_new_lastname);

            builder.setNegativeButton(resources.getString(R.string.cancel_message), null);
            builder.setPositiveButton(resources.getString(R.string.modify_message), (dialogInterface, i) -> {
                new_lastname = editText_new_lastname.getText().toString();
                new_name = textViewUserName.getText().toString();
                action = "update_name";
                UpdateData = new UpdateDataAsyncTask();
                UpdateData.execute();
            })
                    .create()
                    .show();
        });

        textViewUserPassword.setOnClickListener(view -> {
            LinearLayout layout = new LinearLayout(UserProfilActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            EditText editText_actual_password = new EditText(UserProfilActivity.this);
            editText_actual_password.setHint(resources.getString(R.string.hold_password_message));
            layout.addView(editText_actual_password);

            EditText editText_new_password = new EditText(UserProfilActivity.this);
            editText_new_password.setHint(resources.getString(R.string.new_password_message));
            layout.addView(editText_new_password);

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage(resources.getString(R.string.modification_password_message));
            builder.setView(layout);

            builder.setNegativeButton(resources.getString(R.string.cancel_message), null);
            builder.setPositiveButton(resources.getString(R.string.modify_message), (dialogInterface, i) -> {
                actual_password = editText_actual_password.getText().toString();
                new_password = editText_new_password.getText().toString();
                if((!actual_password.equals("")) && (!new_password.equals(""))){
                    action = "update_password";
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder1.setMessage(resources.getString(R.string.empty_fields_message));
                    builder1.setPositiveButton(resources.getString(R.string.understand_message), null);
                    builder1.show();
                }
            })
                    .create()
                    .show();
        });

        textViewUserEmail.setOnClickListener(view -> {
            EditText editText_new_email = new EditText(UserProfilActivity.this);
            editText_new_email.setHint(resources.getString(R.string.new_email_message));

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage(resources.getString(R.string.modification_email_message));
            builder.setView(editText_new_email);

            builder.setNegativeButton(resources.getString(R.string.cancel_message), null);
            builder.setPositiveButton(resources.getString(R.string.modify_message), (dialogInterface, i) -> {
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
             */
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                new_birthday = "" + year + "/" + month + "/" + day;
                if(month <10 && day <10)
                    new_birthday2 = "" + year + "-0" + month + "-0" + day;
                 else if (month<10 && day>9)
                    new_birthday2 = "" + year + "-0" + month + "-" + day;
                 else if (day<10)
                    new_birthday2 = "" + year + "-" + month + "-0" + day;
                 else if (day>9) {
                    new_birthday2 = "" + year + "-" + month + "-" + day;
                }

                action = "update_birthday";
                UpdateData = new UpdateDataAsyncTask();
                UpdateData.execute();
            }
        };

        String[] goals = resources.getStringArray(R.array.goal);
        textViewUserGoal.setOnClickListener(view -> {
            LinearLayout layout = new LinearLayout(UserProfilActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            Button textView_1 = new Button(UserProfilActivity.this);
            textView_1.setText(goals[0]);
            layout.addView(textView_1);

            Button textView_2 = new Button(UserProfilActivity.this);
            textView_2.setText(goals[1]);
            layout.addView(textView_2);

            Button textView_3 = new Button(UserProfilActivity.this);
            textView_3.setText(goals[2]);
            layout.addView(textView_3);

            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
            builder.setMessage(resources.getString(R.string.modification_goal));
            builder.setView(layout);

            builder.setNegativeButton(resources.getString(R.string.cancel_message), null);

            textView_1.setOnClickListener(v -> {
                action = "update_goal";
                new_goal = goals[0];
                if(new_goal.equals(textViewUserGoal.getText().toString())){
                    AlertDialog.Builder builder12 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder12.setMessage(resources.getString(R.string.current_goal));
                    builder12.setPositiveButton(resources.getString(R.string.understand_message), null);
                    builder12.show();
                } else {
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                }
            });
            textView_2.setOnClickListener(v -> {
                action = "update_goal";
                new_goal = goals[1];
                if(new_goal.equals(textViewUserGoal.getText().toString())){
                    AlertDialog.Builder builder13 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder13.setMessage(resources.getString(R.string.current_goal));
                    builder13.setPositiveButton(resources.getString(R.string.understand_message), null);
                    builder13.show();
                } else {
                    UpdateData = new UpdateDataAsyncTask();
                    UpdateData.execute();
                }
            });
            textView_3.setOnClickListener(v -> {
                action = "update_goal";
                new_goal = goals[2];
                if(new_goal.equals(textViewUserGoal.getText().toString())){
                    AlertDialog.Builder builder14 = new AlertDialog.Builder(UserProfilActivity.this);
                    builder14.setMessage(resources.getString(R.string.current_goal));
                    builder14.setPositiveButton(resources.getString(R.string.understand_message), null);
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
         */
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<>(1);
            nameValuePair.add(new BasicNameValuePair("action", action));
            nameValuePair.add(new BasicNameValuePair("user_id", user_id));
            switch (action) {
                case "update_name":
                    nameValuePair.add(new BasicNameValuePair("new_name", new_name));
                    nameValuePair.add(new BasicNameValuePair("new_lastname", new_lastname.toUpperCase()));
                    break;
                case "update_birthday":
                    nameValuePair.add(new BasicNameValuePair("new_birthday", new_birthday));
                    break;
                case "update_adressmail":
                    nameValuePair.add(new BasicNameValuePair("new_adressmail", new_email.toLowerCase()));
                    break;
                case "update_password":
                    nameValuePair.add(new BasicNameValuePair("actual_password", actual_password));
                    nameValuePair.add(new BasicNameValuePair("new_password", new_password));
                    break;
                case "update_goal":
                    nameValuePair.add(new BasicNameValuePair("new_goal", new_goal));
                    break;
            }

            String urlUpdate = "https://dateplaceapp.000webhostapp.com/update_datas.php";
            String jsonStr = sh.makeServiceCall(urlUpdate, ServiceHandler.POST, nameValuePair);

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
            Resources resources = getResources();

            super.onPostExecute(result);
            if (success!=1){
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilActivity.this);
                builder.setMessage(resources.getString(R.string.modification_failed_message));
                builder.setPositiveButton(resources.getString(R.string.understand_message), null);
                builder.show();
            } else {
                switch (action) {
                    case "update_name":
                        if (new_lastname.equals("")) {
                            new_lastname = user_lastname;
                        } else if (new_name.equals("")) {
                            new_name = user_firstname;
                        }
                        user_connected.updateDataUserConnected(Integer.parseInt(user_id), new_lastname.toUpperCase(), new_name, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                        user_connected.updateDataUser(Integer.parseInt(user_id), new_lastname.toUpperCase(), new_name, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                        user_title_name.setText(new_lastname.toUpperCase() + " " + new_name);
                        textViewUserName.setText(new_name);
                        textViewUserLastname.setText(new_lastname.toUpperCase());
                        break;
                    case "update_birthday":
                        user_connected.updateDataUserConnected(Integer.parseInt(user_id), user_lastname, user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), new_birthday2, textViewUserGoal.getText().toString());
                        user_connected.updateDataUser(Integer.parseInt(user_id), user_lastname, user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), new_birthday2, textViewUserGoal.getText().toString());
                        textViewUserBirthday.setText(new_birthday2);
                        break;
                    case "update_adressmail":
                        user_connected.updateDataUserConnected(Integer.parseInt(user_id), user_lastname, user_firstname, new_email.toLowerCase(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                        user_connected.updateDataUser(Integer.parseInt(user_id), user_lastname, user_firstname, new_email.toLowerCase(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                        textViewUserEmail.setText(new_email.toLowerCase());
                        break;
                    case "update_password":
                        user_connected.updateDataUserConnected(Integer.parseInt(user_id), user_lastname, user_firstname, textViewUserEmail.getText().toString(), new_password, textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                        user_connected.updateDataUser(Integer.parseInt(user_id), user_lastname, user_firstname, textViewUserEmail.getText().toString(), new_password, textViewUserBirthday.getText().toString(), textViewUserGoal.getText().toString());
                        break;
                    case "update_goal":
                        user_connected.updateDataUserConnected(Integer.parseInt(user_id), user_lastname, user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), new_goal);
                        user_connected.updateDataUser(Integer.parseInt(user_id), user_lastname, user_firstname, textViewUserEmail.getText().toString(), textViewUserPassword.getText().toString(), textViewUserBirthday.getText().toString(), new_goal);
                        textViewUserGoal.setText(new_goal);
                        Intent go_before = new Intent(getApplicationContext(), UserProfilActivity.class);
                        startActivity(go_before);
                        finish();
                        break;
                }
            }
        }
    }
}
