package com.example.appmysql.Adapters;


import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmysql.API.UserAPI;
import com.example.appmysql.OneProductActivity;
import com.example.appmysql.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    List<Product> productList;

    public ProductsAdapter(List<Product> productList) {this.productList = productList;}

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserAPI myAPI;


//    Intent inte = getIntent();
//
//    private Intent getIntent() {
//        return inte;
//    }
//
//    String epastins = inte.getStringExtra("thisUsersEmail");// lietotaja epasts
//


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_table_view, viewGroup, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(productList.get(i).getName());
        myViewHolder.price.setText(productList.get(i).getPrice().toString());
        myViewHolder.description.setText(productList.get(i).getDescription());


//        Intent inte = getIntent();
//        String user_email = inte.getStringExtra("user_email");// lietotaja epasts


        //Te padodam uz nākamo logu
        myViewHolder.itemView.setOnClickListener((v->{
            Intent cardViewsActivity = new Intent(myViewHolder.name.getContext(), OneProductActivity.class);
            //String str = myMovieDataList.getMovieName().toString();
            cardViewsActivity.putExtra("oneName",  productList.get(i).getName());
            cardViewsActivity.putExtra("onePrice",  productList.get(i).getPrice());
            cardViewsActivity.putExtra("oneDescr",  productList.get(i).getDescription());
            cardViewsActivity.putExtra("oneId",  productList.get(i).getId());
//            cardViewsActivity.putExtra("user_email",  user_email)
//            cardViewsActivity.putExtra("oneImage",  productList.get(i).getImage());
//            String iToS = Integer.toString(productList.get(i).getId());
//            cardViewsActivity.putExtra("id", iToS);

            myViewHolder.name.getContext().startActivity(cardViewsActivity);

        }));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView root_view;
        TextView name, price, description; //vel pievienot visus
        MaterialButton btnEdt, btnDelete;
        //        String epastins = ProductsActivity.class.getName(userEmail);
        Layout userEmailLayout;
        TextView userEmail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


//            Toast.makeText(ProductsAdapter.this, ""+epastins, Toast.LENGTH_SHORT).show();


            root_view = (CardView) itemView.findViewById(R.id.root_view);
            name = (TextView) itemView.findViewById(R.id.txt_name);
            price = (TextView) itemView.findViewById(R.id.txt_price);
            description = (TextView) itemView.findViewById(R.id.txt_desc);



//            btnEdt = (MaterialButton) itemView.findViewById(R.id.btn_edit);
//            btnDelete = (MaterialButton) itemView.findViewById(R.id.btn_delete);

        }
    }
}
