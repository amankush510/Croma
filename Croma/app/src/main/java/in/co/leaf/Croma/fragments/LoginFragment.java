package in.co.leaf.Croma.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import in.co.leaf.Croma.R;

public class LoginFragment extends Fragment {

    EditText et_username,et_password;
    Button but_login;


    public LoginFragment() {
        // Required empty public constructor
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

    }

    public void initUI(View view){
        et_username = (EditText)view.findViewById(R.id.et_username);
        et_password = (EditText)view.findViewById(R.id.et_password);
        but_login = (Button)view.findViewById(R.id.but_login);
    }

    public void initUIActions(){
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
