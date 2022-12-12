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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.MyOrdersAdapter;
import digi.coders.shardaagroagency.Adapters.MyRequirementAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.MyRequirementsModel;
import digi.coders.shardaagroagency.Model.OrderModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class MyRequirementActivity extends AppCompatActivity {

    Button back;
    RecyclerView myorders;
    LinearLayout no_data;
    ArrayList<MyRequirementsModel> arrayList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_my_requirement);

        back=findViewById(R.id.back);
        myorders=findViewById(R.id.myOrders);
        no_data=findViewById(R.id.no_data);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        getRequirements();

    }

    public void getRequirements(){
        arrayList.clear();
        final ProgressDialog pd=ProgressDialog.show(MyRequirementActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getRequirements("requirement_request_history", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                        try{

                            Log.i("dfgl", response.body().toString());

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){
                                JSONArray jsonArray1=jsonObject.getJSONArray("msg");

                                for(int i=0;i<jsonArray1.length();i++) {
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);

                                    MyRequirementsModel myRequirementsModel = new MyRequirementsModel(
                                            jsonObject1.getString("Id"),
                                            jsonObject1.getString("OrderId")
                                    );

                                    arrayList.add(myRequirementsModel);
                                }

                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                                myorders.setLayoutManager(layoutManager);
                                myorders.setHasFixedSize(true);
                                MyRequirementAdapter myRequirementAdapter=new MyRequirementAdapter(arrayList,getApplicationContext());
                                myorders.setAdapter(myRequirementAdapter);
                                no_data.setVisibility(View.GONE);

                            }else{
                                no_data.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            no_data.setVisibility(View.VISIBLE);
                        }
                        //  Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        no_data.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }
                }
        );
    }

}