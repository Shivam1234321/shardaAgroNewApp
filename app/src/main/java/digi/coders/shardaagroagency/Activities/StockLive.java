package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Response;

public class StockLive extends AppCompatActivity {

    WebView webView;
    ProgressDialog pd;
    TextView update_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_stock_live);
        pd=ProgressDialog.show(StockLive.this,"","Authenticating...");
        webView = findViewById(R.id.webview);
        update_date = findViewById(R.id.update_date);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new Callback());
        getProfile();
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pd.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd.show();
        }
    }

    public void getProfile(){

        GetData getData= RetrofitInstance.getRetrofitInstance().create(GetData.class);
        Call<JsonArray> call=getData.getPDF("stocklive");
        call.enqueue(
                new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try{

                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));


                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("res").equalsIgnoreCase("success")){

                                update_date.setText("Last Updated on : "+jsonObject1.getString("Date"));

                                webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+jsonObject1.getString("data"));


                            }else{
                                TastyToast.makeText(getApplicationContext(),"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }

                        }catch (Exception e){
                            TastyToast.makeText(getApplicationContext(),"Data Not Found !", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                     //   Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        TastyToast.makeText(getApplicationContext(), "Server Error !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        //  Toast.makeText(getApplicationContext(),t.getMessage().toString().toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}