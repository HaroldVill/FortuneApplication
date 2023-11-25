package com.example.fortuneapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SyncDatas extends AppCompatActivity {


    CardView z1, z2, z3, z4, z5, z6, z7, z8;
    TextView blkhome, set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_datas);

        z1 = findViewById(R.id.z1);
        z2 = findViewById(R.id.z2);
        z3= findViewById(R.id.z3);
        z4= findViewById(R.id.z4);
        z5= findViewById(R.id.z5);
        z6= findViewById(R.id.z6);
        z7 = findViewById(R.id.z7);
        z8 = findViewById(R.id.z8);
        blkhome = findViewById(R.id.blkhome);
        set = findViewById(R.id.set);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder passwordBuilder = new AlertDialog.Builder(SyncDatas.this);
                passwordBuilder.setTitle("Enter Password");
                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordBuilder.setView(input);
                passwordBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        if (password.equals("admin")) {
                            Intent ff = new Intent(SyncDatas.this, Connections.class);
                            startActivity(ff);
                            Toast.makeText(SyncDatas.this, "Welcome Admin", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SyncDatas.this, "You are not Authorized", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                passwordBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog passwordDialog = passwordBuilder.create();
                passwordDialog.show();

            }
        });

        blkhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dd = new Intent(SyncDatas.this, HomePage.class);
                startActivity(dd);

            }
        });

        z1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ds = new Intent(SyncDatas.this, SyncItemActivity.class);
                startActivity(ds);

            }
        });

        z2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kh = new Intent(SyncDatas.this, SyncCustomer.class);
                startActivity(kh);

            }
        });


        z3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qs = new Intent(SyncDatas.this , SyncLocation.class);
                startActivity(qs);

            }
        });


     z4.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent dd = new Intent(SyncDatas.this , SyncSalesRep.class);
        startActivity(dd);
      ;
    }
    });


z5.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent df = new Intent(SyncDatas .this, SyncUnitMeasures.class);
        startActivity(df);

    }
});
z6.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
         Intent pt = new Intent(SyncDatas.this , SyncPterms .class);
         startActivity(pt);

    }
      });

z7.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent dw = new Intent(SyncDatas.this, NewPriceLEVEl.class);
        startActivity(dw);

    }
});

z8.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent df = new Intent(SyncDatas.this,SyncPriceleveLines.class);
        startActivity(df);

    }
});

    }
}