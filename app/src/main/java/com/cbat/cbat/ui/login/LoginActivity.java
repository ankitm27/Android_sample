package com.cbat.cbat.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cbat.cbat.R;
import com.cbat.cbat.ui.navigation.CustomerDetailActivity;
import com.cbat.cbat.ui.navigation.MainActivity;
import com.cbat.cbat.ui.widgets.sidedrawer.SettingNewActivity;
import com.cbat.cbat.util.Constants;
import com.cbat.cbat.util.GlobalClass;
import com.cbat.cbat.util.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final String getZoneWiseFormListByUserId = "/api/getZoneWiseFormListByUserId";
    public static final String getFormLogDataByFormUserId = "/api/getFormLogDataByFormUserId";
    public static final String getZoneWiseFormListByOrgId = "/api/getZoneWiseFormListByOrgId";
    public static final String formFieldList = "/api/formFieldList";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;


    Spinner spinner;
    Locale myLocale;
    String currentLanguage = "en", currentLang;
    TextView forgetpass;


    protected static final int ACTIVITY_TO_LOAD = 1;

    private static final int REQUEST_CODE_PERMISSION = 0x8141;

    public static final String EXTRA_SHOW_SETTINGS_DIALOG = "SplashActivity:showSettingsDialog";

    private boolean mNeedsShowSettingsDialog = false;

    //@BindView(R.id.btn_guest_login)
    //Button guestButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //spinner = findViewById(R.id.spinner);
        forgetpass = findViewById(R.id.link_forgetdetails);

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });
        currentLanguage = getIntent().getStringExtra(currentLang);

        GlobalClass.currecyPrefix="CR";
        GlobalClass.totalTraget=10000000;
        GlobalClass.sortingOn=-1;

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
              //  setProductColor();
//                getProductList();
//                GlobalClass.startDate="2018-04-01";
//                GlobalClass.endDate="2019-03-31";
//                GlobalClass.comapnyId=6;
//                Intent intent = new Intent();
//        intent.setClass(getBaseContext(), MainActivity.class);
//                intent.putExtra(EXTRA_SHOW_SETTINGS_DIALOG, mNeedsShowSettingsDialog);
//                 startActivity(intent);
            }
        });
//        guestButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                guestLogin();
//            }
//        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

//        SharedPreferences shp = getApplicationContext().getSharedPreferences(BHUserInfo.SHP_NAME_USERINFO, Activity
//                .MODE_PRIVATE);
//        BHUserInfo userInfos = new BHUserInfo();
//
//        /* 不写入设备的数据 */
//        String  loginId = shp.getString(BHUserInfo.USERINFO_KEY_LOGIN_EMAIL, "");
//        String  password = shp.getString(BHUserInfo.USERINFO_KEY_PASSWORD, "");
//Log.d("userID",loginId);
//        Log.d("password",password);
//
//        if(loginId!=null && !loginId.isEmpty() && password!=null && !password.isEmpty()){
//            Intent intent = new Intent();
//            intent.setClass(getBaseContext(), MainActivity.class);
//            intent.putExtra(EXTRA_SHOW_SETTINGS_DIALOG, mNeedsShowSettingsDialog);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            finish();
//        }

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);


    }


//    public void setProductColor() {
//        GlobalClass.productColorMap=new HashMap<>();
//        GlobalClass.productColorMap.put("Vegetables","#e9c46a");
//        GlobalClass.productColorMap.put("Fruits","#f4a261");
//        GlobalClass.productColorMap.put("Grocery","#e76f51");
//        GlobalClass.productColorMap.put("Total Sales","#264553");
//    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);
//    progressDialog = new ProgressDialog(LoginActivity.this,
        //           R.style.AppTheme_Dark_Dialog);
//    progressDialog.setIndeterminate(true);
//    progressDialog.setMessage("Authenticating...");
//    progressDialog.show();


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        Log.d("Login details", email + password);
        try {

            signInRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: Implement your own authentication logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        // TestDB();
        //readAllTables();
        // Log.d("User Type out ",GlobalClass.userRole);
//       if(GlobalClass.userRole.equalsIgnoreCase("User")) {
//           Log.d("User Type in>>",GlobalClass.userRole);
//            Intent intent = new Intent(getApplicationContext(), UserPlannedTask.class);
//            startActivityForResult(intent, REQUEST_SIGNUP);
//        }else  {
//            Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
//            startActivityForResult(intent, REQUEST_SIGNUP);
//        }

        Intent intent = new Intent();
        intent.setClass(getBaseContext(), MainActivity.class);
    //    intent.setClass(getBaseContext(), CustomerDetailActivity.class);
//        intent.putExtra(EXTRA_SHOW_SETTINGS_DIALOG, mNeedsShowSettingsDialog);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    //    Intent intent = new Intent(getBaseContext(), SettingNewActivity.class);
       // startActivity(intent);
  //      Intent intent = new Intent();
       // intent.setClass(getBaseContext(), MainActivity.class);
        intent.putExtra(EXTRA_SHOW_SETTINGS_DIALOG, mNeedsShowSettingsDialog);
       startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

         if (email.isEmpty()) {
       // if (email.isEmpty()) {
            _emailText.setError("enter a valid email/phoneNo/loginId");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    /**
     * Making json object request for Signin with Email
     */
    private void signInRequest() throws Exception {
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        String url = Constants.BASE_LOCAL__URL + "signin/A";
        JSONObject paramsJsonObj = new JSONObject();
        try {
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("androidid",android_id );
            paramsJsonObj.put("loginId", _emailText.getText().toString());

            paramsJsonObj.put("password", _passwordText.getText().toString());

            paramsJsonObj.put("androidId", android_id);

        } catch (JSONException e) {
            e.printStackTrace();

        }
        Log.d("URL :- ", url);
        Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(), this.createRequestErrorListener(System.currentTimeMillis()));
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.d("No resul :- ", paramsJsonObj.toString());
        Volley.newRequestQueue(this).add(jsonObjRequest);
        // Adding request to request queue

        // Cancelling request
//        AppController.getInstance().getRequestQueue().cancelAll(TAG);
    }

    private void GetCompanyDetails() throws Exception {
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        String url = Constants.BASE_LOCAL__URL + "getUserComapnyDetials";
        JSONObject paramsJsonObj = new JSONObject();
        try {


            JSONObject user = new JSONObject();
            user.put("id",GlobalClass.userId);
            paramsJsonObj.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        Log.d("URL :- ", url);
        Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(1), this.createRequestErrorListener(System.currentTimeMillis()));
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.d("No resul :- ", paramsJsonObj.toString());
        Volley.newRequestQueue(this).add(jsonObjRequest);
        // Adding request to request queue

        // Cancelling request
//        AppController.getInstance().getRequestQueue().cancelAll(TAG);
    }


    private Response.Listener<JSONObject> createRequestSuccessListener() throws Exception {
        Response.Listener<JSONObject> listenerObj = null;
        //    if (requestCode == REQUEST_LOGIN) {

        listenerObj = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Res", response.toString());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.getString("code").equals(Integer.toString(206))) {

//                            prefTermsPrivacyPolicy.edit()
//                                    .putString(Constants.PREF_TERMS_URL, response.getString("termsOfUse"))
//                                    .putString(Constants.PREF_PRIVACY_POLICY_URL, response.getString("privacyPolicyUrl"))
//                                    .commit();
                            GlobalClass.userName = response.getString("name");
//                            GlobalClass.userName = response.optString("userName");
//                            GlobalClass.password = _passwordText.getText().toString();
//                            GlobalClass.loginId = _emailText.getText().toString();
//                            GlobalClass.userId=response.optInt("id");
//                            GlobalClass.orgId=response.optInt("orgId");
//                            GlobalClass.userRole=response.optString("role");
                        // GlobalClass.lastName = response.optString("userrole");
                        //Save to prefrence
                        String email = _emailText.getText().toString();
                        String password = _passwordText.getText().toString();
                        int userId = response.optInt("id");
                        GlobalClass.userId=userId;
//                        getProductList();
//                        GlobalClass.startDate="2018-04-01";
//                        GlobalClass.endDate="2019-09-30";
//                        GlobalClass.comapnyId=6;
                        GetCompanyDetails();

                    } else if (response.getString("code").equals(Integer.toString(201))) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Please activate your account, passcode has been sent to your email.", Toast.LENGTH_LONG).show();
                        // _loginButton.setEnabled(true);
                        //selectConfirm("Please activate your account, passcode has been sent to your email ("+_emailText.getText().toString()+").");
                    } else if (response.getString("code").equals(Integer.toString(210))) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Please activate your account, activation link has been sent to your email.", Toast.LENGTH_LONG).show();
                        _loginButton.setEnabled(true);
                    } else if (response.getString("code").equals(Integer.toString(200))) {
                        Toast.makeText(getBaseContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                        // onLoginSuccess();
                        onLoginFailed();
                    } else if (response.getString("code").equals(Integer.toString(207))) {
                        // onLoginSuccess();
                        // selectConfirm("Please activate your account, passcode has been resent to your email ("+_emailText.getText().toString()+").");

                    } else if (response.getString("code").equals(Integer.toString(208))) {
                        // onLoginSuccess();
                        //selectConfirm("Provided passcode is not correct.");

                    } else if (response.getString("code").equals(Integer.toString(209))) {
                        // onLoginSuccess();
                        _loginButton.setEnabled(true);
                        //selectConfirm(response.getString("message"));

                    } else if (response.getString("code").equals(Integer.toString(230))) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Please enter correct password.", Toast.LENGTH_LONG).show();
                        _loginButton.setEnabled(true);
                    } else {
                        onLoginFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };

        return listenerObj;
    }

    private Response.ErrorListener createRequestErrorListener(final long mRequestStartTime) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.d("Error :- ", error.toString());
                    _loginButton.setEnabled(true);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (error instanceof NetworkError) {
                        Toast.makeText(getBaseContext(), "Network Error.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getBaseContext(), "Authentication fail.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(getBaseContext(), "We are not able to process login request due to technical issue.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(getBaseContext(), "No connection available to connect with Server.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof TimeoutError) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;
                        final String timeString =
                                new SimpleDateFormat("HH:mm:ss").format(mRequestStartTime);
                        Log.d("Err", error.toString());
                        Log.d("error.getClass :", error.getClass().toString());
                        Log.d("StartTime", timeString);
                        Log.d("EndTime", getCurrentTimeStamp());
                        Log.d("error total Time :", String.valueOf(totalRequestTime));
                        StringBuffer strBuff = new StringBuffer();
                        strBuff.append("The request timed out");
                        // strBuff.append(GlobalClass.userId);
                        strBuff.append(getCurrentTimeStamp());
                        Toast.makeText(getBaseContext(), strBuff.toString(), Toast.LENGTH_LONG).show();


                    }
                    //  onRequestFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }



    private void getProductList() {
        if (progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        String url = Constants.BASE_LOCAL__URL + "getProductColour";
        JSONObject paramsJsonObj = new JSONObject();
        try {

            paramsJsonObj.put("id", GlobalClass.comapnyId);



            Log.d("URL :- ", url);
            Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(0), this.createRequestErrorListener(System.currentTimeMillis()));
            jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT_TIME,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Log.d("No resul :- ", paramsJsonObj.toString());
            Volley.newRequestQueue(getBaseContext()).add(jsonObjRequest);
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Response.Listener<JSONObject> createRequestSuccessListener(final int responseCode) throws Exception {
        Response.Listener<JSONObject> listenerObj = null;
        //    if (requestCode == REQUEST_LOGIN) {

        listenerObj = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (responseCode == 0) {
                        Log.d("Res", response.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.getString("code").equals(Integer.toString(206))) {

                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to prefrence
                            JSONArray contentList = response.getJSONArray("contentList");
                            GlobalClass.productColorMap=new HashMap<>();
                            if(contentList.length()>0) {
                                for (int i = 0; i < contentList.length(); i++) {
                                    JSONObject element = contentList.getJSONObject(i);
                                    String colourCode = element.getString("colourCode");
                                    String productName = element.getString("productName");
                                    GlobalClass.productColorMap.put(productName, colourCode);
                                }
                            }else{
                                GlobalClass.productColorMap.put("Product", "#f4a261");
                                GlobalClass.productColorMap.put("Total Sales", "#264553");
                            }
                            onLoginSuccess();
                        }
                    }
                    else if (responseCode == 1) {
                        Log.d("Res", response.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.getString("code").equals(Integer.toString(206))) {
                            JSONObject contentList = new JSONObject( response.getString("contentList"));


//                            for (int i = 0; i < contentList.length(); i++) {
                                JSONObject element = contentList.getJSONObject("company");
                            JSONObject element1=null;
                                GlobalClass.comapnyId = element.getInt("id");
                                GlobalClass.comapnyName = element.getString("name");
                                GlobalClass.companyState= element.getString("state");

                                try{
                                    if(contentList.getJSONObject("setting")==null){
                                        Intent intent = new Intent(getBaseContext(), SettingNewActivity.class);
                                        startActivity(intent);
                                    }else{
                                        element1 = contentList.getJSONObject("setting");
                                    }
                                    //       JSONObject element1 = contentList.getJSONObject("setting");
                                    if(element1.isNull("setting")){
                                        Intent intent = new Intent(getBaseContext(), SettingNewActivity.class);
                                        startActivity(intent);
                                    }

                                    if(element1.has("startDate") && element1.getString("startDate")!=null && !element1.getString("startDate").isEmpty() && !element1.getString("startDate").equalsIgnoreCase("null")) {
                                        //      GlobalClass.startDate
                                        String startDate = element1.getString("startDate");
                                        String []date=startDate.split("T");
                                        GlobalClass.startDate=date[0];
                                    }else{
                                        Intent intent = new Intent(getBaseContext(), SettingNewActivity.class);
                                        startActivity(intent);
                                        //  GlobalClass.startDate="2018-04-01";
                                    }
                                    if(element1.has("endDate") && element1.getString("endDate")!=null && !element1.getString("endDate").isEmpty() && !element1.getString("endDate").equalsIgnoreCase("null")) {
                                        //   GlobalClass.endDate
                                        String endDate= element1.getString("endDate");
                                        String []date=endDate.split("T");
                                        GlobalClass.endDate=date[0];

                                    }else{
                                        Intent intent = new Intent(getBaseContext(), SettingNewActivity.class);
                                        startActivity(intent);
                                        //   GlobalClass.endDate="2020-03-31";
                                    }
                                    if(element1.has("targetYearly")) {
                                        GlobalClass.targetYearly = element1.getInt("targetYearly");
                                    }else{
                                        Intent intent = new Intent(getBaseContext(), SettingNewActivity.class);
                                        startActivity(intent);
                                        //  GlobalClass.targetYearly=0;
                                    }
                                    if(element1.has("currencyType")) {
                                        GlobalClass.currencyFormate = element1.getString("currencyType");
                                        GlobalClass.currecyPrefix = element1.getString("currencyType");
                                    }else{
                                        Intent intent = new Intent(getBaseContext(), SettingNewActivity.class);
                                        startActivity(intent);
                                        //  GlobalClass.currencyFormate="TH";
                                        //GlobalClass.currecyPrefix="TH";
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                          //  }
                            // GlobalClass.lastName = response.optString("userrole");
                            //Save to pref99rence
                            getProductList();
                            _loginButton.setEnabled(true);
                            Intent intent = new Intent();
                            intent.setClass(getBaseContext(), MainActivity.class);
                            intent.putExtra(EXTRA_SHOW_SETTINGS_DIALOG, mNeedsShowSettingsDialog);
                            startActivity(intent);
                            finish();
                      //     onLoginSuccess();
                        //    getProductList();
                            //GlobalClass.startDate="2018-04-01";
                           // GlobalClass.endDate="2019-09-30";
//                            GlobalClass.comapnyId=6;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        };
        return listenerObj;
    }



    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(LoginActivity.this, LoginActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

}