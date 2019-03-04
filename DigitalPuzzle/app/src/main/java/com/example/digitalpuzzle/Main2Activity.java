package com.example.digitalpuzzle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num=getNumber();
        setContentView(R.layout.activity_main2);
    }

    public void btn_ConfirmClick(View view) {
        final TextView textView_score=findViewById(R.id.textView_RealScore);
        final int score=Integer.parseInt(textView_score.getText().toString());
        final EditText editText = findViewById(R.id.editText_answer);
        String answerString=editText.getText().toString();
        if(answerString.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setTitle("PROMPT");
            builder.setMessage("Try filling in a number!");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editText.requestFocus();
                }
            });
            builder.show();
        }
        else {
            int answerInt=Integer.parseInt(answerString);
            if(answerInt>100||answerInt==0){
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle("PROMPT");
                builder.setMessage("Your number is over 100 or equal to 0.\nLet's retype it!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editText.setText(null);
                        editText.requestFocus();
                    }
                });
                builder.show();
            }
            else {
                if(answerInt>num){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setTitle("PROMPT");
                    builder.setMessage("Your number is too big.\n Let's get a smaller one");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editText.requestFocus();
                            if(score==1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                                builder.setTitle("PROMPT");
                                builder.setMessage("Your score has been used up.\n Let's have another game!");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                            String newscore=String.valueOf(score-1);
                            textView_score.setText(newscore.toCharArray(),0,newscore.toCharArray().length);
                        }
                    });
                    builder.show();
                }
                else if(answerInt<num){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setTitle("PROMPT");
                    builder.setMessage("Your number is too small.\n Let's get a bigger one");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editText.requestFocus();
                            if(score==1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                                builder.setTitle("PROMPT");
                                builder.setMessage("Your score has been used up.\n Let's have another game!");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                            String newscore=String.valueOf(score-1);
                            textView_score.setText(newscore.toCharArray(),0,newscore.toCharArray().length);
                        }
                    });
                    builder.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setTitle("PROMPT");
                    builder.setMessage("congratulations!\nYour score is"+String.valueOf(score));
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.show();
                }
            }
        }
    }

    private int getNumber(){
        Random random  = new Random();
        int a = random.nextInt(100);
        return a+1;
    }
}
