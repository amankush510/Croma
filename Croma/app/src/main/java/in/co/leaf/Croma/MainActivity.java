package in.co.leaf.Croma;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.co.leaf.Croma.fragments.LoginFragment;
import in.co.leaf.Croma.fragments.PunchInOutFragment;
import in.co.leaf.Croma.interfaces.CallBackInterface;

public class MainActivity extends AppCompatActivity implements CallBackInterface {

    public static String deviceId;

    public final static String ACTION_TYPE = "action_type";
    public final static int ACTION_PUNCH_IN = 1;
    public final static int ACTION_PUNCH_OUT = 2;

    public final static String PUNCH_FRAG_TAG = "punch_frag";

    public static final int CAMERA_RESPONSE_PUNCH_IN_CODE = 1;
    public static final int CAMERA_RESPONSE_PUNCH_OUT_CODE = 2;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            inflater.inflate(R.menu.menu_main, menu);
        } else {

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mFirebaseAuth.signOut();
                loadLoginFragment();
        }
        return super.onOptionsItemSelected(item);

    }

    public void init() {
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            loadLoginFragment();
        } else
            loadPunchInOutFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int action_type = requestCode;
            if(action_type != -1) {
                PunchInOutFragment fragment = (PunchInOutFragment)getSupportFragmentManager().findFragmentByTag(PUNCH_FRAG_TAG);
                if(fragment != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if(action_type == ACTION_PUNCH_IN) {
                        fragment.punchIn(photo);
                    }else if (action_type == ACTION_PUNCH_OUT) {
                        fragment.punchOut(photo);
                    }
                }
            }
        }
    }

    @Override
    public void loadPunchInOutFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PunchInOutFragment cam = new PunchInOutFragment();
        ft.replace(R.id.container, cam , PUNCH_FRAG_TAG);
        ft.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void loadLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginFragment cam = new LoginFragment();
        ft.replace(R.id.container, cam);
        ft.commit();
        invalidateOptionsMenu();
    }


}
