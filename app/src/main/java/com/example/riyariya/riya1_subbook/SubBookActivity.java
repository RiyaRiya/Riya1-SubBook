package com.example.riyariya.riya1_subbook;

/**
 * Copyright (c) 2018. By Riya for CMPUT 301 as Assignment 1
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/** Represents the Subscriptions Book activity, with list and add functionality
 * @author riya1
 * @version 1.0
 */
public class SubBookActivity extends Activity implements DatePickerDialog.OnDateSetListener{

    private static final String FILENAME = "file.sav";

    private ArrayList<Subscription> subscriptionList;
    private SubscriptionsAdapter adapter;
    private EditText nameText;
    private TextView dateStartedText;
    private EditText commentText;
    private Button dateStartedButton;
    private Button saveButton;
    private EditText monthlyChargeText;
    private Date dateStarted;
    private RecyclerView rvSubscriptions;

    //Taken from https://guides.codepath.com/android/using-dialogfragment on Feb 3, 2018
    /**
     * Shows the DatePickerFragment so the date can be selected
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //Taken from https://guides.codepath.com/android/using-dialogfragment on Feb 3, 2018
    /**
     * Handles the date selected from the fragment
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateStarted = c.getTime();
        SimpleDateFormat formatted_date = new SimpleDateFormat("yyyy-MM-dd");
        dateStartedText.setText(formatted_date.format(dateStarted));
    }

    /**
     * Attaches listeners to the save and select date buttons.
     * It also validates the required fields through Toasts.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LifeCycle ---->", "onCreate is called");
        setContentView(R.layout.main);

        nameText = (EditText) findViewById(R.id.name);
        commentText = (EditText) findViewById(R.id.comment);
        dateStartedButton = (Button) findViewById(R.id.dateStarted);
        monthlyChargeText = (EditText) findViewById(R.id.monthlyCharge);
        dateStartedText = (TextView) findViewById(R.id.dateStartedText);

        saveButton = (Button) findViewById(R.id.save);

        dateStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            setResult(RESULT_OK);
            String name = nameText.getText().toString();
            String comment = commentText.getText().toString();
            // Check if Required fields are entered
            if (name.isEmpty() || monthlyChargeText.getText().toString().isEmpty() || dateStarted == null){
                CharSequence text = "Missing Required Fields";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            // Save all the fields
            else{
                BigDecimal monthlyCharge = new BigDecimal(monthlyChargeText.getText().toString());
                Subscription subscription = new Subscription(name, dateStarted, monthlyCharge, comment);
                Boolean isEditing = false ;
                int i;
                //Finding if the action was edit or add
                for (i = 0; i < subscriptionList.size(); i ++) {
                    System.out.println(i);
                    if (subscriptionList.get(i).getName().equals(subscription.getName())){
                        System.out.println("found the element");
                        isEditing = true;
                        break;
                    }
                }
                //On edit, modify the subscription
                if (isEditing){
                    subscriptionList.set(i,subscription);
                }
                //On add, append to the subscriptionsList
                else{
                    subscriptionList.add(subscription);
                }
                adapter.notifyDataSetChanged();
                saveInFile();
                CharSequence text = "Saving Subscription";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Clear all the views
                nameText.getText().clear();
                commentText.getText().clear();
                monthlyChargeText.getText().clear();
                dateStartedText.setText("");
            }
            }
        });
    }

    /**
     * Attaches the adapter
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LifeCycle --->", "onStart is called");

        loadFromFile();
        adapter = new SubscriptionsAdapter(this, subscriptionList);
        rvSubscriptions = (RecyclerView) findViewById(R.id.rvSubscriptions);
        rvSubscriptions.setAdapter(adapter);
        rvSubscriptions.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Loads the subscriptions from the file for use when the application starts
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2018-02-03

            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            subscriptionList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            subscriptionList =  new ArrayList<Subscription>();
        }
    }

    /**
     * View button functionality uses the add functionality EditTexts at the bottom of screen
     * Then these fields are autofilled to show details, where they can be edited and saved.
     * @param subscription
     */
    public void setViewElement(Subscription subscription){
        nameText.setText(subscription.getName());
        commentText.setText(subscription.getComment());
        monthlyChargeText.setText(subscription.getMonthlyCharge().toString());
        SimpleDateFormat formatted_date = new SimpleDateFormat("yyyy-MM-dd");
        dateStarted = subscription.getDateStarted();
        dateStartedText.setText(formatted_date.format(subscription.getDateStarted()));
    }

    /**
     * Saves the subscriptions in the file
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(subscriptionList, out);
            out.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Destructor
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy is called");
    }
}
