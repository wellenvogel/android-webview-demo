package de.wellenvogel.android.wvtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText url=(EditText)findViewById(R.id.editText);

        Button bt=(Button)findViewById(R.id.btStart);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StartActivity.this,MainActivity.class);
                Bundle b=new Bundle();
                b.putString(Constants.URLKEY, String.valueOf(url.getText()));
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });
    }

}
