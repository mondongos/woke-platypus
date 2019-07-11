package com.platypus.woke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {
    private Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        button1 = (Button) findViewById(R.id.answerA);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity4();

            }
        });
        button2 = (Button) findViewById(R.id.answerB);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity3();

            }
        });
        button3= (Button) findViewById(R.id.answerC);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity4();

            }
        });

    }

    public void openActivity3(){
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }

    public void openActivity4(){
        Intent intent = new Intent(this, Activity4.class);
        startActivity(intent);
    }
}