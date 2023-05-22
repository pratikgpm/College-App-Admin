package com.example.mycollege;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class upload_notice extends AppCompatActivity {
    private CardView add_image;
    private final int REQ = 1;

    private Bitmap bitmap;
    private DatabaseReference reference , dbRef ;
    private StorageReference storageReference;
    private EditText notice_titel_text;
    private Button upload_notice_button;
    String download_url ="";
    private ProgressDialog pd ;

    private ImageView notice_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upolad_notice);
        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        notice_imageview = findViewById(R.id.notice_image_view);
        upload_notice_button = findViewById(R.id.upload_notice_button);
        notice_titel_text = findViewById(R.id.notice_title);
         add_image = findViewById( R.id.upload_notice);
         add_image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openGallery();
             }
         });

         upload_notice_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (notice_titel_text.getText().toString().isEmpty()){
                     notice_titel_text.setError("Empty");
                     notice_titel_text.requestFocus();
                 } else if (bitmap == null) {
                     uploadData();
                     //notice_titel_text.setText("");
                     // bitmap = null ;

                 }
                 else {
                     uploadImage();

                 }
             }
         });
    }

    private void uploadImage() {
        pd.setMessage("Uploading....");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath ;
        filepath = storageReference.child("Notice").child(finalimg+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(upload_notice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url =String.valueOf(uri);
                                    uploadData();


                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(upload_notice.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    private void uploadData() {
        dbRef = reference.child("Notice");
        final String uniqueKey = dbRef.push().getKey();
        String title = notice_titel_text.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate =new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime =  Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        NoticeData noticeData  = new  NoticeData(title,download_url,date,time,uniqueKey);
        dbRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(upload_notice.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(upload_notice.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void openGallery() {
        Intent pick_image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick_image,REQ);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            notice_imageview.setImageBitmap(bitmap);
        }
    }
}