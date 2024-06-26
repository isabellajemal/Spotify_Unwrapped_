package com.example.spotifyapp2340;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyapp2340.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import android.os.Bundle;

public class TokenActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "bba1543448324cecabcc2a9328c94179";
    public static final String REDIRECT_URI = "com.example.spotifyapp2340://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;
    private Button tokenBtn, codeBtn, profileBtn, playlists_btn, topTracks_btn;

    private TextView tokenTextView, codeTextView, profileTextView, playlistsTextView, topArtistsTextView, topTracksTextView;

    private static final String SPOTIFY_PLAYLISTS_ENDPOINT = "https://api.spotify.com/v1/me/playlists";
    private static final String SPOTIFY_ARTISTS_ENDPOINT = "https://api.spotify.com/v1/me/top/artists";
    private static final String SPOTIFY_TRACKS_ENDPOINT = "https://api.spotify.com/v1/me/top/tracks";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        // Initialize the views
        tokenTextView = (TextView) findViewById(R.id.token_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);
       // playlistsTextView = (TextView) findViewById(R.id.responsePlaylists_text_view);
        //topArtistsTextView = (TextView) findViewById(R.id.responseTopArtists_text_view);
        //topTracksTextView = (TextView) findViewById(R.id.responseTopTracks_text_view);


       /* // Setup RecyclerView and Adapter
        RecyclerView recyclerViewArtists = findViewById(R.id.recyclerViewArtists);
        adapter = new ArtistsAdapter(); // Initialize the adapter
        recyclerViewArtists.setAdapter(adapter);
        recyclerViewArtists.setLayoutManager(new LinearLayoutManager(this));*/

        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);
        Button codeBtn = (Button) findViewById(R.id.code_btn);
       // Button profileBtn = (Button) findViewById(R.id.profile_btn);
        //Button playlistsBtn = (Button) findViewById(R.id.playlists_btn);
       // Button topArtistsBtn = (Button) findViewById(R.id.topArtists_btn);
       // Button topTracksBtn = (Button) findViewById(R.id.topTracks_btn);

       /* // Now you can call the method
        String userId = "someUserId"; // Get the userId appropriately
        adapter.loadUserTopArtists(userId);*/

        // Set the click listeners for the buttons

        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });

        codeBtn.setOnClickListener((v) -> {
            getCode();
        });

        /*profileBtn.setOnClickListener((v) -> {
            onGetUserProfileClicked();
        });*/
        // find a way to get user playlists
        /*playlistsBtn.setOnClickListener((v) -> {
            onGetUserPlaylistsClicked();*/
        /*});*/
        // find a way to get user's top artists
       /* topArtistsBtn.setOnClickListener((v) -> {
            onGetTopArtistsClicked();
        });*/
        /*//find a way to get user's top tracks
        topTracksBtn.setOnClickListener((v) -> {
            onGetTopTracksClicked();
        });*/

    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(TokenActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * Get code from Spotify
     * This method will open the Spotify login activity and get the code
     * What is code?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(TokenActivity.this, AUTH_CODE_REQUEST_CODE, request);
    }


    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
            setTextAsync(mAccessToken, tokenTextView);

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
            setTextAsync(mAccessCode, codeTextView);
        }
    }


    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    public void onGetUserProfileClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(TokenActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(TokenActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
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

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(true)
                .setScopes(new String[]{"user-read-email", "user-top-read"}) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
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


    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }



    /**
     * Get user playlists info
     * This method will get user's playlists using the token
     */
    public void onGetUserPlaylistsClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/be0luah4gpo9cbfzc3zwellep/playlists")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(TokenActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(TokenActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }

    /**
     * Get user's top artists
     * This method will get user's top artists using the token
     */
    public void onGetTopArtistsClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user's top artists
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(TokenActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(TokenActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }

    /**
     * Get user's top tracks
     * This method will get user's top tracks using the token
     */
    public void onGetTopTracksClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a request to get the user's top tracks
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                runOnUiThread(() -> {
                    Toast.makeText(TokenActivity.this, "Failed to fetch data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    runOnUiThread(() -> {
                        Toast.makeText(TokenActivity.this, "Failed to parse data, watch Logcat for more details", Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }
    }
