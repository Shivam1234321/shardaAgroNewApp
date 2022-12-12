package digi.coders.shardaagroagency.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import digi.coders.shardaagroagency.Activities.AllProduct;
import digi.coders.shardaagroagency.Activities.HomeActivity;
import digi.coders.shardaagroagency.Adapters.HomeProducts;
import digi.coders.shardaagroagency.Adapters.OffersSliderAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Model.ShopProductModel;
import digi.coders.shardaagroagency.Model.SliderModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    int limit = 0;
    ViewPager viewPager;
    OffersSliderAdapter offersSliderAdapter;
    int currentPage = 0;
    Timer timer;
    RecyclerView recyclerView;
    TextView viewAll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        viewAll = view.findViewById(R.id.view_all);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        viewAll.setOnClickListener(view1 -> {
//            startActivity(new Intent(getActivity(), AllProduct.class));
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new ShopFragment()).commit();
            HomeActivity.bottomNav.getMenu().findItem(R.id.nav_shop).setChecked(true);
        });

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == 6 - 1) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 5000);
        showSlider();
        homeProduct();

        return view;
    }

    public void homeProduct() {
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.homeProduct("home_product");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.getString("res").equalsIgnoreCase("success")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("msg");
                        ArrayList<ShopProductModel> arrayList = new ArrayList<>();
                        for (int i=0;i<jsonArray1.length();i++) {
                            arrayList.add(new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),ShopProductModel.class));
                        }
                        recyclerView.setAdapter(new HomeProducts(arrayList,getActivity()));
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("homeProduct e", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("homeProduct t", t.getMessage());
            }
        });




    }

    public void showSlider() {
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getSlider("banner");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.getString("res").equalsIgnoreCase("success")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("msg");
                        List<SliderModel> arrayList = new ArrayList<>();
                        for (int i=0;i<jsonArray1.length();i++) {
                            arrayList.add(new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),SliderModel.class));
                        }
                        limit = arrayList.size();
                        offersSliderAdapter = new OffersSliderAdapter(getActivity(), arrayList);
                        viewPager.setAdapter(offersSliderAdapter);
                        offersSliderAdapter.notifyDataSetChanged();
                        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
                        viewPager.setOffscreenPageLimit(limit);


                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("showSlider e", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("showSlider t", t.getMessage());
            }
        });




    }


}