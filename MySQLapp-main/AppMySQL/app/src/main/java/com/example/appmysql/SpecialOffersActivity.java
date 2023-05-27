package com.example.appmysql;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import com.example.appmysql.API.RetrofitProduct;
import com.example.appmysql.API.UserAPI;
//import Adapters.Product;

import com.example.appmysql.Adapters.Product;
import com.example.appmysql.Adapters.ProductsAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SpecialOffersActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;

    RecyclerView recycler_search;
    LinearLayoutManager layoutManager;
    ProductsAdapter adapter;
    TextView userEmail, toH;


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_offers);

        toH = (TextView) findViewById(R.id.toHomeFromSpOffers);
        toH.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        Intent inte = getIntent();
        String user_email = inte.getStringExtra("user_email");// lietotaja epasts
        userEmail = (TextView) findViewById(R.id.user_email_id);
        userEmail.setText(user_email);

        Retrofit retrofit = RetrofitProduct.getInstance();
        myAPI = getAPI();

        recycler_search = (RecyclerView) findViewById(R.id.recycler_searchSp);

        layoutManager = new LinearLayoutManager(this);


        recycler_search.setLayoutManager(layoutManager);
        recycler_search.setHasFixedSize(true);
//        adapter = new ProductAdapter(List<Product>);
//        recycler_search.setAdapter(adapter);
        recycler_search.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        getAllPerson();

    }

    private UserAPI getAPI() {
        return RetrofitProduct.getInstance().create(UserAPI.class);
    }


    private void getAllPerson() {
//        Toast.makeText(ProductsActivity.this, "Ieejam seit", Toast.LENGTH_SHORT).show();

//        Intent category = getIntent();
//        String kategorija = category.getStringExtra("categoryName");
        compositeDisposable.add(myAPI.getProductList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Product>>() {
                               @Override
                               public void accept(List<Product> people) throws Exception {
//                                   String vajadzigaKategorija = kategorija;
                                   Iterator<Product> itr = people.iterator();
                                   while(itr.hasNext()){
                                       Product person = itr.next();
                                       Log.d(TAG,"Te:"+String.valueOf(person.getSpecial_offer()));

                                       int bool = person.getSpecial_offer();
//                                       int val = (bool) ? 1 : 0;
//                                       String listesProduktaKategorija = person.getSpecial_offer();
                                       if (bool == 0) {
                                           itr.remove();
                                       } else { }
                                   }
                                   adapter = new ProductsAdapter(people);
                                   recycler_search.setAdapter(adapter);

//                                   Intent intent = new Intent(getApplicationContext(), DoneActivity.class);
//                                   startActivity(intent);

//                                   int produkta_id = people.get(2).getId();
//                                   Toast.makeText(ProductsActivity.this, "TE: " + produkta_id, Toast.LENGTH_SHORT).show();



                               }
                               /* @Override
                                public void accept(List<Product> people) throws Exception {
                                    String vajadzigaKategorija = kategorija;
                                    Iterator<Product> itr = people.iterator();
                                    while(itr.hasNext()){
                                        Product person = itr.next();
                                        String listesProduktaKategorija = person.getCategory();
                                        if ((listesProduktaKategorija.equals(vajadzigaKategorija)) != true) {
                                            itr.remove();
                                        } else { }
                                    }
                                    adapter = new ProductsAdapter(people);
                                    recycler_search.setAdapter(adapter);
                                }*/
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(SpecialOffersActivity.this, "Not found from All Products", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }
}