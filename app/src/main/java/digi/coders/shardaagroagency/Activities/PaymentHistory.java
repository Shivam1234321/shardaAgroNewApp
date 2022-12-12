package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.PaymentAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.PaymentModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistory extends AppCompatActivity {
    ProgressDialog dialog;
    Button back;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_payment_history);
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        back=findViewById(R.id.back);
        recycler=findViewById(R.id.recycler);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(manager);
        recycler.setHasFixedSize(true);

        getPaymentHistory();
    }
    public void getPaymentHistory(){
        dialog.show();
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.paymentHistory("show_payment_details", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    Log.e("payment history",response.body().toString());
                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    if (jsonObject.getString("res").equals("success")){
                        JSONArray jsonArray1=jsonObject.getJSONArray("data");
                        ArrayList<PaymentModel> arrayList=new ArrayList<>();
                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                            PaymentModel model = new PaymentModel(
                                    jsonObject1.getString("PaymentId"),
                                    jsonObject1.getString("Amount"),
                                    jsonObject1.getString("Type"),
                                    jsonObject1.getString("Remark"),
                                    jsonObject1.getString("PayType"),
                                    jsonObject1.getString("Date")+", "+jsonObject1.getString("Time"),
                                    jsonObject1.getString("Status"),
                                    jsonObject1.getString("Name")
                            );
                            arrayList.add(model);
                        }
                        PaymentAdapter adapter =new PaymentAdapter(arrayList,PaymentHistory.this);
                        recycler.setAdapter(adapter);

                    }else {
                        Toast.makeText(PaymentHistory.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(PaymentHistory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(PaymentHistory.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}