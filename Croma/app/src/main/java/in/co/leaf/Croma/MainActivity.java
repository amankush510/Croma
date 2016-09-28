package in.co.leaf.Croma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import in.co.leaf.Croma.fragments.CaptureResultFromCameraFragment;
import in.co.leaf.Croma.fragments.LoginFragment;
import in.co.leaf.Croma.fragments.PunchInOutFragment;
import in.co.leaf.Croma.interfaces.CallBackInterface;

public class MainActivity extends AppCompatActivity implements CallBackInterface {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadLoginFragment();
    }

    public void init(){

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
    }

    public void loadPunchInOutFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PunchInOutFragment cam = new PunchInOutFragment();
        ft.replace(R.id.container, cam);
        ft.commit();
    }

    public void loadLoginFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginFragment cam = new LoginFragment();
        ft.replace(R.id.container, cam).addToBackStack(null);
        ft.commit();
    }


}
