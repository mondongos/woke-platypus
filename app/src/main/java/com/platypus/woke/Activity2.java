package com.platypus.woke;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Activity2 extends AppCompatActivity implements AIListener {
    private Button button1, button2, button3, button4, buttonSkip;
    private TextView message, voiceInput, points;


    List<QuestionItem> questionItems;
    int currentQuestion = 0;
    int correct = 0;


    private TextToSpeech speech;
    //private SpeechRecognizer recognizer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private AIService aiService;
    private static final int INTERNET = 200;
    Activity2 autoListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        button1 = (Button) findViewById(R.id.answerA);
        button2 = (Button) findViewById(R.id.answerB);
        button3 = (Button) findViewById(R.id.answerC);
        button4 = (Button) findViewById(R.id.listenButton);
        buttonSkip = (Button) findViewById(R.id.skip);

        message = findViewById(R.id.textView);
        voiceInput = findViewById(R.id.resultTextView);
        points = findViewById(R.id.points);
        points.setText("Score: " + correct);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswerA();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswerB();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswerC();
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswerSkip();
            }
        });

        final AIConfiguration config = new AIConfiguration("f5fe8871954f4ef18a7b741ab7d37373",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
            aiService = AIService.getService(this, config);
            aiService.setListener(this);
        validateOS();
        //get all questions
        loadAllQuestions();
        // shuffle the questions if you want
//        Collections.shuffle(questionItems);
        // load first question
        setQuestionScreen(currentQuestion);
        // automated button-click for listenButton
        startListen();

    }

    private void validateOS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, INTERNET);
        }
    }

    private void textToVoice(){
        final String readQuestion = message.getText().toString();
        final String readAnswer1 = button1.getText().toString();
        final String readAnswer2 = button2.getText().toString();
        final String readAnswer3 = button3.getText().toString();

        final String wholeSpeech = readQuestion + "Is it, A...:" + readAnswer1 + "\nIs it, B...:" + readAnswer2 + "\nIs it, C...:" + readAnswer3;
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // replace this Locale with whatever you want
                    Locale localeToUse = new Locale.Builder().setLanguage("en").setRegion("GB").build();
                    voiceInput.setVisibility(TextView.INVISIBLE);
                    speech.setLanguage(localeToUse);
                    speech.setSpeechRate(1.0f);
                    speech.speak(wholeSpeech, TextToSpeech.QUEUE_FLUSH, null);
                    startListen();
                }
            }
        });

    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }

    public void startListen(){
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
                }, 9000); // ms time to delay until execution
            }
        });}


    // Loader method for the json file from assets folder
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


    // set the questions to the screen
    private void setQuestionScreen(int number) {
        message.setText(questionItems.get(number).getQuestion());
        button1.setText(questionItems.get(number).getAnswer1());
        button2.setText(questionItems.get(number).getAnswer2());
        button3.setText(questionItems.get(number).getAnswer3());
        buttonSkip.setText(questionItems.get(number).getSkip());
        textToVoice();

    }

    // make a list with all the questions
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
                String skipString = question.getString("skip");


                questionItems.add(new QuestionItem(
                        questionString,
                        answer1String,
                        answer2String,
                        answer3String,
                        correctString,
                        skipString

                ));


            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getAnswerA(){

         if(questionItems.get(currentQuestion).getAnswer1()
                            .equals(questionItems.get(currentQuestion).getCorrect())) {
            // correct
            correct+=10;
            points.setText("Score: " + correct);

            Toast.makeText(Activity2.this, "Correct!", Toast.LENGTH_SHORT).show();
        }else {
            // wrong
            Toast.makeText(Activity2.this, "Wrong! Correct answer: "
                    + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
        }
        // load next question if any

        currentQuestion++;
        setQuestionScreen(currentQuestion);



        }


    public void getAnswerB(){

         if(questionItems.get(currentQuestion).getAnswer2()
                            .equals(questionItems.get(currentQuestion).getCorrect())) {
            // correct
            correct+=10;
             points.setText("Score: " + correct);

             Toast.makeText(Activity2.this, "Correct!", Toast.LENGTH_SHORT).show();
        }else {
            // wrong
            Toast.makeText(Activity2.this, "Wrong! Correct answer: "
                    + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
        }
        // load next question if any

        currentQuestion++;
        setQuestionScreen(currentQuestion);



        }

    public void getAnswerC(){

         if(questionItems.get(currentQuestion).getAnswer3()
                            .equals(questionItems.get(currentQuestion).getCorrect())) {
            // correct
            correct+=10;
             points.setText("Score: " + correct);

             Toast.makeText(Activity2.this, "Correct!", Toast.LENGTH_LONG).show();
        }else {
            // wrong
            Toast.makeText(Activity2.this, "Wrong! Correct answer: "
                    + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_LONG).show();
        }
        // load next question if any

         currentQuestion++;
         setQuestionScreen(currentQuestion);


        }

        public void getAnswerSkip() {

            if(questionItems.get(currentQuestion).getSkip()
                    .equals(questionItems.get(currentQuestion).getSkip())) {
                // correct
//                correct+=10;
                points.setText("Score: " + correct);

                Toast.makeText(Activity2.this, "Skipped!", Toast.LENGTH_LONG).show();
            }
            else {
                // wrong
                Toast.makeText(Activity2.this, "Invalid Answer! Correct answer: "
                        + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_LONG).show();}
            // load next question if any

            if( currentQuestion < questionItems.size() - 1) {
                currentQuestion++;
                setQuestionScreen(currentQuestion);
            }

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
        voiceInput.setVisibility(TextView.VISIBLE);

        if (voiceInput.getText().toString().contains("option A") || voiceInput.getText().toString().contains("A") || voiceInput.getText().toString().contains("hey") ) {
            button1.performClick();
        } else if (voiceInput.getText().toString().contains("option b") || voiceInput.getText().toString().contains("B") || voiceInput.getText().toString().contains("bee") ) {
            button2.performClick();
        } else if  (voiceInput.getText().toString().contains("option C") || voiceInput.getText().toString().contains("C") || voiceInput.getText().toString().contains("see") || voiceInput.getText().toString().contains("sea") || voiceInput.getText().toString().contains("say")) {
            button3.performClick();
        } else {
            buttonSkip.performClick();
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
}

