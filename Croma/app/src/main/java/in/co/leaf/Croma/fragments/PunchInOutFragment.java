package in.co.leaf.Croma.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;

import in.co.leaf.Croma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PunchInOutFragment extends Fragment {
    Context context;

    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

    Button but_punch_in, but_punch_out;

    String deviceId = "";
    Long timestamp;


    public PunchInOutFragment() {

        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_punch_in_out, null, false);
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
        but_punch_in = (Button)view.findViewById(R.id.but_punch_in);
        but_punch_out = (Button)view.findViewById(R.id.but_punch_out);
    }

    public void initUIActions(){
        but_punch_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

            }



        });

        but_punch_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public String getDeviceId()
    {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    class MarkAttendance extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            deviceId = getDeviceId();
            timestamp = new Date().getTime();
            HashMap<String, Object> params = new HashMap<>();
            return null;
        }
    }
}