package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmysql.API.RetrofitProduct;
import com.example.appmysql.API.UserAPI;
import com.example.appmysql.Adapters.User;
import com.google.android.material.button.MaterialButton;

import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SettingsActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;

    TextView name, email, phone, pass, address, toHome;
    Button btnN, btnE, btnPh, btnPass, btnA;
    MaterialButton delUser;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toHome = (TextView) findViewById(R.id.toHFromSet);
        toHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        name = (TextView) findViewById(R.id.settingsName);
        email = (TextView) findViewById(R.id.settingsEmail);
        phone = (TextView) findViewById(R.id.settingsPhone);
        pass = (TextView) findViewById(R.id.settingsPass);
        address = (TextView) findViewById(R.id.settingsAddress);

//        String epa = PrefConfig.loadEpasts(this);
        Retrofit retrofit = RetrofitProduct.getInstance();
        myAPI = getAPI();//retrofit.create(UserAPI.class);


        getUsersData();

        delUser = (MaterialButton) findViewById(R.id.deleteAccount);
        delUser.setOnClickListener(view->{
           // String email = String.valueOf(PrefConfig.loadEpasts(this));

            getUserId();


        });

    }
    private UserAPI getAPI() {
        return RetrofitProduct.getInstance().create(UserAPI.class);
    }

    private void deleteU(int uId){
        String delUId = String.valueOf(uId);
        compositeDisposable.add(myAPI.deleteUser(delUId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        PrefConfig.saveUserEmail(getApplicationContext(), "Not Logged In");
                        startActivity(intent);
                    }
                }));
    }

    private void getUserId(){//String user_email){
        String epa = PrefConfig.loadEpasts(this);
        compositeDisposable.add(myAPI.getUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                               @Override
                               public void accept(List<User> people) throws Exception {
                                   String vajadzigaisEpasts = epa;
                                   Iterator<User> itr = people.iterator();
                                   while(itr.hasNext()){
                                       User person = itr.next();
                                       String listesProduktaKategorija = person.getEmail();
                                       if ((listesProduktaKategorija.equals(vajadzigaisEpasts)) != true) {
                                           itr.remove();
                                       } else {

                                           int userID = person.getId();
                                           deleteU(userID);
                                       }
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(SettingsActivity.this, "Not found from All Products" , Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }



    private void getUsersData(){
        String epa = PrefConfig.loadEpasts(this);

        //)int prod_id,int amount, String name, float price, String user_email)
        compositeDisposable.add(myAPI.getUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                               @Override
                               public void accept(List<User> people) throws Exception {
                                   String vajadzigaisEpasts = epa;//"lll@l.com";//user_email;////so te dabut no ielogosanas
                                   Iterator<User> itr = people.iterator();
                                   while(itr.hasNext()){

//                                       cenaVienam = 0;
                                       User person = itr.next();

                                       String listesProduktaKategorija = person.getEmail();
                                       if ((listesProduktaKategorija.equals(vajadzigaisEpasts)) != true) {
                                           itr.remove();
                                       } else {
//                                           int uId = person.getId();

//                                           Toast.makeText(SettingsActivity.this, "Te Esam", Toast.LENGTH_SHORT).show();
//
                                           name.setText("Full Name: " + person.getName());
                                           email.setText("Email: " +person.getEmail());
                                           phone.setText("Phone: " +person.getPhone());
                                           pass.setText("Password: " +person.getPassword());
                                           address.setText("Address: " +person.getAddress());
                                       }
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(SettingsActivity.this, "Not found from All Products" , Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }

}