package fr.blackmamba.dateplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class UserSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
    }

    public void image_setting1 (View view){
        startActivity(new Intent(this, UserSettingActivity.class));
    }
}
