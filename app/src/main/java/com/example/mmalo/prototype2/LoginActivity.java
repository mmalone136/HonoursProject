package com.example.mmalo.prototype2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by mmalo on 20/10/2016.
 */

public class LoginActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void dummyLogin(View v){
        Toast t = Toast.makeText(this, "LOGIN_ACT", Toast.LENGTH_LONG);
        t.show();
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);


    }

}
