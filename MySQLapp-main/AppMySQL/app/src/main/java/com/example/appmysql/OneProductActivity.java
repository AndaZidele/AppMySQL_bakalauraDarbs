package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.appmysql.API.RetrofitCart;
import com.example.appmysql.API.RetrofitProduct;
import com.example.appmysql.API.RetrofitUser;
import com.example.appmysql.API.UserAPI;
import com.example.appmysql.Adapters.Cart;
//import com.example.appmysql.Adapters.CartsAdapter;
import com.example.appmysql.Adapters.Product;
import com.example.appmysql.Adapters.ProductsAdapter;
import com.example.appmysql.Adapters.User;
import com.google.android.material.button.MaterialButton;

import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class OneProductActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI, myAPIforAddUpdate;

//    int produkta_id=0;

    RecyclerView recycler_search;
    LinearLayoutManager layoutManager;
    ProductsAdapter adapter;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

//    @Override
//    protected void onDestroy() {
//        compositeDisposable.clear();
//        super.onDestroy();
//    }

    TextView description, price, name, toH;
    ImageView image;
    MaterialButton addTo;
//    String saraksts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_product);

        toH = (TextView) findViewById(R.id.oneProductToMenu);
        toH.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        //Init API
        Retrofit retrofit = RetrofitProduct.getInstance();
        myAPI = getAPI();//retrofit.create(UserAPI.class);

        String epa = PrefConfig.loadEpasts(this);

//        Retrofit retrofitc = RetrofitCart.getInstance();
//        myAPI = getAPI();
        //Vajadzes ari retrofitUser!!!
        //Init API
        Retrofit retrofitForAddAndUpdate = RetrofitUser.getInstance();
        myAPIforAddUpdate = retrofitForAddAndUpdate.create(UserAPI.class);


//        Layout r_lay = (Layout) findViewById(R.layout.activity_cart);
//        recycler_search = (RecyclerView) r_lay.findViewById(R.id.productListInCart);
//        recycler_search = new RecyclerView(this);
//        layoutManager = new LinearLayoutManager(this);
//        recycler_search.setLayoutManager(layoutManager);
//        recycler_search.setHasFixedSize(true);
//        recycler_search.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));


        name = (TextView) findViewById(R.id.productName);
        price = (TextView) findViewById(R.id.productPrice);
        image = (ImageView) findViewById(R.id.productImage);
        description = (TextView) findViewById(R.id.productDescription);
        addTo = (MaterialButton) findViewById(R.id.btnAddToCart);
        //lietotājam bus jauns pasutijums ar id, kam bus produktu saraksts ar to id

        Intent oneProduct = getIntent();
//        String user_email = oneProduct.getStringExtra("user_email");// lietotaja epasts
        String strName = oneProduct.getStringExtra("oneName");
        float strPrice = oneProduct.getFloatExtra("onePrice", 0);
//        String strImage = oneProduct.getStringExtra("oneImage");
        String strDescr = oneProduct.getStringExtra("oneDescr");
        int strId = oneProduct.getIntExtra("oneId",0);
//
//        if (strName!=null && strPrice!=null && strDescr!=null){ //&& strImage!=null
            name.setText(strName);
            price.setText(strPrice+ " EUR");
//            image.setImageResource(Integer.parseInt(strImage));
            description.setText(strDescr);
//        }
//        String user = "3"; //Te japarliecinas vai lietotajs ir ielogojies un japanjem vinja id
//        String product_id = strId.toString();

        int prodId = strId;
//        float pri = Float.parseFloat(strPrice);


//        addTo.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//
//                //nakamaja rinda dati ir jamaina un nakama rinda jaliek getAllCart funkcija pie add!!!
////                float fl = (float) 9.09;
////                addToCart(4, 4, 1, "Zils zimulis", fl);
//
//                getAllCart();
//
//                Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
////                intent.putExtra("user_email",  userLE.getText().toString());
////                startActivity(intent);
//
//            }
//        });

        addTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

//                    Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
//                intent.putExtra("user_email",  userLE.getText().toString());
//                startActivity(intent);

                if (epa.equals("Not Logged In") == true){

                    //Snackbar mySnackbar = Snackbar;//.make(view, stringId, duration);

                    String nav = "To Add Product To Cart You Have To Login!";
//                        Snackbar.make(findViewById(R.id.myCoordinatorLayout), nav,
//                                        Snackbar.LENGTH_SHORT)
//                                .show();
                    Toast.makeText(OneProductActivity.this, nav, Toast.LENGTH_LONG).show();
                } else {
                    getUsersId();//prodId, 1, strName, pri, user_email);

                }
//                    getAllPerson();
            }
        });


    }
    private UserAPI getAPI() {
        return RetrofitProduct.getInstance().create(UserAPI.class);
    }

    private void getUsersId(){
        String epa = PrefConfig.loadEpasts(this);

        Intent oneProduct = getIntent();
//        String user_email = oneProduct.getStringExtra("user_email");// lietotaja epasts
        String strName = oneProduct.getStringExtra("oneName");
        Float strPrice = oneProduct.getFloatExtra("onePrice", 0);
//        String strImage = oneProduct.getStringExtra("oneImage");
        String strDescr = oneProduct.getStringExtra("oneDescr");
        int strId = oneProduct.getIntExtra("oneId", 0);

        int prodId = strId;
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

                                           int userID = person.getId();
                                           addToCart(prodId, userID, 1, strName, strPrice);

                                       }
                                   }
                               }

                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(OneProductActivity.this, "Not found from All Products" , Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }

    private void addToCart(int id, int users_id, int amount, String name, float price){
        String pId = String.valueOf(id);
        String uId = String.valueOf(users_id);
        String am = String.valueOf(amount);
        String pr = String.valueOf(price);
        compositeDisposable.add(myAPIforAddUpdate.addToCart(pId, uId, am, name, pr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                    Toast.makeText(OneProductActivity.this, "Product is Successfully Added to Cart!", Toast.LENGTH_SHORT).show();


                               }

                           }
                ));
//        Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
////            intent.putExtra("user_email",  user_email);
//        startActivity(intent);
    }


    private void getAllCart() {
        compositeDisposable.add(myAPI.getCartList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                               @Override
                               public void accept(List<Cart> people) throws Exception {
//                                   adapter = new CartsAdapter(people);
//                                   recycler_search.setAdapter(adapter);

                                   int produkta_id;// = people.get(1).getId();

                                   //Toast.makeText(OneProductActivity.this, "TE: " + produkta_id, Toast.LENGTH_SHORT).show();


                                   for (int i=0; i<people.size(); i++) {
                                       //pagaidam product_id ir producta id in cart (groza)
                                       produkta_id = people.get(i).getId();
//                                       Toast.makeText(OneProductActivity.this, "TE: " + produkta_id, Toast.LENGTH_SHORT).show();

                                       if (produkta_id == 10){ //10 ir noklikskinata produkta id
                                           //izpildam update
//                                           Toast.makeText(OneProductActivity.this, "Izpildam UPDATE", Toast.LENGTH_SHORT).show();

                                           updateProduct("1", "4", "3");
                                           break;
                                       } else {
                                           if (i == (people.size() - 1)){
                                               //izpildam add
//                                               Toast.makeText(OneProductActivity.this, "Izpildam ADD", Toast.LENGTH_SHORT).show();

//                                               addToCart(60, 2, 4, "Peldkostims", (float) 12.49);
//                                               addToCart(4, 4, 1, "Zils zimulis", (float)6.88);
                                               registerProduct("Zimulitis", "8", "16", "2.99", "100");
                                               break;
                                           }
                                       }
                                   }

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
//                                   Toast.makeText(CartActivity.this, "Not found from All Products", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }



    private void registerProduct(String name, String id, String user, String price, String amount){

        compositeDisposable.add(myAPIforAddUpdate.registerProduct(name, id, user, price, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(OneProductActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }));


    }
    private void updateProduct(String amount, String product, String user){

        compositeDisposable.add(myAPIforAddUpdate.updateProduct(amount, product, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(OneProductActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }));


    }
}