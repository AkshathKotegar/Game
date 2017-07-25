package com.improstech.housie.app.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.improstech.housie.app.R;
import com.improstech.housie.app.activity.HomeActivity;
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
 * Created by User2 on 18-07-2017.
 */

public class Login extends Fragment {
    public static Login newInstance() {
        return new Login();
    }

    private SessionManager session;
    private ArrayList<Parameters> toSend = new ArrayList<>();
    private Button btnLogin, btnToRegister;
    private EditText etCCode, etMobile;
    private TextInputLayout tilCCode, tilMobile;
    private DataHelper dh;
    ProgressDialog progDailog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnToRegister = (Button) view.findViewById(R.id.btnToRegister);
        etCCode = (EditText) view.findViewById(R.id.etCCode);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        tilMobile = (TextInputLayout) view.findViewById(R.id.tilMobile);
        tilCCode = (TextInputLayout) view.findViewById(R.id.tilCCode);
        session = new SessionManager(getActivity());
        dh = new DataHelper(getActivity());
        progDailog = new ProgressDialog(getActivity(), R.style.ProgressDialog);

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_container, Registration.newInstance(), "Registration")
                        //.addToBackStack(null)
                        .commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCCode = etCCode.getText().toString().trim();
                String strMobile = etMobile.getText().toString().trim();
                if (!(strCCode.isEmpty())) {
                    if (!(strMobile.isEmpty())) {
                        btnLogin.setEnabled(false);
                        btnToRegister.setEnabled(false);
                        progDailog.setTitle("Please Wait");
                        progDailog.setMessage("Loading Data... Please Wait");
                        progDailog.setCancelable(false);
                        progDailog.show();
                        String strMob = "+" + strCCode + strMobile;
                        loginUser(strMob);
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
            }
        });
        return view;
    }

    private void loginUser(final String mobileNo) {
        String serviceUrlSecondPart = "housie/User/UserService.svc?wsdl";
        String Method_name = "IUserService/LoginUser";
        String Function_Name = "LoginUser";
        Parameters paramMobile = new Parameters(mobileNo, "String", "Mobile");
        Parameters paramFBAppKey = new Parameters(Config.FIREBASE_ID, "String", "FBAppKey");
        toSend.clear();
        toSend.add(paramMobile);
        toSend.add(paramFBAppKey);
        Webservice webMsg = new Webservice();
        webMsg.callMethodName(toSend, getActivity(), serviceUrlSecondPart, Method_name, Function_Name);
        Webservice.YourTask taskSendMessage = webMsg.new YourTask(getActivity(), new OnTaskCompleted() {
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
                                String userKey = optString(jsonObj, ("UserKey"));
                                String strName = optString(jsonObj, ("UserName"));
                                String strEmail = optString(jsonObj, ("Email"));
                                String response = optString(jsonObj, ("Response"));
                                progDailog.setTitle("Please Wait");
                                progDailog.setMessage("Loading Data... Please Wait");
                                progDailog.setCancelable(false);
                                progDailog.show();
                                confirmUser(strOtp, userKey, strEmail, mobileNo, strName);
                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                btnToRegister.setEnabled(true);
                                btnLogin.setEnabled(true);
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();
                    } else {
                        btnToRegister.setEnabled(true);
                        btnLogin.setEnabled(true);
                        showToast(rtn + "\t");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Some Error Occurred\t");
                    btnToRegister.setEnabled(true);
                    btnLogin.setEnabled(true);
                }
            }
        });
        taskSendMessage.execute();
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
                    btnToRegister.setEnabled(true);
                    btnLogin.setEnabled(true);
                }
            }
        });
        taskAppLogin.execute();
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
        return value;
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
}
