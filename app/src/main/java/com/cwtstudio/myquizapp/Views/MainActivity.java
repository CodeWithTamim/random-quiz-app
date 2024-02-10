package com.cwtstudio.myquizapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cwtstudio.myquizapp.Models.Quiz;
import com.cwtstudio.myquizapp.R;
import com.cwtstudio.myquizapp.Volley.ApiHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView txt_question, txt_category, txtCoins;
    private RadioGroup radioGroup;
    private RadioButton rb_ans1, rb_ans2, rb_ans3, rb_ans4;
    private AppCompatButton btnSubmit;
    private int index = 0;
    private int points = 0;
    private List<Quiz> quizList;
    private List<String> incorrectAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeAll();
        callApi();
    }

    private void callApi() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        ApiHandler.getQuizList(requestQueue, new ApiHandler.QuizCallBack() {
            @Override
            public void onQuizSuccess(List<Quiz> quiz) {
                quizList = quiz;
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                setQuestions();
            }

            @Override
            public void onQuizError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setQuestions() {
        txt_question.setText(quizList.get(index).getQuestion());
        txt_category.setText(quizList.get(index).getCategory());
        txtCoins.setText("Coins: " + points);

        List<String> answers = new ArrayList<>();
        answers.add(quizList.get(index).getCorrectAnswer());
        incorrectAnswers = quizList.get(index).getIncorrectAnswers();
        answers.addAll(incorrectAnswers);

        // Check if answers list has at least 4 elements
        if (answers.size() < 4) {
            Toast.makeText(MainActivity.this, "Not enough answers for the question", Toast.LENGTH_SHORT).show();
            return;
        }

        // Shuffle the answers list
        Collections.shuffle(answers);

        // Assign shuffled answers to radio buttons
        rb_ans1.setText(answers.get(0));
        rb_ans2.setText(answers.get(1));
        rb_ans3.setText(answers.get(2));
        rb_ans4.setText(answers.get(3));

        // Reset radio button selection
        radioGroup.clearCheck();

        btnSubmit.setOnClickListener(v -> {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) {
                // No radio button is checked
                Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton checkedRadioButton = findViewById(checkedId);
            String selectedAnswer = checkedRadioButton.getText().toString();

            if (selectedAnswer.equals(quizList.get(index).getCorrectAnswer())) {
                // Correct answer
                points++;
                if (index < quizList.size() - 1) {
                    index++;
                    Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                    setQuestions();
                } else {
                    Toast.makeText(MainActivity.this, "The end", Toast.LENGTH_SHORT).show();
                    index = 0;
                    callApi();

                    setQuestions();
                }
            } else {
                // Incorrect answer
                if (index < quizList.size() - 1) {
                    index++;
                    setQuestions();
                    Toast.makeText(MainActivity.this, "Wrong answer!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "The end!", Toast.LENGTH_SHORT).show();
                    index = 0;
                    callApi();

                    setQuestions();
                }
            }
        });
    }

    private void initializeAll() {
        txt_question = findViewById(R.id.txt_question);
        txt_category = findViewById(R.id.txt_category);
        txtCoins = findViewById(R.id.txtCoins);
        radioGroup = findViewById(R.id.radioGroup);
        rb_ans1 = findViewById(R.id.rb_ans1);
        rb_ans2 = findViewById(R.id.rb_ans2);
        rb_ans3 = findViewById(R.id.rb_ans3);
        rb_ans4 = findViewById(R.id.rb_ans4);
        btnSubmit = findViewById(R.id.btnSubmit);
    }
}
