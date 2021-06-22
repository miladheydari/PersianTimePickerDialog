package io.miladheydari.persiantimepicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import io.miladheydari.persiantimepicker.api.PersianPickerTime;
import io.miladheydari.persiantimepicker.api.PersianPickerListener;
import io.miladheydari.persiantimepicker.date.PersianTimeImpl;

/**
 * Created by aliabdolahi on 1/23/17.
 */

public class PersianTimePickerDialog {

    public static final int NO_TITLE = 0;
    public static final int DAY_MONTH_YEAR = 1;
    public static final int WEEKDAY_DAY_MONTH_YEAR = 2;

    private Context context;
    private String positiveButtonString = "تایید";
    private String negativeButtonString = "انصراف";
    private PersianPickerListener persianPickerListener;
    private PersianPickerTime initTime = new PersianTimeImpl();
    public static Typeface typeFace;
    private int actionColor = Color.GRAY;
    private int actionTextSize = 12;
    private int negativeTextSize = 12;
    private int backgroundColor = Color.WHITE;
    private int titleColor = Color.parseColor("#111111");
    private boolean cancelable = true;
    private int pickerBackgroundColor;
    private int pickerBackgroundDrawable;
    private int titleType = NO_TITLE;
    private boolean showInBottomSheet;


    public PersianTimePickerDialog(Context context) {
        this.context = context;
    }


    public PersianTimePickerDialog setListener(PersianPickerListener listener) {
        this.persianPickerListener = listener;
        return this;
    }




    public PersianTimePickerDialog setTypeFace(Typeface typeFace) {
        PersianTimePickerDialog.typeFace = typeFace;
        return this;
    }



    public PersianTimePickerDialog setInitTime(int hour, int minute) {
        this.initTime.setTime(hour,minute);
        return this;
    }





    public PersianTimePickerDialog setPositiveButtonString(String positiveButtonString) {
        this.positiveButtonString = positiveButtonString;
        return this;
    }

    public PersianTimePickerDialog setPositiveButtonResource(@StringRes int positiveButton) {
        this.positiveButtonString = context.getString(positiveButton);
        return this;
    }



    public PersianTimePickerDialog setNegativeButton(String negativeButton) {
        this.negativeButtonString = negativeButton;
        return this;
    }

    public PersianTimePickerDialog setNegativeButtonResource(@StringRes int negativeButton) {
        this.negativeButtonString = context.getString(negativeButton);
        return this;
    }

    public PersianTimePickerDialog setNegativeTextSize(int sizeInt) {
        this.negativeTextSize = sizeInt;
        return this;
    }

    public PersianTimePickerDialog setActionTextColor(@ColorInt int colorInt) {
        this.actionColor = colorInt;
        return this;
    }


    public PersianTimePickerDialog setActionTextColorResource(@ColorRes int colorInt) {
        this.actionColor = ContextCompat.getColor(context, colorInt);
        return this;
    }

    public PersianTimePickerDialog setActionTextSize(int sizeInt) {
        this.actionTextSize = sizeInt;
        return this;
    }

    public PersianTimePickerDialog setAllButtonsTextSize(int sizeInt) {
        this.actionTextSize = sizeInt;
        this.negativeTextSize = sizeInt;
        return this;
    }

    public PersianTimePickerDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public PersianTimePickerDialog setBackgroundColor(@ColorInt int bgColor) {
        this.backgroundColor = bgColor;
        return this;
    }

    public PersianTimePickerDialog setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public PersianTimePickerDialog setPickerBackgroundColor(@ColorInt int color) {
        this.pickerBackgroundColor = color;
        return this;
    }

    public PersianTimePickerDialog setPickerBackgroundDrawable(@DrawableRes int drawableBg) {
        this.pickerBackgroundDrawable = drawableBg;
        return this;
    }

    public PersianTimePickerDialog setTitleType(int titleType) {
        this.titleType = titleType;
        return this;
    }

    public PersianTimePickerDialog setShowInBottomSheet(boolean b) {
        this.showInBottomSheet = b;
        return this;
    }

    public void show() {

        View v = View.inflate(context, R.layout.dialog_time_picker, null);
        final PersianTimePicker datePickerView = v.findViewById(R.id.timePicker);
        final TextView dateText = v.findViewById(R.id.dateText);
        final AppCompatButton positiveButton = v.findViewById(R.id.positive_button);
        final AppCompatButton negativeButton = v.findViewById(R.id.negative_button);
        final LinearLayout container = v.findViewById(R.id.container);

        container.setBackgroundColor(backgroundColor);
        dateText.setTextColor(titleColor);


        if (pickerBackgroundColor != 0) {
            datePickerView.setBackgroundColor(pickerBackgroundColor);
        } else if (pickerBackgroundDrawable != 0) {
            datePickerView.setBackgroundDrawable(pickerBackgroundDrawable);
        }



        if (initTime != null) {

                datePickerView.setDisplayPersianDate(initTime);


        }

        if (typeFace != null) {
            dateText.setTypeface(typeFace);
            positiveButton.setTypeface(typeFace);
            negativeButton.setTypeface(typeFace);
            datePickerView.setTypeFace(typeFace);
        }

        positiveButton.setTextSize(actionTextSize);
        negativeButton.setTextSize(negativeTextSize);

        positiveButton.setTextColor(actionColor);
        negativeButton.setTextColor(actionColor);

        positiveButton.setText(positiveButtonString);
        negativeButton.setText(negativeButtonString);

        updateView(dateText, datePickerView.getPersianDate());

        datePickerView.setOnDateChangedListener(new PersianTimePicker.OnTimeChangedListener() {
            @Override
            public void onDateChanged(int newHour, int newMinute) {
                updateView(dateText, datePickerView.getPersianDate());
            }
        });


        final AppCompatDialog dialog;
        if (showInBottomSheet) {
            dialog = new BottomSheetDialog(context);
            dialog.setContentView(v);
            dialog.setCancelable(cancelable);
        } else {
            dialog = new AlertDialog.Builder(context)
                    .setView(v)
                    .setCancelable(cancelable)
                    .create();
        }

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (persianPickerListener != null) {
                    persianPickerListener.onDismissed();
                }
                dialog.dismiss();
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (persianPickerListener != null) {
                    persianPickerListener.onDateSelected(datePickerView.getPersianDate());
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void updateView(TextView dateText, PersianPickerTime persianDate) {
        String date;
        switch (titleType) {
            case NO_TITLE:
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dateText.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 30);
                dateText.setLayoutParams(layoutParams);
                break;
//            case DAY_MONTH_YEAR:
//                date = persianDate.getPersianDay() + " " +
//                        persianDate.getPersianMonthName() + " " +
//                        persianDate.getHour();
//
//                dateText.setText(PersianHelper.toPersianNumber(date));
//                break;
//            case WEEKDAY_DAY_MONTH_YEAR:
//                date = persianDate.getPersianDayOfWeekName() + " " +
//                        persianDate.getPersianDay() + " " +
//                        persianDate.getPersianMonthName() + " " +
//                        persianDate.getHour();
//
//                dateText.setText(PersianHelper.toPersianNumber(date));
//                break;
            default:
                Log.d("PersianDatePickerDialog", "never should be here");
                break;
        }

    }


}
