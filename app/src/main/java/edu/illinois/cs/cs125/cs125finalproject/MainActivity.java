package edu.illinois.cs.cs125.cs125finalproject;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private static final String TAG = "CS125FinalProject.main";
    public void sendMessage(View view) {
        TextInputEditText playerName = (TextInputEditText)findViewById(R.id.player_name);
        Log.d(TAG, "Button Pressed " + playerName.getText().toString());
    }
}
