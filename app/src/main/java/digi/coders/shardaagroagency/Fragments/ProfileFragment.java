package digi.coders.shardaagroagency.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.R;
import digi.coders.shardaagroagency.databinding.FragmentProfileBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    String Img1="",Img2="",Img3="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.image1.setOnClickListener(v -> openPopUp(Img1));
        binding.image2.setOnClickListener(v -> openPopUp(Img2));
        binding.image3.setOnClickListener(v -> openPopUp(Img3));

        getProfile();

        return view;

    }

    public void getProfile() {

        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Authenticating...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getProfile("profile", SharedPrefManager.getInstance(getActivity()).getUser().getId());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {
                            Log.e("my profiel",response.body().toString());
                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                                JSONArray jsonObject11 = jsonObject1.getJSONArray("msg");


                                binding.name.setText(jsonObject11.getJSONObject(0).getString("UserName"));
                                binding.name1.setText(jsonObject11.getJSONObject(0).getString("UserName"));
                                binding.email.setText(jsonObject11.getJSONObject(0).getString("UserEmail"));
                                binding.email1.setText(jsonObject11.getJSONObject(0).getString("UserEmail"));
                                binding.mobile.setText(jsonObject11.getJSONObject(0).getString("UserMobile"));
                                binding.address.setText(jsonObject11.getJSONObject(0).getString("UserAddress")+", "+jsonObject11.getJSONObject(0).getString("State"));
                                binding.billingAddress.setText(jsonObject11.getJSONObject(0).getString("BillingAddress") + " " + jsonObject11.getJSONObject(0).getString("BillingCity") + " " + jsonObject11.getJSONObject(0).getString("BillingState") + " " + jsonObject11.getJSONObject(0).getString("BillingPincode"));
                                binding.type.setText(jsonObject11.getJSONObject(0).getString("UserType"));
                                binding.shopName.setText(jsonObject11.getJSONObject(0).getString("ShopName"));
                                binding.licence.setText(jsonObject11.getJSONObject(0).getString("LicenceNumber"));
                                binding.gst.setText(jsonObject11.getJSONObject(0).getString("GSTNumber"));
                                binding.userid.setText("User ID : " + jsonObject11.getJSONObject(0).getString("regid"));

                                binding.firmName.setText(jsonObject11.getJSONObject(0).getString("firm_name"));
                                binding.acNo.setText(jsonObject11.getJSONObject(0).getString("account_number"));
                                binding.ifsc.setText(jsonObject11.getJSONObject(0).getString("ifsc"));
                                binding.googlePay.setText(jsonObject11.getJSONObject(0).getString("google_pay"));
                                binding.panNo.setText(jsonObject11.getJSONObject(0).getString("pan_no"));
                                binding.city.setText(jsonObject11.getJSONObject(0).getString("City"));
                                binding.area.setText(jsonObject11.getJSONObject(0).getString("Area"));

                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("Profile")).placeholder(R.drawable.logo_sharda).into(binding.profileImage);


                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("licence_no_1")).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(binding.image1);
                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("licence_no_2")).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(binding.image2);
                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("gst_file")).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(binding.image3);
                                Img1=jsonObject11.getJSONObject(0).getString("licence_no_1");
                                Img2=jsonObject11.getJSONObject(0).getString("licence_no_2");
                                Img3=jsonObject11.getJSONObject(0).getString("gst_file");


                            } else {
                                TastyToast.makeText(getActivity(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        } catch (Exception e) {
                            TastyToast.makeText(getActivity(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                        //     Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getActivity(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                }
        );
    }

    public void openPopUp( String string){

        View view = getLayoutInflater().inflate(R.layout.image_popup, null, false);

        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        ImageView image=view.findViewById(R.id.image);
        TextView close=view.findViewById(R.id.close);
        Picasso.get().load(string).placeholder(R.drawable.logo_sharda).into(image);
        close.setOnClickListener(v -> {
            popupWindow.dismiss();
        });


    }

}
