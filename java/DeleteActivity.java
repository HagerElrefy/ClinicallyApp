package com.mustafa.r.hegazi.trying;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteActivity extends AppCompatActivity {
    Button delete;
    EditText patientNatID;
    DBHelper dbHelper;
    DBHelper.PatientDB patientDB;
    SwitchCompat switchCompat;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }
        else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        switchCompat = findViewById(R.id.bt_switch);


        initView();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = patientNatID.getText().toString();
                if(patientDB.patientSearch(id))
                {
                    if(patientDB.deletePatient(id))
                    {
                        Toast.makeText(DeleteActivity.this, "Patient deleted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(DeleteActivity.this, "Deleting failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(DeleteActivity.this, "No such patient id", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void initView()
    {
        delete = findViewById(R.id.deleteBtnItself);
        patientNatID = findViewById(R.id.patientNatIdDelete);
        dbHelper = new DBHelper(this);
        patientDB = new DBHelper.PatientDB(this);
    }
}