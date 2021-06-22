package io.miladheydari.persiantimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import io.miladheydari.persiantimepicker.api.PersianPickerTime;
import io.miladheydari.persiantimepicker.date.PersianTimeImpl;
import io.miladheydari.persiantimepicker.util.PersianHelper;
import io.miladheydari.persiantimepicker.view.PersianNumberPicker;


class PersianTimePicker extends LinearLayout {

    private PersianPickerTime persianDate;
    private int selectedMinute;
    private int selectedHour;
    private OnTimeChangedListener mListener;
    private final PersianNumberPicker hourNumberPicker;
    private final PersianNumberPicker minuteNumberPicker;


    private boolean displayDescription;
    private final TextView descriptionTextView;
    private Typeface typeFace;
    private int dividerColor;
    private int hourRange;

    public PersianTimePicker(Context context) {
        this(context, null, -1);
    }

    public PersianTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PersianTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // inflate views
        View view = LayoutInflater.from(context).inflate(R.layout.sl_persian_time_picker, this);

        // get views
        hourNumberPicker = view.findViewById(R.id.hourNumberPicker);
        minuteNumberPicker = view.findViewById(R.id.minuteNumberPicker);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);


        hourNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {

                return PersianHelper.toPersianNumber(String.format("%02d", i));
            }
        });

        minuteNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return PersianHelper.toPersianNumber(String.format("%02d", i));
            }
        });


        // init calendar
        persianDate = new PersianTimeImpl();

        // update variables from xml
        updateVariablesFromXml(context, attrs);

        // update view
        updateViewData();
    }

    private void updateVariablesFromXml(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PersianDatePicker, 0, 0);

        /*
         * displayDescription
         */
        displayDescription = a.getBoolean(R.styleable.PersianDatePicker_displayDescription, false);
        selectedHour = a.getInt(R.styleable.PersianDatePicker_selectedHour, persianDate.getHour());
        selectedMinute = a.getInteger(R.styleable.PersianDatePicker_selectedMinute, persianDate.getMinute());


        a.recycle();
    }

    public void setBackgroundColor(@ColorInt int color) {
        hourNumberPicker.setBackgroundColor(color);
        minuteNumberPicker.setBackgroundColor(color);
    }

    public void setBackgroundDrawable(@DrawableRes int drawableBg) {
        hourNumberPicker.setBackgroundResource(drawableBg);
        minuteNumberPicker.setBackgroundResource(drawableBg);
    }



    public void setTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
        updateViewData();
    }

    public void setDividerColor(@ColorInt int color) {
        this.dividerColor = color;
        updateViewData();
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    private void updateViewData() {

        if (typeFace != null) {
            hourNumberPicker.setTypeFace(typeFace);
            minuteNumberPicker.setTypeFace(typeFace);
        }

        if (dividerColor > 0) {
            setDividerColor(hourNumberPicker, dividerColor);
            setDividerColor(minuteNumberPicker, dividerColor);
        }

        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(23);




        hourNumberPicker.setValue(selectedHour);
        hourNumberPicker.setOnValueChangedListener(dateChangeListener);

        minuteNumberPicker.setMinValue(0);
        minuteNumberPicker.setMaxValue(59);


        if (selectedMinute < 0 || selectedMinute > 59) {
            throw new IllegalArgumentException(String.format("Selected minute (%d) must be between 0 and 59", selectedMinute));
        }
        minuteNumberPicker.setValue(selectedMinute);
        minuteNumberPicker.setOnValueChangedListener(dateChangeListener);


//        if (displayDescription) {
//            descriptionTextView.setVisibility(View.VISIBLE);
//            descriptionTextView.setText(persianDate.getPersianLongDate());
//        }
    }

    NumberPicker.OnValueChangeListener dateChangeListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


            persianDate.setTime(hourNumberPicker.getValue(), minuteNumberPicker.getValue());


            if (mListener != null) {
                mListener.onDateChanged(hourNumberPicker.getValue(), minuteNumberPicker.getValue());
            }

        }

    };

    public void setOnDateChangedListener(OnTimeChangedListener onTimeChangedListener) {
        mListener = onTimeChangedListener;
    }

    /**
     * The callback used to indicate the user changed the date.
     * A class that wants to be notified when the date of PersianDatePicker
     * changes should implement this interface and register itself as the
     * listener of date change events using the PersianDataPicker's
     * setOnDateChangedListener method.
     */
    public interface OnTimeChangedListener {

        /**
         * Called upon a date change.
         *
         * @param newHour   The hour that was set.
         * @param newMinute The minute that was set (0-59)
         */
        void onDateChanged(int newHour, int newMinute);
    }

    public String getDisplayTime() {
        return persianDate.getTimeAsString();
    }

    public void setDisplayDate(int hour, int minute) {
        persianDate.setTime(hour, minute);
        setDisplayPersianDate(hour, minute);
    }


    @Deprecated
    public String getDisplayPersianDate() {
        return persianDate.getTimeAsString();
    }
    public int getDisplayPersianHour() {
        return persianDate.getHour();
    }

    public int getDisplayPersianMinute() {
        return persianDate.getMinute();
    }


    public PersianPickerTime getPersianDate() {
        return persianDate;
    }

    /**
     * @Deprecated Use setDisplayPersianDate(PersianPickerDate displayPersianDate) instead
     */
    @Deprecated
    public void setDisplayPersianDate(int hour, int minute) {
        PersianPickerTime persianPickerTime = new PersianTimeImpl();
        persianPickerTime.setTime(hour, minute);
        setDisplayPersianDate(persianPickerTime);
    }

    public void setDisplayPersianDate(PersianPickerTime displayPersianDate) {

        persianDate.setTime(displayPersianDate.getHour(), displayPersianDate.getMinute());

        final int hour = persianDate.getHour();
        final int minute = persianDate.getMinute();

        selectedHour = hour;
        selectedMinute = minute;



        hourNumberPicker.post(new Runnable() {
            @Override
            public void run() {
                hourNumberPicker.setValue(hour);
            }
        });
        minuteNumberPicker.post(new Runnable() {
            @Override
            public void run() {
                minuteNumberPicker.setValue(minute);

            }
        });

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        // end

        ss.time = this.getDisplayTime();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        // end

        setDisplayDate(Integer.parseInt(ss.time.split(":")[0]), Integer.parseInt(ss.time.split(":")[1]));
    }

    static class SavedState extends BaseSavedState {
        String time;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.time = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.time);
        }

        // required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
