package com.example.digitalpuzzle;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textView)).setTypeface(Typeface.createFromAsset(findViewById(R.id.textView).getContext().getAssets(),"Fonts/ARBERKLEY.ttf"));
    }

    public void btn_startClick(View view) {
        Intent intent =new Intent(MainActivity.this,Main2Activity.class);
        startActivity(intent);
    }

    public void btn_exitClick(View view) {
        finish();
    }
}
