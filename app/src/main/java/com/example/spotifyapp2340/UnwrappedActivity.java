package com.example.spotifyapp2340;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.BreakIterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UnwrappedActivity extends AppCompatActivity {

    private Button buttonThisWeek, buttonLastSixMonths, buttonThisYear, tracksButtonThisWeek,tracksButtonLastSixMonths, tracksButtonThisYear;

    private TextView textViewYourTopArtists, textViewYourTopTracks;
    public static final String CLIENT_ID = "bba1543448324cecabcc2a9328c94179";
    public static final String REDIRECT_URI = "com.example.spotifyapp2340://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_TOKEN_ACTIVITY = 1; // Class constant

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;


    private static final String SPOTIFY_PLAYLISTS_ENDPOINT = "https://api.spotify.com/v1/me/playlists";
    private static final String SPOTIFY_ARTISTS_ENDPOINT = "https://api.spotify.com/v1/me/top/artists";
    private static final String SPOTIFY_TRACKS_ENDPOINT = "https://api.spotify.com/v1/me/top/tracks";
    private BreakIterator textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications);

        //text views for top artists and tracks
        textViewYourTopArtists = (TextView) findViewById(R.id.textViewYourTopArtists);
        textViewYourTopTracks = (TextView) findViewById(R.id.textViewYourTopTracks);

        //Buttons for time ranges for top artists
        Button buttonThisWeek = (Button) findViewById(R.id.buttonThisWeek);
        Button buttonLastSixMonths = (Button) findViewById(R.id.buttonLastSixMonths);
        Button buttonThisYear = (Button) findViewById(R.id.buttonThisYear);

        //Buttons for time ranges for top tracks
        Button tracksButtonThisWeek = (Button) findViewById(R.id.tracksButtonThisWeek);
        Button tracksButtonLastSixMonths = (Button) findViewById(R.id.tracksButtonLastSixMonths);
        Button tracksButtonThisYear = (Button) findViewById(R.id.tracksButtonThisYear);


        //On click listeners for buttons for top artists
        buttonThisWeek.setOnClickListener((v) -> {
            onGetTopArtistsThisWeekClicked();
        });
        buttonLastSixMonths.setOnClickListener((v) -> {
            onGetTopArtistsLastSixMonthsClicked();
        });
        buttonThisYear.setOnClickListener((v) -> {
            onGetTopArtistsThisYearClicked();
        });

        //on click listeners for buttons for top tracks
        tracksButtonThisWeek.setOnClickListener((v) -> {
            onGetTopTracksThisWeekClicked();
        });
        tracksButtonLastSixMonths.setOnClickListener((v) -> {
            onGetTopTracksLastSixMonthsClicked();
        });
        tracksButtonThisYear.setOnClickListener((v) -> {
            onGetTopTracksThisYearClicked();
        });

    getToken();
    // And the code
    getCode();

}

    private void getToken() {
        Log.i("UnwrappedActivity", "runningGetToken");
        AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private void getCode() {
        AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            Log.i("OnActivityResult, UnWrappedActivity:", "Authorization Token Recieved" + response.getAccessToken());
            mAccessToken = response.getAccessToken();
        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            mAccessCode = response.getCode();
        }
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();
    }

            /**
             * Get user top artists this week
             * This method will get user's top artists this week
             */
            private void onGetTopArtistsThisWeekClicked() {
            if (mAccessToken == null) {
                Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Create a request to get the user profile
            final Request request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/artists?time_range=short_term")
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();
            cancelCall();
            mCall = mOkHttpClient.newCall(request);
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("HTTP", "Failed to fetch data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(UnwrappedActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String userTopArtistWeek = response.body().string();
                        Log.i("Get User Top Artist This Week: ", userTopArtistWeek );
                        final JSONObject jsonObject = new JSONObject(response.body().string());
                        setTextAsync(jsonObject.toString(3), textViewYourTopArtists);
                    } catch (JSONException e) {
                        Log.d("JSON", "Failed to parse data: " + e);
                        runOnUiThread(() -> {
                            Toast.makeText(UnwrappedActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                        });
                    }
                }

            });
        }

    /**
     * Get user top artists the last six months
     * This method will get user's top artists last six months
     */
    private void onGetTopArtistsLastSixMonthsClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=medium_term")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(UnwrappedActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), textViewYourTopArtists);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(UnwrappedActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }
    /**
     * Get user top artists the last year
     * This method will get user's top artists this year
     */
    private void onGetTopArtistsThisYearClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=long_term")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(UnwrappedActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), textViewYourTopArtists);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(UnwrappedActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }

    /**
     * Get user top tracks this week
     * This method will get user's top tracks this week
     */
    private void onGetTopTracksThisWeekClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=short_term")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(UnwrappedActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), textViewYourTopArtists);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(UnwrappedActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }
    /**
     * Get user top tracks the last six months
     * This method will get user's top tracks in the last six months
     */
    private void onGetTopTracksLastSixMonthsClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=medium_term")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(UnwrappedActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), textViewYourTopArtists);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(UnwrappedActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }
    /**
     * Get user top tracks the last six months
     * This method will get user's top tracks in the last six months
     */
    private void onGetTopTracksThisYearClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=long_term")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(UnwrappedActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), textViewYourTopArtists);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(UnwrappedActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text     the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

}

