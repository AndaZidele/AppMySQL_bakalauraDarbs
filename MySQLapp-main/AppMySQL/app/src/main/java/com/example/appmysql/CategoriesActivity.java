package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmysql.Adapters.Cart;
import com.google.android.material.button.MaterialButton;

public class CategoriesActivity extends AppCompatActivity {

    MaterialButton toM, toW, toCh, toCart, toLogin;
    TextView toH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        toH = (TextView) findViewById(R.id.toHomeFromCategories);
        toH.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

//        toCart = (MaterialButton) findViewById(R.id.cartFromCategories);
//        toCart.setOnClickListener(view -> {
//            String epa = PrefConfig.loadEpasts(this);
//            if (epa.equals("Not Logged In") == true){
//
//                String nav = "To See Cart You Have To Login!";
////
//                Toast.makeText(CategoriesActivity.this, nav, Toast.LENGTH_LONG).show();
//            } else {
//                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
////            intent.putExtra("user_email",  user_email);
////            intent.putExtra("thisUsersEmail",  epastins);
//                startActivity(intent);
//            }
//        });

//        toLogin = (MaterialButton) findViewById(R.id.loginFromCategories);
//        toLogin.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//        });
//
//        toH = (TextView) findViewById(R.id.toHomeFromCategories);
//        toH.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//        });

        toM = (MaterialButton) findViewById(R.id.toMen);
        toM.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
//            intent.putExtra("user_email",  user_email);
            intent.putExtra("categoryName",  "men");
//            intent.putExtra("thisUsersEmail",  epastins);

            startActivity(intent);
        });
        toW = (MaterialButton) findViewById(R.id.toWomen);
        toW.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
//            intent.putExtra("user_email",  user_email);
//            intent.putExtra("thisUsersEmail",  epastins);
            intent.putExtra("categoryName",  "women");
            startActivity(intent);
        });

        toCh = (MaterialButton) findViewById(R.id.toChildren);
        toCh.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
//            intent.putExtra("user_email",  user_email);
//            intent.putExtra("thisUsersEmail",  epastins);
            intent.putExtra("categoryName",  "children");
            startActivity(intent);
        });


    }
}