package com.cbat.cbat.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    // @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
//    @BindView(R.id.input_height)
//    TextView _height;
//    @BindView(R.id.input_weight)
//    TextView _weight;

    ProgressDialog progressDialog;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnDisplay;
    View v1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        //android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        //  toolbar.setTitle("Signup");=

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1=v;
                signup();
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
        addListenerOnButton();
    }

    public void addListenerOnButton() {
       // radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        //btnDisplay = (Button) findViewById(R.id.btnDisplay);

//        btnDisplay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//// get selected radio button from radioGroup
//                int selectedId = radioSexGroup.getCheckedRadioButtonId();
//// find the radiobutton by returned id
//                radioSexButton = (RadioButton) findViewById(selectedId);
//
//                Toast.makeText(SignupActivity.this, selectedId+"   "+radioSexButton.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

        String name = _nameText.getText().toString();
        //String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if (progressDialog != null) {
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Creating Account...");
                            progressDialog.show();
                        }
                        saveUserData();

                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        // String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

//        if (address.isEmpty()) {
//            _addressText.setError("Enter Valid Address");
//            valid = false;
//        } else {
//            _addressText.setError(null);
//        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    public void saveUserData() {
        //String url = Constants.BASE_LOCAL__URL + "/api/SaveUsers";

        String url = Constants.BASE_LOCAL__URL + "/signup";

        JSONObject paramsJsonObj = new JSONObject();
        try {

            String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            paramsJsonObj.put("name", _nameText.getText().toString());
            paramsJsonObj.put("email", _emailText.getText().toString());
            paramsJsonObj.put("password", _passwordText.getText().toString());
            paramsJsonObj.put("phoneNumber", _mobileText.getText().toString());

            paramsJsonObj.put("androidId", androidId);


            Log.d("phoneUID", " : " + androidId);



            Log.d("URL :- ", url);
            Log.d("paramsJsonObj :- ", paramsJsonObj.toString());
            JsonObjectRequest jsonObjRequest = null;

            jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, paramsJsonObj, this.createRequestSuccessListener(1), this.createRequestErrorListener(System.currentTimeMillis()));

            jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT_TIME,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Log.d("No resul :- ", paramsJsonObj.toString());
            Volley.newRequestQueue(getBaseContext().getApplicationContext()).add(jsonObjRequest);

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Response.Listener<JSONObject> createRequestSuccessListener(int requestCode) throws Exception {
        Response.Listener<JSONObject> listenerObj = null;
        if (requestCode == 1) {
            listenerObj = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Log.d("Res with 0", response.toString());
                        if (response.getString("code").equals(Integer.toString(206))) {
                            // formId = response.getInt("FORM_ID");
                            // Log.d("formId", String.valueOf(formId));

                            Toast.makeText(getBaseContext(), "Sucessfully Save the User.", Toast.LENGTH_LONG).show();
                            Intent iBus = new Intent(getBaseContext(), LoginActivity.class);
                            iBus.putExtra("tab", 1);
                            startActivity(iBus);
                            _emailText.setText("");
                            _nameText.setText("");
                            _mobileText.setText("");
                            _passwordText.setText("");;
                            _reEnterPasswordText.setText("");
                        }
                        else if (response.getString("code").equals(Integer.toString(207))) {
                            Toast.makeText(getBaseContext(), "Email Already Exists.", Toast.LENGTH_LONG).show();
                            Snackbar.make(v1, " " + response.getString("message") , Snackbar.LENGTH_SHORT).show();
                 //           _emailText.setError("Email Already Exists.")
                            _emailText.setText("");
                            _nameText.setText("");
                             _mobileText.setText("");
                          _passwordText.setText("");;
                          _reEnterPasswordText.setText("");
                            _emailText.requestFocus();
                            _signupButton.setEnabled(true);
                        } else if (response.getString("code").equals(Integer.toString(200))) {
                            Toast.makeText(getBaseContext(), "Error in process.", Toast.LENGTH_LONG).show();
                            Snackbar.make(v1, " " + response.getString("message") , Snackbar.LENGTH_SHORT).show();
                            _emailText.setText("");
                            _nameText.setText("");
                            _mobileText.setText("");
                            _passwordText.setText("");;
                            _reEnterPasswordText.setText("");
                            _signupButton.setError("Error in process.");
                            _signupButton.requestFocus();
                            Intent intent = getIntent();
                            //intent.putExtra("tab",1);

                            //finish();
                            startActivity(intent);
                            _signupButton.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            };

        }

        return listenerObj;
    }

    private Response.ErrorListener createRequestErrorListener(final long mRequestStartTime) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.d("Error :- ", error.toString());
//                    btnUserCreate.setEnabled(true);
//                    if (progressDialog != null) {
//                        progressDialog.dismiss();
//                    }
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
                        // Log.d("EndTime", Utility.getCurrentTimeStamp());
                        Log.d("error total Time :", String.valueOf(totalRequestTime));
                        StringBuffer strBuff = new StringBuffer();
                        strBuff.append("The request timed out");
                        // strBuff.append(GlobalClass.userId);
                        // strBuff.append(Utility.getCurrentTimeStamp());
                        Toast.makeText(getBaseContext(), strBuff.toString(), Toast.LENGTH_LONG).show();


                    }
                    //  onRequestFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }


}
