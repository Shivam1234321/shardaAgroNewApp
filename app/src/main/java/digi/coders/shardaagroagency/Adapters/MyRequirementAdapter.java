package digi.coders.shardaagroagency.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Activities.MyOredrsDeatils;
import digi.coders.shardaagroagency.Activities.MyRequirementActivity;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.MyRequirementsModel;
import digi.coders.shardaagroagency.Model.MyRequirementsProModel;
import digi.coders.shardaagroagency.Model.OrderModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class MyRequirementAdapter extends RecyclerView.Adapter<MyRequirementAdapter.ViewHolder> {

    ArrayList<MyRequirementsModel> arrayList;
    Context ctx;
    ArrayList<MyRequirementsProModel> arrayList1=new ArrayList<>();

    public MyRequirementAdapter(ArrayList<MyRequirementsModel> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public MyRequirementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_requirement,parent,false);

        return new MyRequirementAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequirementAdapter.ViewHolder holder, final int position) {

        holder.orderId.setText("OderId- #"+arrayList.get(position).getOrderId());
        getRequirementsProduct(arrayList.get(position).getId(), holder);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView orderId;
        CardView card;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId=itemView.findViewById(R.id.orderId);
            card=itemView.findViewById(R.id.card);
            recyclerView=itemView.findViewById(R.id.recycler);

        }
    }

    public void getRequirementsProduct(String id, final ViewHolder holder){
        arrayList1.clear();
        //final ProgressDialog pd=ProgressDialog.show(ctx,"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getRequirementsPro("requirement_request_product", id);
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                        try{

                            Log.i("dfglkm", response.body().toString());

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            if(jsonObject.getString("res").equalsIgnoreCase("success")){
                                JSONArray jsonArray1=jsonObject.getJSONArray("msg");

                                for(int i=0;i<jsonArray1.length();i++) {
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);

                                    MyRequirementsProModel myRequirementsModel = new MyRequirementsProModel(
                                            jsonObject1.getString("Id"),
                                            jsonObject1.getString("Name"),
                                            jsonObject1.getString("Quantity"),
                                            jsonObject1.getString("Rate"),
                                            jsonObject1.getString("Availability"),
                                            jsonObject1.getString("Remark")
                                    );

                                    arrayList1.add(myRequirementsModel);
                                }

                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ctx);
                                holder.recyclerView.setLayoutManager(layoutManager);
                                holder.recyclerView.setHasFixedSize(true);
                                MyReqProductAdapter myRequirementAdapter=new MyReqProductAdapter(arrayList1,ctx);
                                holder.recyclerView.setAdapter(myRequirementAdapter);

                            }else{

                            }
                        }catch (Exception e){
                            Toast.makeText(ctx,e.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                        //pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        Toast.makeText(ctx,t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();
                        //pd.dismiss();
                    }
                }
        );
    }
}
