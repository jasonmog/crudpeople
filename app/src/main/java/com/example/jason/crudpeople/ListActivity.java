package com.example.jason.crudpeople;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    List<Person> people;
    static final int NEW_REQUEST = 0, EDIT_REQUEST = 1;
    protected ListView listView;
    protected int editIndex;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Activity activity = this;

        ((FloatingActionButton)findViewById(R.id.add_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                startActivityForResult(new Intent(Intent.ACTION_INSERT_OR_EDIT, Uri.parse(getString(R.string.scheme) + "://person")), NEW_REQUEST);
            }
        });

        populatePeople();

        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount () {
                return people.size();
            }

            @Override
            public Object getItem (int i) {
                return people.get(i);
            }

            @Override
            public long getItemId (int i) {
                return 0;
            }

            @Override
            public View getView (int i, View view, ViewGroup viewGroup) {
                Boolean bindListener;

                if (view == null)
                    bindListener = true;
                else
                    bindListener = false;

                final View result = ListItem.getView(activity, view, (Person)getItem(i));
                result.setTag(i);

                if (bindListener) {
                    result.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick (View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                            final View v = view;

                            builder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialogInterface, int i) {
                                    editIndex = (int)result.getTag();

                                    Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT, Uri.parse("crudpeople://person"));
                                    intent.putExtra(FormActivity.PERSON_EXTRA, (Serializable)getItem((int)v.getTag()));

                                    startActivityForResult(intent, EDIT_REQUEST);
                                }
                            });

                            builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    people.remove((int)result.getTag());

                                    ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();

                                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.person_deleted), Snackbar.LENGTH_SHORT).show();
                                }
                            });

                            builder.create().show();

                            return false;
                        }
                    });
                }

                return result;
            }
        });
    }

    protected void populatePeople ()  {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);

        String str = prefs.getString(getString(R.string.people_key), null);

        if (str == null) {
            people = new ArrayList<Person>();
        } else {
            ByteArrayInputStream sr = new ByteArrayInputStream(Base64.decode(str, 0));

            try {
                ObjectInputStream ois = new ObjectInputStream(sr);

                people = (List<Person>)ois.readObject();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Person person = (Person)data.getSerializableExtra(FormActivity.PERSON_EXTRA);
            int messageId;

            if (requestCode == NEW_REQUEST) {
                people.add(person);

                messageId = R.string.person_added;
            } else if (requestCode == EDIT_REQUEST) {
                people.set(editIndex, person);

                messageId = R.string.person_edited;
            } else {
                return;
            }c

            save();

            ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();

            Snackbar.make(findViewById(android.R.id.content), getString(messageId), Snackbar.LENGTH_SHORT).show();
        }
    }

    protected void save () {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(buffer);
            os.writeObject(people);
            os.close();

            SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String encoded = Base64.encodeToString(buffer.toByteArray(), 0);
            editor.putString(getString(R.string.people_key), encoded);
            editor.commit();
        } catch (Exception e){
        }
    }
}