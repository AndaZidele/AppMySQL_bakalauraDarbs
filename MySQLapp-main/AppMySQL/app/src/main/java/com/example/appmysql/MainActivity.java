package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmysql.API.RetrofitUser;
import com.example.appmysql.API.UserAPI;
import com.google.android.material.button.MaterialButton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    MaterialButton toCart, toLog;
    TextView toMenu;
    Button toProd;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String epa = PrefConfig.loadEpasts(this);
        toCart = (MaterialButton) findViewById(R.id.mainToCart);
        toCart.setOnClickListener(view -> {
//            String epa = PrefConfig.loadEpasts(this);
            if (epa.equals("Not Logged In") == true){

                //Snackbar mySnackbar = Snackbar;//.make(view, stringId, duration);
                Intent intent = new Intent(getApplicationContext(), HaventLoginActivity.class);
                startActivity(intent);
//                String nav = "To See Cart You Have To Login!";
//                Toast.makeText(MainActivity.this, nav, Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
//            intent.putExtra("user_email",  user_email);
//            intent.putExtra("thisUsersEmail",  epastins);
                startActivity(intent);
            }
        });


        toLog = (MaterialButton) findViewById(R.id.mainToLogin);
        toLog.setOnClickListener(view -> {

//            addProd();
//            compositeDisposable.add(myAPI.deleteTableProduct("Hi")
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String s) throws Exception {
////                        Toast.makeText(CartsAdapter.this, ""+s, Toast.LENGTH_SHORT).show();
//                        }
//                    }));
            if (epa.equals("Not Logged In") == true){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.putExtra("user_email",  user_email);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                intent.putExtra("thisUsersEmail",  epastins);
                startActivity(intent);
            }
        });

        Retrofit retrofit = RetrofitUser.getInstance();
        myAPI = retrofit.create(UserAPI.class);
        toMenu = (TextView) findViewById(R.id.toMenu);
        toMenu.setOnClickListener(view -> {


            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
//            intent.putExtra("thisUsersEmail",  epastins);
            startActivity(intent);
        });

        toProd = (Button) findViewById(R.id.mainToProducts);
        toProd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });

    }

    private void addProd(){
        compositeDisposable.add(myAPI.registerProdKaFirebase("Te")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                intent.putExtra("thisUsersEmail",  epastins);
                        startActivity(intent);
                    }
                }));



    }
}