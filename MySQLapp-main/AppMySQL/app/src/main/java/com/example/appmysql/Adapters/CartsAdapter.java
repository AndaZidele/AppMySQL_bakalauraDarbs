package com.example.appmysql.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmysql.API.RetrofitUser;
import com.example.appmysql.API.UserAPI;
import com.example.appmysql.CartActivity;
import com.example.appmysql.OneProductActivity;
import com.example.appmysql.PrefConfig;
import com.example.appmysql.R;
import com.google.android.material.button.MaterialButton;

import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CartsAdapter extends RecyclerView.Adapter<CartsAdapter.MyViewHolderCart>{

    List<Cart> cartList;

    public CartsAdapter(List<Cart> cartList) {this.cartList = cartList;}

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;

    @NonNull
    @Override
    public MyViewHolderCart onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_table_view, viewGroup, false);

        return new MyViewHolderCart(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCart myViewHolder, int i) {
//        myViewHolder.name.setText(productList.get(i).getName());
//        myViewHolder.price.setText(productList.get(i).getPrice());
//        myViewHolder.description.setText(productList.get(i).getDescription());

        myViewHolder.name.setText(cartList.get(i).getName());

        myViewHolder.price.setText((cartList.get(i).getPrice()).toString());
        myViewHolder.amount.setText(String.valueOf(cartList.get(i).getAmount()));

        String prIdSt = String.valueOf(cartList.get(i).getId());
        String usIdSt = String.valueOf(cartList.get(i).getUsers_id());
        String am = String.valueOf(cartList.get(i).getAmount());

        myViewHolder.btnPlus.setOnClickListener((v->{
            Intent cardViewsActivity = new Intent(myViewHolder.name.getContext(), CartActivity.class);

            incProduct(prIdSt, usIdSt, am);
            //vajadzetu reloadoties sim skatam ja nesanak, tad tikai ka seit pamaina skaitu un kopejo cenu
            myViewHolder.name.getContext().startActivity(cardViewsActivity);
        }));

        myViewHolder.btnMinus.setOnClickListener((v->{
            Intent cardViewsActivity = new Intent(myViewHolder.name.getContext(), CartActivity.class);
            //funkcija kur skaitu samazina pa 1
            if (Integer.parseInt(am)==1){
                deleteProduct( prIdSt, usIdSt);
            } else {
                decProduct(prIdSt, usIdSt, am);
            }
            //vajadzetu reloadoties sim skatam ja nesanak, tad tikai ka seit pamaina skaitu un kopejo cenu
            myViewHolder.name.getContext().startActivity(cardViewsActivity);
        }));


//            myViewHolder.btnEdt.setOnClickListener((v->{
//            Intent cardViewsActivity = new Intent(myViewHolder.name.getContext(), OneProductActivity.class);
//            //String str = myMovieDataList.getMovieName().toString();
//            cardViewsActivity.putExtra("name",  productList.get(i).getName());
//            String iToS = Integer.toString(productList.get(i).getId());
//            cardViewsActivity.putExtra("id", iToS);
//            myViewHolder.name.getContext().startActivity(cardViewsActivity);
//        }));

//        myViewHolder.btnDelete.setOnClickListener((v->{
//            Intent cardViewsActivity = new Intent(myViewHolder.name.getContext(), DeleteActivity.class);
//            String iToS = Integer.toString(productList.get(i).getId());
//            cardViewsActivity.putExtra("id", iToS);
//            cardViewsActivity.putExtra("name",  productList.get(i).getName());
//            myViewHolder.name.getContext().startActivity(cardViewsActivity);
//        }));

        myViewHolder.btnDel.setOnClickListener((v->{
            Intent cardViewsActivity = new Intent(myViewHolder.name.getContext(), CartActivity.class);

            //1)sakuma dabut produktsa id no tabulas product
            //2) tad usera id no tabulas user
//            if ((cartList.get(i).getAmount()) == 1){
            deleteProduct( prIdSt, usIdSt);


            // bet ja amount jau ir 1 tad paradas pazinjojuma logs vai
            // velaties dzest so produktu no groza un ja ja
            //tad aiziet uz deleteProduct funkciju

//            cardViewsActivity.putExtra("name",  productList.get(i).getName());
//            String iToS = Integer.toString(productList.get(i).getId());
//            cardViewsActivity.putExtra("id", iToS);
            //vajadzetu reloadoties sim skatam ja nesanak, tad tikai ka seit pamaina skaitu un kopejo cenu
            myViewHolder.name.getContext().startActivity(cardViewsActivity);
        }));



    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MyViewHolderCart extends RecyclerView.ViewHolder {
        CardView c_root_view;
        TextView name, price, amount; //vel pievienot visus
        MaterialButton btnPlus, btnMinus, btnDel;
        //
        public MyViewHolderCart(@NonNull View itemView) {
            super(itemView);



            c_root_view = (CardView) itemView.findViewById(R.id.cart_root_view);
            name = (TextView) itemView.findViewById(R.id.cart_product_name);
            price = (TextView) itemView.findViewById(R.id.cart_product_price);
            amount = (TextView) itemView.findViewById(R.id.cart_product_amount);

            btnMinus = (MaterialButton) itemView.findViewById(R.id.btn_minuss);
            btnPlus = (MaterialButton) itemView.findViewById(R.id.btn_plus);
            btnDel = (MaterialButton) itemView.findViewById(R.id.btn_delete_product);

            Retrofit retrofitUser = RetrofitUser.getInstance();
            myAPI = retrofitUser.create(UserAPI.class);

//            String epa = PrefConfig.loadEpastsCart(CartActivity.class);

        }
    }


    private void incProduct(String product, String user, String amount){
        compositeDisposable.add(myAPI.incProduct(product, user, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
//                        Toast.makeText(CartsAdapter.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    private void decProduct(String product, String user, String amount){
        compositeDisposable.add(myAPI.decProduct(product, user, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
//                        Toast.makeText(CartsAdapter.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    private void deleteProduct(String product, String user){
        compositeDisposable.add(myAPI.deleteProduct(product, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
//                        Toast.makeText(CartsAdapter.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    public void deleteItem(int position){
        this.cartList.remove(position);
        notifyItemRemoved(position);
    }

/*
    private void getUsersIdInc(){


//        Intent oneProduct = getIntent();
////        String user_email = oneProduct.getStringExtra("user_email");// lietotaja epasts
//        String strName = oneProduct.getStringExtra("oneName");
//        Float strPrice = oneProduct.getFloatExtra("onePrice", 0);
////        String strImage = oneProduct.getStringExtra("oneImage");
//        String strDescr = oneProduct.getStringExtra("oneDescr");
//        int strId = oneProduct.getIntExtra("oneId", 0);
//
//        int prodId = strId;
//        String user_email = epa;

//        Toast.makeText(OneProductActivity.this, "Te esam" , Toast.LENGTH_SHORT).show();
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

                                           int userID = person.getId();
//                                           float priceFl = Float.parseFloat(strPrice);
//                                           addToCart(prod_id, userID, amount, name, price);
//                                           Toast.makeText(OneProductActivity.this, prodId  +" "+ userID  +" "+ 1  +" "+strName +" "+ strPrice, Toast.LENGTH_SHORT).show();
                                          // addToCart(prodId, userID, 1, strName, strPrice);

                                       }
                                   }
                               }

                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
//                                   Toast.makeText(OneProductActivity.this, "Not found from All Products" , Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }
*/
}