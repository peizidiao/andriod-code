package com.example.goslinglovelace;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinfo);
        if (isChinese()) {
            int orientation = getResources().getConfiguration().orientation;
            String str;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                TextView namePort = (TextView) findViewById(R.id.textView_James);
                namePort.setText("詹姆斯·高斯林");
                TextView tvPort = (TextView) findViewById(R.id.textView_James_content);
                str = getResources().getString(R.string.James_content_Zh);
                tvPort.setText(str);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                TextView nameLand = (TextView) findViewById(R.id.textView_Ada);
                nameLand.setText("艾达·洛维斯");
                TextView tvLand = (TextView) findViewById(R.id.textView_Ada_content);
                str = getResources().getString(R.string.Ada_content_Zh);
                tvLand.setText(str);
            }
        }
        else {
            int orientation = getResources().getConfiguration().orientation;
            String str;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                TextView namePort = (TextView) findViewById(R.id.textView_James);
                namePort.setText("James Gosling");
                TextView tvPort = (TextView) findViewById(R.id.textView_James_content);
                str = getResources().getString(R.string.James_content_En);
                tvPort.setText(str);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                TextView nameLand = (TextView) findViewById(R.id.textView_Ada);
                nameLand.setText("Ada_Lovelace");
                TextView tvLand = (TextView) findViewById(R.id.textView_Ada_content);
                str = getResources().getString(R.string.Ada_content_En);
                tvLand.setText(str);
            }
        }
    }
    public boolean isChinese(){
        Locale locale = this.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if(language.endsWith("zh")){
            return true;
        }
        else return false;
    }
}
