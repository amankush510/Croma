package in.co.leaf.Croma.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import in.co.leaf.Croma.utils.InternalStorageContentProvider;
import in.co.leaf.Croma.MainActivity;


public class CaptureResultFromCameraFragment extends Fragment {


    private File imageFileTempPath;

    public CaptureResultFromCameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        takePicFromCamera();
        return null;
    }

    public void init(){
        imageFileTempPath = getUserProfilePath(getActivity().getApplicationContext());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static File getUserProfilePath(Context context) {
        String locState = Environment.getExternalStorageState();
        File imageFileTempPath = null;
        if (Environment.MEDIA_MOUNTED.equals(locState)) {
            imageFileTempPath = new File(Environment.getExternalStorageDirectory(),
                    InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME);
        } else {
            imageFileTempPath = new File(context.getFilesDir(),
                    InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME);
        }
        return imageFileTempPath;
    }

    public void takePicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(imageFileTempPath);
            } else {
                /*
                 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            //AppConstant.log("exception", "cannot take picture");
        }
    }

}
