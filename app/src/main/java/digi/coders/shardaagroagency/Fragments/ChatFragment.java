package digi.coders.shardaagroagency.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Adapters.ChattingAdapter;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.ChattingModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    String msg;
    ImageView send;
    EditText editText;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    RelativeLayout bottom;
    public int counter=0;
    LinearLayoutManager manager;
    TextView no;
    ProgressDialog dialog;
    CountDownTimer countDownTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        send=view.findViewById(R.id.send);
        editText=view.findViewById(R.id.msg);
        no=view.findViewById(R.id.no);
        recyclerView=view.findViewById(R.id.chat_recycler);

        refreshLayout=view.findViewById(R.id.swipe_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                showChat();

            }
        });
        bottom=view.findViewById(R.id.bottom);
        manager=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg=editText.getText().toString();
                if (msg.isEmpty()){
                    editText.setError("Enter message");
                    editText.requestFocus();
                }else {
                    sendSms();
                }

            }
        });


        showChat();


        return view;
    }



    public void sendSms(){
        dialog.show();
        GetData getDataService= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getDataService.user_chat("user_chat", SharedPrefManager.getInstance(getActivity()).getUser().getId(),msg);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String res=jsonObject.getString("res");
                    if (res.equalsIgnoreCase("success")){
                        editText.getText().clear();
                        showChat();
                    }else {
                        Toast.makeText(getActivity(),jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showChat(){
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        GetData getDataService=RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getDataService.user_chat_show("user_chat_show",SharedPrefManager.getInstance(getActivity()).getUser().getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String res=jsonObject.getString("res");
                    if (res.equalsIgnoreCase("success")){
                        JSONArray jsonArray1=jsonObject.getJSONArray("data");
                        ArrayList<ChattingModel> arrayList=new ArrayList<>();
                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                            ChattingModel model=new ChattingModel(
                                    jsonObject1.getString("ChatId"),
                                    jsonObject1.getString("Message"),
                                    jsonObject1.getString("MessageBy"),
                                    jsonObject1.getString("Profile"),
                                    jsonObject1.getString("Date")+", "+jsonObject1.getString("Time")
                            );
                            arrayList.add(model);
                            ChattingAdapter adapter=new ChattingAdapter(arrayList, getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                        getTimer();

                    }else {
                        no.setVisibility(View.VISIBLE);
                        getTimer();
                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                refreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void getTimer () {
        counter=10000;
        countDownTimer= new CountDownTimer(counter,1000){
            @Override
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

            }

            @Override
            public void onFinish() {
                showChat();
            }
        };
        countDownTimer.start();
    }

}
