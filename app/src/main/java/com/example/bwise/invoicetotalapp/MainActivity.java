package com.example.bwise.invoicetotalapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends Activity
        implements TextView.OnEditorActionListener {
        //declare instance variables for the widgets
    private EditText inputeditText;
    private TextView percenttextView;
    private TextView discounttextView;
    private TextView totaltextView;

    private String subtotalString;

    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to the widgets from the R class(resources)
        inputeditText = findViewById(R.id.inputeditText);
        percenttextView = findViewById(R.id.percenttextView);
        discounttextView = findViewById(R.id.discounttextView);
        totaltextView = findViewById(R.id.totaltextView);
        //listener
        inputeditText.setOnEditorActionListener(this);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause(){
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume(){
        subtotalString = savedValues.getString("subtotalString", "");
        inputeditText.setText(subtotalString);
        calculateAndDisplay();

        super.onResume();

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        calculateAndDisplay();
        return false;
    }

    private void calculateAndDisplay(){
        //get subtotal
        subtotalString = inputeditText.getText().toString();
        float subtotal;
        if(subtotalString.equals("")){
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalString);
        }


    //get discount percent
    float discountPercent;

    if(subtotal >=200){
        discountPercent = .2f;
    } else if (subtotal>=100){
        discountPercent = .1f;
    } else {
        discountPercent = 0;
    }

    //calculate discount
    float discountAmount = subtotal * discountPercent;
    float total =  subtotal - discountAmount;

    //display data to layout
    NumberFormat percent = NumberFormat.getPercentInstance();
    percenttextView.setText(percent.format(discountPercent));

    NumberFormat currency = NumberFormat.getCurrencyInstance();
    discounttextView.setText(currency.format(discountAmount));

    totaltextView.setText(currency.format(total));

    }//end calculateAndDisplay method
}