package com.app.speech_follower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.RecognitionListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import android.widget.TextView;
import android.widget.Toast;

public class SpeechFollower extends AppCompatActivity implements RecognitionListener {

    private SpeechRecognizer recognizer;

    public static final String KEYPHRASE = "begin follower";

    public static final String INIT_SEARCH = "init";
    public static final String SENTENCE_1_SEARCH = "Sentence1";
    public static final String SENTENCE_2_SEARCH = "Sentence2";

    public static final String jsgfString1 = "#JSGF V1.0;\n grammar digits; \n public <digits> = [the] [quick] [brown] [fox] [jumps] [over] [the] [lazy] [dog];";
    public static final String jsgfString2 = "#JSGF V1.0;\n grammar table; \n public <table> = [the] [book] [is] [on] [the] [table];";

    public static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Check if user has previously granted audio recording permission */
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        runRecognizerSetup();
    }

    private void runRecognizerSetup(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Assets assets = new Assets(SpeechFollower.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    System.err.println("IOException thrown");
                    e.printStackTrace();
                    finish();
                }


            }
        }).start();
    }

    private void setupRecognizer(File assetsDir) throws IOException {

        try {
            recognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                    .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                    .getRecognizer();
        } catch (IOException e) {
            throw new IOException("Problem getting Recognizer", e);
        }
        recognizer.addListener(this);
        recognizer.addKeyphraseSearch(INIT_SEARCH, KEYPHRASE);
        recognizer.addGrammarSearch(SENTENCE_1_SEARCH, jsgfString1);
        recognizer.addGrammarSearch(SENTENCE_2_SEARCH, jsgfString1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                runRecognizerSetup();
        } else {
            finish();
        }
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null){
            String text = hypothesis.getHypstr();
            if(text.equals(KEYPHRASE)){
                System.out.println("Keyphrase pronounced");
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
            else {
                System.out.println("Keyphrase not pronounced");
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }
}

