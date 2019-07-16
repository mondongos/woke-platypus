package com.platypus.woke;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.*;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    List<QuestionItem> questionItems;
    int currentQuestion = 0;
//    int correct = 0, wrong = 0;

    private TextToSpeech speech;
//    private SpeechRecognizer recognizer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private AIService aiService;
    private static final int INTERNET = 200;
    Activity2 autoListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Young Sinatra");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        button1 = (Button) findViewById(R.id.answerA);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("Young Sinatra v3");
//                openActivity4();
                if(questionItems.get(currentQuestion).getAnswer1()
                        .equals(questionItems.get(currentQuestion).getCorrect())) {
                    // correct
//                    correct++;
                    Toast.makeText(Activity2.this, "Correct!", Toast.LENGTH_SHORT).show();
                }else {
                    // wrong
//                    wrong++;
                    Toast.makeText(Activity2.this, "Wrong! Correct answer: "
                            + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
                }
                // load next question if any

                if( currentQuestion < questionItems.size() - 1) {
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                }
//                else {

//                    Intent intent = new Intent(getApplicationContext(), EndActivity.class);
//                    intent.putExtra("correct", correct);
//                    intent.putExtra("wrong", wrong);
//                    startActivity(intent);
//                    finish();

//                }

            }
        });

        button2 = (Button) findViewById(R.id.answerB);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openActivity3();
                if(questionItems.get(currentQuestion).getAnswer2()
                        .equals(questionItems.get(currentQuestion).getCorrect())) {
                    // correct
//                    correct++;
                    Toast.makeText(Activity2.this, "Correct!", Toast.LENGTH_SHORT).show();
                }else {
                    // wrong
//                    wrong++;
                    Toast.makeText(Activity2.this, "Wrong! Correct answer: "
                            + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
                }
                // load next question if any

                if( currentQuestion < questionItems.size() - 1) {
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                }

//                else {
//
//                    Intent intent = new Intent(getApplicationContext(), EndActivity.class);
//                    intent.putExtra("correct", correct);
//                    intent.putExtra("wrong", wrong);
//                    startActivity(intent);
//                    finish();
//
//                }

            }
        });
        button3 = (Button) findViewById(R.id.answerC);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openActivity4();
                if(questionItems.get(currentQuestion).getAnswer3()
                        .equals(questionItems.get(currentQuestion).getCorrect())) {
                    // correct
//                    correct++;
                    Toast.makeText(Activity2.this, "Correct!", Toast.LENGTH_SHORT).show();
                }else {
                    // wrong
//                    wrong++;
                    Toast.makeText(Activity2.this, "Wrong! Correct answer: "
                            + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
                }
                // load next question if any

                if( currentQuestion < questionItems.size() - 1) {
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                }

                else {

                    Intent intent = new Intent(getApplicationContext(), EndActivity.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    startActivity(intent);
                    finish();

                }
            }
        });
        button4 = (Button) findViewById(R.id.listenButton);

        //get all questions
//        loadAllQuestions();
//        // shuffle the questions if you want
////        Collections.shuffle(questionItems);
//        // load first question
//        setQuestionScreen(currentQuestion);

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

    // set the questions to the screen
//1
    private void setQuestionScreen(int number) {
        message.setText(questionItems.get(number).getQuestion());
        button1.setText(questionItems.get(number).getAnswer1());
        button2.setText(questionItems.get(number).getAnswer2());
        button3.setText(questionItems.get(number).getAnswer3());
    }

    // make a list with all the questions
//2
    private void loadAllQuestions() {

        questionItems = new ArrayList<>();
        String jsonStr = loadJsonFromAsset();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray questions = jsonObj.getJSONArray("questions");
            for (int i = 0; i < questions.length(); i++) {
                JSONObject question = questions.getJSONObject(i);

                String questionString = question.getString("question");
                String answer1String = question.getString("answer1");
                String answer2String = question.getString("answer2");
                String answer3String = question.getString("answer3");
                String correctString = question.getString("correct");


                questionItems.add(new QuestionItem(
                        questionString,
                        answer1String,
                        answer2String,
                        answer3String,
                        correctString

                ));


            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // Loader method for the json file from assets folder
//3
    private String loadJsonFromAsset() {
        String json = "";
        try {
            InputStream is = getAssets().open("quiz.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return json;
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


        if (voiceInput.getText().toString().equals("option A")) {
            button1.performClick();
        } else if (voiceInput.getText().toString().equals("option b")) {
            button2.performClick();
        } else if  (voiceInput.getText().toString().equals("option c")) {
            button3.performClick();
        }


        if(answerOutcome.getText().toString().equals(voiceInput.getText().toString())) {
            answerOutcome.setText(R.string.correct);
        } else {
            answerOutcome.setText(R.string.incorrect);
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

