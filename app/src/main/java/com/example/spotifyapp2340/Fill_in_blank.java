package com.example.spotifyapp2340;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Fill_in_blank extends AppCompatActivity {

    private TextView totalQuestionTextView;
    private TextView questionTextView;
    private EditText userAnswerEditText;
    private Button submitButton;

    private Button backBtn3;

    private int currentQuestionIndex = 0;

    private int correctAnswersNum = 0;

    // Arrays to hold questions and answers
    private String[] questions = {"My team good, we don't really need a _____.", "I know when that hotline _____.", "Your bodyguard put in some work like a ____."};
    private String[] correctAnswers = {"mascot", "bling", "fluke"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_blank);

        totalQuestionTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        userAnswerEditText = findViewById(R.id.user_answer);
        submitButton = findViewById(R.id.submit_btn);
        backBtn3 = findViewById(R.id.back_btn);

        // Initialize the first question
        setQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAnswer();
            }
        });

        backBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fill_in_blank.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void setQuestion() {
        // Set the current question and update the UI
        questionTextView.setText(questions[currentQuestionIndex]);
        totalQuestionTextView.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.length);
        userAnswerEditText.setText(""); // Clear previous answer
    }

    private void verifyAnswer() {
        String userAnswer = userAnswerEditText.getText().toString().trim();
        if(userAnswer.equalsIgnoreCase(correctAnswers[currentQuestionIndex])) {
            correctAnswersNum = correctAnswersNum + 1;
            // Show success message or proceed to next question
        }

        // Move to next question if any
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            setQuestion();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("You got " + correctAnswersNum + " correct!")
                    .setPositiveButton("Restart", (dialogInterface, i) -> restartQuiz())
                    .setCancelable(false)
                    .create(); // Create the AlertDialog instance
            dialog.show(); // Show t
        }
    }

    void restartQuiz(){
        correctAnswersNum = 0;
        currentQuestionIndex =0;
        setQuestion();
    }
}
