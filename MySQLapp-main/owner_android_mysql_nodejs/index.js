var crypto = require('crypto');
var uuid = require('uuid');
var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser');

var con = mysql.createConnection({
    host:'localhost',
    user:'root',
    password:'',
    database:'AppDB'  
});

//Password util
var genRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2))
    .toString('hex')
    .slice(0,length);
};

var sha512 = function(password,salt){
    var hash = crypto.createHmac('sha512', salt);
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt:salt,
        passwordHash:value
    };
};

function saltHashPassword(userPassword){
    var salt = genRandomString(16);
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}


function checkHashPassword(userPassword,salt){
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}


var app=express();
var publicDir=(__dirname+'/public/');
app.use(express.static(publicDir));
app.use(bodyParser.json()); //Accept json Params
app.use(bodyParser.urlencoded({extended: true})); //Accept ur encoded params

app.post('/register/',(req,res,next)=>{

    var post_data = req.body;
    var uid = uuid.v4();
    //te, kad savienosies ar android, janjem nost ierakstitas vertibas un jaatstaj post.data.xxxxx
    var plaint_password = post_data.password;
    var hash_data = saltHashPassword(plaint_password);
    var password = hash_data.passwordHash;
    var salt = hash_data.salt; //get salt
    var name = post_data.name;
    var email = post_data.email;
    var phone = post_data.phone;

    con.query('SELECT * FROM users WHERE email=?',[email],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length)
            res.json('User already exists!!!');
        else{
            console.log("Te reg");
            con.query('INSERT INTO `users`(`name`, `email`, `phone`, `password`, `address`, `encrypted_password`, `salt`) VALUES (?,?,?,?,?,?,?)',[name,email,phone,plaint_password,"",password,salt],function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                    res.json('Register error: ', err);
                });
                res.json('Register successful');
            });
        }
    });
    

});

app.post('/addProduct/',(req,res,next)=>{
    var post_data = req.body;

    var name = post_data.name;
    var price = post_data.price;
    var description = post_data.description;
    var image = post_data.image;
    var amount = post_data.amount;

    
    con.query('INSERT INTO `products`( `name`, `price`, `description`, `image`, `amount`, `firm`) VALUES (?,?,?,?,?,?)',[name,price,description,image,amount,"Firma1"],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            res.json('Register error: ', err);
        });
        res.json('Product successfully added to DB');
    });



});

app.post('/makeOrder/',(req,res,next)=>{
    var post_data = req.body;

    let d = new Date();
    var diena = ("0" + d.getDate()).slice(-2);
    var menesis  = ("0" + (d.getMonth() + 1)).slice(-2);
    var gads = d.getFullYear();
    var datums = diena + "." + menesis + "." + gads + ".";

        var user_id_st = post_data.user_id;
        var user_id = parseInt(user_id_st);
        var user_name = post_data.user_name;
        var user_email = post_data.email;
        var user_phone = post_data.phone;
        var user_address = post_data.address;
        var products_names = post_data.prod_names;
        var products_ids = post_data.prod_ids;
        var products_price_st = post_data.price;
        var products_price = parseFloat(products_price_st);
        var dat = datums;
        var statuss = false; //piegadats vai nepiegadats


        con.query('INSERT INTO `orders`( `user_id`, `user_name`, `email`, `phone`, `address`, `products_names`, `prod_ids`, `products_price`, `datums`) VALUES (?,?,?,?,?,?,?,?,?)',[user_id,user_name,user_email,user_phone,user_address,products_names,products_ids,products_price,dat],function(err,result,fields){
            con.on('error',function(err){
                console.log('[MySQL ERROR]', err);
                res.json('Register error: ', err);
            });
            // res.json('Product successfully added to DB');
        });


    var sqlc = `DELETE FROM cart WHERE user_id=?`;
    con.query(sqlc,[user_id],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            res.json('Updating error: ', err);
        });
         res.json('Order has been successfully made!');
    });

});

app.post('/addProductToUsersCart/',(req,res,next)=>{
    var post_data = req.body;

    var name = post_data.name;
    var price = post_data.price;
    price = parseFloat(price);
    var user = post_data.users_id;
    user = parseInt(user);
    var prod_id = post_data.id;
    prod_id = parseInt(prod_id);
    var amount = post_data.amount;
    amount = parseInt(amount);

    ///
    con.query('SELECT COUNT(*) AS namesCount FROM cart WHERE user_id=? and prod_id=?',[user,prod_id],function(err,rows,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            
        });
        // res.end(JSON.stringify(result[0]));
        console.log(rows[0].namesCount);
        if((rows[0].namesCount) > 0){//result && result.length){ //if !=0 update
            // res.json('User already exists!!!');
            // amount = 1 + result;
            
            con.query('SELECT amount FROM cart WHERE user_id=? and prod_id=?',[user,prod_id],function(err,result,fields){
            //     con.on('error',function(err){
                    console.log(result[0].amount);
                    amount = result[0].amount + 1;
                    // var sql = `UPDATE cart SET amount="${amount}" where user_id=${user} and prod_id=${prod_id}`;
                    con.query( `UPDATE cart SET amount="${amount}" where user_id=${user} and prod_id=${prod_id}`,function(err,result,fields){
                        con.on('error',function(err){
                            console.log('[MySQL ERROR]', err);
                            res.json('Updating error: ', err);
                        });
                        // res.redirect('/products');
                        // res.json('Product successfully updated!' + product_id + ' ' + name);
                    });

            });

        }
        else{ //if ==0 insert
            con.query('INSERT INTO `cart`( `user_id`, `prod_id`, `name`, `price`, `amount`) VALUES (?,?,?,?,?)',[user,prod_id,name,price,amount,],function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                    res.json('Register error: ', err);
                });
                res.json('Product successfully added to DB');
            });

        };
    });



});

//So kodu parrakstit lai ari velparbauda vai ir ko mainit un lai saiet kopa ar android!!!
app.post('/updateProduct/',(req,res,next)=>{
    var post_data = req.body;
    var product_id = post_data.id;
    // post_data.name = "Sarkana loti miksta mantina";

    // if(result && result.length){

        if(post_data.name!=null){
            var name = post_data.name;

            var sql = `UPDATE products SET name="${post_data.name}" where id=${product_id}`;
            // var sql = 'UPDATE products SET name=? where id=?';
            
            con.query(sql,[name],function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                    res.json('Updating error: ', err);
                });
                // res.redirect('/products');
                res.json('Product successfully updated!' + product_id + ' ' + name);
            });
        }
        if(post_data.price!=null){
            var price = post_data.price;
        }
        if(post_data.description!=null){
            var description = post_data.description;
        }
        if(post_data.image!=null){
            var image = post_data.image;
        }
        if(post_data.amount!=null){
            var amount = post_data.amount;
        }

    // }
    
    // con.query('SELECT * FROM products WHERE id=?',[product_id],function(err,result,fields){
    //     con.on('error',function(err){
    //         console.log('[MySQL ERROR]', err);
    //     });
        
        
    // });
    

    
});

app.post('/login/',(req,res,next)=>{

    var post_data = req.body;
    var user_password = post_data.password; //"parolite"
    var email = post_data.email;//"anda@m.com"//

    con.query('SELECT * FROM users WHERE email=?',[email],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length){
            var salt = result[0].salt;
            var encrypted_password = result[0].encrypted_password;
            var hashed_password = checkHashPassword(user_password,salt).passwordHash;

            if(encrypted_password == hashed_password)
                res.end(JSON.stringify(result[0]));
            else
                res.end(JSON.stringify('Wrong password'));
        }
        else{
            res.json('User not exists!!!');
        }
    });


});

app.post('/deleteUser/',(req,res,next)=>{

    var post_data = req.body;
    var user_id = post_data.delUId;
    // product_id = parseInt(product_id);
    // var user_id = post_data.user;
    user_id = parseInt(user_id);

    var sql = `DELETE FROM users WHERE id=?`;
            
    con.query(sql,[user_id],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            res.json('Updating error: ', err);
        });
        // res.json('User successfully deleted!');
    });

    var sqlc = `DELETE FROM cart WHERE user_id=?`;
    con.query(sqlc,[user_id],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            res.json('Updating error: ', err);
        });
        // res.json('Product successfully deleted!' + product_id);
    });

    var sqlo = `DELETE FROM orders WHERE user_id=?`;
    con.query(sqlo,[user_id],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            res.json('Updating error: ', err);
        });
        res.json('User successfully deleted!');
    });


    // var sqlcart = `DELETE FROM cart WHERE email=?`;
            
    // con.query(sql,[email],function(err,result,fields){
    //     con.on('error',function(err){
    //         console.log('[MySQL ERROR]', err);
    //         res.json('Updating error: ', err);
    //     });
    //     res.json('Product successfully deleted!' + product_id);
    // });
    // var sqlorders = `DELETE FROM orders WHERE email=?`;
            
    // con.query(sql,[email],function(err,result,fields){
    //     con.on('error',function(err){
    //         console.log('[MySQL ERROR]', err);
    //         res.json('Updating error: ', err);
    //     });
    //     res.json('Product successfully deleted!' + product_id);
    // });

});





app.post('/deleteProduct/',(req,res,next)=>{

    var post_data = req.body;
    var product_id = post_data.product;
    product_id = parseInt(product_id);
    var user_id = post_data.user;
    user_id = parseInt(user_id);

    var sql = `DELETE FROM cart WHERE prod_id=? and user_id=?`;
            
    con.query(sql,[product_id,user_id],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            res.json('Updating error: ', err);
        });
        res.json('Product successfully deleted!' + product_id);
    });

    // con.query('DELETE FROM products WHERE id=${product_id}',[product_id],function(err,result,fields){
    //     con.on('error',function(err){
    //         console.log('[MySQL ERROR]', err);
    //     });
    //     res.json('Product successfully deleted!');
    // });


});

app.post('/decProduct/',(req,res,next)=>{

    var post_data = req.body;
    var prod_id = post_data.product;
    prod_id = parseInt(prod_id);
    var user = post_data.user;
    user = parseInt(user);
    var amount = post_data.amount;
    amount = parseInt(amount);
    amount = amount - 1;


    // con.query('SELECT amount FROM cart WHERE user_id=? and prod_id=?',[user,prod_id],function(err,result,fields){
    //     //     con.on('error',function(err){
    //             console.log(result[0].amount);
    //             amount = result[0].amount - 1;
                // var sql = `UPDATE cart SET amount="${amount}" where user_id=${user} and prod_id=${prod_id}`;
                con.query( `UPDATE cart SET amount="${amount}" where user_id=${user} and prod_id=${prod_id}`,function(err,result,fields){
                    con.on('error',function(err){
                        console.log('[MySQL ERROR]', err);
                        res.json('Updating error: ', err);
                    });
                    // res.redirect('/products');
                    // res.json('Product successfully updated!' + product_id + ' ' + name);
                });

                res.json('Order has been successfully made!');


        // });

    // con.query( `UPDATE cart SET amount="${amount}" where user_id=${user_id} and prod_id=${product_id}`,function(err,result,fields){
    //                 con.on('error',function(err){
    //                     console.log('[MySQL ERROR]', err);
    //                     res.json('Updating error: ', err);
    //                 });
    //                 // res.redirect('/products');
    //                 // res.json('Product successfully updated!' + product_id + ' ' + name);
    //             });

    
});

app.post('/incProduct/',(req,res,next)=>{
    var post_data = req.body;
    var prod_id = post_data.product;
    prod_id = parseInt(prod_id);
    var user = post_data.user;
    user = parseInt(user);
    var amount = post_data.amount;
    amount = parseInt(amount);
    amount = amount + 1;

    con.query( `UPDATE cart SET amount="${amount}" where user_id=${user} and prod_id=${prod_id}`,function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            res.json('Updating error: ', err);
        });
    });
    res.json('Order has been successfully made!');


    // var post_data = req.body;
    // var product_id = post_data.product;
    // product_id = parseInt(product_id);
    // var user_id = post_data.user;
    // user_id = parseInt(user_id);
    // var amount = post_data.amount;
    // amount = parseInt(amount);
    // amount = amount + 1;

    // // con.query('SELECT amount FROM cart WHERE user_id=? and prod_id=?',[user_id,product_id],function(err,result,fields){
    // //             console.log(result[0].amount);
    // //             amount = result[0].amount + 1;
                
    //             // var sql = `UPDATE cart SET amount="${amount}" where user_id=${user} and prod_id=${prod_id}`;
    //             con.query( `UPDATE cart SET amount="${amount}" where user_id=${user_id} and prod_id=${product_id}`,function(err,result,fields){
    //                 con.on('error',function(err){
    //                     console.log('[MySQL ERROR]', err);
    //                     res.json('Updating error: ', err);
    //                 });
    //             });

        // });
});
//DELETE FROM `products` WHERE
//dzēšanas funkcija
// app.post('/deleteProduct/',(req,res,next)=>{

//     var post_data = req.body;
//     var product_id = post_data.id;

//     var sql = `DELETE FROM products WHERE id=?`;
            
//     con.query(sql,[product_id],function(err,result,fields){
//         con.on('error',function(err){
//             console.log('[MySQL ERROR]', err);
//             res.json('Updating error: ', err);
//         });
//         res.json('Product successfully deleted!' + product_id);
//     });

//     // con.query('DELETE FROM products WHERE id=${product_id}',[product_id],function(err,result,fields){
//     //     con.on('error',function(err){
//     //         console.log('[MySQL ERROR]', err);
//     //     });
//     //     res.json('Product successfully deleted!');
//     // });


// });


app.get("/",(re,res,next)=>{
    console.log('Password: 123456');
    var encrypt = saltHashPassword("123456");
    console.log('Encript: '+encrypt.passwordHash);
    console.log('Salt: '+encrypt.salt);
})

app.get("/product/",(req,res,next)=>{
    
    con.query('SELECT * FROM product',function(error,result,fields){
        // console.log("Te");
        con.on('error',function(err){
            console.log('[MYSQL]ERROR',err);
        });
        if(result && result.length){
            res.end(JSON.stringify(result));
        }else{
            res.end(JSON.stringify('No product here in product'));
        }
    });
});

app.get("/getUList/", (req,res,next)=>{
    
    con.query('SELECT * FROM users',function(error,result,fields){
        con.on('error',function(err){
            console.log('[MYSQL]ERROR',err);
        });
        if(result && result.length){
            res.end(JSON.stringify(result));
            console.log("Te");
        }else{
            res.end(JSON.stringify('No person here in person'));
            console.log("Te-err");
        }
    });
});

app.get("/getUserCart/", (req,res,next)=>{
    
    con.query('SELECT * FROM cart',function(error,result,fields){
        con.on('error',function(err){
            console.log('[MYSQL]ERROR',err);
        });
        if(result && result.length){
            res.end(JSON.stringify(result));
            console.log("Te");
        }else{
            res.end(JSON.stringify('No person here in person'));
            console.log("Te-err");
        }
    });
});

app.get("/getUOrder/", (req,res,next)=>{
    con.query('SELECT * FROM orders',function(error,result,fields){
        con.on('error',function(err){
            console.log('[MYSQL]ERROR',err);
        });
        if(result && result.length){
            res.end(JSON.stringify(result));
        }else{
            res.end(JSON.stringify('No person here in person'));
        }
    });
});

app.post("/search/",(req,res,next)=>{

    //nakamas divas rindas neker jo nav kas padod datus no android!!!
    var post_data = req.body; //get post body

    var name_search = post_data.search; //get field search from post data
    var query = "SELECT * FROM products WHERE name LIKE '%"+name_search+"%'";

    

    con.query(query,function(error,result,fields){
        con.on('error',function(err){
            console.log('[MYSQL]ERROR',err);
        });
        if(result && result.length){
            res.end(JSON.stringify(result));
        }else{
            res.end(JSON.stringify(query));
            res.end(JSON.stringify('No person here in search'));
        }
    });
});

//Start server
app.listen(3000, ()=>{
    console.log('Server running on port 3000');
})

app.post('/login_online_store/',(req,res,next)=>{

    var post_data = req.body;
    var user_password = post_data.password; //"parolite"
    var email = post_data.email;//"anda@m.com"//

    con.query('SELECT * FROM users WHERE email=?',[email],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length){
            var salt = result[0].salt;
            var encrypted_password = result[0].encrypted_password;
            var hashed_password = checkHashPassword(user_password,salt).passwordHash;

            if(encrypted_password == hashed_password)
                res.end(JSON.stringify(result[0]));
            else
                res.end(JSON.stringify('Wrong password'));
        }
        else{
            res.json('User not exists!!!');
        }
    });


});


app.post('/register_online_store/',(req,res,next)=>{

    var post_data = req.body;
    var uid = uuid.v4();
    //te, kad savienosies ar android, janjem nost ierakstitas vertibas un jaatstaj post.data.xxxxx
    var plaint_password = post_data.password;
    var hash_data = saltHashPassword(plaint_password);
    var password = hash_data.passwordHash;
    var salt = hash_data.salt; //get salt
    var name = post_data.name;
    var email = post_data.email;
    var phone = post_data.phone;

    con.query('SELECT * FROM users WHERE email=?',[email],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length)
            res.json('User already exists!!!');
        else{
            con.query('INSERT INTO `user`( `unique_id`, `name`, `email`, `encrypted_password`, `salt`, `phone`, `created_at`, `updated_at`) VALUES (?,?,?,?,?,?,NOW(),NOW())',[uid,name,email,password,salt,phone],function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                    res.json('Register error: ', err);
                });
                res.json('Register successful');
            });
        }
    });
    

});

app.post('/registerProd/', (request,response,next)=>{
    // var db = client.db('appdb');
    var uid = uuid.v4();
    for(var i=1; i<51; i++){
        var prod_id = (100 + i);
        var prod_n = "Children's Product No. " + i.toString();
        var boole;
        var prod_price;
        if (i < 26) {
            prod_price = 24.99 + i;
            boole = true;
        } else {
            prod_price = 69.49 - i;
            boole = false;
        }
        var prod_desc = "Children's Product No. " + i.toString() + " is very comfortable and soft.";
        var prod_img = "Children" + i.toString() + ".png";
        var insertJson = {
            'id': prod_id,
            'name': prod_n,
            'special_offer':boole,
            'price':prod_price,
            'description':prod_desc,
            'image':prod_img,
            'category':"children"
        };
                    con.query('INSERT INTO `product`(`name`, `price`, `description`, `image`, `category`, `special_offer`) VALUES (?,?,?,?,?,?)',[prod_n, prod_price, prod_desc, prod_img, "children",boole],function(err,result,fields){
                        
                    });
    }
});


app.post('/deleteTableProducts/', (request,response,next)=>{

var cate = "women";
    var sqlc = `DELETE FROM product WHERE category=?`;
    con.query(sqlc,[cate],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
            // res.json('Updating error: ', err);
        });

        //  res.json('Order has been successfully made!');
    });
    console.log("Hi");
});

app.post('/pievienoProduktusKaaFirebaseVir/', (request,response,next)=>{
    // var db = client.db('appdb');
    for(var i=1; i<51; i++){//1101-10101;151-1101;1-51
        // var prod_id = (100 + i);
        var prod_n = "Women's Product No. " + i.toString();
        var boole;
        var prod_price;
        if (i < 26) {
            prod_price = 24.99 + i;
            boole = 1;
        } else {
            prod_price = 99.49 - i;
            boole = 0;
        }
        var prod_desc = "Women's Product No. " + i.toString() + " is very comfortable and soft.";
        var prod_img = "Women" + i.toString() + ".png";
        con.query('INSERT INTO `product`(`name`, `price`, `description`, `image`, `category`, `special_offer`) VALUES (?,?,?,?,?,?)',[prod_n, prod_price, prod_desc, prod_img, "women",boole],function(err,result,fields){

        });
    }

    for(var i=51; i<101; i++){//1101-10101;151-1101;1-51
        // var prod_id = (100 + i);
        var prod_n = "Men's Product No. " + i.toString();
        var boole;
        var prod_price;
        if (i < 76) {
            prod_price = 24.99 + i;
            boole = 1;
        } else {
            prod_price = 199.49 - i;
            boole = 0;
        }
        var prod_desc = "Men's Product No. " + i.toString() + " is very comfortable and soft.";
        var prod_img = "Men" + i.toString() + ".png";
        con.query('INSERT INTO `product`(`name`, `price`, `description`, `image`, `category`, `special_offer`) VALUES (?,?,?,?,?,?)',[prod_n, prod_price, prod_desc, prod_img, "men",boole],function(err,result,fields){

        });
    }

    for(var i=101; i<151; i++){//1101-10101;151-1101;1-51
        // var prod_id = (100 + i);
        var prod_n = "Children's Product No. " + i.toString();
        var boole;
        var prod_price;
        if (i < 126) {
            prod_price = 24.99 + i;
            boole = 1;
        } else {
            prod_price = 199.49 - i;
            boole = 0;
        }
        var prod_desc = "Children's Product No. " + i.toString() + " is very comfortable and soft.";
        var prod_img = "Children" + i.toString() + ".png";
        con.query('INSERT INTO `product`(`name`, `price`, `description`, `image`, `category`, `special_offer`) VALUES (?,?,?,?,?,?)',[prod_n, prod_price, prod_desc, prod_img, "children",boole],function(err,result,fields){

        });
    }
    response.json('Order has been successfully made!');
    console.log("Pievienojam sievietes");
    
});

app.post('/registerProd_2/', (request,response,next)=>{
    // var db = client.db('appdb');
    for(var i=1; i<51; i++){
        var prod_id = (100 + i);
        var prod_n = "Women's Product No. " + i.toString();
        var boole;
        var prod_price;
        if (i < 26) {
            prod_price = 24.99 + i;
            boole = true;
        } else {
            prod_price = 69.49 - i;
            boole = false;
        }
        var prod_desc = "Women's Product No. " + i.toString() + " is very comfortable and soft.";
        var prod_img = "Women" + i.toString() + ".png";
        var insertJson = {
            'id': prod_id,
            'name': prod_n,
            'special_offer':boole,
            'price':prod_price,
            'description':prod_desc,
            'image':prod_img,
            'category':"women"
        };
        con.query('INSERT INTO `product`(`name`, `price`, `description`, `image`, `category`, `special_offer`) VALUES (?,?,?,?,?,?)',[prod_n, prod_price, prod_desc, prod_img, "women",boole],function(err,result,fields){

        });
        // db.collection('product')
        //             .insertOne(insertJson,function(err,res){      
        //               //  response.json('Registration successful');
        //                 console.log('Registration successful');
        //             })
    }
});