package com.example.spotifyapp2340.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotifyapp2340.databinding.FragmentHomeBinding;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class HomeFragment extends Fragment {
    public static final String CLIENT_ID = "bba1543448324cecabcc2a9328c94179";
    public static final String REDIRECT_URI = "com.example.spotifyapp2340://auth";

    private FragmentHomeBinding binding;

    private ActivityResultLauncher<Intent> authActivityResultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ActivityResultLauncher
        authActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    final AuthorizationResponse response = AuthorizationClient.getResponse(result.getResultCode(), result.getData());

                    switch (response.getType()) {
                        case TOKEN:
                            binding.tokenTextView.setText(response.getAccessToken());
                            break;
                        case CODE:
                            binding.codeTextView.setText(response.getCode());
                            break;
                        case ERROR:
                            Toast.makeText(getContext(), "Auth error: " + response.getError(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getContext(), "Auth canceled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
<<<<<<< Updated upstream
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
=======

        // Set the click listeners for the buttons
        binding.tokenBtn.setOnClickListener((v) -> getToken());
        binding.codeBtn.setOnClickListener((v) -> getCode());

        return binding.getRoot();
    }

    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        Intent intent = AuthorizationClient.createLoginActivityIntent(getActivity(), request);
        authActivityResultLauncher.launch(intent);
    }

    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        Intent intent = AuthorizationClient.createLoginActivityIntent(getActivity(), request);
        authActivityResultLauncher.launch(intent);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, REDIRECT_URI)
                .setShowDialog(true)
                .setScopes(new String[]{"user-read-email", "user-top-read"})
                .build();
>>>>>>> Stashed changes
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
