package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.UserDetails;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassswordActivity extends AppCompatActivity {


    Button back;
    ElasticButton submit;
    EditText old_pass,new_pass,con_pass;

    View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_change_passsword);

        parentLayout=findViewById(android.R.id.content);
        old_pass=findViewById(R.id.old_pass);
        new_pass=findViewById(R.id.new_pass);
        con_pass=findViewById(R.id.con_pass);
        back=findViewById(R.id.back);
        submit=findViewById(R.id.submit);

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validate();
                    }
                }
        );
    }

    public void validate(){

        String password=old_pass.getText().toString();
        String newpass=new_pass.getText().toString();
        String conpass=con_pass.getText().toString();

        if(password.equals("")){
            old_pass.setError("Please Enter password");
            old_pass.requestFocus();
            return;
        }
        if(newpass.equals("")){
            new_pass.setError("Please Enter password");
            new_pass.requestFocus();
            return;
        }
        if(conpass.equals(newpass)){

        }else{
            con_pass.setError("Password Does not Match");
            con_pass.requestFocus();
            return;
        }
        changePassword();
    }

    public void changePassword(){

        final ProgressDialog pd=ProgressDialog.show(ChangePassswordActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.changePassword("",SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),old_pass.getText().toString(),new_pass.getText().toString());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){


                                TastyToast.makeText(getApplicationContext(), "Your Password has been Changed !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                finish();
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
}
