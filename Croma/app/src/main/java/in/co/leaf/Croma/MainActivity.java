package in.co.leaf.Croma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.co.leaf.Croma.fragments.CaptureResultFromCameraFragment;
import in.co.leaf.Croma.fragments.LoginFragment;
import in.co.leaf.Croma.fragments.PunchInOutFragment;
import in.co.leaf.Croma.interfaces.CallBackInterface;

public class MainActivity extends AppCompatActivity implements CallBackInterface {


    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

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
        //inflater.inflate(R.menu.menu_main, menu);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            inflater.inflate(R.menu.menu_main, menu);
        }
        else{

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_logout :
                mFirebaseAuth.signOut();
                loadLoginFragment();
        }
        return super.onOptionsItemSelected(item);

    }

    public void init(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null){
            loadLoginFragment();
        }
        else
            loadPunchInOutFragment();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){

        }
    }

    public void loadCaptureResultFromCameraFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CaptureResultFromCameraFragment cam = new CaptureResultFromCameraFragment();
        ft.replace(R.id.container, cam);
        ft.commit();
        invalidateOptionsMenu();
    }

    public void loadPunchInOutFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PunchInOutFragment cam = new PunchInOutFragment();
        ft.replace(R.id.container, cam);
        ft.commit();
        invalidateOptionsMenu();
    }

    public void loadLoginFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginFragment cam = new LoginFragment();
        ft.replace(R.id.container, cam);
        ft.commit();
        invalidateOptionsMenu();
    }


}
