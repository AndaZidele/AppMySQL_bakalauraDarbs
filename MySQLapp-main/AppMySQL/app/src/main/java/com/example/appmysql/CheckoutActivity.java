package com.example.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmysql.API.RetrofitProduct;
import com.example.appmysql.API.RetrofitUser;
import com.example.appmysql.API.UserAPI;
import com.example.appmysql.Adapters.Cart;
import com.example.appmysql.Adapters.User;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CheckoutActivity extends AppCompatActivity {

    TextView productsNames, totalCena, toHome;
    EditText userN, userE, userPh, userA;
    MaterialButton btnMakeOrder;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI, myAPIforRegister;

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
        setContentView(R.layout.activity_checkout);


        toHome = (TextView) findViewById(R.id.toHomeFromCheckout);
        toHome.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        Retrofit retrofit = RetrofitProduct.getInstance();
        myAPI = getAPI();

        Retrofit retrofitUser = RetrofitUser.getInstance();
        myAPIforRegister = retrofitUser.create(UserAPI.class);

        getUserId();

        btnMakeOrder = (MaterialButton) findViewById(R.id.btnMakeOrder);
        btnMakeOrder.setOnClickListener(view -> {
            getUserIdOrderam();
            //1)Order: userId, userName, userEmail, userPhone, userAddress, productsName(String virkne), orderPrice, datums, statuss(true/false=piegadats/nepiegadats)

//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
        });

    }
        private UserAPI getAPI() {
            return RetrofitProduct.getInstance().create(UserAPI.class);
        }

        private void makeProdIds ( int user_id){
            compositeDisposable.add(myAPI.getUserCart()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.functions.Consumer<List<Cart>>() {
                                   @Override
                                   public void accept(List<Cart> people) throws Exception {
                                       float cenaVienam, cenaKopa = 0;
                                       String produktuVirkne = "";

                                       Iterator<Cart> itr = people.iterator();
                                       while (itr.hasNext()) {

                                           cenaVienam = 0;
                                           Cart person = itr.next();

                                           int produktaUsers = person.getUsers_id();
                                           if (produktaUsers != user_id) {
                                               itr.remove();
                                           } else {
                                               if (produktuVirkne.equals("")) {
                                                   if (person.getAmount() == 1) {
                                                       produktuVirkne = produktuVirkne + String.valueOf(person.getId());
                                                   } else {
                                                       produktuVirkne = produktuVirkne + String.valueOf(person.getId()) + " ( x " + String.valueOf(person.getAmount()) + ")";

                                                   }
                                               } else {
                                                   if (person.getAmount() == 1) {
                                                       produktuVirkne = produktuVirkne + "; " + String.valueOf(person.getId());
                                                   } else {
                                                       produktuVirkne = produktuVirkne + "; " + String.valueOf(person.getId()) + " ( x " + String.valueOf(person.getAmount()) + ")";

                                                   }

                                               }
                                               cenaVienam = person.getAmount() * person.getPrice();


                                           }

                                           cenaKopa = cenaKopa + cenaVienam;
                                       }
                                       String cenina = new DecimalFormat("####.##").format(cenaKopa);

                                       userA = (EditText) findViewById(R.id.changeAddress);
                                       String address = String.valueOf(userA.getText());

                                       if (address.equals("")) {
                                           Toast.makeText(CheckoutActivity.this, "Please Write Your Address", Toast.LENGTH_SHORT).show();
                                       } else {
                                           makeOrder(user_id, produktuVirkne, cenina);
                                           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                           startActivity(intent);
                                       }
                                   }
//                               }, new Consumer<Throwable>() {
//                                   @Override
//                                   public void accept(Throwable throwable) throws Exception {
//                                       Toast.makeText(CheckoutActivity.this, "Not found from All Products", Toast.LENGTH_SHORT).show();
//                                   }
                               }
                    ));

        }
        private void makeOrder ( int user_id, String prod_ids, String cena){
            userN = (EditText) findViewById(R.id.changeName);
            userE = (EditText) findViewById(R.id.changeEmail);
            userPh = (EditText) findViewById(R.id.changePhone);
            userA = (EditText) findViewById(R.id.changeAddress);
            String email = String.valueOf(userE.getText());
            String phone = String.valueOf(userPh.getText());
            String address = String.valueOf(userA.getText());
            String name = String.valueOf(userN.getText());
            productsNames = (TextView) findViewById(R.id.checkout_txt_name);
            String prod_names = String.valueOf(productsNames.getText());
            String userId = String.valueOf(user_id);

            compositeDisposable.add(myAPIforRegister.registerOrder(userId, name, email, phone, address, prod_names, prod_ids, cena)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.functions.Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
//                            Toast.makeText(CheckoutActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                            sendUserToNextView();
                        }
                    }));


        }

        private void getUserId () {//String user_email){
            String epa = PrefConfig.loadEpasts(this);
            userN = (EditText) findViewById(R.id.changeName);
            userE = (EditText) findViewById(R.id.changeEmail);
            userPh = (EditText) findViewById(R.id.changePhone);
            userA = (EditText) findViewById(R.id.changeAddress);

            compositeDisposable.add(myAPI.getUserList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.functions.Consumer<List<User>>() {
                                   @Override
                                   public void accept(List<User> people) throws Exception {

                                       String vajadzigaisEpasts = epa;//"lll@l.com";//user_email;////so te dabut no ielogosanas
                                       Iterator<User> itr = people.iterator();
                                       while (itr.hasNext()) {

//                                       cenaVienam = 0;
                                           User person = itr.next();

                                           String listesProduktaKategorija = person.getEmail();
                                           if ((listesProduktaKategorija.equals(vajadzigaisEpasts)) != true) {
                                               itr.remove();
                                           } else {

                                               int userID = person.getId();
//                                           String izvadit = String.valueOf(userID);
//                                           float priceFl = Float.parseFloat(strPrice);
//                                           addToCart(prod_id, userID, amount, name, price);
//                                           Toast.makeText(CartActivity.this, izvadit+" ", Toast.LENGTH_SHORT).show();
//                                           addToCart(prodId, userID, 1, strName, priceFl);

                                               userN.setText(person.getName());
                                               userE.setText(person.getEmail());
                                               userPh.setText(person.getPhone());
                                               userA.setText(person.getAddress());

                                               getAllCart(userID);
//
                                           }
                                       }

//                                   int produkta_id = people.get(2).getId();
//                                   Toast.makeText(ProductsActivity.this, "TE: " + produkta_id, Toast.LENGTH_SHORT).show();
                                   }


//                               }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            Toast.makeText(CartActivity.this, "Not found from All Products" , Toast.LENGTH_SHORT).show();
//                        }
                               }
                    ));
        }

        private void sendUserToNextView () {
            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
//        intent.putExtra("thisUsersEmail",  epastins);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        private void getUserIdOrderam () {//String user_email){
            String epa = PrefConfig.loadEpasts(this);
            userN = (EditText) findViewById(R.id.changeName);
            userE = (EditText) findViewById(R.id.changeEmail);
            userPh = (EditText) findViewById(R.id.changePhone);
            userA = (EditText) findViewById(R.id.changeAddress);

            compositeDisposable.add(myAPI.getUserList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.functions.Consumer<List<User>>() {
                                   @Override
                                   public void accept(List<User> people) throws Exception {

                                       String vajadzigaisEpasts = epa;//"lll@l.com";//user_email;////so te dabut no ielogosanas
                                       Iterator<User> itr = people.iterator();
                                       while (itr.hasNext()) {

//                                       cenaVienam = 0;
                                           User person = itr.next();

                                           String listesProduktaKategorija = person.getEmail();
                                           if ((listesProduktaKategorija.equals(vajadzigaisEpasts)) != true) {
                                               itr.remove();
                                           } else {

                                               int userID = person.getId();
//                                           String izvadit = String.valueOf(userID);
//                                           float priceFl = Float.parseFloat(strPrice);
//                                           addToCart(prod_id, userID, amount, name, price);
//                                           Toast.makeText(CartActivity.this, izvadit+" ", Toast.LENGTH_SHORT).show();
//                                           addToCart(prodId, userID, 1, strName, priceFl);


                                               makeProdIds(userID);//te vjg datus iekavas
//
                                           }
                                       }

//                                   int produkta_id = people.get(2).getId();
//                                   Toast.makeText(ProductsActivity.this, "TE: " + produkta_id, Toast.LENGTH_SHORT).show();
                                   }


//                               }, new Consumer<Throwable>() {
//                                   @Override
//                                   public void accept(Throwable throwable) throws Exception {
//                                       Toast.makeText(CheckoutActivity.this, "Not found from All Products", Toast.LENGTH_SHORT).show();
//                                   }
                               }
                    ));
        }

        private void getAllCart ( int thisUserId){
            productsNames = (TextView) findViewById(R.id.checkout_txt_name);
            totalCena = (TextView) findViewById(R.id.checkout_txt_price);

            compositeDisposable.add(myAPI.getUserCart()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.functions.Consumer<List<Cart>>() {
                                   @Override
                                   public void accept(List<Cart> people) throws Exception {
                                       float cenaVienam, cenaKopa = 0;
                                       String produktuVirkne = "";

                                       Iterator<Cart> itr = people.iterator();
                                       while (itr.hasNext()) {

                                           cenaVienam = 0;
                                           Cart person = itr.next();

                                           int produktaUsers = person.getUsers_id();
                                           if (produktaUsers != thisUserId) {
                                               itr.remove();
                                           } else {
                                               if (produktuVirkne.equals("")) {
                                                   if (person.getAmount() == 1) {
                                                       produktuVirkne = produktuVirkne + person.getName();
                                                   } else {
                                                       produktuVirkne = produktuVirkne + person.getName() + " (" + String.valueOf(person.getAmount()) + ")";

                                                   }
                                               } else {
                                                   if (person.getAmount() == 1) {
                                                       produktuVirkne = produktuVirkne + "; " + person.getName();
                                                   } else {
                                                       produktuVirkne = produktuVirkne + "; " + person.getName() + " (" + String.valueOf(person.getAmount()) + ")";

                                                   }

                                               }
                                               cenaVienam = person.getAmount() * person.getPrice();


                                           }

                                           cenaKopa = cenaKopa + cenaVienam;
                                       }
//                                   totalCena = (TextView) findViewById(R.id.total_id);
                                       String cenina = new DecimalFormat("####.##").format(cenaKopa);
                                       totalCena.setText("Price: " + cenina + " EUR");
                                       productsNames.setText(produktuVirkne);

//                                       Intent intent = new Intent(getApplicationContext(), CartActivity.class);
//                                       startActivity(intent);

                                   }
//                               }, new Consumer<Throwable>() {
//                                   @Override
//                                   public void accept(Throwable throwable) throws Exception {
//                                       Toast.makeText(CheckoutActivity.this, "Not found from All Products", Toast.LENGTH_SHORT).show();
//                                   }
                               }
                    ));
        }
    }
