package digi.coders.shardaagroagency.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import digi.coders.shardaagroagency.Fragments.EditProfileFragment;
import digi.coders.shardaagroagency.Fragments.ProfileFragment;
import digi.coders.shardaagroagency.R;

public class MyProfileActivity extends AppCompatActivity {


    Button edit,back;
    TextView toolbar_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_my_profile);

        edit=findViewById(R.id.edit);
        back=findViewById(R.id.back);
        toolbar_text=findViewById(R.id.toolbar_text);

        exchangeFragment1(new ProfileFragment());
        edit.setOnClickListener(
                v -> {
                    toolbar_text.setText("Update Profile");
                 exchangeFragment1(new EditProfileFragment());
                }
        );

        back.setOnClickListener(
                v -> {
                    if(toolbar_text.getText().toString().equalsIgnoreCase("Update Profile")){
                        toolbar_text.setText("My Profile");
                        exchangeFragment1(new ProfileFragment());
                    }else {
                        finish();
                    }
                }
        );
    }

    public void exchangeFragment1(androidx.fragment.app.Fragment fragment){
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
        }
    }
}
