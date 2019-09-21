package com.example.laujame.firebase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity  extends AppCompatActivity  {
    public static final String user="names";
    TextView txtUser;

        Button realtime,
                Storage,
                Multimedia;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




    txtUser =(TextView)findViewById(R.id.textser);
    String user = getIntent().getStringExtra("names");
    txtUser.setText("¡Bienvenido "+ user +"!");

    realtime = findViewById(R.id.btn_realtime);
    realtime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MenuActivity.this,realtimeActivity.class);
            startActivity(intent);
        }
    });

    Multimedia= (Button)findViewById(R.id.btn_multimedia);
    Multimedia.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent anterior = new Intent(MenuActivity.this, Multimadia.class);
            startActivity(anterior);




        }
    });


    Storage = (Button) findViewById(R.id.btnStorage);
    Storage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent st = new Intent(MenuActivity.this, StorangeActivity.class);
            startActivity(st);
        }
    });


}
}



