package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_TABLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class LogIn extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_USERNAME = "username";
    EditText user, pass;
    Button login;
    TextView forgot;
    ImageView pict;
    private PazDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
       login = findViewById(R.id.login);
       forgot = findViewById(R.id.forgot);
       pict = findViewById(R.id.pict);
       mDatabaseHelper = new PazDatabaseHelper(this);

       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String username = user.getText().toString();
               String password = pass.getText().toString();

               if (username.equals("admin") && password.equals("admin") || username.equals("jbags") && password.equals("1")) {
                   Toast.makeText(LogIn.this, "WELCOME ADMIN", Toast.LENGTH_SHORT).show();
                   Intent FGG = new Intent(LogIn.this, HomePage.class);
                   user.setText("");
                   pass.setText("");

                   startActivity(FGG);
                   finish();
                   return;
               }

               if (username.isEmpty() || password.isEmpty() ){
                   Toast.makeText(LogIn.this, "USERNAME OR PASSWORD EMPTY", Toast.LENGTH_SHORT).show();
               } else {

                   String[] nameParts = username.split(",");
                   String firstName = "";
                   if (nameParts.length > 0) {
                       firstName = nameParts[0].trim();
                   }

                   if (firstName.length() < 4) {
                       Toast.makeText(LogIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                       return;
                   }
                   SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
                   String selectQuery = "SELECT * FROM " + SALESREP_TABLE + " WHERE " + SALESREP_NAME + " LIKE '" + firstName + "%'";
                   Cursor cursor = db.rawQuery(selectQuery, null);
                   boolean isValidUser = false;

                   while (cursor.moveToNext()) {
                       int passwordColumnIndex = cursor.getColumnIndexOrThrow("password");
                       String storedPassword = cursor.getString(passwordColumnIndex);
                       if (!storedPassword.equals("1") && password.equals("1")) {
                           // Default password is "1" and entered password is also "1"
                           isValidUser = false;
                           break;

                       } else if (storedPassword.equals(password)) {
                           isValidUser = true;
                           break;
                       }
                   }
                   if (isValidUser) {
                       SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putString(PREF_USERNAME, username);
                       editor.apply();

                       Intent hm = new Intent(LogIn.this, HomePage.class);
                       startActivity(hm);
                       user.setText("");
                       pass.setText("");
                       finish();

                       } else {
                       Toast.makeText(LogIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                   }

                   cursor.close();
                   db.close();

               }

           }
       });

       forgot.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent hk =  new Intent(LogIn.this, ChangePass.class);
               startActivity(hk);

           }
       });

    }
}