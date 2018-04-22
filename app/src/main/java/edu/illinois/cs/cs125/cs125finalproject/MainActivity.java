package edu.illinois.cs.cs125.cs125finalproject;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ImageView nbaLogo = (ImageView)findViewById(R.id.imageView2);
        //nbaLogo.setImageResource(R.drawable.nba_logo);
        ImageView dRose = (ImageView)findViewById(R.id.imageView);
        dRose.setImageResource(R.drawable.nba_logo);
    }
    private static final String TAG = "CS125FinalProject.main";
    public void sendMessage(View view) {
        TextInputEditText playerName = (TextInputEditText)findViewById(R.id.input_player);
        TextInputEditText season = (TextInputEditText)findViewById(R.id.input_season);
        Log.d(TAG, "Player Name: " + playerName.getText().toString());
        Log.d(TAG, "season: " + season.getText().toString());
        Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_LONG).show();
    }
}
