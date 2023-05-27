package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    TextView regToHome, regToLog;
    EditText fullName, email, phone, pass, confPass;
    MaterialButton btnReg;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Retrofit retrofitUser = RetrofitUser.getInstance();
        myAPI = retrofitUser.create(UserAPI.class);

        regToHome = (TextView) findViewById(R.id.toRegisterMenu);
        regToHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });
        regToLog = (TextView) findViewById(R.id.userGoToLoginPage);
        regToLog.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });

        fullName = (EditText) findViewById(R.id.userFullName);
        email = (EditText) findViewById(R.id.userEmail);
        phone = (EditText) findViewById(R.id.userPhone);
        pass = (EditText) findViewById(R.id.userPassword);
        confPass = (EditText) findViewById(R.id.userConfirmPassword);
        btnReg = (MaterialButton) findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }

    private void registerUser(){
        String epasts = email.getText().toString();
        String parole = pass.getText().toString();
        String confParole = confPass.getText().toString();
        String telefons = phone.getText().toString();
        String vards = fullName.getText().toString();

        if(TextUtils.isEmpty(epasts)){
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(parole)){
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(confParole)){
            Toast.makeText(this, "Confirm Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(telefons)){
            Toast.makeText(this, "Phone cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(vards)){
            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }


        compositeDisposable.add(myAPI.registerUser(epasts,vards,telefons,parole)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(RegisterActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        PrefConfig.saveUserEmail(getApplicationContext(), epasts);
                        sendUserToNextView();
                    }
                }));


      /*  compositeDisposable.add(myAPI.registerUserProducts(telefons)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
//                        Toast.makeText(RegisterActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                        //ja izdodas:
                        sendUserToNextView();

                    }
                }));*/




    }

    private void sendUserToNextView() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//        intent.putExtra("thisUsersEmail",  epastins);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}