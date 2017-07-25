package com.improstech.housie.app.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.improstech.housie.app.R;
import com.improstech.housie.app.activity.HomeActivity;
import com.improstech.housie.app.activity.MainActivity;
import com.improstech.housie.app.activity.SplashScreen;
import com.improstech.housie.app.app.Config;
import com.improstech.housie.app.helper.DataHelper;
import com.improstech.housie.app.helper.OnTaskCompleted;
import com.improstech.housie.app.helper.Parameters;
import com.improstech.housie.app.helper.SessionManager;
import com.improstech.housie.app.helper.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User2 on 17-07-2017.
 */

public class Registration extends Fragment {
    public static Registration newInstance() {
        return new Registration();
    }

    EditText etName, etEmail, etCCode, etMobile;
    TextInputLayout tilName, tilEmail, tilCCode, tilMobile;
    Button btnRegister, btnToLogin;
    private ArrayList<Parameters> toSend = new ArrayList<>();
    ProgressDialog progDailog;
    private DataHelper dh;
    private SessionManager session;
    private TextView tvTerms;
    private CheckBox checkTerms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnToLogin = (Button) view.findViewById(R.id.btnToLogin);
        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etCCode = (EditText) view.findViewById(R.id.etCCode);
        tilName = (TextInputLayout) view.findViewById(R.id.tilName);
        tilEmail = (TextInputLayout) view.findViewById(R.id.tilEmail);
        tilMobile = (TextInputLayout) view.findViewById(R.id.tilMobile);
        tilCCode = (TextInputLayout) view.findViewById(R.id.tilCCode);
        checkTerms = (CheckBox) view.findViewById(R.id.checkBox);
        progDailog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
        tvTerms = (TextView) view.findViewById(R.id.tvTerms);
        dh = new DataHelper(getActivity());
        session = new SessionManager(getActivity());

        String text = "<font color=#2b5a83>By proceeding, you agree to </font> <font color=#01bcd5>Terms & Conditions</font>";
        tvTerms.setText(Html.fromHtml(text));

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progDailog.setTitle("Please Wait");
                progDailog.setMessage("Loading Data... Please Wait");
                progDailog.setCancelable(false);
                progDailog.show();
                showTerms();
            }
        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_container, Login.newInstance(), "Registration")
                        //.addToBackStack(null)
                        .commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = etName.getText().toString().trim();
                String strEmail = etEmail.getText().toString().trim();
                String strCCode = etCCode.getText().toString().trim();
                String strMobile = etMobile.getText().toString().trim();
                if (!(strName.isEmpty())) {
                    if (!(strEmail.isEmpty())) {
                        if (!(strCCode.isEmpty())) {
                            if (!(strMobile.isEmpty())) {
                                if (checkTerms.isChecked()) {
                                    btnRegister.setEnabled(false);
                                    btnToLogin.setEnabled(false);
                                    progDailog.setTitle("Please Wait");
                                    progDailog.setMessage("Loading Data... Please Wait");
                                    progDailog.setCancelable(false);
                                    progDailog.show();
                                    String strMob = "+" + strCCode + strMobile;
                                    RegisterUser(strName, strEmail, strMob);
                                } else {
                                    showToast("You need to agree Terms & Conditions\t");
                                }
                            } else {
                                tilMobile.setErrorEnabled(true);
                                tilMobile.setError("You need to enter your mobile number");
                                OnValidation(etMobile, tilMobile);
                            }
                        } else {
                            tilCCode.setErrorEnabled(true);
                            tilCCode.setError("You need to enter your country code");
                            OnValidation(etCCode, tilCCode);
                        }
                    } else {
                        tilEmail.setErrorEnabled(true);
                        tilEmail.setError("You need to enter your email id");
                        OnValidation(etEmail, tilEmail);
                    }
                } else {
                    tilName.setErrorEnabled(true);
                    tilName.setError("You need to enter your name");
                    OnValidation(etName, tilName);
                }
            }
        });


        return view;
    }

    private void RegisterUser(final String strName, final String strEmail, final String strMobile) {
        String serviceUrlSecondPart = "housie/User/UserService.svc?wsdl";
        String Method_name_login = "IUserService/RegisterUser";
        String Function_Name_login = "RegisterUser";
        Parameters paramEmail = new Parameters(strEmail, "String", "Email");
        Parameters paramUserKey = new Parameters("NEW", "String", "userKey");
        Parameters paramMobile = new Parameters(strMobile, "String", "Mobile");
        Parameters paramName = new Parameters(strName, "String", "Name");
        Parameters paramFBAppKey = new Parameters(Config.FIREBASE_ID, "String", "FBAppKey");
        Parameters paramIsNew = new Parameters("true", "String", "IsNew");
        toSend.clear();
        toSend.add(paramEmail);
        toSend.add(paramUserKey);
        toSend.add(paramMobile);
        toSend.add(paramName);
        toSend.add(paramFBAppKey);
        toSend.add(paramIsNew);
        Webservice webLogin = new Webservice();
        webLogin.callMethodName(toSend, getActivity(), serviceUrlSecondPart, Method_name_login, Function_Name_login);
        Webservice.YourTask taskAppLogin = webLogin.new YourTask(getActivity(), new OnTaskCompleted() {
            @Override

            public void onTaskCompleted(String webResponse) {
                try {
                    progDailog.dismiss();
                    final JSONObject jsonObj = new JSONObject(webResponse);
                    String response = optString(jsonObj, ("Response"));
                    String rtn = getValue(response);
                    if (response.equals("100")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                        dialogBuilder.setView(dialogView);
                        final EditText edt = (EditText) dialogView.findViewById(R.id.editText);
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setTitle("OTP has been sent to your Mobile Number");
                        dialogBuilder.setMessage("Enter OTP");
                        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String strOtp = edt.getText().toString().trim();
                                String recordKey = optString(jsonObj, ("RecordKey"));
                                progDailog.setTitle("Please Wait");
                                progDailog.setMessage("Loading Data... Please Wait");
                                progDailog.setCancelable(false);
                                progDailog.show();
                                confirmUser(strOtp, recordKey, strEmail, strMobile, strName);
                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                btnRegister.setEnabled(true);
                                btnToLogin.setEnabled(true);
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();
                    } else {
                        btnRegister.setEnabled(true);
                        btnToLogin.setEnabled(true);
                        showToast(rtn + "\t");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Some Error Occurred\t");
                    btnRegister.setEnabled(true);
                    btnToLogin.setEnabled(true);
                }
            }
        });
        taskAppLogin.execute();
    }

    private void showTerms() {
        String serviceUrlSecondPart = "housie/User/UserService.svc?wsdl";
        String Method_name_login = "IUserService/GetTerms";
        String Function_Name_login = "GetTerms";
        Webservice webLogin = new Webservice();
        webLogin.callMethodName(toSend, getActivity(), serviceUrlSecondPart, Method_name_login, Function_Name_login);
        Webservice.YourTask taskAppLogin = webLogin.new YourTask(getActivity(), new OnTaskCompleted() {
            @Override

            public void onTaskCompleted(String webResponse) {
                try {
                    progDailog.dismiss();
                    final JSONObject jsonObj = new JSONObject(webResponse);
                    String response = optString(jsonObj, ("Response"));
                    String rtn = getValue(response);
                    if (response.equals("100")) {
                        String terms = optString(jsonObj, ("Terms"));
                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Terms and Conditions");
                        alertDialogBuilder.setMessage(Html.fromHtml(terms));
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        showToast(rtn + "\t");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Some Error Occurred\t");
                }
            }
        });
        taskAppLogin.execute();

    }

    private void showToast(String text) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View toastRoot = inflater.inflate(R.layout.toast, null);
        ((TextView) toastRoot.findViewById(R.id.custom)).setText(text);
        Toast mytoast = new Toast(getActivity());
        mytoast.setView(toastRoot);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.show();
    }

    private String optString(JSONObject json, String key) {
        String toReturn = "hello";
        if (json.isNull(key))
            return null;
        else {
            try {
                toReturn = json.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }

    public void OnValidation(final EditText editText, final TextInputLayout tilayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public String getValue(String response) {
        String value = null;
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();

        hashMap.put(100, "RTN_SUCCESS");
        hashMap.put(101, "RTN_ERROR");
        hashMap.put(997, "RTN_DATABASE_ERROR");
        hashMap.put(998, "RTN_UNKNOWN_ERROR");
        hashMap.put(999, "RTN_TECH_ERROR");

        hashMap.put(111, "RTN_USER_NAME_EMPTY");
        hashMap.put(112, "RTN_USER_NAME_SHORT");
        hashMap.put(113, "RTN_USER_NAME_HAS_SPECIAL_CHAR");

        hashMap.put(121, "RTN_USER_EMAIL_EMPTY");
        hashMap.put(122, "RTN_USER_EMAIL_SHORT");
        hashMap.put(123, "RTN_USER_EMAIL_INVALID");

        hashMap.put(131, "RTN_USER_KEY_EMPTY");
        hashMap.put(132, "RTN_USER_KEY_SHORT");
        hashMap.put(133, "RTN_USER_KEY_INVALID");

        hashMap.put(141, "RTN_USER_MOBILE_EMPTY");
        hashMap.put(142, "RTN_USER_MOBILE_SHORT");
        hashMap.put(143, "RTN_USER_MOBILE_INVALID");
        hashMap.put(144, "RTN_USER_MOBILE_ALREADY_REGISTERED");
        hashMap.put(145, "RTN_USER_MOBILE_NOT_REGISTERED");

        hashMap.put(151, "RTN_USER_FBAPPKEY_EMPTY");
        hashMap.put(152, "RTN_USER_FBAPPKEY_SHORT");

        hashMap.put(161, "RTN_USER_INVALID_MOBILE_USER");
        hashMap.put(162, "RTN_USER_INVALID_OTP");

        value = hashMap.get(Integer.parseInt(response));
        //showToast(value);
        return value;
    }

    public void showOTPDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editText);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("OTP has been sent to your Mobile Number");
        dialogBuilder.setMessage("Enter OTP");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //done
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void confirmUser(final String strOtp, final String userKey, final String strEmail, final String strMobile, final String strName) {
        String serviceUrlSecondPart = "housie/User/UserService.svc?wsdl";
        String Method_name_login = "IUserService/ConfirmUser";
        String Function_Name_login = "ConfirmUser";
        Parameters paramMobile = new Parameters(strMobile, "String", "Mobile");
        Parameters paramUserKey = new Parameters(userKey, "String", "UserKey");
        Parameters paramFBAppKey = new Parameters(Config.FIREBASE_ID, "String", "FBAppKey");
        Parameters paramOTPValue = new Parameters(strOtp, "String", "OTPValue");
        toSend.clear();
        toSend.add(paramMobile);
        toSend.add(paramUserKey);
        toSend.add(paramFBAppKey);
        toSend.add(paramOTPValue);
        Webservice webLogin = new Webservice();
        webLogin.callMethodName(toSend, getActivity(), serviceUrlSecondPart, Method_name_login, Function_Name_login);
        Webservice.YourTask taskAppLogin = webLogin.new YourTask(getActivity(), new OnTaskCompleted() {
            @Override

            public void onTaskCompleted(String webResponse) {
                try {
                    progDailog.dismiss();
                    JSONObject jsonObj = new JSONObject(webResponse);
                    String response = optString(jsonObj, ("Response"));
                    String rtn = getValue(response);
                    switch (Integer.parseInt(response)) {
                        case 100:
                            String dbResult = dh.insertUserDetails(userKey, "", strName, strEmail, strMobile);
                            if (dbResult.equalsIgnoreCase("success")) {
                                session.setLogin(true);
                                updateTermsAccepted(strMobile, userKey);
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                intent.putExtra("Key_IsLogin", "true");
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                showToast("Some Error Occurred\t");
                            }
                            break;
                        default:
                            showToast(rtn + "\t");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Some Error Occurred\t");
                    btnRegister.setEnabled(true);
                    btnToLogin.setEnabled(true);
                }
            }
        });
        taskAppLogin.execute();
    }

    private void updateTermsAccepted(final String mobileNo, String userKey) {
        String serviceUrlSecondPart = "MyJobsApp/User/UserService.svc?wsdl";
        String Method_name = "IUserService/UpdateTermsAccepted";
        String Function_Name = "UpdateTermsAccepted";
        Parameters paramMobile = new Parameters(mobileNo, "String", "Mobile");
        Parameters paramUserKey = new Parameters(userKey, "String", "UserKey");
        Parameters paramFBAppKey = new Parameters(Config.FIREBASE_ID, "String", "FBAppKey");
        Parameters paramIsTermsAccepted = new Parameters("true", "String", "IsTermsAccepted");
        toSend.clear();
        toSend.add(paramMobile);
        toSend.add(paramUserKey);
        toSend.add(paramFBAppKey);
        toSend.add(paramIsTermsAccepted);
        Webservice webMsg = new Webservice();
        webMsg.callMethodName(toSend, getActivity(), serviceUrlSecondPart, Method_name, Function_Name);
        Webservice.YourTask taskSendMessage = webMsg.new YourTask(getActivity(), new OnTaskCompleted() {
            @Override

            public void onTaskCompleted(String webResponse) {
                String result;
                result = webResponse;

            }
        });
        taskSendMessage.execute();
    }
}
