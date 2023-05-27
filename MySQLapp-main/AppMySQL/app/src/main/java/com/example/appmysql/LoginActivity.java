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

public class LoginActivity extends AppCompatActivity {

    TextView logToMain, logToReg;
    EditText userLE, userLP;
    MaterialButton btnLog;

    UserAPI myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        setContentView(R.layout.activity_login);

        Retrofit retrofitUser = RetrofitUser.getInstance();
        myAPI = retrofitUser.create(UserAPI.class);

        logToMain = (TextView) findViewById(R.id.loginToMenu);
        logToMain.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });

        userLE = (EditText) findViewById(R.id.userLoginEmail);
        userLP = (EditText) findViewById(R.id.userLoginPassword);
        btnLog = (MaterialButton) findViewById(R.id.btnLogin);
        btnLog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loginUser();
//                loginUser(userLE.getText().toString(),
//                        userLP.getText().toString());


//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("user_email",  userLE.getText().toString());
//                startActivity(intent);
            }
        });

        logToReg = (TextView) findViewById(R.id.userGoToRegisterPage);
        logToReg.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });




    }

    private void loginUser(){
        String email = userLE.getText().toString();
        String parole = userLP.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(parole)){
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else {


            compositeDisposable.add(myAPI.loginUser(email,parole)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            Toast.makeText(LoginActivity.this, ""+response+" "+email, Toast.LENGTH_SHORT).show();
                            PrefConfig.saveUserEmail(getApplicationContext(), email);
                            sendUserToNextView();
                        }
                    }));
        }
    }

    private void sendUserToNextView() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}