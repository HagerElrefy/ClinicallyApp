package com.mustafa.r.hegazi.trying;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class ActionTakeActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    AddPersonFragment addPersonFragment;
    BottomNavigationView navigationView;
    EditText patientName, phone, nationalId, consultation, date;
    RadioButton genderMale, genderFemale;
    Button save, reset;
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
        setContentView(R.layout.activity_action_take);

        switchCompat = findViewById(R.id.bt_switch);



        initViews();

        homeFragment = new HomeFragment();
        addPersonFragment = new AddPersonFragment();
        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment.class, null)
                .commit();
        navigationView = findViewById(R.id.bottomNav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view, homeFragment, null)
                                .commit();
                        return (true);
                    }

                    case R.id.sideMenu: {
                        startActivity(new Intent(ActionTakeActivity.this,MenuActivity.class));
                        return (true);
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void initViews() {
        dbHelper = new DBHelper(this);
        patientDB = new DBHelper.PatientDB(this);
        patientName = findViewById(R.id.patientName);
        phone = findViewById(R.id.patientPhone);
        nationalId = findViewById(R.id.patientNatId);
        consultation = findViewById(R.id.patientSuffering);
        date = findViewById(R.id.patientConsultationDate);
        genderMale = findViewById(R.id.maleRadBtn);
        genderFemale = findViewById(R.id.femaleRadBtn);
        save = findViewById(R.id.saveNewPatient);
        reset = findViewById(R.id.reset);
    }


    public void addBtnClick(View v) {
        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, addPersonFragment, null)
                .commit();
    }

    public void showBtnClick(View v) {

        startActivity(new Intent(ActionTakeActivity.this,ShowDataActivity.class));
    }

    public void deleteBtnClick(View v) {
        startActivity(new Intent(ActionTakeActivity.this, DeleteActivity.class));
    }

    public void resetClick(View v) {
        initViews();
        patientName.setText("");
        phone.setText("");
        nationalId.setText("");
        consultation.setText("");
        date.setText("");
        genderMale.setChecked(false);
        genderFemale.setChecked(false);
    }

    public void saveClick(View v) {
        initViews();
        String _name = patientName.getText().toString();
        String _phone = phone.getText().toString();
        String _natId = nationalId.getText().toString();
        String _consultation = consultation.getText().toString();
        String _date = date.getText().toString();
        String _gender = "";
        if (genderMale.isChecked()) {
            _gender = "Male";
        } else if (genderFemale.isChecked()) {
            _gender = "Female";
        } else {
            Toast.makeText(ActionTakeActivity.this, "Select gender", Toast.LENGTH_SHORT).show();
        }
        if (_gender.equals("")) {
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
        } else {
            if (patientDB.patientSearch(_natId)) {
                Toast.makeText(this, "Patient already exist, so his data will be updated", Toast.LENGTH_SHORT).show();
                if(patientDB.updatePatient(_name,_phone,_natId,_consultation,_date,_gender))
                {
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "An error occurred while updating data", Toast.LENGTH_SHORT).show();
                }
            } else {

                if (patientDB.insertPatient(_name, _phone, _natId, _consultation, _date, _gender)) {
                    Toast.makeText(ActionTakeActivity.this, "New patient has been added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActionTakeActivity.this, "Invalid insertion", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dateClick(View v)
    {
                Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int mounth = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(ActionTakeActivity.this, new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                date.setText(i + "/" + i1 +"/"+i2);
                            }
                        },year,mounth,day);
                dp.setTitle("select date");
                dp.show();
            }
        });
    }

}