package io.miladheydari;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Date;

import io.miladheydari.persiantimepicker.PersianTimePickerDialog;
import io.miladheydari.persiantimepicker.api.PersianPickerTime;
import io.miladheydari.persiantimepicker.api.PersianPickerListener;


public class MainActivity extends AppCompatActivity {

    private PersianTimePickerDialog picker;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void showCalendar(View v) {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Shabnam-Light-FD.ttf");

        picker = new PersianTimePickerDialog(this)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setAllButtonsTextSize(12)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(typeface)
                .setTitleType(PersianTimePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new PersianPickerListener() {
                    @Override
                    public void onDateSelected(PersianPickerTime persianPickerTime) {
                        Log.d(TAG, "onDateSelected: " + persianPickerTime.getTimeAsString());
                        Toast.makeText(MainActivity.this, persianPickerTime.getHour() + ":" + persianPickerTime.getMinute() + "/" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDismissed() {
                        Toast.makeText(MainActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
                    }
                });

        picker.show();
    }

    public void showCalendarInDarkMode(View v) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Shabnam-Light-FD.ttf");

        Date initDate = new Date();

        picker = new PersianTimePickerDialog(this)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setInitTime(initDate.getHours(),initDate.getMinutes())
                .setTypeFace(typeface)
                .setBackgroundColor(Color.BLACK)
                .setTitleColor(Color.WHITE)
                .setActionTextColor(Color.RED)
                .setPickerBackgroundDrawable(R.drawable.darkmode_bg)
                .setTitleType(PersianTimePickerDialog.DAY_MONTH_YEAR)
                .setCancelable(false)
                .setListener(new PersianPickerListener() {

                    @Override
                    public void onDateSelected(PersianPickerTime persianPickerTime) {
                        Toast.makeText(MainActivity.this, persianPickerTime.getTimeAsString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDismissed() {

                    }
                });
        picker.show();
    }

}
