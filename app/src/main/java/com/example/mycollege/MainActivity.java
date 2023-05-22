package com.example.mycollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button login , register ;
    EditText username_main ,password_main;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        username_main =findViewById(R.id.username);
        password_main = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username_main.getText().toString().equals("admin") && password_main.getText().toString().equals("123"))
                {
                    Intent intent = new Intent(MainActivity.this , Admin_page.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Cridentials",Toast.LENGTH_LONG).show();

                }
            }
        });


    }
}