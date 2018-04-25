package edu.illinois.cs.cs125.cs125finalproject;
import android.nfc.Tag;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import com.google.gson.JsonParser;
import org.json.JSONObject;


/** <p>These are the packages we'll need for the API calls. Required some tweaking
 * to build.gradle(Module:app) to get the files for the MainActivity to recognize the
 * imports.
 * Do not delete the unused ones. They will get used. </p>
 */

public class MainActivity extends AppCompatActivity {

    /** Context for the main activity, which should be the only one we need */
    private static final String TAG = "CS125FinalProject.main";

    /** Request queue for the API razzle dazzle**/
    private static RequestQueue requestQueue;

    /** JSON Array for storing the API request data globally.*/
    JSONArray playerData = new JSONArray();

    /** Runs when the app is booted up */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Get the request queue ready. I'm taking this from Lab 12.
         * I don't know it myself.
         */
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);
        ImageView nbaLogo = (ImageView)findViewById(R.id.ImageView_logo);
        nbaLogo.setImageResource(R.drawable.nba_logo);
    }
    public void sendMessage(View view) {
        TextInputEditText playerName = (TextInputEditText)findViewById(R.id.input_player);
        TextInputEditText season = (TextInputEditText)findViewById(R.id.input_season);
        TextView textOutput = (TextView)findViewById(R.id.text_output);
        //Log.d(TAG, "Player Name: " + playerName.getText().toString());
        //Log.d(TAG, "season: " + season.getText().toString());
        //Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_LONG).show();
        textOutput.setText("This is where the information will go."
                + "\nPlayer: " + playerName.getText().toString()
                + "\nSeason: " + season.getText().toString());
        startAPICall();
    }
    void startAPICall() {
        TextInputEditText playerName = (TextInputEditText)findViewById(R.id.input_player);
        TextInputEditText season = (TextInputEditText)findViewById(R.id.input_season);
        TextView textOutput = (TextView)findViewById(R.id.text_output);
        String nameString = playerName.getText().toString().toLowerCase();
        String seasonString = season.getText().toString();
        String[] fullname = nameString.split(" ");
        String firstName = fullname[0];
        String lastName = fullname[1];
        Log.d(TAG, "lastName: " + lastName);
        Log.d(TAG, "firstName: " + firstName);
        Log.d(TAG, "season: " + seasonString);
        try {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    "http://api.suredbits.com/nba/v0/stats/" + lastName + "/" + firstName + "/" + seasonString,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                playerData = response;
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseData(final JSONArray data) {
        JsonParser parser = new JsonParser();
    }
}
