//package com.platypus.woke;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class EndActivity extends AppCompatActivity {
//
//    TextView tv_result;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_end);
//
//        tv_result = findViewById(R.id.result);
//
//        int correct = getIntent().getIntExtra("Correct", 0);
//        int wrong = getIntent().getIntExtra("Wrong", 0);
//
//        tv_result.setText("Correct: " + correct + "\nWrong: " + wrong);
//    }
//
//};
