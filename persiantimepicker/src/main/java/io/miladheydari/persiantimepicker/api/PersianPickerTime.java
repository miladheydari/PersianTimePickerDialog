package io.miladheydari.persiantimepicker.api;

public interface PersianPickerTime {


    void setTime(int hour, int minute);

    int getHour();

    int getMinute();

    String getTimeAsString();
}
