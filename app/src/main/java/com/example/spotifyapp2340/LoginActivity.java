package com.example.spotifyapp2340;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "bba1543448324cecabcc2a9328c94179";
    private static final String REDIRECT_URI = "com.example.spotifyapp2340://auth";
    private static final int REQUEST_CODE = 1337; // Can be any unique integer

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initiate the authentication request
                AuthorizationRequest request = new AuthorizationRequest.Builder(
                        CLIENT_ID,
                        AuthorizationResponse.Type.TOKEN,
                        REDIRECT_URI
                ).build();

                AuthorizationClient.openLoginActivity(LoginActivity.this, REQUEST_CODE, request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result comes from the Spotify login activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                case TOKEN:
                    // Authorization was successful, you can use response.getAccessToken() to make API requests
                    String accessToken = response.getAccessToken();
                    // Handle successful login, e.g., navigate to the main activity
                    break;
                case ERROR:
                    // Authorization was unsuccessful, handle the error
                    String errorMessage = response.getError();
                    // Handle error, e.g., display an error message to the user
                    break;
                default:
                    // Other cases
                    break;
            }
        }
    }
}