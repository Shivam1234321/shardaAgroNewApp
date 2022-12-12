package digi.coders.shardaagroagency.Helper;

/**
 * Created by Hacker Abhi on 1/28/2019.
 */

import android.content.Context;
import android.content.SharedPreferences;

import digi.coders.shardaagroagency.Model.UserDetails;

public class SharedPrefManager {

//the constants
private static final String SHARED_PREF_NAME = "ShardaAgroAgency";
        private static final String KEY_USER_ID = "user_id";
        private static final String KEY_USERNAME = "user_name";
        private static final String KEY_EMAIL = "key_email";
        private static final String KEY_MOBILE = "key_mobile";
        private static final String KEY_TYPE = "key_type";


private static SharedPrefManager mInstance;
private static Context mCtx;

private SharedPrefManager(Context context) {
        mCtx = context;
}

public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
                mInstance = new SharedPrefManager(context);
        }
        return mInstance;
}

//method to let the user login
//this method will store the user data in shared preferences
public void userLogin(UserDetails user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL,user.getEmail());
        editor.putString(KEY_MOBILE, user.getMobile());
        editor.putString(KEY_TYPE, user.getType());

        editor.apply();
}

//this method will checker whether user is already logged in or not
public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
}

//this method will give the logged in user
public UserDetails getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserDetails(

                sharedPreferences.getString(KEY_USER_ID,null),
                sharedPreferences.getString(KEY_USERNAME,null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILE, null),
                sharedPreferences.getString(KEY_TYPE, null)
        );
}

                //this method will logout the user
public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}


