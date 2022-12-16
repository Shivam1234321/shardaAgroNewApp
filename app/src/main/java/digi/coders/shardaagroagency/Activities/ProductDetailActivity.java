package digi.coders.shardaagroagency.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import digi.coders.shardaagroagency.Adapters.OffersSliderAdapter;
import digi.coders.shardaagroagency.Helper.Constants;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.ShopProductModel;
import digi.coders.shardaagroagency.Model.SliderModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {


    ElasticButton addCart,goCart;
    LinearLayout CountLine,ll_price;
    ElasticImageView plusBtn,minusBtn, Plus, Minus;
    TextView productName,price,cutprice,yousave,offers,txt_count,count,brand,title,product_price_single,category,sub_category;
    Button back,cart;
    int currentPage = 0;

    TextView webView;
    public  static  ShopProductModel shopProductModel;
    public  static Activity ProductDetailActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_product_detail);

        ProductDetailActivity=this;
        Constants.ProductDetailActivity=1;
        back=findViewById(R.id.back);
        webView=findViewById(R.id.desc);
        addCart=findViewById(R.id.addCart);
        CountLine=findViewById(R.id.line_count);
        goCart=findViewById(R.id.goCart);
        plusBtn=findViewById(R.id.plus_btn);
        minusBtn=findViewById(R.id.minus_btn);
        Plus=findViewById(R.id.plus);
        Minus=findViewById(R.id.minus);
        productName=findViewById(R.id.name);
        price=findViewById(R.id.price);
        cutprice=findViewById(R.id.cutPrice);
        yousave=findViewById(R.id.you_save);
        offers=findViewById(R.id.offers_percentage);
        txt_count=findViewById(R.id.txt_count);
        count=findViewById(R.id.count);
        cart=findViewById(R.id.cart);
        brand=findViewById(R.id.brand);
        title=findViewById(R.id.title);
        ll_price=findViewById(R.id.ll_price);
        product_price_single=findViewById(R.id.product_price_single);
        category=findViewById(R.id.category);
        sub_category=findViewById(R.id.sub_category);

        webView.setText(Html.fromHtml(shopProductModel.getProductDescription()));
        productName.setText(shopProductModel.getProductName());
        brand.setText(shopProductModel.getProductTitle());
        title.setText(shopProductModel.getProductBrand());
        category.setText(shopProductModel.getProductCategory());
        sub_category.setText(shopProductModel.getProductSubCategory());
        price.setText("Rs. "+shopProductModel.getProductOfferPrice());
        product_price_single.setText("Rs. "+shopProductModel.getSinglePrice());
        cutprice.setText( Html.fromHtml("<strike>Rs. " + shopProductModel.getProductPrice()+"</strike>"));
        double d=Integer.parseInt(shopProductModel.getProductPrice())-Integer.parseInt(shopProductModel.getProductOfferPrice());
        double all=(d/Integer.parseInt(shopProductModel.getProductPrice()))*100;
        DecimalFormat decim = new DecimalFormat("#.##");

        if (shopProductModel.getStock().equals("0")){
            ll_price.setVisibility(View.GONE);
            addCart.setVisibility(View.GONE);
        }

        offers.setText(decim.format(all)+"% OFF");
        yousave.setText("You Save Rs. "+(Integer.parseInt(shopProductModel.getProductPrice())-Integer.parseInt(shopProductModel.getProductOfferPrice())));
        cart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                    }
                }
        );


        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        addCart.setVisibility(View.VISIBLE);
        CountLine.setVisibility(View.GONE);

        addCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         addToCart(shopProductModel,0);
                        addCart.setVisibility(View.GONE);
                        CountLine.setVisibility(View.VISIBLE);
                    }
                }
        );
        goCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                    }
                }
        );

//        plusBtn.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        txt_count.setText((Integer.parseInt(txt_count.getText().toString())+1)+"");
//
//                    }
//                }
//        );
//        minusBtn.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(Integer.parseInt(txt_count.getText().toString())>1) {
//                            txt_count.setText((Integer.parseInt(txt_count.getText().toString()) - 1) + "");
//                        }else{
//                            TastyToast.makeText(getApplicationContext(),"Item can not be zero !",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
//                        }
//                    }
//                }
//        );

        Plus.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //count.setText((Integer.parseInt(count.getText().toString())+1)+"");
                        addToCart(shopProductModel,0);
                    }
                }
        );
        Minus.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Integer.parseInt(count.getText().toString())>1) {
                            addToCart(shopProductModel,1);
                            //count.setText((Integer.parseInt(count.getText().toString()) - 1) + "");
                        }else{
                            addCart.setVisibility(View.VISIBLE);
                            CountLine.setVisibility(View.GONE);
                            TastyToast.makeText(getApplicationContext(),"Item can not be zero !",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                        }
                    }
                }
        );
        showSlider();

    }

    public void showSlider() {

        int limit = 0;
        ViewPager viewPager;
        OffersSliderAdapter offersSliderAdapter;
        Timer timer;

        viewPager = findViewById(R.id.viewPager);

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == 6 - 1) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 5000);
        ArrayList<SliderModel> arrayList = new ArrayList<>();
        arrayList.add(new SliderModel("",shopProductModel.getProductImage1()));
        if (!shopProductModel.getProductImage2().isEmpty()){
        arrayList.add(new SliderModel("",shopProductModel.getProductImage2()));}
        if (!shopProductModel.getProductImage3().isEmpty()){
            arrayList.add(new SliderModel("",shopProductModel.getProductImage3()));}


        limit = arrayList.size();
                        offersSliderAdapter = new OffersSliderAdapter(getApplicationContext(), arrayList);
                        viewPager.setAdapter(offersSliderAdapter);
                        offersSliderAdapter.notifyDataSetChanged();
                        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
                        viewPager.setOffscreenPageLimit(limit);




    }

    public void addToCart(ShopProductModel productModel, final int status1){

        final ProgressDialog pd=ProgressDialog.show(ProductDetailActivity.this,"","Loading...");
        GetData getDataServices= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=null;
        if(status1==1) {
            call = getDataServices.addItems("cart", SharedPrefManager.getInstance(ProductDetailActivity.this).getUser().getId(), productModel.getProductId(),(Integer.parseInt(count.getText().toString())-1)+"");

        }else{
            call = getDataServices.addItems("cart", SharedPrefManager.getInstance(ProductDetailActivity.this).getUser().getId(), productModel.getProductId(),(Integer.parseInt(count.getText().toString())+1)+"");

        }
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JSONArray jsonArray;
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    jsonArray = new JSONArray(new Gson().toJson(response.body()));

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String status = jsonObject.getString("res");


                    if (status.equalsIgnoreCase("success")) {
                        if(status1==1){
                            count.setText((Integer.parseInt(count.getText().toString())-1)+"");
                            TastyToast.makeText(ProductDetailActivity.this,"Item Removed from Cart",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();

                        }else{

                            count.setText((Integer.parseInt(count.getText().toString())+1)+"");
                            TastyToast.makeText(ProductDetailActivity.this,"Item Added to Cart",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();

                        }

                    }
                    else {
                        TastyToast.makeText(ProductDetailActivity.this,status,TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();

                    }
                }catch(Exception e){
                    Toast.makeText(ProductDetailActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                //  Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();

            }
        });
    }
}
