package in.co.leaf.Croma.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import in.co.leaf.Croma.MainActivity;
import in.co.leaf.Croma.R;
import in.co.leaf.Croma.interfaces.CallBackInterface;

public class LoginFragment extends Fragment {

    EditText et_username,et_password;
    Button but_login;
    ProgressBar pg_logging;

    FirebaseAuth mFirebaseAuth;

    CallBackInterface callback;

    String username, password;

    ProgressDialog pg;

    LoggingIn loggingIn;

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

    public void init(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        pg = new ProgressDialog(getActivity());
    }

    public void initUI(View view){
        et_username = (EditText)view.findViewById(R.id.et_username);
        et_password = (EditText)view.findViewById(R.id.et_password);
        but_login = (Button)view.findViewById(R.id.but_login);
        pg_logging = (ProgressBar)view.findViewById(R.id.pg_logging);
    }

    public void initUIActions(){
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                loggingIn = new LoggingIn();
                loggingIn.execute();

            }
        });
    }

    class LoggingIn extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg_logging.setVisibility(View.VISIBLE);
            /*pg.setMessage("Logging In...");
            pg.show();*/
        }

        @Override
        protected String doInBackground(String... strings) {
            mFirebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                callback.loadPunchInOutFragment();
                                pg_logging.setVisibility(View.GONE);
                            } else {
                                pg_logging.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(task.getException().getMessage())
                                        .setTitle("Error")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
            return null;
        }

    }


}
