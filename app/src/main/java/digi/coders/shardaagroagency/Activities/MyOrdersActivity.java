package digi.coders.shardaagroagency.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

import digi.coders.shardaagroagency.Adapters.MyOrdersAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.OrderModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {

    Button back;
    RecyclerView myorders;
    LinearLayout no_data;

    ArrayList<OrderModel> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_my_orders);

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
        getCartItems();
    }

    public void getCartItems(){
        final ProgressDialog pd=ProgressDialog.show(MyOrdersActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getOrders("OrderDetails", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            Log.e("orders",response.body().toString());
                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));

                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){
                                JSONArray jsonArray1=jsonObject.getJSONArray("msg");
                                for(int i=0;i<jsonArray1.length();i++) {
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                                    OrderModel orderModel = new OrderModel(

                                            jsonObject1.getString("Id"),
                                            jsonObject1.getString("OrderId"),
                                            jsonObject1.getString("Amount"),
                                            jsonObject1.getString("Date")+", "+jsonObject1.getString("Time"),
                                            jsonObject1.getString("OrderStatus"),
                                            jsonObject1.getString("TransportorName"),
                                            jsonObject1.getString("TransportorId"),
                                            jsonObject1.getString("Description"),
                                            jsonObject1.getString("TransportName"),
                                            jsonObject1.getString("GRNO"),
                                            jsonObject1.getString("NOS"),
                                            jsonObject1.getString("DeliveryDate")

                                            );

                                    arrayList.add(orderModel);
                                }
//
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                                myorders.setLayoutManager(layoutManager);
                                myorders.setHasFixedSize(true);
                                MyOrdersAdapter cartItemsAdapter=new MyOrdersAdapter(arrayList,getApplicationContext());

                                myorders.setAdapter(cartItemsAdapter);
                                no_data.setVisibility(View.GONE);

                            }else{
                                no_data.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            Log.e("order e",e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            no_data.setVisibility(View.VISIBLE);
                        }
                      //  Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        no_data.setVisibility(View.VISIBLE);
                         Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();

                         pd.dismiss();
                    }
                }
        );
    }
}
