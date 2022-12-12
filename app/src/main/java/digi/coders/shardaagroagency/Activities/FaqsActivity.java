package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.shardaagroagency.Adapters.FaqsAdapter;
import digi.coders.shardaagroagency.Adapters.MyOrdersAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.FaqModel;
import digi.coders.shardaagroagency.Model.OrderModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static digi.coders.shardaagroagency.Fragments.EditProfileFragment.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class FaqsActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = CheckOutActivity.class.getSimpleName();

    RecyclerView faqs;
    NestedScrollView nested_faqs,nested_call;
    Button back;
    TextView toolbarText;

    ArrayList<FaqModel> arrayList=new ArrayList<>();

    LinearLayout call_line,whats_line,mail_line;


    EditText name,email,subject,msg;
    ElasticButton send;

    LinearLayout pay_amount;
    ElasticButton pay_online;
    EditText amount,remark;
    Spinner type;
    ArrayAdapter aa;
    String[] types;
    String str_type="";

    String orderID="";
    RadioGroup radioGroup;
    String payment_method="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_faqs);
        Checkout.preload(getApplicationContext());

        faqs=findViewById(R.id.faqs);
        nested_faqs=findViewById(R.id.nested_faqs);
        nested_call=findViewById(R.id.nested_call);
        back=findViewById(R.id.back);
        toolbarText=findViewById(R.id.toolbar_text);
        call_line=findViewById(R.id.call_line);
        whats_line=findViewById(R.id.whatsapp_lina);
        mail_line=findViewById(R.id.mail_line);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        subject=findViewById(R.id.subject);
        msg=findViewById(R.id.msg);
        send=findViewById(R.id.send);
        radioGroup=findViewById(R.id.radioGroup);

        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contact();
                    }
                }
        );

        type=findViewById(R.id.type);
        amount=findViewById(R.id.amount);
        remark=findViewById(R.id.remark);
        pay_amount=findViewById(R.id.pay_amount);
        pay_online=findViewById(R.id.pay_online);

        pay_online.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validation()){
                            if (payment_method.equals("Online")){
                                payAmount();
                            }else {
                                startUpiPayment(amount.getText().toString());
                            }
                        }
                    }
                }
        );

        types=new String[]{"<-- Select Amount Type -->","Old","Advance"};
        type.setAdapter(new ArrayAdapter<>(FaqsActivity.this, android.R.layout.simple_spinner_dropdown_item, types));

        aa = new ArrayAdapter<>(FaqsActivity.this, android.R.layout.simple_spinner_item, types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_type=(String)adapterView.getItemAtPosition(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }


        });
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.online){
                            payment_method="Online";
                        }else if(checkedId==R.id.pay_wid_upi){
                            payment_method="UPI";
                        }
                    }
                }
        );
        call_line.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + "9305230787"));//change the number
                        if (checkAndRequestPermissions()) {
                            if (ActivityCompat.checkSelfPermission(FaqsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(callIntent);
                        }
                    }
                }
        );
        whats_line.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getLink("https://api.whatsapp.com/send?phone=919305230787&text=&source=&data=");
                    }
                }
        );

        mail_line.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "info@shardaagroagency.com"));
                        startActivity(intent);
                    }
                }
        );
        if(getIntent().getExtras().getString("status").equalsIgnoreCase("faq")){
            nested_faqs.setVisibility(View.VISIBLE);
            nested_call.setVisibility(View.GONE);
            pay_amount.setVisibility(View.GONE);
            toolbarText.setText("FAQs");
         getFAQ();
        }else    if(getIntent().getExtras().getString("status").equalsIgnoreCase("pay")){
            nested_faqs.setVisibility(View.GONE);
            nested_call.setVisibility(View.GONE);
            pay_amount.setVisibility(View.VISIBLE);
            toolbarText.setText("Pay Amount");
            getFAQ();
        }else{
            nested_faqs.setVisibility(View.GONE);
            nested_call.setVisibility(View.VISIBLE);
            pay_amount.setVisibility(View.GONE);
            toolbarText.setText("Call to support");
        }

        getProfile();
    }
    public void getLink(String url){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
    public void getFAQ(){

        final ProgressDialog pd=ProgressDialog.show(FaqsActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getFaq("faq");
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
                                    FaqModel orderModel = new FaqModel(
                                            jsonObject1.getString("Question"),
                                            jsonObject1.getString("Answer")
                                    );

                                    arrayList.add(orderModel);
                                }
//
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                                faqs.setLayoutManager(layoutManager);
                                faqs.setHasFixedSize(true);
                                FaqsAdapter cartItemsAdapter=new FaqsAdapter(arrayList,getApplicationContext());
                                faqs.setAdapter(cartItemsAdapter);


                            }else{


                            }

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        pd.dismiss();
                         Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED ) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);

        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void getProfile(){

        final ProgressDialog pd=ProgressDialog.show(FaqsActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getProfile("profile",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){

                                JSONArray jsonObject11=jsonObject1.getJSONArray("msg");


                                name.setText(jsonObject11.getJSONObject(0).getString("UserName"));
                                email.setText(jsonObject11.getJSONObject(0).getString("UserEmail"));
                               }else{
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(),"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        //     Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public boolean validation(){
        if (amount.getText().toString().isEmpty()){
            amount.setError("required");
            amount.requestFocus();
            return false;
        }

        if (str_type.equals("<-- Select Amount Type -->")){
            Toast.makeText(this, "Select Amount Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (payment_method.equals("")){
            Toast.makeText(this, "Select Payment Method", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void payAmount(){
        orderID=System.currentTimeMillis()+"";
        final ProgressDialog pd=ProgressDialog.show(FaqsActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.payAmount("payment",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),amount.getText().toString(),str_type,orderID,remark.getText().toString(),payment_method);
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){

                                if (payment_method.equals("Online")){
                                    Uri uri = Uri.parse(jsonObject1.getString("msg")); // missing 'http://' will cause crashed
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                    finish();
                                }
                                amount.setText("");
                                remark.setText("");
                                TastyToast.makeText(getApplicationContext(), "Amount Submitted !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }else{
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(),"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        //     Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }


    private void startUpiPayment(String total_amount) {
        ArrayList<String> existingApps = UpiPayment.getExistingUpiApps(FaqsActivity.this);

        existingApps.add("paytm");
        existingApps.add("google pay");
        existingApps.add("bhim");


        PaymentDetail payment = new PaymentDetail("9450373735@upi","Sharda Agro Agency",
                "","", "Sharda Agro Agency",total_amount+".00");

//        PaymentDetail payment = new PaymentDetail("9140967607@paytm","Ludo Fans Adda",
//                "","", "Ludo Fans Adda",amount+".00");

        new UpiPayment(FaqsActivity.this)
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
//                        addStatus(link,orderID);
                        payAmount();
                    }

                    @Override
                    public void onSubmitted(com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Toast.makeText(getApplicationContext(), "transaction pending: " + transactionDetails, Toast.LENGTH_LONG).show();
                    }

                }).pay();
    }


    public void contact(){

        orderID=System.currentTimeMillis()+"";
        final ProgressDialog pd=ProgressDialog.show(FaqsActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getContact("contact",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),name.getText().toString(),email.getText().toString(),SharedPrefManager.getInstance(getApplicationContext()).getUser().getMobile(),msg.getText().toString(),subject.getText().toString());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));

                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){
                                subject.setText("");
                                msg.setText("");
                                TastyToast.makeText(getApplicationContext(), "Your query has been submitted !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            }else{
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(),"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        //     Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
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
            options.put("amount", Integer.parseInt(amount.getText().toString())*100);

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

            payAmount();

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
}
