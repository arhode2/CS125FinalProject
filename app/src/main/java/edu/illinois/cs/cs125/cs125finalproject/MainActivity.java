package edu.illinois.cs.cs125.cs125finalproject;
import android.nfc.Tag;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;


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

    /** variable to store playerID globally.*/
    int playerID = 0;

    /** map for the stats to display. Updated on successful api calls.*/
    Map mainStats = new HashMap();

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
    public void getStats(View view) {
        TextInputEditText playerName = (TextInputEditText) findViewById(R.id.input_player);
        TextView textOutput = (TextView) findViewById(R.id.text_output);
        String playerNameString = playerName.getText().toString();
        String[] fullname = playerName.getText().toString().toLowerCase().split(" ");
        if (fullname.length != 2) {
            Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_LONG).show();
            return;
        }
        String firstName = fullname[0];
        String lastName = fullname[1];
        Log.d(TAG, "LAST: " + lastName + " FIRST: " + firstName);
        startFirstAPICall("http://api.suredbits.com/nba/v0/stats/" + lastName + "/" + firstName);
    }
    void startFirstAPICall(final String inputURL) {
        try {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                     inputURL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            try {
                                playerData = response;
                                Log.d(TAG, playerData.toString(2));
                                playerID = getPlayerID();
                                startSecondAPICall("https://stats.nba.com/stats/playerprofilev2?playerID=" + playerID + "&PerMode=PerGame");
                                } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Please enter a current player name. Check your spelling.", Toast.LENGTH_LONG).show();
                            }
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
    /** I know I'm an idiot, but I spent a long time fucking with this and got nowhere better.*/
    void startSecondAPICall(final String inputURL) {
        try {
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    inputURL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            parseStats(response);
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
    public int getPlayerID() {
        JsonParser parser = new JsonParser();
        JsonArray playerDataArray = parser.parse(playerData.toString()).getAsJsonArray();
        //Log.d(TAG,"PLAYER DATA ARRAY IS: " + playerDataArray.toString());
        JsonObject playerDataObject = playerDataArray.get(0).getAsJsonObject();
        //Log.d(TAG, "PLAYER DATA OBJECT IS: " + playerDataObject.toString());
        int playerID = playerDataObject.get("playerId").getAsInt();
        //Log.d(TAG, "playerId: " + playerID);
        return playerID;
    }
    public void parseStats(final JSONObject allStats) {
        JsonParser parser = new JsonParser();
        JsonObject dataObject = parser.parse(allStats.toString()).getAsJsonObject();
        JsonArray resultSets = dataObject.get("resultSets").getAsJsonArray();
        JsonObject resultSetsObject = resultSets.get(0).getAsJsonObject();
        JsonArray rowSet = resultSetsObject.get("rowSet").getAsJsonArray();
        JsonArray currentArray = null;
        String season = "";
        for (int i = 0; i < rowSet.size(); i++) {
            currentArray = rowSet.get(i).getAsJsonArray();
            season = currentArray.get(1).getAsString();
        }
        Log.d(TAG, currentArray.toString());
        mainStats.put("Team", currentArray.get(4).getAsString());
        if (mainStats.get("Team").equals("TOT")) {
            mainStats.put("Team", "Multiple");
        }
        mainStats.put("PPG", currentArray.get(26).getAsString());
        mainStats.put("APG", currentArray.get(21).getAsString());
        mainStats.put("RPG", currentArray.get(20).getAsString());
        mainStats.put("BLK", currentArray.get(23).getAsString());
        mainStats.put("STL", currentArray.get(22).getAsString());
        mainStats.put("TOV", currentArray.get(24).getAsString());
        mainStats.put("Age", currentArray.get(5).getAsString());
        mainStats.put("MPG", currentArray.get(8).getAsString());
        updateUI();
        return;
    }
    public void updateUI() {
        TextView textOutput = (TextView) findViewById(R.id.text_output);
        textOutput.setText(
                "Age: " + mainStats.get("Age")
                + "\nTeam: " + mainStats.get("Team")
                + "\nPPG: " + mainStats.get("PPG")
                + "\nAPG: " + mainStats.get("APG")
                + "\nRPG: " + mainStats.get("RPG")
                + "\nBLK: " + mainStats.get("BLK")
                + "\nSTL: " + mainStats.get("STL")
                + "\nTOV: " + mainStats.get("TOV")
                + "\nMPG: " + mainStats.get("MPG"));
    }
}
