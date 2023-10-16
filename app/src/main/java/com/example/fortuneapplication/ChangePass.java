package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_TABLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePass extends AppCompatActivity {
    EditText cpass, cuser, conpas;
    TextView cre;
    Button upd;
    private PazDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);


        cuser = findViewById(R.id.cuser);
        cpass = findViewById(R.id.cpass);
        conpas = findViewById(R.id.conpas);
        cre = findViewById(R.id.cre);
        upd = findViewById(R.id.upd);
        mDatabaseHelper = new PazDatabaseHelper(this);

cre.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        Intent df = new Intent(ChangePass.this, LogIn.class);
        startActivity(df);
        finish();
    }
 });

        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = cuser.getText().toString();
                String password = cpass.getText().toString();
                String confirmPassword = conpas.getText().toString();


                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.length() < 4 ) {
                    Toast.makeText(ChangePass.this, "Please enter all credentials & Check if Valid ", Toast.LENGTH_SHORT).show();

                } else {
                    // Check if the username exists in the sales rep table
                    SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
                    String selectQuery = "SELECT * FROM " + SALESREP_TABLE + " WHERE " + SALESREP_NAME + " LIKE '" + username + "%'";
                    Cursor cursor = db.rawQuery(selectQuery, null);

                    if (cursor.moveToFirst()) {
                        // Username exists in the sales rep table, proceed with password change
                        if (!password.equals(confirmPassword)) {
                            Toast.makeText(ChangePass.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                        } else {
                            // Update the password in the database for the specified username
                            int salesRepIdColumnIndex = cursor.getColumnIndexOrThrow(SALESREP_ID);
                            int salesRepId = cursor.getInt(salesRepIdColumnIndex);
                            ContentValues values = new ContentValues();
                            values.put("password", password);

                            int rowsAffected = db.update(SALESREP_TABLE, values, SALESREP_ID + " = ?", new String[]{String.valueOf(salesRepId)});

                            if (rowsAffected > 0) {
                                Toast.makeText(ChangePass.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Finish the activity and go back to the previous screen
                            } else {
                                Toast.makeText(ChangePass.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Username doesn't exist in the sales rep table
                        Toast.makeText(ChangePass.this, "Invalid username", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                    db.close();
                }
            }
        });


    }
}