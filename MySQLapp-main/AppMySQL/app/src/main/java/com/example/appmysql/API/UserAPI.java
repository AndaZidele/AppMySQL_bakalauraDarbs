package com.example.appmysql.API;


import com.example.appmysql.Adapters.Cart;
import com.example.appmysql.Adapters.Order;
import com.example.appmysql.Adapters.Product;
import com.example.appmysql.Adapters.Cart;
import com.example.appmysql.Adapters.Product;
import com.example.appmysql.Adapters.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAPI {

    @POST("pievienoProduktusKaaFirebaseVir")
    @FormUrlEncoded
    Observable<String> registerProdKaFirebase(@Field("te") String te);

    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("phone") String phone,
                                    @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password);


    //incProduct
    @POST("incProduct")
    @FormUrlEncoded
    Observable<String> incProduct(@Field("product") String product,
                                 @Field("user") String user,
                                  @Field("amount") String amount);
    ;
    @POST("decProduct")
    @FormUrlEncoded
    Observable<String> decProduct(@Field("product") String product,
                                  @Field("user") String user,
                                  @Field("amount") String amount);

    @POST("deleteProduct")
    @FormUrlEncoded
    Observable<String> deleteProduct(@Field("product") String product,
                                  @Field("user") String user);

    @POST("deleteTableProducts")
    @FormUrlEncoded
    Observable<String> deleteTableProduct(@Field("hi") String hi);


    @GET("getUserCart")
    Observable<List<Cart>> getUserCart();

//    @POST("addProduct")
//    @FormUrlEncoded
//    Observable<String> registerProduct(@Field("name") String name,
//                                       @Field("price") String price,
//                                       @Field("description") String description,
//                                       @Field("image") String image,
//                                       @Field("amount") String amount);

    @POST("updateProductInUsersCart")//updateProductForCart")
    @FormUrlEncoded
    Observable<String> updateProduct(@Field("id") String amount,
                                     @Field("product") String product,
                                     @Field("user") String user);

    //Si funkcija pagaidam nestradaa!!!
    @POST("addProductToUsersCart")
    @FormUrlEncoded
    Observable<String> addToCart(@Field("id") String pId,
                                     @Field("users_id") String uId,
                                     @Field("amount") String am,
                                     @Field("name") String name,
                                     @Field("price") String pr);

    @POST("addProduct")
    @FormUrlEncoded
    Observable<String> registerProduct(@Field("name") String name,
                                       @Field("id") String id,
                                       @Field("user") String user,
                                       @Field("price") String price,
                                       @Field("amount") String amount);

    @POST("makeOrder")
    @FormUrlEncoded
    Observable<String> registerOrder(@Field("user_id") String user_id,
                                       @Field("user_name") String user_name,
                                       @Field("email") String email,
                                       @Field("phone") String phone,
                                     @Field("address") String address,
                                     @Field("prod_names") String prod_names,
                                     @Field("prod_ids") String prod_ids,
                                       @Field("price") String price);



//    @POST("deleteProduct")
//    @FormUrlEncoded
//    Observable<String> deleteProduct(@Field("id") Integer id);


    @POST("addProductToCart")
    @FormUrlEncoded
    Observable<String> addProductToCart(@Field("user_id") Integer user_id,
                                        @Field("cart_id") Integer cart_id,
                                        @Field("product_in_cart_id") Integer product_in_cart_id,
                                        @Field("product_from_all_id") Integer product_from_all_id);

    @GET("product")
    Observable<List<Product>> getProductList();

    @GET("productsForCart")
    Observable<List<Cart>> getCartList();

    @POST("usersProducts")
    @FormUrlEncoded
    Observable<String> usersProducts(@Field("user_id") String user_id);

    @POST("deleteUser")
    @FormUrlEncoded
    Observable<String> deleteUser(@Field("delUId") String delUId);

    @GET("product")
    Observable<List<Cart>> getCartListPr();

    @GET("forCart1")
    Observable<String> getProductForCart();

    @GET("getUList")
    Observable<List<User>> getUserList();

    @GET("getUOrder")
    Observable<List<Order>> getUserOrder();

////pievienot produktu grozam
//    @POST("addProductForCart")
//    @FormUrlEncoded
//    Observable<String> addToCart(@Field("id") String id,
//                                     @Field("name") String name,
//                                     @Field("price") String price,
//                                     @Field("user") String user);

    @POST("search")
    @FormUrlEncoded
    Observable<List<Product>> searchPerson(@Field("search") String searchQuery);


}
