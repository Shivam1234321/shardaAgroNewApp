package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.shardaagroagency.BuildConfig;
import digi.coders.shardaagroagency.Helper.Constants;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.UserDetails;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relay1;

    ProgressBar progressBar;
    Handler handler= new Handler();
    Runnable runnable= new Runnable() {
        @Override
        public void run() {

            if(!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            relay1.setVisibility(View.GONE);
            relay2.setVisibility(View.VISIBLE);
              //  checkStatus();

            }else {

                checkStatus();
            }
        }
    };

    LinearLayout relay2;
    TextView forgotPass,help;
    ElasticButton login,register;
    EditText username,password;
    ViewGroup root;


    View parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashActivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);


        parentLayout=findViewById(android.R.id.content);
        relay1=findViewById(R.id.relay1);
        relay2=findViewById(R.id.ralay2);
        help=findViewById(R.id.help);

        progressBar=findViewById(R.id.progressBar);

        root=findViewById(R.id.root);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        forgotPass=findViewById(R.id.forgot_pass);

        forgotPass.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),ForgetPasswordActivity.class));
                    }
                }
        );
        login=findViewById(R.id.login);

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userLogin();
                    }
                }
        );

        register=findViewById(R.id.register);

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
                    }
                }
        );
        TransitionSet set = new TransitionSet()
                .addTransition(new Scale())
                // .addTransition(new Fade())
                .setInterpolator( new LinearOutSlowInInterpolator());

        TransitionManager.beginDelayedTransition(root, set);

        handler.postDelayed(runnable, 3000);


        animateProgressBar(100);

        checkVersion();


    }
    private void animateProgressBar(int targetProgress) {
        ObjectAnimator anim = ObjectAnimator.ofInt(progressBar, "progress", targetProgress);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(3000);
        anim.start();
    }


    public void userLogin(){

        final ProgressDialog pd=ProgressDialog.show(MainActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.userLogin(username.getText().toString(),password.getText().toString(),"login");
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            Log.i("wertyu", response.body().toString());

                             JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                             JSONObject jsonObject1=jsonArray.getJSONObject(0);
                             if(jsonObject1.getString("res").equalsIgnoreCase("success")){

                                 JSONObject jsonObject11=jsonObject1.getJSONObject("data");

                                 UserDetails userDetails=new UserDetails(
                                         jsonObject11.getString("UserId"),
                                         jsonObject11.getString("UserName"),
                                         jsonObject11.getString("UserEmail"),
                                         jsonObject11.getString("UserMobile"),
                                         jsonObject11.getString("Type")
                                 );

                                 SharedPrefManager.getInstance(getApplicationContext()).userLogin(userDetails);

                                 startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                 finish();
                                 TastyToast.makeText(getApplicationContext(), "Welcome "+jsonObject11.getString("UserName")+" !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                             }else{
                                 TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                             }
                           //  Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(), "Something Went Wrong !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        Snackbar.make(parentLayout, t.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();
                    }
                }
        );
    }

    public void checkStatus(){

        //final ProgressDialog pd=ProgressDialog.show(MainActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.checkStatus(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),"user_check");
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("status").equalsIgnoreCase("active")){


                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                finish();
                            }else if(jsonObject1.getString("status").equalsIgnoreCase("delete")){

                                relay1.setVisibility(View.GONE);
                                relay2.setVisibility(View.VISIBLE);
                                TastyToast.makeText(getApplicationContext(), "You are removed from this App !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }else if(jsonObject1.getString("status").equalsIgnoreCase("block")){

                                relay1.setVisibility(View.GONE);
                                relay2.setVisibility(View.VISIBLE);
                                TastyToast.makeText(getApplicationContext(), "You are Blocked by Admin !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }else {
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(), "Something Went Wrong !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }

                       // pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        //pd.dismiss();
                        Snackbar.make(parentLayout, t.getMessage().toString(), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();
                    }
                }
        );
    }

    public void checkVersion() {
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.checkVersion("version");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    if (res.equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                        Constants.VideoLink = jsonObject1.getString("VideoLink");
                        Constants.UPI = jsonObject1.getString("UPI");
                        if (!Constants.VideoLink.isEmpty()){
                            help.setOnClickListener(v -> {
                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(Constants.VideoLink));
                                startActivity(intent);
                            });
                        }else {
                            help.setVisibility(View.GONE);
                        }




                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
