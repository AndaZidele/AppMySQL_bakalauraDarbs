package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmysql.API.RetrofitProduct;
import com.example.appmysql.API.UserAPI;
import com.example.appmysql.Adapters.Cart;
//import com.example.appmysql.Adapters.CartsAdapter;
import com.example.appmysql.Adapters.Order;
import com.example.appmysql.Adapters.OrdersAdapter;
import com.example.appmysql.Adapters.User;
import com.example.appmysql.API.UserAPI;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;

    RecyclerView recycler_search;
    LinearLayoutManager layoutManager;
    OrdersAdapter adapter;

    MaterialButton logOut, settings;
    TextView toHome;

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
    //
//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toHome = (TextView) findViewById(R.id.toHomeFromProfile);
        toHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        settings = (MaterialButton) findViewById(R.id.btnSettings);
        settings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

        logOut = (MaterialButton) findViewById(R.id.btnLogout);
        logOut.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PrefConfig.saveUserEmail(getApplicationContext(), "Not Logged In");
            startActivity(intent);
        });

        Retrofit retrofit = RetrofitProduct.getInstance();
        myAPI = getAPI();

        recycler_search = (RecyclerView) findViewById(R.id.profile_recycle);
        layoutManager = new LinearLayoutManager(this);
        recycler_search.setLayoutManager(layoutManager);
        recycler_search.setHasFixedSize(true);
        recycler_search.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        getUserId();

    }

    private UserAPI getAPI() {
        return RetrofitProduct.getInstance().create(UserAPI.class);
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
                                           getAllCartO(userID);
                                       }
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
//                                   Toast.makeText(CartActivity.this, "Not found from All Products" , Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }


    private void getAllCartO(int thisUserId){
        compositeDisposable.add(myAPI.getUserOrder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Order>>() {
                               @Override
                               public void accept(List<Order> people) throws Exception {
                                   Iterator<Order> itr = people.iterator();
                                   while(itr.hasNext()){

//                                       cenaVienam = 0;
                                       Order person = itr.next();

                                       int produktaUsers = person.getUser_id();
                                       if (produktaUsers != thisUserId) {
                                           itr.remove();
                                       } else {

                                       }

                                   }
                                   adapter = new OrdersAdapter(people);
                                   recycler_search.setAdapter(adapter);

//                                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                       startActivity(intent);

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(ProfileActivity.this, "Not found from All Products" + thisUserId, Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }
}