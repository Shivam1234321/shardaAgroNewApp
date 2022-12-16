package digi.coders.shardaagroagency.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import digi.coders.shardaagroagency.Helper.Constants;
import digi.coders.shardaagroagency.Helper.FunctionClass;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.Model.CityModel;
import digi.coders.shardaagroagency.R;
import digi.coders.shardaagroagency.databinding.FragmentEditProfileBinding;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class EditProfileFragment extends Fragment {

    FragmentEditProfileBinding binding;
    private int GALLERY = 1, CAMERA = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static Bitmap bitmap;
    public static Uri contentURI;
    public static File file;
    String mCameraFileName = "";
    String str_profile = "";
    String Name, Email, Mobile, Address, Gst, ShopName, Licence, FirmName, ACNo, IFSC, GooglePay, PanNo, State, City, Area;
    Uri uri1 = Uri.parse(""), uri2 = Uri.parse(""), uri3 = Uri.parse("");
    String encodedString1 = "", encodedString2 = "", encodedString3 = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.profileImage.setOnClickListener(
                v -> ShowOptionDialogue()
        );

        binding.save.setOnClickListener(v -> {
            if (validation()) {
                updateDetails();
            }
        });


        binding.licence1.setOnClickListener(v -> {
            Constants.image = "image1";
            uri1 = Uri.parse("");
            CropImage.activity(uri1)
                    .start(getContext(), this);
        });
        binding.licence2.setOnClickListener(v -> {
            Constants.image = "image2";
            uri2 = Uri.parse("");
            CropImage.activity(uri2)
                    .start(getContext(), this);
        });
        binding.gstFile.setOnClickListener(v -> {
            Constants.image = "image3";
            uri3 = Uri.parse("");
            CropImage.activity(uri3)
                    .start(getContext(), this);
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Constants.state.split(","));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.state.setAdapter(dataAdapter);
        binding.state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) binding.state.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));
                State = binding.state.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getProfile();
        getCity();
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
                            Log.e("getProfile", response.body().toString());

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));

                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                                JSONArray jsonObject11 = jsonObject1.getJSONArray("msg");
                                binding.firstName.setText(jsonObject11.getJSONObject(0).getString("UserName"));
                                binding.email.setText(jsonObject11.getJSONObject(0).getString("UserEmail"));
                                binding.mobile.setText(jsonObject11.getJSONObject(0).getString("UserMobile"));
                                binding.licence.setText(jsonObject11.getJSONObject(0).getString("LicenceNumber"));
                                binding.gst.setText(jsonObject11.getJSONObject(0).getString("GSTNumber"));
                                binding.shopName.setText(jsonObject11.getJSONObject(0).getString("ShopName"));
                                binding.address.setText(jsonObject11.getJSONObject(0).getString("UserAddress"));
                                binding.firmName.setText(jsonObject11.getJSONObject(0).getString("firm_name"));
                                binding.acNo.setText(jsonObject11.getJSONObject(0).getString("account_number"));
                                binding.ifsc.setText(jsonObject11.getJSONObject(0).getString("ifsc"));
                                binding.googlePay.setText(jsonObject11.getJSONObject(0).getString("google_pay"));
                                binding.panNo.setText(jsonObject11.getJSONObject(0).getString("pan_no"));
                                binding.area.setText(jsonObject11.getJSONObject(0).getString("Area"));
                                binding.pincode.setText(jsonObject11.getJSONObject(0).getString("pincode"));
                                List<String> list = new ArrayList<>(Arrays.asList(Constants.state.split(",")));
                                binding.state.setSelection(list.indexOf((jsonObject11.getJSONObject(0).getString("State"))));


                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("Profile")).placeholder(R.drawable.logo_sharda).into(binding.profileImage);
                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("licence_no_1")).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(binding.image1);
                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("licence_no_2")).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(binding.image2);
                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("gst_file")).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(binding.image3);

                            } else {
                                TastyToast.makeText(getActivity(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        } catch (Exception e) {
                            // TastyToast.makeText(getActivity(),"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        //Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getActivity(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public boolean validation() {

        Name = binding.firstName.getText().toString();
        ShopName = binding.shopName.getText().toString();
        Email = binding.email.getText().toString();
        Mobile = binding.mobile.getText().toString();
        Address = binding.address.getText().toString();
        Gst = binding.gst.getText().toString();
        Licence = binding.licence.getText().toString();
        FirmName = binding.firmName.getText().toString();
        ACNo = binding.acNo.getText().toString();
        IFSC = binding.ifsc.getText().toString();
        GooglePay = binding.googlePay.getText().toString();
        PanNo = binding.panNo.getText().toString();
        Area = binding.area.getText().toString();

        return true;
    }


    void getCity() {
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getCityList("city");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    if (res.equalsIgnoreCase("success")) {

                        Type type = new TypeToken<ArrayList<CityModel>>() {
                        }.getType();

                        List<CityModel> list = new Gson().fromJson(jsonObject.getJSONArray("msg").toString(), type);
                        List<String> cityList = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            cityList.add(list.get(i).getCity());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cityList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.citySpinner.setAdapter(dataAdapter);
                        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ((TextView) binding.citySpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));
                                City = binding.citySpinner.getSelectedItem().toString();
                                Log.e(TAG, "onItemSelected: " + City);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {

            }
        });

    }
    public void updateDetails() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Authenticating...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.updateProfileDetail("update_info", SharedPrefManager.getInstance(getActivity()).getUser().getId(),
                Name, ShopName, Email, Mobile, Address, Gst, Licence, FirmName, ACNo, IFSC, GooglePay, PanNo,
                encodedString1, encodedString2, encodedString3, State, City, Area,binding.pincode.getText().toString());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                        try {

                            Log.e("update", response.body().toString());

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                                Toast.makeText(getActivity(), "Details Updated", Toast.LENGTH_SHORT).show();

                                JSONArray jsonObject11 = jsonObject1.getJSONArray("msg");

                                binding.firstName.setText(jsonObject11.getJSONObject(0).getString("UserName"));
                                binding.email.setText(jsonObject11.getJSONObject(0).getString("UserEmail"));
                                binding.mobile.setText(jsonObject11.getJSONObject(0).getString("UserMobile"));
                                binding.address.setText(jsonObject11.getJSONObject(0).getString("UserAddress"));

                            } else {
                                TastyToast.makeText(getActivity(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        } catch (Exception e) {
                            // Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            // TastyToast.makeText(getActivity(),"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        //   Toast.makeText(getActivity(),response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getActivity(), t.getMessage().toString(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void updateProfile() {

        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Authenticating...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.updateProfile("update_profile", SharedPrefManager.getInstance(getActivity()).getUser().getId(), str_profile);
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                                Picasso.get().load(jsonObject1.getString("msg")).placeholder(R.drawable.logo_sharda).into(binding.profileImage);


                            } else {
                                TastyToast.makeText(getActivity(), jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        } catch (Exception e) {
                            TastyToast.makeText(getActivity(), "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(getActivity(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        //  Toast.makeText(getActivity(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    protected void ShowOptionDialogue() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.select_pic_option, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        LinearLayout camera = promptView.findViewById(R.id.take_photo);
        LinearLayout gallary = promptView.findViewById(R.id.choose_from_gallary);
        final android.app.AlertDialog alert = alertDialogBuilder.create();
        camera.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {


                        Boolean status = checkAndRequestPermissions();
                        if (status) {
                            takePhotoFromCamera();
                            alert.hide();
                        } else {
                            alert.hide();
                            // Toast.makeText(getApplicationContext(),"Permision Denied",Toast.LENGTH_LONG).show();
                        }


                    }
                }
        );
        gallary.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        choosePhotoFromGallary();
                        alert.hide();

                    }
                }
        );


        alert.show();

    }

    public void choosePhotoFromGallary() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private boolean checkAndRequestPermissions() {
        int camera1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera1 != PackageManager.PERMISSION_GRANTED || camera2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void takePhotoFromCamera() {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");
        String newPicFile = df.format(date) + ".jpg";
        String outPath = "/sdcard/" + newPicFile;
        File outFile = new File(outPath);
        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            switch (Constants.image) {
                case "image1": {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    uri1 = result.getUri();
                    binding.image1.setImageURI(uri1);
                    encodeImagetoString();

                    break;
                }
                case "image2": {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    uri2 = result.getUri();
                    binding.image2.setImageURI(uri2);
                    encodeImagetoString();

                    break;
                }
                case "image3": {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    uri3 = result.getUri();
                    binding.image3.setImageURI(uri3);
                    encodeImagetoString();
                    break;
                }


            }

        }

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    //performCrop();

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);

                    binding.profileImage.setImageBitmap(bitmap);
                    encodeImagetoString(bitmap);

                    // Toast.makeText(getApplicationContext(), encoded_image, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CAMERA) {
            if (data != null) {
                contentURI = data.getData();
            }
            if (contentURI == null && mCameraFileName != null) {
                contentURI = Uri.fromFile(new File(mCameraFileName));
            }
            file = new File(mCameraFileName);
            if (!file.exists()) {
                file.mkdir();
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                    binding.profileImage.setImageBitmap(bitmap);
                    str_profile = new FunctionClass().encodeImagetoString(bitmap);

                    encodeImagetoString(bitmap);
                    //   Toast.makeText(getApplicationContext(),encoded_image,Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //     startActivity(new Intent(getApplicationContext(), SendPrescription.class));
            }
        } else if (requestCode == 3) {
            try {
                contentURI = data.getData();
            } catch (Exception e) {
            }
        }
    }

    public String encodeImagetoString(final Bitmap bitmap) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        // options.inSampleSize = 3;
        options.inSampleSize = 5;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        str_profile = Base64.encodeToString(byte_arr, 0);
        // Toast.makeText(getApplicationContext(),encodedString,Toast.LENGTH_SHORT).show();

        updateProfile();
        return str_profile;
    }

    public void encodeImagetoString() {


        class c1 extends AsyncTask<Void, Void, String> {

            ProgressDialog pd;

            protected void onPreExecute() {
                pd = ProgressDialog.show(getActivity(), "", "Loading...");
            }

            @Override
            protected String doInBackground(Void... params) {
//                BitmapFactory.Options options = null;
//                options = new BitmapFactory.Options();
//                // options.inSampleSize = 3;
//                options.inSampleSize = 5;
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy

                switch (Constants.image) {
                    case "image1": {
                        BitmapFactory.Options options = null;
                        options = new BitmapFactory.Options();
                        // options.inSampleSize = 3;
                        options.inSampleSize = 5;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        pd.dismiss();
                        Constants.image = "";
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                        byte[] byte_arr = stream.toByteArray();
                        encodedString1 = Base64.encodeToString(byte_arr, 0);
                        Log.e("encodedString1", encodedString1);
                        break;
                    }
                    case "image2": {
                        BitmapFactory.Options options = null;
                        options = new BitmapFactory.Options();
                        // options.inSampleSize = 3;
                        options.inSampleSize = 5;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        pd.dismiss();
                        Constants.image = "";
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                        byte[] byte_arr = stream.toByteArray();
                        encodedString2 = Base64.encodeToString(byte_arr, 0);
                        Log.e("encodedString2", encodedString2);

                        break;
                    }
                    case "image3": {
                        BitmapFactory.Options options = null;
                        options = new BitmapFactory.Options();
                        // options.inSampleSize = 3;
                        options.inSampleSize = 5;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        pd.dismiss();
                        Constants.image = "";
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                        byte[] byte_arr = stream.toByteArray();
                        encodedString3 = Base64.encodeToString(byte_arr, 0);
                        Log.e("encodedString3", encodedString3);

                        break;
                    }
                }
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                pd.dismiss();
            }
        }
        c1 ob = new c1();
        ob.execute(null, null, null);
    }

}
