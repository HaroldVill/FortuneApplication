package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Notes extends AppCompatActivity {
  EditText takenote;
    Button savenote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        takenote = findViewById(R.id.takenote);
        savenote= findViewById(R.id.savenote);

        savenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nts = takenote.getText().toString();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("NOTE", nts);
                    editor.apply();

                    Intent intent = new Intent(Notes.this, SOActivity.class);
                    startActivity(intent);
                    finish();

            }
        });



    }
}