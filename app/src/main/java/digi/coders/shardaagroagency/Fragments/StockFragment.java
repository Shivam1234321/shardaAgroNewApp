package digi.coders.shardaagroagency.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import digi.coders.shardaagroagency.Activities.CalulateQuantity;
import digi.coders.shardaagroagency.Activities.ChattingActivity;
import digi.coders.shardaagroagency.Activities.SellerQuoteActivity;
import digi.coders.shardaagroagency.Activities.StockLive;
import digi.coders.shardaagroagency.Adapters.ProductsAdapter;
import digi.coders.shardaagroagency.Adapters.SellerProductsAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.Refresh;
import digi.coders.shardaagroagency.Helper.RefreshData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.Model.RequirementModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static digi.coders.shardaagroagency.Adapters.SellerProductsAdapter.listforReqdata;

public class StockFragment extends Fragment implements RefreshData, Refresh {


    CardView order_now;
    RecyclerView products, sellerProducts;
    ViewGroup root;
    ArrayList<ProductModel> arrayList = new ArrayList<>();
    ArrayList<RequirementModel> arrayList1 = new ArrayList<>();
    LinkedHashSet<String> lhSetColors;

    LinearLayout no_data;
    AlertDialog alert2;

    public static Refresh refresh;
    FloatingActionButton stockLive, chat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        refresh = StockFragment.this;
        stockLive = view.findViewById(R.id.stock);
        chat = view.findViewById(R.id.chat);
        order_now = view.findViewById(R.id.order_now);
        products = view.findViewById(R.id.products);
        sellerProducts = view.findViewById(R.id.sellerProducts);

        if (SharedPrefManager.getInstance(getActivity()).getUser().getType().equalsIgnoreCase("Seller")) {
            products.setVisibility(View.GONE);
            sellerProducts.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
        } else {
            products.setVisibility(View.VISIBLE);
            sellerProducts.setVisibility(View.GONE);
        }

        no_data = view.findViewById(R.id.no_data);
        root = view.findViewById(R.id.root);
        TransitionSet set = new TransitionSet()
                .addTransition(new Scale())
                // .addTransition(new Fade())
                .setInterpolator(new LinearOutSlowInInterpolator());

        TransitionManager.beginDelayedTransition(root, set);

        order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getActivity()).getUser().getType().equalsIgnoreCase("Seller")) {
                    startActivity(new Intent(getActivity(), SellerQuoteActivity.class));
                    Log.i("kjhgfdsaASDFGHJK", listforReqdata + "");
                } else {
                    startActivity(new Intent(getActivity(), CalulateQuantity.class));
                }
            }
        });

        if (SharedPrefManager.getInstance(getActivity()).getUser().getType().equalsIgnoreCase("Seller")) {
            stockLive.setVisibility(View.GONE);
        }
        stockLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StockLive.class));
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChattingActivity.class));
            }
        });

        getProducts();
        getSellerProducts();


        return view;
    }

    public void getProducts() {
        arrayList.clear();
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Authenticating...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getProducts();
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                                JSONArray jsonObject11 = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < jsonObject11.length(); i++) {

                                    JSONObject jsonObject12 = jsonObject11.getJSONObject(i);
                                    ProductModel userDetails = new ProductModel(
                                            jsonObject12.getString("ProductId"),
                                            jsonObject12.getString("ProductName"),
                                            jsonObject12.getString("ProductStock"));

                                    arrayList.add(userDetails);
                                }

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                products.setLayoutManager(layoutManager);
                                products.setHasFixedSize(true);
                                ProductsAdapter productsAdapter = new ProductsAdapter(arrayList, getActivity(), StockFragment.this);
                                products.setAdapter(productsAdapter);

                                if (arrayList.size() == 0) {
                                    no_data.setVisibility(View.VISIBLE);
                                } else {
                                    no_data.setVisibility(View.GONE);
                                }
                            } else {
                                no_data.setVisibility(View.VISIBLE);
                                TastyToast.makeText(getActivity(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        } catch (Exception e) {
                            TastyToast.makeText(getActivity(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            no_data.setVisibility(View.VISIBLE);
                        }
                        //  Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        no_data.setVisibility(View.VISIBLE);
                        TastyToast.makeText(getActivity(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }
        );
    }

    public void getSellerProducts() {
        arrayList1.clear();
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Authenticating...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getSellerProducts("requirement");
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                        Log.i("wertyu", response.body().toString());
                        try {

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                                JSONArray jsonObject11 = jsonObject1.getJSONArray("msg");
                                for (int i = 0; i < jsonObject11.length(); i++) {

                                    JSONObject jsonObject12 = jsonObject11.getJSONObject(i);
                                    RequirementModel requirementModel = new RequirementModel(
                                            jsonObject12.getString("RequirementId"),
                                            jsonObject12.getString("ProductName"),
                                            jsonObject12.getString("ProductQuantity"),
                                            jsonObject12.getString("ProductRequirementType"));

                                    arrayList1.add(requirementModel);
                                }

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                sellerProducts.setLayoutManager(layoutManager);
                                sellerProducts.setHasFixedSize(true);
                                SellerProductsAdapter sellerProductsAdapter = new SellerProductsAdapter(arrayList1, getActivity(), StockFragment.this);
                                sellerProducts.setAdapter(sellerProductsAdapter);

                                if (arrayList1.size() == 0) {
                                    no_data.setVisibility(View.VISIBLE);
                                } else {
                                    no_data.setVisibility(View.GONE);
                                }
                            } else {
                                no_data.setVisibility(View.VISIBLE);
                                TastyToast.makeText(getActivity(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        } catch (Exception e) {
                            TastyToast.makeText(getActivity(), e.getMessage().toString(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            no_data.setVisibility(View.VISIBLE);
                        }
                        //  Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        no_data.setVisibility(View.VISIBLE);
                        TastyToast.makeText(getActivity(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                }
        );
    }

    @Override
    public void onRefresh(boolean b) {
        if (b) {
            order_now.setVisibility(View.VISIBLE);
        } else {
            int count = 0;
            if (SharedPrefManager.getInstance(getActivity()).getUser().getType().equalsIgnoreCase("Seller")) {
                for (int i = 0; i < SellerProductsAdapter.str.length; i++) {
                    if (!SellerProductsAdapter.str[i].equalsIgnoreCase("")) {
                        count++;
                        //Toast.makeText(getActivity(), "countSellerProducts"+count++, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                for (int i = 0; i < ProductsAdapter.str.length; i++) {
                    if (!ProductsAdapter.str[i].equalsIgnoreCase("")) {
                        count++;
                        //Toast.makeText(getActivity(), "countProducts"+count++, Toast.LENGTH_LONG).show();
                    }
                }
            }

            if (count == 0) {
                order_now.setVisibility(View.GONE);
            } else {
                order_now.setVisibility(View.VISIBLE);
            }
        }
    }


    protected void showInputDialog2(String[] products, String[] qtylist) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.confirmation_for_product, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);


        alert2 = alertDialogBuilder.create();

        TextView productName = promptView.findViewById(R.id.productsName);
        String name = "";
        int j = 1;
        for (int i = 0; i < products.length; i++) {
            if (products[i] != null) {
                if (!products[i].equalsIgnoreCase("")) {
                    if (i == 0) {
                        name = (j) + ". " + products[i] + "   >  " + qtylist[i] + "\n";
                    } else if (i == products.length - 1) {
                        name = name + (j) + ". " + products[i] + "   >  " + qtylist[i];
                    } else {
                        name = name + (j) + ". " + products[i] + "   >  " + qtylist[i] + "\n";
                    }
                    j++;
                }
            }
        }
        productName.setText(name);
        ElasticButton ok = promptView.findViewById(R.id.ok);
        ElasticButton cancel = promptView.findViewById(R.id.cancel);

        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert2.dismiss();
                    }
                }
        );

        alert2.show();
    }

    @Override
    public void onRefreshData() {
        getProducts();
    }
}
