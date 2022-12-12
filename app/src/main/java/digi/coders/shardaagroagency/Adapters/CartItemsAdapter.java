package digi.coders.shardaagroagency.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Activities.CartActivity;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.Refresh;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.CartItemModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {


    ArrayList<CartItemModel> str;
    Context ctx;
    Refresh refresh;

    public CartItemsAdapter(ArrayList<CartItemModel> str, Context ctx, Refresh refresh) {
        this.str = str;
        this.ctx = ctx;
        this.refresh=refresh;
    }

    @NonNull
    @Override
    public CartItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
        return new CartItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartItemsAdapter.ViewHolder holder, final int position) {

        CartItemModel model=str.get(position);

        holder.name.setText(model.getName());
        holder.txt_count.setText(model.getQty());
        Picasso.get().load(model.getImage()).into(holder.image);
        holder.minus_btn.setOnClickListener(
                v -> {
                    if(Integer.parseInt(holder.txt_count.getText().toString())<2){
                        deleteItems(model);
                    }else{
                       addToCart(model,holder,0);
                   }
                }
        );
        holder.plus_btn.setOnClickListener(
                v -> addToCart(model,holder,1)
        );

        holder.net_cost.setText(Html.fromHtml("<strike>Rs. "+model.getPrice()+"</strike>"));
        int oldprice=Integer.parseInt(model.getQty())*Integer.parseInt(model.getOldPrice());
        int offerprice=Integer.parseInt(model.getQty())*Integer.parseInt(model.getPrice());
        holder.cart_cost.setText("Rs. "+oldprice);

        holder.discountAmount.setText("Rs. "+(Integer.parseInt(model.getQty())*(Integer.parseInt(model.getPrice())-Integer.parseInt(model.getOldPrice()))));
        holder.total_amount.setText("Rs. "+offerprice);

        if (model.getStockStatus().equals("true")){
            holder.cart_btn.setVisibility(View.VISIBLE);
        }else {
            holder.cart_btn.setVisibility(View.GONE);
        }

        holder.afetrDiscountAmount.setText("Rs. "+oldprice);

                            holder.show_detail.setText("");
                            holder.estimated_cost.setVisibility(View.VISIBLE);
                            holder.cart_cost.setVisibility(View.GONE);
                            holder.net_cost.setVisibility(View.GONE);
                            holder.show_detail.setEnabled(true);




        holder.delete_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItems(model);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return str.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //MaterialRippleLayout card;
        TextView show_detail,cart_cost,net_cost;
        LinearLayout estimated_cost,cart_btn;
        ElasticImageView plus_btn,minus_btn;
        TextView txt_count,name;
        ElasticImageView delete_btn;
        ImageView image;
        TextView total_amount,discountAmount,afetrDiscountAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // card=itemView.findEViewById(R.id.card);
            estimated_cost=itemView.findViewById(R.id.estimated_cost);
            show_detail=itemView.findViewById(R.id.show_details);
            cart_cost=itemView.findViewById(R.id.product_cast);
            net_cost=itemView.findViewById(R.id.net_price);
            name=itemView.findViewById(R.id.name);
            image=itemView.findViewById(R.id.image);
            delete_btn=itemView.findViewById(R.id.delete_btn);
            cart_btn=itemView.findViewById(R.id.cart_btn);

            plus_btn=itemView.findViewById(R.id.plus_btn);
            minus_btn=itemView.findViewById(R.id.minus_btn);
            txt_count=itemView.findViewById(R.id.txt_count);
            total_amount=itemView.findViewById(R.id.total_amount);
            discountAmount=itemView.findViewById(R.id.discount_amount);
            afetrDiscountAmount=itemView.findViewById(R.id.afetrDiscountAmount);
        }
    }

    public void deleteItems(CartItemModel productModel){

        final ProgressDialog pd=ProgressDialog.show(ctx,"","Loading...");

        GetData GetData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call;
            call = GetData.deleteCart("delete_cart", productModel.getId());

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JSONArray jsonArray;
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    jsonArray = new JSONArray(new Gson().toJson(response.body()));

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String status = jsonObject.getString("res");


                    if (status.equalsIgnoreCase("success")) {

                        refresh.onRefreshData();
                        TastyToast.makeText(ctx,"Item Deleted !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    }
                    else {
                        TastyToast.makeText(ctx,status, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                    }
                }catch(Exception e){
                    Toast.makeText(ctx,e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                //  Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();

            }
        });
    }

    public void addToCart(final CartItemModel productModel, final ViewHolder holder, final int status1){

        final ProgressDialog pd=ProgressDialog.show(ctx,"","Loading...");

        GetData GetData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=null;
        if(status1==1) {
                call = GetData.addItems("cart", SharedPrefManager.getInstance(ctx).getUser().getId(), productModel.getProduct_id(),(Integer.parseInt(holder.txt_count.getText().toString())+1)+"");

        }else{
                call = GetData.addItems("cart", SharedPrefManager.getInstance(ctx).getUser().getId(),  productModel.getProduct_id(),(Integer.parseInt(holder.txt_count.getText().toString())-1)+"");

        }
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JSONArray jsonArray;
                // SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body());
                try {
                    jsonArray = new JSONArray(new Gson().toJson(response.body()));

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String status = jsonObject.getString("res");


                    if (status.equalsIgnoreCase("success")) {
                        if(status1==0){

                            CartActivity.refreCart.onrefreshCart(productModel.getPrice(),0);
                            holder.txt_count.setText((Integer.parseInt(holder.txt_count.getText().toString())-1)+"");
                        }else{
                            CartActivity.refreCart.onrefreshCart(productModel.getPrice(),1);
                            holder.txt_count.setText((Integer.parseInt(holder.txt_count.getText().toString())+1)+"");
                        }

                        TastyToast.makeText(ctx,"Cart Items Updated !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    }
                    else {
                        TastyToast.makeText(ctx,status, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                    }
                }catch(Exception e){
                    Toast.makeText(ctx,e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                //  Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();

            }
        });
    }

}
