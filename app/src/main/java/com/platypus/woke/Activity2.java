package com.platypus.woke;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonElement;

import java.util.Locale;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

//import ai.api.AIConfiguration;

public class Activity2 extends AppCompatActivity implements AIListener {
    private Button button1, button2, button3, button4;
    private TextView message, voiceInput, answerOutcome;
    private TextToSpeech speech;
//    private SpeechRecognizer recognizer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private AIService aiService;
    private static final int INTERNET = 200;
    Activity2 autoListen;

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
        button3 = (Button) findViewById(R.id.answerC);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity4();

            }
        });
        button4 = (Button) findViewById(R.id.listenButton);


        message = findViewById(R.id.textView);
        voiceInput = findViewById(R.id.resultTextView);
        answerOutcome = findViewById(R.id.textView2);

        final String readQuestion = message.getText().toString();
        final String readAnswer1 = button1.getText().toString();
        final String readAnswer2 = button2.getText().toString();
        final String readAnswer3 = button3.getText().toString();

        final String wholeSpeech = readQuestion + "Is it" + readAnswer1 + "Or" + readAnswer2 + "Or" + readAnswer3;
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
        final AIConfiguration config = new AIConfiguration("f5fe8871954f4ef18a7b741ab7d37373",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
            aiService = AIService.getService(this, config);
            aiService.setListener(this);
        autoListen = Activity2.this;
        autoListen.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button4.performClick();
                    }
                }, 10000); // ms time to delay until execution
            }
        });
        validateOS();
    }



    private void validateOS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, INTERNET);
        }
    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }

    @Override
    public void onResult(AIResponse result) {
        Result result1 = result.getResult();

        // Get parameters
        String parameterString = "";
        if (result1.getParameters() != null && !result1.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result1.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        voiceInput.setText( result1.getResolvedQuery());
        answerOutcome.setVisibility(TextView.VISIBLE);

        { if(answerOutcome.getText().toString().equals(voiceInput.getText().toString())) {
            answerOutcome.setText(R.string.correct);
        } else {
            answerOutcome.setText(R.string.incorrect);
        }

        }

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }


    public void openActivity3() {
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }

    public void openActivity4() {
        Intent intent = new Intent(this, Activity4.class);
        startActivity(intent);
    }


}





// this a new branch

