package com.example.mycollege;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Admin_page extends AppCompatActivity implements View.OnClickListener {
    CardView upload_notice , upload_image , addEbook ,add_faculty, delete_notice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        upload_notice = findViewById(R.id.add_notice);
        upload_image = findViewById(R.id.add_galleryImage);
        addEbook = findViewById(R.id.add_ebook);
        add_faculty = findViewById(R.id.add_faculty);
        delete_notice = findViewById(R.id.delete_notice);

        upload_notice.setOnClickListener(this);
        upload_image.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        add_faculty.setOnClickListener(this);
        delete_notice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_notice:
                Intent intent = new Intent(Admin_page.this, com.example.mycollege.upload_notice.class);
                startActivity(intent);
                break;

            case R.id.add_galleryImage:
                Intent intent1 = new Intent(Admin_page.this, com.example.mycollege.upload_image.class);
                startActivity(intent1);
                break;
            case R.id.add_ebook:
                Intent intent2 = new Intent(Admin_page.this,uploadPdfActivity.class);
                startActivity(intent2);
                break;
                case R.id.add_faculty:
                    Intent intent3 = new Intent(Admin_page.this ,UpdateFaculty.class);
                    startActivity(intent3);
                    break;

            case R.id.delete_notice:
                Intent intent4 = new Intent(Admin_page.this ,deleteNoticeActivity.class);
                startActivity(intent4);
                break;




        }

    }
}