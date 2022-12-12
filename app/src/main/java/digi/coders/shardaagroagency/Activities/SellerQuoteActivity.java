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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import digi.coders.shardaagroagency.Adapters.SellerAfterSelectAdapter;
import digi.coders.shardaagroagency.Fragments.StockFragment;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.RequirementModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static digi.coders.shardaagroagency.Adapters.SellerProductsAdapter.listforReqdata;

public class SellerQuoteActivity extends AppCompatActivity {

    RecyclerView recycler;
    LinkedHashSet<String> lhSetColors;
    ElasticButton continues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_seller_quate);
        Button back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler=findViewById(R.id.recycler);
        continues=findViewById(R.id.continues);

        continues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId="" ;
                String rate="" ;
                String available="" ;
                String remark="" ;
                String stockType="" ;
                for (int i=0;i<SellerAfterSelectAdapter.productid.size();i++){
                    if (i==0){
                        productId=SellerAfterSelectAdapter.productid.get(i);
                        rate=SellerAfterSelectAdapter.ratelist.get(i);
                        available=SellerAfterSelectAdapter.availablebilitylist.get(i);
                        remark=SellerAfterSelectAdapter.remarklist.get(i);
                        stockType=SellerAfterSelectAdapter.stockType.get(i);
                    }else {
                        productId = productId + "-sharda-" + SellerAfterSelectAdapter.productid.get(i);
                        rate = rate + "-sharda-" + SellerAfterSelectAdapter.ratelist.get(i);
                        available = available + "-sharda-" + SellerAfterSelectAdapter.availablebilitylist.get(i);
                        remark = remark + "-sharda-" + SellerAfterSelectAdapter.remarklist.get(i);
                        stockType = stockType + "-sharda-" + SellerAfterSelectAdapter.stockType.get(i);
                    }
                }

                Log.i("sldjakhksfn",productId+"   "+rate);
                Log.i("sldflskjg",SellerAfterSelectAdapter.availablebilitylist+"  "+SellerAfterSelectAdapter.remarklist+"   "+SellerAfterSelectAdapter.ratelist);
                quoteRequirement(productId, rate, available, remark,stockType);

            }
        });

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        ArrayList<RequirementModel> arrayList=new ArrayList<>();
        Log.i("asasasasasfdsfdf if","sa"+listforReqdata);
        for(int i=0;i<listforReqdata.size();i++){

            if (listforReqdata.get(i)!=null) {
                arrayList.add(listforReqdata.get(i));
            }
        }
        SellerAfterSelectAdapter sellerAfterSelectAdapter=new SellerAfterSelectAdapter(getApplicationContext(), arrayList);
        recycler.setAdapter(sellerAfterSelectAdapter);

    }

    public void quoteRequirement(String productId, String rate, String available, String remark,String stockType){

        final ProgressDialog pd=ProgressDialog.show(SellerQuoteActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.quoteRequirement("quote_requirement", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),
                productId, rate, available, remark,stockType);
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){

                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                StockFragment.refresh.onRefreshData();
                                finish();

                            }else{
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(), e.getMessage().toString(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                        //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        listforReqdata.clear();
    }
}