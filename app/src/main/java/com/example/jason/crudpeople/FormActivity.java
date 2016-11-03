package com.example.jason.crudpeople;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by jason on 11/2/2016.
 */

public class FormActivity extends Activity {
    protected EditText firstText, lastText, phoneText, zipText;
    protected DatePicker datePicker;
    public static final String PERSON_EXTRA = "person";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        ((FloatingActionButton)findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        firstText = (EditText)findViewById(R.id.firstText);
        lastText = (EditText)findViewById(R.id.lastText);
        phoneText = (EditText)findViewById(R.id.phoneText);
        zipText = (EditText)findViewById(R.id.zipText);
        datePicker = (DatePicker)findViewById(R.id.datePicker);

        Object obj = getIntent().getSerializableExtra(PERSON_EXTRA);

        if (obj != null) {
            Person person = (Person)obj;

            firstText.setText(person.firstName);
            lastText.setText(person.lastName);
            zipText.setText(person.zipCode);
            phoneText.setText(person.phoneNumber);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(person.dateOfBirth);
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    public static java.util.Date getDateFromDatePicker (DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    protected boolean validate () {
        List<String> errors = new ArrayList<String>();

        if (firstText.getText() == null || firstText.getText().length() == 0)
            errors.add("First name cannot be blank.");

        if (lastText.getText() == null || lastText.getText().length() == 0)
            errors.add("Last name cannot be blank.");

        if (zipText.getText() == null || zipText.getText().length() != 5)
            errors.add("Zip code is invalid.");

        if (phoneText.getText() == null || !PhoneNumberUtils.isGlobalPhoneNumber(phoneText.getText().toString()))
            errors.add("Phone number is invalid.");

        if (getDateFromDatePicker(datePicker).compareTo(new Date()) >= 0)
            errors.add("Date of birth must be in the past.");

        if (errors.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Invalid");
            builder.setMessage(TextUtils.join("\n", errors));

            builder.create().show();

            return false;
        }

        return true;
    }

    protected void save () {
        if (!validate())
            return;

        Person person = new Person();
        person.firstName = firstText.getText().toString();
        person.lastName = lastText.getText().toString();
        person.zipCode = zipText.getText().toString();
        person.phoneNumber = phoneText.getText().toString();
        person.dateOfBirth = getDateFromDatePicker(datePicker);

        Intent intent = new Intent();
        intent.putExtra(PERSON_EXTRA, person);
        setResult(RESULT_OK, intent);

        finish();
    }
}