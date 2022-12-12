package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import digi.coders.shardaagroagency.Adapters.BillingAdapter;
import digi.coders.shardaagroagency.Adapters.CartItemsAdapter;
import digi.coders.shardaagroagency.Helper.Constants;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.CartItemModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = CheckOutActivity.class.getSimpleName();

     RecyclerView billings;
     Button back;
     int total_amount=0;
     int subTotal=0;

     ElasticButton checkoutBtn;
     ArrayList<CartItemModel> arrayList=new ArrayList<>();

     EditText address,city,pincode;
     String link="";
     public static String txcID="";
     TextView subToatlAmount,totalAmount,discount,productFinalDiscountAmount;
     EditText gst,transportname;
     RadioGroup radioGroup;
     RadioButton PayOnline, PayWithUPI;
     String payment_method="Online",State;
     Spinner state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_check_out);
        Checkout.preload(getApplicationContext());

        checkoutBtn=findViewById(R.id.checkoutBtn);
        address=findViewById(R.id.address);
        city=findViewById(R.id.city);
        pincode=findViewById(R.id.pincode);
        state=findViewById(R.id.state);
        transportname=findViewById(R.id.transportname);

        subToatlAmount=findViewById(R.id.sub_total_amount);
        totalAmount=findViewById(R.id.total_amount);
        discount=findViewById(R.id.discount_amount);
        productFinalDiscountAmount=findViewById(R.id.product_final_discount_amount);
        gst=findViewById(R.id.gst);

        radioGroup = findViewById(R.id.radioGroup3);
        PayOnline = findViewById(R.id.pay_online);
        PayWithUPI = findViewById(R.id.pay_wid_upi);
        PayOnline.setChecked(true);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Constants.state.split(","));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(dataAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State = state.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.pay_online){
                            payment_method="Online";
                        }else if(checkedId==R.id.pay_wid_upi){
                            payment_method="UPI";
                        }
                    }
                }
        );

        checkoutBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                     //   if(gst.getText().toString().length()==15){
                        if(address.getText().toString().length()>0){
                            if(city.getText().toString().length()>0){
                                if(pincode.getText().toString().length()==6){
                                   if( state.getSelectedItemPosition()>-1){
                                       checkout();
                                   }else{
                                       TastyToast.makeText(getApplicationContext(),"Please Enter State ..!",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                                   }
                                }else{
                                    TastyToast.makeText(getApplicationContext(),"Please Enter Pincode ..!",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                                }
                            }else{
                                TastyToast.makeText(getApplicationContext(),"Please Enter City ..!",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                            }
                        }else{
                            TastyToast.makeText(getApplicationContext(),"Please Enter Local Address ..!",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                        }
//                        }else{
//                            TastyToast.makeText(getApplicationContext(),"Invalid GST Number !",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
//                        }
                    }
                }
        );
        billings=findViewById(R.id.billingDetails);
        back=findViewById(R.id.back);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        getCartItems();
        getAddress();

    }

    public void getCartItems(){

        final ProgressDialog pd=ProgressDialog.show(CheckOutActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getCartItems("show_cart", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

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
                                    if(jsonObject1.getString("StockStatus").equalsIgnoreCase("true")) {
                                        total_amount=total_amount+(Integer.parseInt(jsonObject1.getString("ProductOfferPrice"))*Integer.parseInt( jsonObject1.getString("ProductQuantity")));
                                        subTotal=subTotal+(Integer.parseInt(jsonObject1.getString("ProductOldPrice"))*Integer.parseInt( jsonObject1.getString("ProductQuantity")));
                                        arrayList.add(categoryProduct);
                                    }

                                }

                                //total_amount = Integer.parseInt(jsonObject.getString("total_amount"));
                                int final_discount_amount = Integer.parseInt(jsonObject.getString("final_discount_amount"));
                                checkoutBtn.setText("PAY ONLINE Rs. "+(total_amount-final_discount_amount));
                                subToatlAmount.setText("Rs. "+subTotal);
                                discount.setText("Rs. "+(subTotal-total_amount));
                                // Visibilty gone when product discount is zero
                                if(final_discount_amount==0){
                                    findViewById(R.id.product_discount_view).setVisibility(View.GONE);
                                }
                                productFinalDiscountAmount.setText("Rs."+final_discount_amount);
                                totalAmount.setText("Rs. "+(total_amount-final_discount_amount));
                                total_amount = total_amount-final_discount_amount;
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                                billings.setLayoutManager(layoutManager);
                                billings.setHasFixedSize(true);
                                BillingAdapter cartItemsAdapter=new BillingAdapter(arrayList,getApplicationContext());

                                billings.setAdapter(cartItemsAdapter);


                            }else{


                            }

                        }catch (Exception e){

                        }
                        //   Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        pd.dismiss();
                        //  Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void getAddress(){

        final ProgressDialog pd=ProgressDialog.show(CheckOutActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getAddress("address", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));

                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){
                                JSONObject jsonArray1=jsonObject.getJSONObject("msg");

                                gst.setText(jsonArray1.getString("GST"));
                                address.setText(jsonArray1.getString("Address"));
                                city.setText(jsonArray1.getString("City"));
                                pincode.setText(jsonArray1.getString("Pincode"));

                                List<String> list = new ArrayList<>(Arrays.asList(Constants.state.split(",")));
                                state.setSelection(list.indexOf((jsonArray1.getString("State"))));

                            }else{


                            }

                        }catch (Exception e){

                        }
                        //   Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        pd.dismiss();
                        //  Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void checkout(){

        final ProgressDialog pd=ProgressDialog.show(CheckOutActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.checkout("checkout", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),gst.getText().toString(),address.getText().toString(),city.getText().toString(),pincode.getText().toString(),state.getSelectedItem().toString(),payment_method,transportname.getText().toString(),
                total_amount+"",discount.getText().toString().replace("Rs. ",""));
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.i("456789", response.body().toString());
                        //Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_SHORT).show();

                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){

                               link= jsonObject.getString("msg");
                               // Toast.makeText(CheckOutActivity.this, link, Toast.LENGTH_SHORT).show();
                                if(payment_method.equalsIgnoreCase("Online")){

                                    TastyToast.makeText(getApplicationContext(),total_amount+""+payment_method,TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();

                                    Uri uri = Uri.parse(jsonObject.getString("msg")); // missing 'http://' will cause crashed
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                    finish();

                                }else if(payment_method.equalsIgnoreCase("UPI")) {

                                    //TastyToast.makeText(getApplicationContext(),payment_method+"",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                                    startUpiPayment();
                                }

                            }else{
                                TastyToast.makeText(getApplicationContext(),"Something Went Wrong  ..!",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();

                            }

                        }catch (Exception e){

                        }
                         Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    private void startUpiPayment() {
        txcID = System.currentTimeMillis() + "";
        ArrayList<String> existingApps = UpiPayment.getExistingUpiApps(CheckOutActivity.this);

        existingApps.add("paytm");
        existingApps.add("google pay");
        existingApps.add("bhim");


        PaymentDetail payment = new PaymentDetail(Constants.UPI,"Sharda Agro Agency",
                "","", "Sharda Agro Agency",total_amount+".00");


        new UpiPayment(CheckOutActivity.this)
                .setPaymentDetail(payment)
                .setUpiApps(existingApps)
                .setCallBackListener(new UpiPayment.OnUpiPaymentListener() {
                    @Override
                    public void onError(String s) {
                        Toast.makeText(getApplicationContext(), "transaction failed: " + s, Toast.LENGTH_LONG).show();
                        //sendTransactionStatus("false");

                    }

                    @Override
                    public void onSuccess(com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Toast.makeText(getApplicationContext(), "transaction success: " + transactionDetails.toString(), Toast.LENGTH_LONG).show();
                        addStatus(link,txcID,transactionDetails.toString());
                    }

                    @Override
                    public void onSubmitted(com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Toast.makeText(getApplicationContext(), "transaction pending: " + transactionDetails, Toast.LENGTH_LONG).show();
                    }

                }).pay();
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", SharedPrefManager.getInstance(getApplicationContext()).getUser().getName());
            options.put("description", "Shopping Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", total_amount*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail());
            preFill.put("contact", SharedPrefManager.getInstance(getApplicationContext()).getUser().getMobile());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
          //  Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            addStatus(link,razorpayPaymentID,"");

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    public void addStatus(String orderId,String txn,String txndata){

        final ProgressDialog pd=ProgressDialog.show(CheckOutActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.addStatus("payment", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),orderId,txn,txndata);
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_SHORT).show();

                        try{

                            Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){

                                CartActivity.CartActivity.finish();
                                finish();
                                if(Constants.ProductDetailActivity==1){
                                    ProductDetailActivity.ProductDetailActivity.finish();
                                }

                                startActivity(new Intent(getApplicationContext(),MyOrdersActivity.class));

                            }else{


                            }

                        }catch (Exception e){

                        }
                        Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        //  Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}
