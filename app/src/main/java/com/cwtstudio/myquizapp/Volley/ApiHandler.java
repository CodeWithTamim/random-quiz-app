package com.cwtstudio.myquizapp.Volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cwtstudio.myquizapp.Models.Quiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiHandler {
    private static final String API_URL = "https://opentdb.com/api.php?amount=10";

    public static void getQuizList(RequestQueue requestQueue, final QuizCallBack callBack) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callBack.onQuizSuccess(parseQuizes(response));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.onQuizError(error.getMessage());

                    }
                });

        requestQueue.add(request);


    }

    private static List<Quiz> parseQuizes(JSONObject response) {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            JSONArray resultsArray = response.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                Quiz quiz = new Quiz();
                quiz.setCategory(resultObject.getString("category"));
                quiz.setDifficulty(resultObject.getString("difficulty"));
                quiz.setType(resultObject.getString("type"));
                quiz.setQuestion(resultObject.getString("question"));
                quiz.setCorrectAnswer(resultObject.getString("correct_answer"));
                JSONArray incorrectAnwersArray = resultObject.getJSONArray("incorrect_answers");
                List<String> incorrectAnswersList = new ArrayList<>();
                for (int j = 0; j < incorrectAnwersArray.length(); j++) {
                    incorrectAnswersList.add(incorrectAnwersArray.getString(j));

                }
                quiz.setIncorrectAnswers(incorrectAnswersList);
                quizzes.add(quiz);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return quizzes;


    }


    public interface QuizCallBack {
        void onQuizSuccess(List<Quiz> quiz);

        void onQuizError(String errorMessage);
    }
}
