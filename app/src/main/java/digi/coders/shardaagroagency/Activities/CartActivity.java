package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.CartItemsAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RefreCart;
import digi.coders.shardaagroagency.Helper.Refresh;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.CartItemModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements Refresh , RefreCart {
    ElasticLayout select_payment_option;

    RecyclerView cart_items;
//    int total_amount=0;
    View parentLayout;
    LinearLayout no_data,caluclatio_money;
    ArrayList<CartItemModel> arrayList=new ArrayList<>();
    TextView item_count;
    Button back;
    ElasticButton continue_shopping;
    TextView total_amount_to_pay;

    TextView discount,discount_price;
    LinearLayout coupon_layout,disc_layout;

    public static Refresh refresh;
    public static RefreCart refreCart;
    public static Activity CartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_cart);
        no_data=findViewById(R.id.no_data);
        refresh=this;
        refreCart=this;
        CartActivity=this;
        caluclatio_money=findViewById(R.id.calculation);
        item_count=findViewById(R.id.item_count);
        continue_shopping=findViewById(R.id.continue_shopping);
        total_amount_to_pay=findViewById(R.id.total_amount);
        disc_layout=findViewById(R.id.disc_layout);
        discount=findViewById(R.id.discount);
        coupon_layout=findViewById(R.id.coupon_layout);
        cart_items=findViewById(R.id.cart_items);
        discount_price=findViewById(R.id.discount_price);


        continue_shopping.setOnClickListener(
                v -> finish()
        );

        back=findViewById(R.id.back);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        parentLayout=findViewById(android.R.id.content);
        select_payment_option=findViewById(R.id.select_payment_option);
        select_payment_option.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CheckOutActivity.price=total_amount_to_pay.getText().toString();
                        startActivity(new Intent(getApplicationContext(),CheckOutActivity.class));
                    }
                }
        );

        getCartItems();
    }

    public void getCartItems(){
        arrayList.clear();
        final ProgressDialog pd=ProgressDialog.show(CartActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getCartItems("show_cart", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{
                            Log.e("cart",response.body().toString());
                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){
                                JSONArray jsonArray1=jsonObject.getJSONArray("msg");
                                for(int i=0;i<jsonArray1.length();i++) {
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                                    CartItemModel categoryProduct = new CartItemModel(
                                            jsonObject1.getString("CartId"),
                                            jsonObject1.getString("ProductName"),
                                            jsonObject1.getString("ProductQuantity"),
                                            jsonObject1.getString("ProductId"),
                                            jsonObject1.getString("ProductOldPrice"),
                                            jsonObject1.getString("ProductOfferPrice"),
                                            jsonObject1.getString("ProductImage"),
                                            ((Integer.parseInt( jsonObject1.getString("ProductOfferPrice"))-Integer.parseInt( jsonObject1.getString("ProductOldPrice")))+""),
                                            jsonObject1.getString("StockStatus")
                                    );
                                    arrayList.add(categoryProduct);
                                }

                                if (jsonObject.getString("total_amount").equals("0")){
                                    select_payment_option.setVisibility(View.GONE);
                                }else {
                                    total_amount_to_pay.setText("₹ "+jsonObject.getString("total_amount"));
                                }


                                if (jsonObject.getString("final_discount_amount").equals("0")){
                                    disc_layout.setVisibility(View.GONE);
                                }else {
                                    discount_price.setText("₹ "+jsonObject.getString("final_discount_amount"));
                                }


                                if(arrayList.size()==0){
                                    cart_items.setVisibility(View.GONE);
                                    item_count.setVisibility(View.GONE);
                                    caluclatio_money.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
                                }else{
                                    cart_items.setVisibility(View.VISIBLE);
                                    item_count.setVisibility(View.VISIBLE);
                                    caluclatio_money.setVisibility(View.VISIBLE);
                                    item_count.setText("Cart ("+arrayList.size()+" Items)");
                                    no_data.setVisibility(View.GONE);
                                }

                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                                cart_items.setLayoutManager(layoutManager);
                                cart_items.setHasFixedSize(true);
                                CartItemsAdapter cartItemsAdapter=new CartItemsAdapter(arrayList,CartActivity.this,CartActivity.this);
                                cart_items.setAdapter(cartItemsAdapter);


                            }else{
                                cart_items.setVisibility(View.GONE);
                                item_count.setVisibility(View.GONE);
                                caluclatio_money.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);

                            }

                        }catch (Exception e){
                            cart_items.setVisibility(View.GONE);
                            item_count.setVisibility(View.GONE);
                            caluclatio_money.setVisibility(View.GONE);
                            no_data.setVisibility(View.VISIBLE);
                        }
                     //  Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        cart_items.setVisibility(View.GONE);
                        item_count.setVisibility(View.GONE);
                        caluclatio_money.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    @Override
    public void onrefreshCart(String m, int s) {
        getCartItems();
    }

    @Override
    public void onRefreshData() {
        getCartItems();
    }
}
