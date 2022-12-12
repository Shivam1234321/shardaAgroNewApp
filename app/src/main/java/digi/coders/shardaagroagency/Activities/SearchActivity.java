package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.ShopProducts;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.Model.ShopProductModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    EditText searchText;
    LinearLayout no_data;
    Button back,cart;
    RecyclerView searchResult;

    ProgressBar progressbar;
    ArrayList<ShopProductModel> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_search);

        back=findViewById(R.id.back);
        searchText=findViewById(R.id.searchText);
        no_data=findViewById(R.id.no_data);
        searchResult=findViewById(R.id.searchResult);
        progressbar=findViewById(R.id.progressbar);
        cart=findViewById(R.id.cart);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchText, 0);
        back.setOnClickListener(
                v -> finish()
        );
        cart.setOnClickListener(
                v -> {
                 startActivity(new Intent(getApplicationContext(),CartActivity.class));
                 finish();
                }
        );

        searchText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if(s.length()>=3){

                            getSearchResult(searchText.getText().toString());

                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
    }

    public void getSearchResult(String str){
        progressbar.setVisibility(View.VISIBLE);
        arrayList.clear();
      //  final ProgressDialog pd=ProgressDialog.show(SearchActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.search("", SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),str);
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){

                                JSONArray jsonArray1=jsonObject.getJSONArray("msg");
                                for(int i=0;i<jsonArray1.length();i++){

                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                                    arrayList.add(new Gson().fromJson(jsonObject1.toString(),ShopProductModel.class));

                                }


                                RecyclerView.LayoutManager layoutManager=new GridLayoutManager(SearchActivity.this,2);
                                searchResult.setLayoutManager(layoutManager);
                                searchResult.setHasFixedSize(true);
                                ShopProducts shopProducts=new ShopProducts(arrayList,SearchActivity.this);
                                searchResult.setAdapter(shopProducts);
                                no_data.setVisibility(View.GONE);
                            }else{

                                TastyToast.makeText(SearchActivity.this,"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                                no_data.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            TastyToast.makeText(SearchActivity.this,"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            no_data.setVisibility(View.VISIBLE);

                        }
                        // Toast.makeText(SearchActivity.this,response.body().toString(),Toast.LENGTH_LONG).show();
                        progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        TastyToast.makeText(SearchActivity.this, t.getMessage().toString(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        no_data.setVisibility(View.VISIBLE);

                        progressbar.setVisibility(View.GONE);

                        //  Toast.makeText(SearchActivity.this,t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}
