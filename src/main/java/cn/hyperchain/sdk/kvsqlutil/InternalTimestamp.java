package cn.hyperchain.sdk.kvsqlutil;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class InternalTimestamp extends InternalDate {

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int nanos = 0;
    private int scale = 0;

    /**
     * Constructs a zero datetime.
     */
    public InternalTimestamp() {
        super();
    }

    /**
     * create a InternalTimestamp.
     *
     * @param year    year
     * @param month   month
     * @param day     day
     * @param hours   hours
     * @param minutes minutes
     * @param seconds seconds
     * @param nanos   nanos
     * @param scale   scale
     */
    public InternalTimestamp(int year, int month, int day, int hours, int minutes, int seconds, int nanos, int scale) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.nanos = nanos;
        this.scale = scale;
    }

    /**
     * convert InternalTimestamp to Timestamp.
     * @return Timestamp value
     */
    public Timestamp getTimestamp() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
        cal.setLenient(false);
        cal.set(year, month - 1, day, hours, minutes, seconds);
        Timestamp ts = new Timestamp(cal.getTimeInMillis());
        ts.setNanos(nanos);
        return ts;
    }

    public int getHours() {
        return this.hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getNanos() {
        return this.nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }

    @Override
    public boolean isZero() {
        return super.isZero() && this.hours == 0 && this.minutes == 0 && this.seconds == 0 && this.nanos == 0;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
