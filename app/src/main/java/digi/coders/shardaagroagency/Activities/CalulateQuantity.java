package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.LinkedHashSet;

import digi.coders.shardaagroagency.Adapters.AfterSelectAdapter;
import digi.coders.shardaagroagency.Adapters.ProductsAdapter;
import digi.coders.shardaagroagency.Fragments.StockFragment;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalulateQuantity extends AppCompatActivity {

    Button back;
    RecyclerView items;
    ElasticButton continu;

    String orderID="",qty="";
    LinkedHashSet<String> lhSetColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_calulate_quantity);

        back=findViewById(R.id.back);
        items=findViewById(R.id.items);

        continu=findViewById(R.id.continues);
        if(lhSetColors==null) {
            lhSetColors =
                    new LinkedHashSet<String>(Arrays.asList(ProductsAdapter.str));
        }else{
            lhSetColors.clear();
            lhSetColors =
                    new LinkedHashSet<String>(Arrays.asList(ProductsAdapter.str));
        }

        //create array from the LinkedHashSet
        final String[] newArray = lhSetColors.toArray(new String[ lhSetColors.size() ]);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        items.setLayoutManager(layoutManager);
        items.setHasFixedSize(true);
        ArrayList<ProductModel> arrayList=new ArrayList<>();
        for(int i=0;i<newArray.length;i++){
            for(int j=0;j<ProductsAdapter.listfordata.size();j++) {
                if (ProductsAdapter.listfordata.get(j) != null) {
                    if(ProductsAdapter.listfordata.get(j).getId().equalsIgnoreCase(newArray[i])){

                        arrayList.add(ProductsAdapter.listfordata.get(j));
                        break;
                    }
                }

            }
        }
        AfterSelectAdapter afterSelectAdapter=new AfterSelectAdapter(getApplicationContext(),arrayList);
        items.setAdapter(afterSelectAdapter);

        continu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                   //     items.getAdapter().notifyDataSetChanged();
                        orderID="";
                        qty="";

                        String[] name=new String[newArray.length];
                        String[] qtylist=new String[newArray.length];
                        for(int i=0;i<newArray.length;i++){
                            for(int j=0;j<ProductsAdapter.listfordata.size();j++) {
                                if (ProductsAdapter.listfordata.get(j) != null) {
                                    if(ProductsAdapter.listfordata.get(j).getId().equalsIgnoreCase(newArray[i])){

                                        if (i != 0) {
                                            if(!orderID.equalsIgnoreCase("")) {
                                                orderID = orderID + "sharda" + ProductsAdapter.listfordata.get(j).getId();
                                            }else{
                                                orderID = ProductsAdapter.listfordata.get(j).getId();
                                            }
                                        } else {
                                            orderID = ProductsAdapter.listfordata.get(j).getId();

                                        }
                                        name[i]=ProductsAdapter.listfordata.get(j).getName();
                                        qtylist[i]=ProductsAdapter.listfordata.get(j).getStock();

                                        break;
                                    }
                                }

                            }
                        }

                        for(int i=0;i<AfterSelectAdapter.quantity.length;i++){
                            if (i != 0) {
                                if(!qty.equalsIgnoreCase("")) {
                                    qty = qty + "sharda" + AfterSelectAdapter.quantity[i];
                                }else{
                                    qty = AfterSelectAdapter.quantity[i];
                                }
                            } else {
                                qty = AfterSelectAdapter.quantity[i];
                            }
                        }
                        addProducts();

                    }
                }
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );


    }

    public void addProducts(){

        final ProgressDialog pd=ProgressDialog.show(CalulateQuantity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.addProducts("add_product", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),qty,orderID);
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
                            TastyToast.makeText(getApplicationContext(), "Data not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
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

}
