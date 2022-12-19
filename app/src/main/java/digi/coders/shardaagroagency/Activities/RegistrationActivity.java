package digi.coders.shardaagroagency.Activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import digi.coders.shardaagroagency.Helper.Constants;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.CityModel;
import digi.coders.shardaagroagency.Model.UserDetails;
import digi.coders.shardaagroagency.R;
import digi.coders.shardaagroagency.databinding.ActivityRegistrationBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding binding;


    LinearLayout user_details, otp_line;
    ElasticButton register;
    ElasticImageView back;
    ViewGroup view_group;
    CheckBox check_terms;
    TextView txt_terms;
    int i = 0;
    View parentLayout;
    EditText name, shop_name, email, mobile, password, otp, address, gst;
    TextView resendOtp;
    Spinner state;
    String State, City, PinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(binding.getRoot());

        state = findViewById(R.id.state);
        user_details = findViewById(R.id.user_details);
        otp_line = findViewById(R.id.otp_line);
        back = findViewById(R.id.back);
        register = findViewById(R.id.register);
        address = findViewById(R.id.address);
        gst = findViewById(R.id.gst);

        shop_name = findViewById(R.id.shop_name);
        otp = findViewById(R.id.otp);
        check_terms = findViewById(R.id.check_terms);
        txt_terms = findViewById(R.id.txt_terms);

        resendOtp = findViewById(R.id.resend);
        resendOtp.setOnClickListener(
                v -> resendOTP()
        );

        parentLayout = findViewById(android.R.id.content);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        view_group = findViewById(R.id.root);

        txt_terms.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://shardaagroagency.com/term-for-use/"));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
        );
        register.setOnClickListener(
                v -> {
                    if (i == 0) {
                        validateUser();
                    } else {
                        otpVerification();
                    }
                }
        );


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Constants.state.split(","));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(dataAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) state.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));
                State = state.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back.setOnClickListener(
                v -> {
                    if (i == 1) {
                        @SuppressLint({"NewApi", "LocalSuppress"}) TransitionSet set = new TransitionSet()
                                .addTransition(new Slide(Gravity.LEFT))
                                .setInterpolator(
                                        new FastOutLinearInInterpolator());
                        TransitionManager.beginDelayedTransition(view_group, set);
                        user_details.setVisibility(View.VISIBLE);
                        otp_line.setVisibility(View.GONE);
                        i = 0;
                    } else {
                        finish();
                    }
                }
        );

        getCity();
    }

    void getCity() {
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getCityList("city");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    if (res.equalsIgnoreCase("success")) {

                        Type type = new TypeToken<ArrayList<CityModel>>() {
                        }.getType();

                        List<CityModel> list = new Gson().fromJson(jsonObject.getJSONArray("msg").toString(), type);
                        List<String> cityList = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            cityList.add(list.get(i).getCity());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.citySpinner.setAdapter(dataAdapter);
                        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ((TextView) binding.citySpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));
                                City = binding.citySpinner.getSelectedItem().toString();
                                Log.e(TAG, "onItemSelected: " + City);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {

            }
        });

    }

    void validateUser() {
        if (check_terms.isChecked()) {
            userRegistration();
        } else {
            Toast.makeText(getApplicationContext(), "You have to agree for the terms and conditions !", Toast.LENGTH_LONG).show();
        }
    }

    public void userRegistration() {
        final ProgressDialog pd = ProgressDialog.show(RegistrationActivity.this, "", "Loading...");
        GetData GetData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = GetData.userRegistration(name.getText().toString(), shop_name.getText().toString(),
                email.getText().toString(), mobile.getText().toString(),
                address.getText().toString(), password.getText().toString(), "register", gst.getText().toString(), State,
                binding.area.getText().toString(), City, binding.pincode.getText().toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    Log.e(TAG, "onResponse: reg "+response.body().toString() );
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if (jsonObject1.getString("res").equalsIgnoreCase("success")) {
                        @SuppressLint({"NewApi", "LocalSuppress"}) TransitionSet set = new TransitionSet()
                                .addTransition(new Slide(Gravity.LEFT))
                                .setInterpolator(
                                        new FastOutLinearInInterpolator());

                        TransitionManager.beginDelayedTransition(view_group, set);
                        user_details.setVisibility(View.GONE);
                        otp_line.setVisibility(View.VISIBLE);
                        i = 1;

                        TastyToast.makeText(getApplicationContext(), jsonObject1.getString("res"), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    } else {
                        //Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();
                        Log.d("Shivam", jsonObject1.getString("msg"));
                        Snackbar.make(parentLayout, jsonObject1.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                .show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong !", Toast.LENGTH_LONG).show();

                }
                //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: t "+t.getMessage() );
                pd.dismiss();
                Snackbar.make(parentLayout, t.getMessage().toString(), Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        });
    }

    public void otpVerification() {
        final ProgressDialog pd = ProgressDialog.show(RegistrationActivity.this, "", "Loading...");
        GetData GetData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = GetData.verifyOTP(mobile.getText().toString(), otp.getText().toString(), "otp_match");
        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {

                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if (jsonObject1.getString("res").equalsIgnoreCase("success")) {
                        JSONObject jsonObject11 = jsonObject1.getJSONObject("data");

                        UserDetails userDetails = new UserDetails(
                                jsonObject11.getString("UserId"),
                                jsonObject11.getString("UserName"),
                                jsonObject11.getString("UserEmail"),
                                jsonObject11.getString("UserMobile"),
                                jsonObject11.getString("Type")
                        );

                        showInputDialog2(jsonObject11.getString("RegisterId"));
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userDetails);

                        TastyToast.makeText(getApplicationContext(), "Welcome " + jsonObject11.getString("UserName") + " !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    } else {

                        Snackbar.make(parentLayout, jsonObject1.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                .show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();

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
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        });
    }

    public void resendOTP() {

        final ProgressDialog pd = ProgressDialog.show(RegistrationActivity.this, "", "Loading...");

        GetData GetData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = GetData.resendOTP(mobile.getText().toString(), "resend");
        call.enqueue(new Callback<JsonArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {

                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                        TastyToast.makeText(getApplicationContext(), jsonObject1.getString("res"), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    } else {

                        Snackbar.make(parentLayout, jsonObject1.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                .show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong !", Toast.LENGTH_LONG).show();

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
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        });
    }

    protected void showInputDialog2(String id) {
        LayoutInflater layoutInflater = LayoutInflater.from(RegistrationActivity.this);
        View promptView = layoutInflater.inflate(R.layout.registerid_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
        alertDialogBuilder.setView(promptView);


        final AlertDialog alert2 = alertDialogBuilder.create();
        ElasticButton ok = promptView.findViewById(R.id.ok);
        TextView registerid = promptView.findViewById(R.id.register);

        registerid.setText(id);
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );


        alert2.setCanceledOnTouchOutside(false);
        alert2.show();
    }
}
