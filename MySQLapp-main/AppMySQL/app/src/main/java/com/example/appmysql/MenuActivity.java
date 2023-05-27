package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {

    MaterialButton toProfile, toProd, toSpecial, toCart;
    TextView toMain;
    Button toAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

//        Intent inte = getIntent();
//        String epastins = inte.getStringExtra("thisUsersEmail");// lietotaja epasts

        String epa = PrefConfig.loadEpasts(this);
        String notLorR = "Not Logged In";

        toMain = (TextView) findViewById(R.id.toMainViewFromMenu);
        toMain.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        toProd = (MaterialButton) findViewById(R.id.toProductsFromMenu);
        toProd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
//            intent.putExtra("user_email",  user_email);
//            intent.putExtra("thisUsersEmail",  epastins);
            startActivity(intent);
        });

        toSpecial = (MaterialButton) findViewById(R.id.toSpecialOffersFromMenu);
        toSpecial.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SpecialOffersActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });

        toCart = (MaterialButton) findViewById(R.id.toMyCartFromMenu);
        toCart.setOnClickListener(view -> {
//            String epa = PrefConfig.loadEpasts(this);
            if (epa.equals("Not Logged In") == true){
                Intent intent = new Intent(getApplicationContext(), HaventLoginActivity.class);
                startActivity(intent);
                //Snackbar mySnackbar = Snackbar;//.make(view, stringId, duration);
//
//                String nav = "To See Cart You Have To Login!";
////                        Snackbar.make(findViewById(R.id.myCoordinatorLayout), nav,
////                                        Snackbar.LENGTH_SHORT)
////                                .show();
//                Toast.makeText(MenuActivity.this, nav, Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
//            intent.putExtra("user_email",  user_email);
//            intent.putExtra("thisUsersEmail",  epastins);
                startActivity(intent);
            }

        });

        toAbout = (Button) findViewById(R.id.toAboutUsFromMenu);
        toAbout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });

        toProfile = (MaterialButton) findViewById(R.id.toMyProfileFromMenu);
        toProfile.setOnClickListener(view -> {

            Intent intent;
            if (epa.equals("Not Logged In") == true){
                intent = new Intent(getApplicationContext(), HaventLoginActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });


    }
}