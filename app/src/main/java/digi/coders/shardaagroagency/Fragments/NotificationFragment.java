package digi.coders.shardaagroagency.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.NotiFicationAdapter;
import digi.coders.shardaagroagency.Adapters.ProductsAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.NotificatioModel;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {


    RecyclerView notification;

    LinearLayout no_data;

    ArrayList<NotificatioModel> arrayList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);

        no_data=view.findViewById(R.id.no_data);
        notification=view.findViewById(R.id.notification);
        getNotification();
        return view;
    }
    public void getNotification(){

        arrayList.clear();
        final ProgressDialog pd=ProgressDialog.show(getActivity(),"","Authenticating...");
        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getNotification(SharedPrefManager.getInstance(getActivity()).getUser().getId(),"notification");
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){

                                JSONArray jsonObject11=jsonObject1.getJSONArray("msg");

                                for(int i=0;i<jsonObject11.length();i++) {

                                    JSONObject jsonObject12=jsonObject11.getJSONObject(i);
                                    NotificatioModel userDetails = new NotificatioModel(
                                            jsonObject12.getString("Title"),
                                            jsonObject12.getString("Description"),
                                            jsonObject12.getString("PDF"),
                                            jsonObject12.getString("Status"),
                                            jsonObject12.getString("Date")+" , "+jsonObject12.getString("Time"));

                                   arrayList.add(userDetails);
                                }

                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
                                notification.setLayoutManager(layoutManager);
                                notification.setHasFixedSize(true);
                                NotiFicationAdapter productsAdapter=new NotiFicationAdapter(arrayList,getActivity());
                                notification.setAdapter(productsAdapter);


                                if(arrayList.size()==0) {
                                    no_data.setVisibility(View.VISIBLE);
                                }else{
                                    no_data.setVisibility(View.GONE);
                                }
                            }else{
                                no_data.setVisibility(View.VISIBLE);
                                TastyToast.makeText(getActivity(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        }catch (Exception e){
                            no_data.setVisibility(View.VISIBLE);
                            TastyToast.makeText(getActivity(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                          //Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

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

}
