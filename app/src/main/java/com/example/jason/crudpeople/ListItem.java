package com.example.jason.crudpeople;

import android.app.Activity;
import android.icu.text.DateFormat;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by jason on 11/2/2016.
 */

public class ListItem {
    public static View getView (Activity activity, View view, Person person) {
        if (view == null)
            view = activity.getLayoutInflater().inflate(R.layout.list_item, null);

        ((TextView)view.findViewById(R.id.nameLabel)).setText(activity.getString(R.string.name) + " " + person.firstName + " " + person.lastName);
        ((TextView)view.findViewById(R.id.phoneLabel)).setText(activity.getString(R.string.phone_number) + " " + person.phoneNumber);
        ((TextView)view.findViewById(R.id.zipLabel)).setText(activity.getString(R.string.zip_code) + " " + person.zipCode);
        ((TextView)view.findViewById(R.id.birthLabel)).setText(activity.getString(R.string.date_of_birth) + " " + SimpleDateFormat.getDateInstance().format(person.dateOfBirth));

        return view;
    }
}