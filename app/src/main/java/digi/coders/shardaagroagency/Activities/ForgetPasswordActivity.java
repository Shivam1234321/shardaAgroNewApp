package digi.coders.shardaagroagency.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.R;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPasswordActivity extends AppCompatActivity {

    ElasticButton Next;
    EditText Mobile, CreatePassword ,ConfirmPassword;
    ProgressDialog pd;
    String mobile,otp, createPass, ConfirmPass;
    LinearLayout mobileline,otpLine,passLine;
    EditText Otp;
    TextView Resend;

    ViewGroup view_group;
    View parentLayout;
    ElasticImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_forget_password);

//        ForgetPass=this;
        //Constaints.ForgetPasswordActivity=1;

        back=findViewById(R.id.back);
        Resend=findViewById(R.id.resend);
        view_group=findViewById(R.id.view_group);
        Next = findViewById(R.id.login);
        Mobile = findViewById(R.id.mobile);
        mobileline = findViewById(R.id.mobile_line);
        otpLine = findViewById(R.id.otp_line);
        passLine = findViewById(R.id.confirm_pass);
        Otp=findViewById(R.id.otp);
        CreatePassword=findViewById(R.id.password);
        ConfirmPassword=findViewById(R.id.conpass);
        parentLayout = findViewById(android.R.id.content);

        mobileline.setVisibility(View.VISIBLE);
        otpLine.setVisibility(View.GONE);
        passLine.setVisibility(View.GONE);

        Next.setText("Let's go!");

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendOTP();
            }
        });


        Next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (Next.getText().equals("Let's go!"))
                {

                    mobile=Mobile.getText().toString();
                    validate();
                }
                else if (Next.getText().equals("Verify"))
                {

                    //VerifyOTP();
             sendOTP();

                }
                else if (Next.getText().equals("Create Password"))
                {


//                    createPass=CreatePassword.getText().toString();
//                    ConfirmPass=ConfirmPassword.getText().toString();
                    CreatePassword();
                }

            }
        });

        back.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        if (Next.getText().equals("Let's go!"))
                        {
                            finish();

                        }
                        else if (Next.getText().equals("Verify"))
                        {


                            TransitionSet set = new TransitionSet()
                                    .addTransition(new Slide(Gravity.LEFT))
                                    .setInterpolator(
                                            new FastOutLinearInInterpolator());

                            TransitionManager.beginDelayedTransition(view_group,set);
                            mobileline.setVisibility(View.VISIBLE);
                            otpLine.setVisibility(View.GONE);
                            Next.setText("Let's go!");

                        }
                        else if (Next.getText().equals("Create Password"))
                        {

                            TransitionSet set = new TransitionSet()
                                    .addTransition(new Slide(Gravity.LEFT))
                                    .setInterpolator(
                                            new FastOutLinearInInterpolator());

                            TransitionManager.beginDelayedTransition(view_group,set);
                            otpLine.setVisibility(View.VISIBLE);
                            passLine.setVisibility(View.GONE);
                            Next.setText("Verify");

                        }

                    }
                }
        );

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void validate(){

        if(mobile.length()!=10){
            Mobile.setError("Please Enter Valid Mobile Number");
            Mobile.requestFocus();
            return;
        }

        sendMobile();

       // sendMobile(Mobile.getText().toString());

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CreatePassword(){
        String password=CreatePassword.getText().toString();
        String conpass=ConfirmPassword.getText().toString();

        if(password.equals("")){
            CreatePassword.setError("Please Enter password");
            CreatePassword.requestFocus();
            return;
        }
        if(conpass.equals(password)){

        }else{
            ConfirmPassword.setError("Password Does not Match");
            ConfirmPassword.requestFocus();
            return;
        }


       createPassword();

    }

    public void sendMobile(){

        final ProgressDialog pd=ProgressDialog.show(ForgetPasswordActivity.this,"","Loading...");

        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call= getData.sendMobile(Mobile.getText().toString(),"1");
        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JSONArray jsonArray;
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    jsonArray = new JSONArray(new Gson().toJson(response.body()));

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String status = jsonObject.getString("res");


                    if (status.equalsIgnoreCase("success")) {
                        @SuppressLint({"NewApi", "LocalSuppress"})  TransitionSet set = new TransitionSet()
                                .addTransition(new Slide(Gravity.LEFT))
                                .setInterpolator(
                                        new FastOutLinearInInterpolator());

                        TransitionManager.beginDelayedTransition(view_group,set);
                        mobileline.setVisibility(View.GONE);
                        otpLine.setVisibility(View.VISIBLE);
                        Next.setText("Verify");
                        Snackbar.make(parentLayout, "OTP Send !", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();

                    }
                    else {

                        Snackbar.make(parentLayout, jsonObject.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong !",Toast.LENGTH_LONG).show();

                }
                //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();
                Snackbar.make(parentLayout, t.getMessage().toString(), Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        });
    }
    public void resendOTP(){

        final ProgressDialog pd=ProgressDialog.show(ForgetPasswordActivity.this,"","Loading...");

        GetData GetData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=GetData.resendOTP(Mobile.getText().toString(),"3");
        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JSONArray jsonArray;
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    jsonArray = new JSONArray(new Gson().toJson(response.body()));

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String status = jsonObject.getString("res");


                    if (status.equalsIgnoreCase("success")) {

                        Snackbar.make(parentLayout, "OTP Send !", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();

                    }
                    else {

                        Snackbar.make(parentLayout, jsonObject.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong !",Toast.LENGTH_LONG).show();

                }
                //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();
                Snackbar.make(parentLayout, t.getMessage().toString(), Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        });
    }
    public void sendOTP(){

        final ProgressDialog pd=ProgressDialog.show(ForgetPasswordActivity.this,"","Loading...");

        GetData GetData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call= GetData.sendOTP(Mobile.getText().toString(),Otp.getText().toString(),"2");
        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JSONArray jsonArray;
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    jsonArray = new JSONArray(new Gson().toJson(response.body()));

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String status = jsonObject.getString("res");


                    if (status.equalsIgnoreCase("success")) {
                        @SuppressLint({"NewApi", "LocalSuppress"}) TransitionSet set = new TransitionSet()
                                .addTransition(new Slide(Gravity.LEFT))
                                .setInterpolator(
                                        new FastOutLinearInInterpolator());

                        TransitionManager.beginDelayedTransition(view_group,set);
                        otpLine.setVisibility(View.GONE);
                        passLine.setVisibility(View.VISIBLE);
                        //otp=Otp.getText().toString();
                        Next.setText("Create Password");

                        Snackbar.make(parentLayout, "OTP Verified !", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();

                    }
                    else {

                        Snackbar.make(parentLayout, jsonObject.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong !",Toast.LENGTH_LONG).show();

                }
               // Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();
                Snackbar.make(parentLayout, t.getMessage().toString(), Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        });
    }
    public void createPassword(){

        final ProgressDialog pd=ProgressDialog.show(ForgetPasswordActivity.this,"","Loading...");

        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call= getData.sendPass(Mobile.getText().toString(),CreatePassword.getText().toString(),"4");
        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JSONArray jsonArray;
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    jsonArray = new JSONArray(new Gson().toJson(response.body()));

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String status = jsonObject.getString("res");


                    if (status.equalsIgnoreCase("success")) {

                        Snackbar.make(parentLayout, "Password Changed !", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();
                        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    else {

                        Snackbar.make(parentLayout, jsonObject.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong !",Toast.LENGTH_LONG).show();

                }
                //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();
                Snackbar.make(parentLayout, t.getMessage().toString(), Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        });
    }

}
