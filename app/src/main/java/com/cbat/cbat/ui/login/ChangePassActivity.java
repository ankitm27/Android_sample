package com.cbat.cbat.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.cbat.cbat.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class ChangePassActivity extends AppCompatActivity {

    @BindView(R.id.btn_change)
    Button _btn_change;
    private ProgressDialog progressDialog;
    @BindView(R.id.old_pass)
    EditText _old_pass;
    @BindView(R.id.new_pass)
    EditText _new_pass;
    @BindView(R.id.re_new_pass)
    EditText _re_new_pass;
    @BindView(R.id.link_login)
    TextView _loginLink;

    String loginId;
    String password;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        //_forgetBtn.on
        progressDialog = new ProgressDialog(ChangePassActivity.this,
                R.style.AppTheme_Dark_Dialog);

        _btn_change=findViewById(R.id.btn_change);
        _old_pass=findViewById(R.id.old_pass);
        _new_pass=findViewById(R.id.new_pass);
        _re_new_pass=findViewById(R.id.re_new_pass);
        _loginLink=findViewById(R.id.link_login);
//        SharedPreferences shp = getApplicationContext().getSharedPreferences(BHUserInfo.SHP_NAME_USERINFO, Activity
//                .MODE_PRIVATE);
//        BHUserInfo userInfos = new BHUserInfo();
//
//        /* 不写入设备的数据 */
//          loginId = shp.getString(BHUserInfo.USERINFO_KEY_LOGIN_EMAIL, "");
//          password = shp.getString(BHUserInfo.USERINFO_KEY_PASSWORD, "");
//        userId = shp.getInt(BHUserInfo.USERINFO_USERID, 0);
        _btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!_old_pass.getText().toString().equalsIgnoreCase(password)){
                        _old_pass.setError(getResources().getString(R.string.old_pass_error));
                        _old_pass.setFocusable(true);

                    }else if(!_new_pass.getText().toString().equalsIgnoreCase(_re_new_pass.getText().toString())){
                        _re_new_pass.setError(getResources().getString(R.string.re_pass_error));
                        _re_new_pass.setFocusable(true);

                    }else {
                        changePassword();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void changePassword() throws Exception {
        if (progressDialog != null ) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Processing...");
            progressDialog.show();
        }
        String url = Constants.BASE_LOCAL__URL + "/api/PasswordChange";

        JSONObject paramsJsonObj = new JSONObject();
        try {

            paramsJsonObj.put("id", userId);
            paramsJsonObj.put("password", _new_pass.getText().toString());


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

    private Response.ErrorListener createRequestErrorListener(final long mRequestStartTime) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.d("Error :- ", error.toString());
                    if(progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                    if (error instanceof NetworkError) {
                        Toast.makeText(getBaseContext(),"Network Error.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getBaseContext(),"Authentication fail.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(getBaseContext(),"We are not able to process login request due to technical issue.", Toast.LENGTH_LONG).show();

                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(getBaseContext(),"No connection available to connect with Server.", Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getBaseContext(),strBuff.toString(), Toast.LENGTH_LONG).show();


                    }
                    //  onRequestFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private Response.Listener<JSONObject> createRequestSuccessListener() throws Exception {
        Response.Listener<JSONObject> listenerObj = null;
        //    if (requestCode == REQUEST_LOGIN) {

        listenerObj = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Res", response.toString());
                    if(progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                    if (response.getString("code").equals(Integer.toString(207))) {
                            Toast.makeText(getBaseContext(), "Password sent on email.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(intent, 0);
                        finish();
                    }else if(response.getString("code").equals(Integer.toString(209))){
                        _new_pass.setError(getResources().getString(R.string.techincalIssue));
                        _new_pass.setFocusable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        //  }
//        if (requestCode == REQUEST_FORGOT_PWD) {
//
//            listenerObj = new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        Log.d("Res", response.toString());
//                        progressDialog.dismiss();
//                        if (response.getString("code").equals(Integer.toString(206))) {
//                            Toast.makeText(getBaseContext(), "Password reset done. Please check your email.", Toast.LENGTH_LONG).show();
//                        } else if (response.getString("code").equals(Integer.toString(220))) {
//                            Toast.makeText(getBaseContext(), "User doesn't exist", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(getBaseContext(), "Request failed. Please try again.", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//                }
//            };
//        }
        return listenerObj;
    }

}
