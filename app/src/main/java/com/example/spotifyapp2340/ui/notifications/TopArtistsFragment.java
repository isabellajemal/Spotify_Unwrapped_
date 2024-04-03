package com.example.spotifyapp2340.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifyapp2340.ArtistsAdapter;
import com.example.spotifyapp2340.databinding.FragmentNotificationsBinding;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifyapp2340.ArtistsAdapter;
import com.example.spotifyapp2340.databinding.FragmentNotificationsBinding;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopArtistsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private ArtistsAdapter adapter;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken; // Will be set after successful authentication

    // Spotify API constants
    private static final String CLIENT_ID = "bba1543448324cecabcc2a9328c94179";
    private static final String REDIRECT_URI = "com.example.spotifyapp2340://auth";

    // ActivityResultLauncher for handling Spotify authentication result
    private ActivityResultLauncher<Intent> spotifyAuthLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spotifyAuthLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleAuthResult
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        adapter = new ArtistsAdapter(); // Assuming a no-argument constructor
        binding.recyclerViewArtists.setAdapter(adapter);
        binding.recyclerViewArtists.setLayoutManager(new LinearLayoutManager(getContext()));

        // Automatically initiate token retrieval on fragment view creation
        getToken();

        // Initialize click listeners for buttons
        binding.buttonThisWeek.setOnClickListener(v -> fetchData("short_term"));
        binding.buttonLastSixMonths.setOnClickListener(v -> fetchData("medium_term"));
        binding.buttonThisYear.setOnClickListener(v -> fetchData("long_term"));

        return binding.getRoot();
    }

    private void getToken() {
        AuthorizationRequest request = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();
        Intent intent = AuthorizationClient.createLoginActivityIntent(getActivity(), request);
        spotifyAuthLauncher.launch(intent);
    }

    private void handleAuthResult(ActivityResult result) {
        if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
            AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());
            if (response.getType() == AuthorizationResponse.Type.TOKEN) {
                mAccessToken = response.getAccessToken();
                // Now you can fetch data or update the UI as needed
            } else {
                Toast.makeText(getContext(), "Error: " + response.getError(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Authorization was cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchData(String timeRange) {
        if (mAccessToken == null) {
            Toast.makeText(getContext(), "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=" + timeRange)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show());
                    return;
                }
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    getActivity().runOnUiThread(() -> {
                        // Update your UI with the fetched data
                        try {
                            System.out.println(jsonObject.toString(3));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (JSONException e) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to parse data", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up
    }
}
