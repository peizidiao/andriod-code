package android.example.myapplication;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        //setContentView(R.layout.layout_login);
        //setContentView(R.layout.activity_main);
    }

    public void SignUp(View view) {
        EditText name = (EditText) findViewById(R.id.Register_name);
        EditText email = (EditText) findViewById(R.id.Register_email);
        EditText password = (EditText) findViewById(R.id.Register_password);
        if (!name.getText().toString().equals(null) && !email.getText().toString().equals(null) && !password.getText().toString().equals(null)) {
            SharedPreferences list = getSharedPreferences("list", 0);
            SharedPreferences.Editor editor = list.edit();
            editor.putString("name", name.getText().toString());
            editor.putString("email", email.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.commit();
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"something empty",Toast.LENGTH_SHORT).show();
        }
    }

    public void btnLogin(View view) {
        EditText email = (EditText) findViewById(R.id.Login_email);
        EditText password = (EditText) findViewById(R.id.Login_password);
        if (!email.getText().toString().equals(null) && !password.getText().toString().equals(null)) {
            SharedPreferences list = getSharedPreferences("list", 0);
            String listemail=list.getString("email","null");
            String listpassword=list.getString("password","null");
            if(email.getText().toString().equals(listemail)&&password.getText().toString().equals(listpassword)){
                new changelayout().execute(R.layout.activity_main);
            }
            else {
                Toast.makeText(this,"something error",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"something empty",Toast.LENGTH_SHORT).show();
        }

    }


    public void   btnLogout(View view) {
        new changelayout().execute(R.layout.layout_login);
    }

    public void btnRegister(View view) {
        new changelayout().execute(R.layout.layout_register);
    }

    public void LoginLayout(View view) {
        new changelayout().execute(R.layout.layout_login);
    }

    private class changelayout extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... ints) {
            return ints[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            setContentView(integer);
        }
    }
}
