package digi.coders.shardaagroagency.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.shardaagroagency.BuildConfig;
import digi.coders.shardaagroagency.Fragments.ChatFragment;
import digi.coders.shardaagroagency.Fragments.HomeFragment;
import digi.coders.shardaagroagency.Fragments.NotificationFragment;
import digi.coders.shardaagroagency.Fragments.ProfileFragment;
import digi.coders.shardaagroagency.Fragments.ShopFragment;
import digi.coders.shardaagroagency.Fragments.StockFragment;
import digi.coders.shardaagroagency.Helper.Constants;
import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MenuItem.OnMenuItemClickListener {

    MaterialRippleLayout home, myProfile, myOrder, myRequirements, offers, payamount, myCart, callsupport, faqs, changepassword, shareapp, logout, pay_history, chat_to_admin;
    DrawerLayout drawerLayout;
    Button menu;
    TextView name, email;
    CircleImageView profile;
    ImageView search,notification;

    public static int verCode;

    public static  BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.layout_drawer);
        search = findViewById(R.id.search);
        notification = findViewById(R.id.notification);
        home = findViewById(R.id.home);
        myProfile = findViewById(R.id.My_profile);
        myOrder = findViewById(R.id.my_orders);
        myRequirements = findViewById(R.id.my_requirement);
        offers = findViewById(R.id.offers);
        payamount = findViewById(R.id.payamount);
        myCart = findViewById(R.id.myCart);
        callsupport = findViewById(R.id.callsupport);
        changepassword = findViewById(R.id.changepass);
        shareapp = findViewById(R.id.shareapp);
        faqs = findViewById(R.id.faq);
        logout = findViewById(R.id.Logout);
        pay_history = findViewById(R.id.pay_history);
        menu = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        profile = findViewById(R.id.profile);
        chat_to_admin = findViewById(R.id.chat_to_admin);

        search.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));

        myOrder.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MyOrdersActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        myRequirements.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MyRequirementActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        callsupport.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FaqsActivity.class);
            intent.putExtra("status", "call");
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        faqs.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FaqsActivity.class);
            intent.putExtra("status", "faq");
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        payamount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FaqsActivity.class);
            intent.putExtra("status", "pay");
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        shareapp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), InviteFriendsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        offers.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CouponActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        pay_history.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PaymentHistory.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });


        myCart.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CartActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        myProfile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        logout.setOnClickListener(v -> {
            showInputDialog2();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        menu.setOnClickListener(
                v -> drawerLayout.openDrawer(GravityCompat.START)
        );

        changepassword.setOnClickListener(
                v -> {
                    startActivity(new Intent(getApplicationContext(), ChangePassswordActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
        );

        notification.setOnClickListener(view -> {
            exchangeFragment(new NotificationFragment());
        });
        checkVersion();
        checkMaintanance();
        if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getType().equals("Seller")){

        }else {

        }


         bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //Toast.makeText(this, SharedPrefManager.getInstance(getApplicationContext()).getUser().getType(), Toast.LENGTH_LONG).show();
        if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getType().equalsIgnoreCase("Seller")) {
//            bottomNav.getMenu().findItem(R.id.nav_notification).setVisible(false);
//            bottomNav.getMenu().findItem(R.id.nav_chat).setVisible(false);
            bottomNav.getMenu().findItem(R.id.nav_shop).setVisible(false);
            bottomNav.getMenu().findItem(R.id.nav_stock).setTitle("Requirement");
            myOrder.setVisibility(View.GONE);
            offers.setVisibility(View.GONE);
            myCart.setVisibility(View.GONE);
            payamount.setVisibility(View.GONE);
            pay_history.setVisibility(View.GONE);
            exchangeFragment1(new StockFragment());
            bottomNav.setSelectedItemId(R.id.nav_stock);
        } else {
            exchangeFragment1(new HomeFragment());
        }



        chat_to_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeFragment(new ChatFragment());
                bottomNav.setSelectedItemId(R.id.nav_chat);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        home.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getType().equalsIgnoreCase("Seller")) {
                            exchangeFragment1(new StockFragment());
                            bottomNav.setSelectedItemId(R.id.nav_stock);
                        } else {
                            exchangeFragment1(new ShopFragment());
                            bottomNav.setSelectedItemId(R.id.nav_shop);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
        );
        final ImageView Type1 = findViewById(R.id.popup_menu);
        Type1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), Type1);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getApplicationContext(),"You Clicked : " + item.getItemId(), Toast.LENGTH_SHORT).show();
                        if (R.id.change_pass == item.getItemId()) {
                            startActivity(new Intent(getApplicationContext(), ChangePassswordActivity.class));

                        } else if (R.id.share_app == item.getItemId()) {
                            showChooser();

                        } else if (R.id.logout == item.getItemId()) {
                            showInputDialog2();
                        } else if (R.id.how_to_use == item.getItemId()) {
                            if (!Constants.VideoLink.isEmpty()){
                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(Constants.VideoLink));
                                startActivity(intent);
                            }

                        }

                        return true;
                    }
                });

                popup.show();
            }
        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("newfirebase", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM
                    // token
                    String token = task.getResult();

                    // Log and toast
                    @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String msg = getString(R.string.msg_token_fmt, token);
                    Log.d("newfirebase", msg);

                        sendTokens(token);

                });
        getProfile();

    }

    public void showChooser() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Install the app Now : " + "https://play.google.com/store/apps/details?id=digi.coders.shardaagroagency&hl=en");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        exchangeFragment1(new ShopFragment());
//        bottomNav.setSelectedItemId(R.id.nav_home);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            search.setVisibility(View.GONE);
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_shop:
                            search.setVisibility(View.GONE);
                            selectedFragment = new ShopFragment();
                            break;
                        case R.id.nav_chat:
                            search.setVisibility(View.GONE);
                            selectedFragment = new ChatFragment();
                            break;
                        case R.id.nav_stock:
                            search.setVisibility(View.GONE);
                            selectedFragment = new StockFragment();
                            break;
                        case R.id.nav_profile:
                            search.setVisibility(View.GONE);
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    exchangeFragment(selectedFragment);
                    return true;
                }
            };

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    public void exchangeFragment(androidx.fragment.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
        }
    }


    public void exchangeFragment1(androidx.fragment.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
        }
    }

    protected void showInputDialog2() {
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.confirmation_for_logout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptView);


        final AlertDialog alert2 = alertDialogBuilder.create();
        ElasticButton ok = promptView.findViewById(R.id.ok);
        ElasticButton cancel = promptView.findViewById(R.id.cancel);

        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        finish();
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
    public void onBackPressed() {
        //  exchangeFragment(new ProfileFragment());
        super.onBackPressed();
    }

    public void getProfile() {

        final ProgressDialog pd = ProgressDialog.show(HomeActivity.this, "", "Authenticating...");
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.getProfile("profile", SharedPrefManager.getInstance(HomeActivity.this).getUser().getId());
        call.enqueue(
                new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {

                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.getString("res").equalsIgnoreCase("success")) {

                                JSONArray jsonObject11 = jsonObject1.getJSONArray("msg");


                                name.setText(jsonObject11.getJSONObject(0).getString("UserName"));
                                email.setText(jsonObject11.getJSONObject(0).getString("UserEmail"));
                                Picasso.get().load(jsonObject11.getJSONObject(0).getString("Profile")).placeholder(R.drawable.logo_sharda).into(profile);


                            } else {
                                TastyToast.makeText(HomeActivity.this, jsonObject1.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        } catch (Exception e) {
                            TastyToast.makeText(HomeActivity.this, "Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                        //Toast.makeText(HomeActivity.this,response.body().toString(),Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        pd.dismiss();
                        TastyToast.makeText(HomeActivity.this, "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        //  Toast.makeText(HomeActivity.this,t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void sendTokens(String str) {

        final ProgressDialog pd = ProgressDialog.show(HomeActivity.this, "", "Loading...");

        GetData getDataServices = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getDataServices.sendTokens(str, SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
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

                        //TastyToast.makeText(getApplicationContext(),"OTP Send to your Mobile Number !",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                    } else {
                        TastyToast.makeText(getApplicationContext(), "Network Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong !", Toast.LENGTH_LONG).show();

                }
                //    Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Server Error !", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void checkMaintanance() {
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.checkVersion("application_maintanance");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    if (res.equals("success")) {
                        if (!jsonObject.getString("msg").equals("1")) {
                            final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(HomeActivity.this).create();
                            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.maintainance_page, null);
                            dialog.setView(view);
                            dialog.show();
                            dialog.setCancelable(false);
                            TextView ok = findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void checkVersion() {
        GetData getData = RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call = getData.checkVersion("version");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    if (res.equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                        int version_name = jsonObject1.getInt("Version");
                        String Type = jsonObject1.getString("Type");
                        Constants.VideoLink = jsonObject1.getString("VideoLink");
                        Constants.UPI = jsonObject1.getString("UPI");
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//                            String version = pInfo.versionName;   //version name
                            verCode = pInfo.versionCode;      //version code
                            if (version_name > verCode) {
                                final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(HomeActivity.this).create();
                                View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.update_page, null);
                                dialog.setView(view);
                                dialog.show();
                                dialog.setCancelable(false);
                                final TextView cancel = view.findViewById(R.id.cancel);

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                if (!Type.equals("importent")) {
                                    cancel.setVisibility(View.VISIBLE);
                                }
                                final TextView update = view.findViewById(R.id.update);
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                                    }
                                });
                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
