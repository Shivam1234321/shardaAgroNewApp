package digi.coders.shardaagroagency.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Activities.CartActivity;
import digi.coders.shardaagroagency.Activities.MyOrdersActivity;
import digi.coders.shardaagroagency.Activities.SearchActivity;
import digi.coders.shardaagroagency.Adapters.ShopProducts;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.OrderModel;
import digi.coders.shardaagroagency.Model.ShopProductModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class ShopFragment extends Fragment {

    ArrayList<OrderModel> arrayList1 = new ArrayList<>();

    RecyclerView products;
    ShopProducts shopProducts;
    FloatingActionButton cart, search;
    FloatingActionButton orders;
    EditText et_search;
    RadioGroup radioGroup;
    String MainCategory="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_live_frament, container, false);

        products = view.findViewById(R.id.products);
        cart = view.findViewById(R.id.cart);
        orders = view.findViewById(R.id.orders);
        search = view.findViewById(R.id.search);
        et_search = view.findViewById(R.id.et_search);
        radioGroup = view.findViewById(R.id.radioGroup);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        products.setLayoutManager(layoutManager);
        products.setHasFixedSize(true);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.all:
                    MainCategory="";
                    getShopProduct();
                    break;
                case R.id.seeds:
                    MainCategory="Seeds";
                    getShopProduct();
                    break;
                case R.id.pesticides:
                    MainCategory="Pesticides";
                    getShopProduct();
                    break;
                default:

                    break;
            }
        });

        getShopProduct();
        products.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0 && search.getVisibility() == View.VISIBLE) {
//                    search.hide();
//                } else if (dy < 0 && search.getVisibility() != View.VISIBLE) {
//                    search.show();
//                }
                if (dy > 0 && cart.getVisibility() == View.VISIBLE) {
                    cart.hide();
                } else if (dy < 0 && cart.getVisibility() != View.VISIBLE) {
                    cart.show();
                }
                if (dy > 0 && orders.getVisibility() == View.VISIBLE) {
                    orders.hide();
                } else if (dy < 0 && orders.getVisibility() != View.VISIBLE) {
                    orders.show();
                }
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shopProducts.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyOrdersActivity.class));
            }
        });
        getCartItems();
        return view;

    }

    public void getShopProduct() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Authenticating...");
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

                                shopProducts = new ShopProducts(arrayList, getActivity());
                                products.setAdapter(shopProducts);

                            } else {
                                products.setAdapter(null);
                                TastyToast.makeText(getActivity(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }

                        } catch (Exception e) {
                            TastyToast.makeText(getActivity(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        // Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        pd.dismiss();
                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void getCartItems() {

        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getOrders("OrderDetails", SharedPrefManager.getInstance(getActivity()).getUser().getId());
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.getString("res").equalsIgnoreCase("success")) {
                                JSONArray jsonArray1 = jsonObject.getJSONArray("msg");
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                    OrderModel orderModel = new OrderModel(

                                            jsonObject1.getString("id"),
                                            jsonObject1.getString("OrderId"),
                                            jsonObject1.getString("Amount"),
                                            jsonObject1.getString("Date") + ", " + jsonObject1.getString("Time")
                                            , "", "", "", "", "", "", "", ""
                                    );

                                    arrayList1.add(orderModel);
                                }
//
                                if (arrayList1.size() == 0) {
                                    orders.setVisibility(View.GONE);
                                } else {
                                    orders.setVisibility(View.VISIBLE);
                                }
                            } else {


                            }

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                        //  Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}
