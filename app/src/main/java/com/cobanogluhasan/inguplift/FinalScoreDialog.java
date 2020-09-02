package com.cobanogluhasan.inguplift;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FinalScoreDialog extends AppCompatActivity {

    private Context mContext;
    public Dialog finalScoreDialog;
    public TextView finalScoreTextview;
    public Button playAgainButton;
    int score;
    private TextView myTextview;
    public FinalScoreDialog(Context mContext) {
        this.mContext = mContext;
    }



    public void finalScoreDialog(final int correctAnswer, final int wrongAnswer, final int totalQuestions, final String keepCurrent,String myFinalScoreText,String buttonText2){

        finalScoreDialog = new Dialog(mContext);
        finalScoreDialog.setContentView(R.layout.final_score_dialog);

         ImageButton closeButton= (ImageButton) finalScoreDialog.findViewById(R.id.closeButton);
         myTextview = (TextView) finalScoreDialog.findViewById(R.id.myTextViewFinal);


        playAgainButton = (Button)  finalScoreDialog.findViewById(R.id.playAgainButton);
        myTextview.setText(myFinalScoreText);
        playAgainButton.setText(myFinalScoreText);
        playAgainButton.setText(buttonText2);




        finalScore(correctAnswer, wrongAnswer,totalQuestions,myFinalScoreText);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalScoreDialog.dismiss();

                int total=totalQuestions-1;


                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra("correct", correctAnswer);
                intent.putExtra("total", total);
                intent.putExtra("wrong", wrongAnswer);
                intent.putExtra("score", score);
                intent.putExtra("filePref", keepCurrent);
                mContext.startActivity(intent);


            }
        });

        finalScoreDialog.show();

        finalScoreDialog.setCancelable(false);
        finalScoreDialog.setCanceledOnTouchOutside(false);

    }



    private void finalScore(int correctAnswer, int wrongAnswer,int totalQuestions,String myFinalScoreText) {

         score=correctAnswer*10-wrongAnswer*5;

        finalScoreTextview = (TextView) finalScoreDialog.findViewById(R.id.finalScoreTextview);

        finalScoreTextview.setText(myFinalScoreText + String.valueOf(score));


    }


}
