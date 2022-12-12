package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.ShopProducts;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Model.ShopProductModel;
import digi.coders.shardaagroagency.R;
import digi.coders.shardaagroagency.databinding.ActivityAllProductBinding;
import retrofit2.Call;
import retrofit2.Response;

public class AllProduct extends AppCompatActivity {
    ActivityAllProductBinding binding;
    String MainCategory="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllProductBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(binding.getRoot());
        binding.recycler.setLayoutManager(new GridLayoutManager(AllProduct.this, 2));

        binding.back.setOnClickListener(view -> finish());
        getShopProduct();
    }


    public void getShopProduct() {
        final ProgressDialog pd = ProgressDialog.show(AllProduct.this, "", "Loading...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getShopProducts(MainCategory);
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                        try {
                            Log.e("shop", response.body().toString());
                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.getString("res").equalsIgnoreCase("success")) {

                                JSONArray jsonArray1 = jsonObject.getJSONArray("msg");
                                ArrayList<ShopProductModel> arrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray1.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                    arrayList.add(new Gson().fromJson(jsonObject1.toString(),ShopProductModel.class));

                                }

                                ShopProducts shopProducts = new ShopProducts(arrayList, AllProduct.this);
                                binding.recycler.setAdapter(shopProducts);

                            } else {
                                binding.recycler.setAdapter(null);
                                TastyToast.makeText(getApplicationContext(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }

                        } catch (Exception e) {
                            TastyToast.makeText(getApplicationContext(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                        // Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        TastyToast.makeText(getApplicationContext(), t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        pd.dismiss();
                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}