package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class HaventLoginActivity extends AppCompatActivity {

    MaterialButton toLog, toReg;
    TextView toHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_havent_login);

        toHome = (TextView) findViewById(R.id.toHomeFromHavent);
        toHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        toLog = (MaterialButton) findViewById(R.id.toLoginFromHaventLog);
        toReg = (MaterialButton) findViewById(R.id.toRegisterFromHaventReg);

        toLog.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        toReg.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }
}