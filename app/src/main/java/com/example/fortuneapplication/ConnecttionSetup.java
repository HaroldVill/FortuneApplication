package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConnecttionSetup extends AppCompatActivity {
    EditText con1, con2;
    Button consave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecttion_setup);

        con1= findViewById(R.id.con1);
        con2 = findViewById(R.id.con2);
        consave = findViewById(R.id.consave);

        consave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getconection();

            }
        });
    }
    public void getconection(){
        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        String cn1 = con1.getText().toString();
        String cn2 = con2.getText().toString();

        if (cn1.isEmpty()||cn2.isEmpty()){
            Toast.makeText(this, "NAME OR IP IS MISSING", Toast.LENGTH_SHORT).show();

        } else if (!cn1.isEmpty()&&!cn2.isEmpty()) {

          CONNECT connect = new CONNECT();
          connect.setName(cn1);
          connect.setIp(cn2);
            databaseHelper.storeConnection(connect);
            Toast.makeText(this, "Successfully Save", Toast.LENGTH_LONG).show();
            Intent fff = new Intent(ConnecttionSetup.this, Connections.class);
            startActivity(fff);
            finish();

        }else{
            Toast.makeText(this, "Please provide Valid NAME and IP", Toast.LENGTH_SHORT).show();
        }

    }


}