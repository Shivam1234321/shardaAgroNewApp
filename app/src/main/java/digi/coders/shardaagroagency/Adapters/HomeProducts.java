package digi.coders.shardaagroagency.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import digi.coders.shardaagroagency.Activities.ProductDetailActivity;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.ShopProductModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeProducts extends RecyclerView.Adapter<HomeProducts.ViewHolder> {

    Context ctx;
    ArrayList<ShopProductModel> productModels;
    private ArrayList<ShopProductModel> arraylist;

    public HomeProducts(ArrayList<ShopProductModel> arrayList, Context ctx) {
        this.ctx = ctx;
        this.productModels = arrayList;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(productModels);
    }

    @NonNull
    @Override
    public HomeProducts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_product_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProducts.ViewHolder holder, final int position) {


        if (productModels.get(position).getStock().equals("0")) {
            holder.product_price_single.setVisibility(View.GONE);
            holder.productPrice.setVisibility(View.GONE);
            holder.ProductNetPrice.setVisibility(View.GONE);
            holder.out_of_stock.setVisibility(View.VISIBLE);
            holder.cart_btn.setVisibility(View.GONE);
        } else {
            holder.product_price_single.setVisibility(View.VISIBLE);
            holder.productPrice.setVisibility(View.VISIBLE);
            holder.ProductNetPrice.setVisibility(View.VISIBLE);
            holder.out_of_stock.setVisibility(View.GONE);
            holder.cart_btn.setVisibility(View.VISIBLE);


        }
        holder.productName.setText(productModels.get(position).getProductName());
        holder.product_price_single.setText("₹. " + productModels.get(position).getSinglePrice());
        holder.productPrice.setText("₹. " + productModels.get(position).getProductPrice());
        holder.ProductNetPrice.setText(Html.fromHtml("<strike>₹ " + productModels.get(position).getProductOfferPrice() + "</strike>"));

        Picasso.get().load(productModels.get(position).getProductImage1()).into(holder.image);

        holder.cart_btn.setOnClickListener(
                v -> addToCart(productModels.get(position))
        );
        holder.card.setOnClickListener(v -> {
                    ProductDetailActivity.shopProductModel = productModels.get(position);
                    Intent intent = new Intent(ctx, ProductDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
        );
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ElasticImageView cart_btn;
        ImageView image;
        MaterialRippleLayout card;
        TextView productName, productPrice, ProductNetPrice, product_price_single, out_of_stock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
            productPrice = itemView.findViewById(R.id.product_price);
            productName = itemView.findViewById(R.id.product_name);
            ProductNetPrice = itemView.findViewById(R.id.netAmount);
            image = itemView.findViewById(R.id.image);
            cart_btn = itemView.findViewById(R.id.cart_btn);
            product_price_single = itemView.findViewById(R.id.product_price_single);
            out_of_stock = itemView.findViewById(R.id.out_of_stock);

        }
    }

    public void filter(String id) {

        id = id.toLowerCase(Locale.getDefault());
        productModels.clear();
        if (id.length() == 0) {
            productModels.addAll(arraylist);
        } else {
            for (ShopProductModel wp : arraylist) {
                if (wp.getProductName().toLowerCase(Locale.getDefault()).contains(id)) {
                    productModels.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }


    public void addToCart(ShopProductModel productModel) {

        final ProgressDialog pd = ProgressDialog.show(ctx, "", "Loading...");

        GetData getDataServices = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getDataServices.addItems("cart", SharedPrefManager.getInstance(ctx).getUser().getId(), productModel.getProductId(), "1");
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

                        TastyToast.makeText(ctx, "Item Added to Cart", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    } else {
                        TastyToast.makeText(ctx, status, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, e.getMessage().toString(), Toast.LENGTH_LONG).show();

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
