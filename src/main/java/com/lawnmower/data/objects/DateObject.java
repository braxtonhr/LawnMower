package com.lawnmower.data.objects;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.lawnmower.util.R;
import org.json.JSONException;
import org.json.JSONObject;


public class DateObject implements Comparable {

    private GregorianCalendar date = new GregorianCalendar();

    public DateObject() {}

    public static DateObject now() {
        return new DateObject();
    }

    private DateObject(GregorianCalendar cal) {

        this();
        this.setDate(cal);

    }

    public DateObject(int year, int month, int date) {
        this();
        this.date.set(Calendar.YEAR, year);
        this.date.set(Calendar.MONTH, month-1);
        this.date.set(Calendar.DATE, date);
    }

    public DateObject(LocalDate date) {
        this();
        System.out.println(this.date);
        System.out.println(date);
        this.date.set(Calendar.YEAR, date.getYear());
        this.date.set(Calendar.MONTH, date.getMonthValue());
        this.date.set(Calendar.DATE, date.getDayOfMonth());
    }

    public DateObject(JSONObject date) {
        this();
        try {
            this.date.set(Calendar.YEAR, date.getInt(R.YEAR));
            this.date.set(Calendar.MONTH, date.getInt(R.MONTH));
            this.date.set(Calendar.DATE, date.getInt(R.DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject date = new JSONObject();
        try {
            date.put(R.YEAR, this.date.get(Calendar.YEAR));
            date.put(R.MONTH, this.date.get(Calendar.MONTH));
            date.put(R.DATE, this.date.get(Calendar.DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date;
    }

    public DateObject getNextMonth() {

        GregorianCalendar next = dateCopy();
        next.add(Calendar.MONTH, 1);
        next.set(Calendar.DATE, 1);

        return (new DateObject(next));

    }

    public DateObject getPrevMonth() {

        GregorianCalendar last = dateCopy();
        last.add(Calendar.MONTH, -1);
        last.set(Calendar.DATE, 1);

        return (new DateObject(last));

    }

    public DateObject getMonthBegin() {

        GregorianCalendar begin = dateCopy();
        begin.set(Calendar.DATE, 1);

        return (new DateObject(begin));

    }

    public DateObject getMonthEnd() {

        GregorianCalendar end = dateCopy();
        end.add(Calendar.MONTH, 1);
        end.add(Calendar.DATE, -1);

        return new DateObject(end);

    }

    public DateObject getNextDay() {

        GregorianCalendar next = dateCopy();
        next.add(Calendar.DATE, 1);

        return new DateObject(next);

    }

    public DateObject getNextWeek() {

        GregorianCalendar next = dateCopy();
        next.add(Calendar.WEEK_OF_YEAR, 1);

        return new DateObject(next);

    }

    public DateObject getThisMonday() {

        GregorianCalendar monday = dateCopy();
        monday.set(Calendar.DAY_OF_WEEK, 2);

        return new DateObject(monday);

    }

    public boolean isMonday() {

        return date.get(Calendar.DAY_OF_WEEK) == 2;

    }

    public boolean isMonthFirst() {

        return date.get(Calendar.DAY_OF_MONTH) == 1;

    }

    public boolean monthEquals(DateObject date) {

        GregorianCalendar compare = date.getDate();
        return this.date.get(Calendar.MONTH) == compare.get(Calendar.MONTH);

    }

    public boolean isWeekOf(DateObject date) {

        return this.date.get(Calendar.WEEK_OF_YEAR) == date.getDate().get(Calendar.WEEK_OF_YEAR);

    }

    public boolean isThisWeek() {

        return isWeekOf(now());

    }

    public int difference(DateObject date) {

        int date1 = this.toJulianTime();
        int date2 = date.toJulianTime();
        return Math.abs(date1 - date2);

    }

    public int weekDayDifference(DateObject date) {
        int date1 = this.getDate().get(Calendar.DAY_OF_WEEK);
        int date2 = date.getDate().get(Calendar.DAY_OF_WEEK);
        return Math.abs(date1 - date2);
    }

    public void addDays(int days) {

        this.date.add(Calendar.DATE, days);

    }

    public void addWeeks(int weeks) {

        this.date.add(Calendar.WEEK_OF_YEAR, weeks);

    }

    public DateObject copyOf() {

        return new DateObject(this.year(), this.month(), this.date());

    }

    public boolean isHoliday() {
        // TODO: implement method
        return false;
    }

    public boolean isWeekend() {
        int day = this.dayOfWeek();
        return day == 1 || day == 7;
    }

    public String toString(int style) {
        String date = "";

        if (style != Calendar.LONG && style != Calendar.SHORT) {
            style = Calendar.LONG;
        }

        date = this.date.getDisplayName(Calendar.MONTH, style, Locale.US);
        date += " " + this.date.getDisplayName(Calendar.DATE, style, Locale.US);
        date += ", " + this.date.getDisplayName(Calendar.YEAR, style, Locale.US);

        return date;
    }

    @Override
    public String toString() {
        String date = "";

        int month = (this.date.get(Calendar.MONTH)+1)%13;
        int day = this.date.get(Calendar.DATE);
        String dayString = "";

        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = day + "";
        }

        int year = this.date.get(Calendar.YEAR);

        date = month + "/" + dayString + "/" + year;

        return date;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public void setYear(int year) {
        this.date.set(Calendar.YEAR, year);
    }

    public void setMonth(int month) {
        this.date.set(Calendar.MONTH, month);
    }

    public void setDate(int date) {
        this.date.set(Calendar.DATE, date);
    }

    public boolean equals(DateObject date) {
        return this.compareTo(date) == 0;
    }

    public boolean isLessThan(DateObject date) {
        return this.compareTo(date) == -1;
    }

    public boolean isGreaterThan(DateObject date) {
        return this.compareTo(date) == 1;
    }

    public boolean isLessThanEqualTo(DateObject date) {
        return this.isLessThan(date) || this.equals(date);
    }

    public boolean isGreaterThanEqualTo(DateObject date) {
        return this.isGreaterThan(date) || this.equals(date);
    }

    private GregorianCalendar dateCopy() {

        GregorianCalendar copy = new GregorianCalendar();

        copy.set(Calendar.YEAR, this.date.get(Calendar.YEAR));
        copy.set(Calendar.MONTH, this.date.get(Calendar.MONTH));
        copy.set(Calendar.DATE, this.date.get(Calendar.DATE));

        return copy;

    }

    public int year() {
        return this.date.get(Calendar.YEAR);
    }

    public int month() {
        return this.date.get(Calendar.MONTH) + 1;
    }

    public int date() {
        return this.date.get(Calendar.DATE);
    }

    public int dayOfWeek() {
        return this.date.get(Calendar.DAY_OF_WEEK);
    }

    private int toJulianTime() {

        int y = this.year();
        int m = this.month();
        int d = this.date();

        return 367*y - (7*(y + ((m + 9)/12))/4) + ((275*m)/9) + d + 1721014;

    }

    public int compareTo(Object o) {

        int result = 0;

        DateObject compare = (DateObject) o;

        int year = this.date.get(Calendar.YEAR);
        int month = this.date.get(Calendar.MONTH);
        int day = this.date.get(Calendar.DATE);

        int compareYear = compare.getDate().get(Calendar.YEAR);
        int compareMonth = compare.getDate().get(Calendar.MONTH);
        int compareDay = compare.getDate().get(Calendar.DATE);

        if (year < compareYear) result = -1;
        else if (year > compareYear) result = 1;
        else
        if (month < compareMonth) result = -1;
        else if (month > compareMonth) result = 1;
        else
        if (day < compareDay) result = -1;
        else if (day > compareDay) result = 1;
        else result = 0;

        return result;
    }



}
