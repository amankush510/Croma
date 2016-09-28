package in.co.leaf.Croma.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.co.leaf.Croma.MainActivity;
import in.co.leaf.Croma.R;
import in.co.leaf.Croma.data.PunchIn;
import in.co.leaf.Croma.data.PunchOut;
import in.co.leaf.Croma.interfaces.CallBackInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class PunchInOutFragment extends Fragment {

    Button but_punch_in, but_punch_out;
    ProgressDialog pb_loader;

    CallBackInterface callback;

    private FirebaseAuth mAuth;
    private FirebaseUser mFireBaseUser;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private Context mContext;

    public PunchInOutFragment() {

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
        return inflater.inflate(R.layout.fragment_punch_in_out, null, false);
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
        pb_loader = new ProgressDialog(mContext);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFireBaseUser = mAuth.getCurrentUser();
    }

    public void initUI(View view) {
        but_punch_in = (Button) view.findViewById(R.id.but_punch_in);
        but_punch_out = (Button) view.findViewById(R.id.but_punch_out);
    }

    public void initUIActions() {
        if (mFireBaseUser.getDisplayName() == null) {
            showNameDialog();
        }
        but_punch_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(cameraIntent, MainActivity.CAMERA_RESPONSE_PUNCH_IN_CODE);
            }
        });
        but_punch_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(cameraIntent, MainActivity.CAMERA_RESPONSE_PUNCH_OUT_CODE);
            }
        });
    }

    public void showNameDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        final EditText edittext = new EditText(mContext);
        alert.setMessage("Please tell us your name");
        alert.setTitle("Name");
        alert.setView(edittext);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(edittext.getText().toString())
                        .build();
                mFireBaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    public void punchIn(final Bitmap photo) {
        final Date date = new Date();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd : HH:mm:ss a").format(date);
        final String date_str = new SimpleDateFormat("yyyy-MM-dd").format(date);
        PunchIn punchIn = new PunchIn(mFireBaseUser.getUid(), mFireBaseUser.getDisplayName(), dateTime, date.getTime(), MainActivity.deviceId, "");
        pb_loader.setMessage("Punching In ...");
        pb_loader.show();
        mDatabaseRef.child("Punch-Ins").child(date_str).push().setValue(punchIn, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                if (databaseError == null) {
                    pb_loader.setMessage("Uploading Photo ...");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] photo_data = baos.toByteArray();
                    StorageReference riversRef = mStorageRef.child("Punch-Ins").child(date_str).child(mFireBaseUser.getDisplayName()).child("Punch-In-" + date_str);
                    UploadTask uploadTask = riversRef.putBytes(photo_data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Do Something
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("photo").setValue(downloadUrl, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        pb_loader.hide();
                                    }else {
                                        //Do Something
                                    }
                                }
                            });
                        }
                    });
                }else {
                    //Do Something
                }
            }
        });
    }

    public void punchOut(final Bitmap photo) {
        Date date = new Date();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd : HH:mm:ss a").format(date);
        final String date_str = new SimpleDateFormat("yyyy-MM-dd").format(date);
        PunchOut punchOut = new PunchOut(mFireBaseUser.getUid(), mFireBaseUser.getDisplayName(), dateTime, date.getTime(), MainActivity.deviceId, "");
        pb_loader.setMessage("Saving ...");
        pb_loader.show();
        mDatabaseRef.child("Punch-Outs").child(date_str).push().setValue(punchOut, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                if (databaseError == null) {
                    pb_loader.setMessage("Uploading Photo ...");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] photo_data = baos.toByteArray();
                    StorageReference riversRef = mStorageRef.child("Punch-Outs").child(date_str).child(mFireBaseUser.getDisplayName()).child("Punch-Out" + date_str);
                    UploadTask uploadTask = riversRef.putBytes(photo_data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Do Something
                        }
                    });
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("photo").setValue(downloadUrl, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        pb_loader.hide();
                                    }else {
                                        //Do Something
                                    }
                                }
                            });
                        }
                    });
                }else {
                    //Do Something
                }
            }
        });
    }
}
