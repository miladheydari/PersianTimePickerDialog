package io.miladheydari.persiantimepicker.date;

import io.miladheydari.persiantimepicker.api.PersianPickerTime;

public class PersianTimeImpl implements PersianPickerTime {

    int hour = 0;
    int minute = 0;

    public PersianTimeImpl() {
    }

    @Override
    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

    }

    @Override
    public int getHour() {
        return hour;
    }

    @Override
    public int getMinute() {
        return minute;
    }

    @Override
    public String getTimeAsString() {
        return hour + ":" + minute;
    }

}
