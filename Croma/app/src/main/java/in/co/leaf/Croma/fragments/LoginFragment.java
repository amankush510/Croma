package in.co.leaf.Croma.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.co.leaf.Croma.R;
import in.co.leaf.Croma.interfaces.CallBackInterface;

public class LoginFragment extends Fragment {

    EditText et_username, et_password;
    Button but_login;
    ProgressDialog pg_logging;

    FirebaseAuth mFirebaseAuth;

    CallBackInterface callback;

    String username, password;

    private Context mContext;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                callback = (CallBackInterface) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement LandingConfigScreenInterface");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initUI(view);
        initUIActions();
    }

    public void init() {
        mContext = getActivity();
        pg_logging = new ProgressDialog(getActivity());
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void initUI(View view) {
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);
        but_login = (Button) view.findViewById(R.id.but_login);
    }

    public void initUIActions() {
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    username = et_username.getText().toString();
                    password = et_password.getText().toString();
                    pg_logging.setMessage("Logging In");
                    pg_logging.show();
                    mFirebaseAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        callback.loadPunchInOutFragment();
                                        pg_logging.hide();
                                    } else {
                                        pg_logging.hide();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle("Error")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });

                }
            }
        });


    }
    public boolean validate(){
        boolean valid = true;
        if(!isValidEmail(et_username.getText().toString())){
            et_username.setError("Please enter a valid email");
            valid = false;
        }
        else
            et_username.setError(null);
        if(et_password.getText().toString().isEmpty()){
            et_password.setError("Please enter password");
            valid = false;
        }
        else
            et_password.setError(null);
        return valid;

    }

    public static boolean isValidEmail(String email) {
        email = email.trim();
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }

    }

}
