package digi.coders.shardaagroagency.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.OrderDetailsAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Model.OrderDetails;
import digi.coders.shardaagroagency.databinding.ActivityMyOredrsDeatilsBinding;
import retrofit2.Call;
import retrofit2.Response;

public class MyOredrsDeatils extends AppCompatActivity {


    public static String id;
    ActivityMyOredrsDeatilsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOredrsDeatilsBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(view -> finish());

        RecyclerView.LayoutManager manager =new LinearLayoutManager(getApplicationContext());
        binding.recycler.setLayoutManager(manager);


        getCartItems();

    }

    public void getCartItems() {

        final ProgressDialog pd = ProgressDialog.show(MyOredrsDeatils.this, "", "Authenticating...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getOrderDetails("cart_order_detail", id);
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {
                            Log.e("cart_order_detail", response.body().toString());

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.getString("res").equalsIgnoreCase("success")) {
                                Type type = new TypeToken<ArrayList<OrderDetails>>() {}.getType();
                                JSONArray jsonArray1 = jsonObject.getJSONArray("msg");
                                ArrayList<OrderDetails> arrayList =new Gson().fromJson(jsonArray1.toString(),type);
                                OrderDetailsAdapter adapter =new OrderDetailsAdapter(arrayList,MyOredrsDeatils.this);
                                binding.recycler.setAdapter(adapter);

                            } else {


                            }

                        } catch (Exception e) {
                            //   Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                        //  Toast.makeText(getApplicationContext(),response.body().toString(), Toast.LENGTH_LONG).show();

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
