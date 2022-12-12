package digi.coders.shardaagroagency.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.CouponsAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Model.CouponsModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponActivity extends AppCompatActivity {


    RecyclerView coupons;
    Button back;

    ArrayList<CouponsModel> arrayList=new ArrayList<>();
    LinearLayout no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_coupon);


        back=findViewById(R.id.back);
        coupons=findViewById(R.id.coupons);
        no_data=findViewById(R.id.no_data);

        getCoupons();
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    public void getCoupons(){

        arrayList.clear();
        final ProgressDialog pd=ProgressDialog.show(CouponActivity.this,"","Loading...");

        GetData getDataServices= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=null;
           call = getDataServices.getOffers("offer");

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

                        JSONArray jsonArray1=jsonObject.getJSONArray("msg");
                        for(int i=0;i<jsonArray1.length();i++) {
                            JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                            CouponsModel categoryProduct = new CouponsModel(

                                    "",
                                    jsonObject1.getString("Code"),
                                    jsonObject1.getString("Title"),
                                    jsonObject1.getString("Discount"),
                                    ""
                            );
                            arrayList.add(categoryProduct);
                        }

                        if(arrayList.size()==0){
                            no_data.setVisibility(View.VISIBLE);
                        }else{

                            no_data.setVisibility(View.GONE);
                        }
                        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        coupons.setLayoutManager(layoutManager);
                        coupons.setHasFixedSize(true);
                        CouponsAdapter cartItemsAdapter;

                            cartItemsAdapter=new CouponsAdapter(arrayList,getApplicationContext());

                        coupons.setAdapter(cartItemsAdapter);

                    }
                    else {

                        no_data.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    no_data.setVisibility(View.VISIBLE);
                    // Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();
                 Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

}
