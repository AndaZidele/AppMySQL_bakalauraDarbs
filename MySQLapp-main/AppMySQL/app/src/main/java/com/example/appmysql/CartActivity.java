package com.example.appmysql;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmysql.API.RetrofitProduct;
import com.example.appmysql.API.UserAPI;
import com.example.appmysql.Adapters.Cart;
import com.example.appmysql.Adapters.CartsAdapter;
import com.example.appmysql.Adapters.User;
import com.example.appmysql.Adapters.Cart;
import com.example.appmysql.Adapters.Product;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

//public String ADR = "adr";
public class CartActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;

    RecyclerView recycler_search;
    LinearLayoutManager layoutManager;
    CartsAdapter adapter;

    TextView totalCena, toHome;

    MaterialButton btnOrder;

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
        setContentView(R.layout.activity_cart);

        toHome = (TextView) findViewById(R.id.toHomeFromCart);
        toHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        Retrofit retrofit = RetrofitProduct.getInstance();
        myAPI = getAPI();

        String epa = PrefConfig.loadEpasts(this);

        recycler_search = (RecyclerView) findViewById(R.id.productListInCart);
        layoutManager = new LinearLayoutManager(this);
        recycler_search.setLayoutManager(layoutManager);
        recycler_search.setHasFixedSize(true);
        recycler_search.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));



//        float kopejaCena = 0;
//        String epa = PrefConfig.loadEpasts(this);
//        if (epa.equals("Not Logged In") == true){
//
//            //Snackbar mySnackbar = Snackbar;//.make(view, stringId, duration);
//
//            String nav = "To Add Product To Cart You Have To Login!";
////                        Snackbar.make(findViewById(R.id.myCoordinatorLayout), nav,
////                                        Snackbar.LENGTH_SHORT)
////                                .show();
//            Toast.makeText(OneProductActivity.this, nav, Toast.LENGTH_LONG).show();
//        } else {
        getUserId();//prodId, 1, strName, pri, user_email);

        btnOrder = (MaterialButton) findViewById(R.id.btnMakeOrder);
        btnOrder.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
//            intent.putExtra("user_email",  user_email);
            startActivity(intent);
        });
//        }

//        String email = "2";
//        String esosaLietotajaEpasts = "lll@l.com";
//       getUserId();//esosaLietotajaEpasts);


//        getAllCart(2);


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
                                           getAllCart(userID);
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

    private void getAllCart(int thisUserId){
        String epa = PrefConfig.loadEpasts(this);
        compositeDisposable.add(myAPI.getUserCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> people) throws Exception {
                                   float cenaVienam, cenaKopa = 0;
                                   Iterator<Cart> itr = people.iterator();
                                   while(itr.hasNext()){
                                       cenaVienam = 0;
                                       Cart person = itr.next();
                                       int produktaUsers = person.getUsers_id();
                                       if (produktaUsers != thisUserId) {
                                           itr.remove();
                                       } else {
                                           cenaVienam = person.getAmount() * person.getPrice();
                                       }
                                       cenaKopa = cenaKopa + cenaVienam;
                                   }
//                        Toast.makeText(CartActivity.this, "Te:" + thisUserId, Toast.LENGTH_SHORT).show();


                        totalCena = (TextView) findViewById(R.id.total_id);
                                   totalCena.setText(new DecimalFormat("####.##").format(cenaKopa));
                                   adapter = new CartsAdapter(people);
                                   recycler_search.setAdapter(adapter);

//                        Intent intent = new Intent(getApplicationContext(), DoneActivity.class);
//                                   startActivity(intent);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
//                                   Toast.makeText(CartActivity.this, "Not found from All Products" + thisUserId, Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }
}