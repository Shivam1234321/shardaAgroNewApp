package digi.coders.shardaagroagency.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.shardaagroagency.Helper.GetData;
import digi.coders.shardaagroagency.Helper.RetrofitInstance;
import digi.coders.shardaagroagency.Helper.SharedPrefManager;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteFriendsActivity extends AppCompatActivity {

    TextView referlid;
    ElasticFloatingActionButton btn_refer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.refer_code_layout);


        Button back=findViewById(R.id.back);
        referlid=findViewById(R.id.referid);
        btn_refer=findViewById(R.id.btn_refer);

        btn_refer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showChooser();
                    }
                }
        );
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        TextView copy=findViewById(R.id.copy);
        referlid.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copy to Clipboard...", referlid.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        TastyToast.makeText(getApplicationContext(),"Copy to Clipboard...", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                    }
                }
        );

     //   getUserDetails();
    }
    public void showChooser(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Use my referral code : *"+referlid.getText().toString()+"* and get extra bonus in shopping. Install the app Now : "+"https://play.google.com/store/apps/details?id=digi.coders.adeshme&hl=en");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}