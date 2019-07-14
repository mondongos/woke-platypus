package com.platypus.woke;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Activity2 extends AppCompatActivity {
    private Button button1, button2, button3;
    private TextView message;
    private TextToSpeech speech;
    private SpeechRecognizer recognizer;

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

        message = findViewById(R.id.textView);
        final String readQuestion = message.getText().toString();
        final String readAnswer1 = button1.getText().toString();
        final String readAnswer2 = button2.getText().toString();
        final String readAnswer3 = button3.getText().toString();

        final String wholeSpeech = readQuestion + "Is it" + readAnswer1 + "Or" + readAnswer2 + "Or" + readAnswer3;

        initializeSpeechRecognizer();
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // replace this Locale with whatever you want
                    Locale localeToUse = new Locale("en","");
                    speech.setLanguage(localeToUse);
                    speech.setSpeechRate(0.8f);
                    speech.speak(wholeSpeech, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    private void initializeSpeechRecognizer() {

        //https://www.youtube.com/watch?v=AnNJPf-4T70
        if(SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {

                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
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




// this a new branch

