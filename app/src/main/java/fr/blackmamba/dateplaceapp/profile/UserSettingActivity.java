package fr.blackmamba.dateplaceapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import fr.blackmamba.dateplaceapp.R;

public class UserSettingActivity extends AppCompatActivity {

    protected ImageView button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        this.button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(v -> {
            Intent back = new Intent(getApplicationContext(), UserProfilActivity.class);
            startActivity(back);
            finish();
        });
    }


    @Override
    public void onBackPressed()
    {
        Intent back = new Intent(getApplicationContext(), UserProfilActivity.class);
        startActivity(back);
        finish();
    }
}
