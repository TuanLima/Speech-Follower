package com.app.speech_follower;

import androidx.appcompat.app.AppCompatActivity;

import edu.cmu.pocketsphinx.Assets;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;

import java.io.File;
import java.io.IOException;

public class SpeechFollower extends AppCompatActivity implements RecognitionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runRecognizerSetup();

    }

    @SuppressLint("StaticFieldLeak")
    private void runRecognizerSetup() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... voids) {
                try {
                    Assets assets = new Assets(SpeechFollower.this);
                    File assetsDir = assets.syncAssets();
                    setupRecognizer(assetsDir);
                } catch (IOException e) {
                    return e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {

            }
        }.execute();
    }

    private void setupRecognizer(File assetsDir) throws IOException {

    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}

