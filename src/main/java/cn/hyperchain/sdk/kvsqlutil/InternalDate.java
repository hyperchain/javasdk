package cn.hyperchain.sdk.kvsqlutil;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class InternalDate {

    protected int year = 0;
    protected int month = 0;
    protected int day = 0;

    /**
     * Constructs a zero date.
     */
    public InternalDate() {
    }

    /**
     * create a InternalDate.
     *
     * @param year  year
     * @param month month
     * @param day   day
     */
    public InternalDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * convert InternalDate to Date.
     * @return Date value
     */
    public Date getDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
        cal.setLenient(false);
        cal.set(year, month - 1, day);
        long ms = cal.getTimeInMillis();
        return new Date(ms);
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isZero() {
        return this.year == 0 && this.month == 0 && this.day == 0;
    }
}
