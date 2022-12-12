package digi.coders.shardaagroagency.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.ChatAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.ChatsModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingActivity extends AppCompatActivity {

    RecyclerView chats;
    LinearLayout no_data;
    EditText txt_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_chat);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chats=findViewById(R.id.chats);

        txt_edit=findViewById(R.id.txt_edit);
        ElasticImageView send=findViewById(R.id.send);
        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txt_edit.getText().toString().length()>0){
                            sendQuery();
                        }
                    }
                }
        );
        no_data=findViewById(R.id.no_data);
        getOrders();
    }


    public void getOrders(){
        final ProgressDialog pd=ProgressDialog.show(ChattingActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getChats("show_chat",SharedPrefManager.getInstance(ChattingActivity.this).getUser().getId());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){
                                JSONArray jsonObject11=jsonObject1.getJSONArray("data");
                                ArrayList<ChatsModel> arrayList=new ArrayList<>();
                                for(int i=0;i<jsonObject11.length();i++) {
                                    JSONObject jsonObject12=jsonObject11.getJSONObject(i);
                                    ChatsModel userDetails = new ChatsModel(
                                            jsonObject12.getString("Quantity"),
                                            jsonObject12.getString("OrderId"),
                                            jsonObject12.getString("ProductName"),
                                            jsonObject12.getString("Date")+", "+jsonObject12.getString("Time"),
                                            jsonObject12.getString("Replay"));

                                    arrayList.add(userDetails);
                                }

                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ChattingActivity.this);
                                chats.setLayoutManager(layoutManager);
                                chats.setHasFixedSize(true);
                                ChatAdapter productsAdapter=new ChatAdapter(arrayList,ChattingActivity.this);
                                chats.setAdapter(productsAdapter);
                                if(arrayList.size()==0){
                                    no_data.setVisibility(View.VISIBLE);
                                }else{
                                    no_data.setVisibility(View.GONE);
                                }

                            }else{
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                no_data.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(), e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            no_data.setVisibility(View.VISIBLE);
                        }
                        // Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        no_data.setVisibility(View.VISIBLE);
                    }
                }
        );
    }
    public void sendQuery(){

        final ProgressDialog pd=ProgressDialog.show(ChattingActivity.this,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.sendQuery("add",SharedPrefManager.getInstance(ChattingActivity.this).getUser().getId(),txt_edit.getText().toString());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                txt_edit.setText("");
                                getOrders();
                            }else{
                                TastyToast.makeText(getApplicationContext(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }


                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(), "Something Went Wrong !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            no_data.setVisibility(View.VISIBLE);
                        }
                        //Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        no_data.setVisibility(View.VISIBLE);
                    }
                }
        );
    }
}