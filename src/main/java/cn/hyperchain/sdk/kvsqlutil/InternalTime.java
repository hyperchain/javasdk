package cn.hyperchain.sdk.kvsqlutil;

import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class InternalTime {

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int nanos = 0;
    private int scale = 0;

    /**
     * Constructs a zero time.
     */
    public InternalTime() {
    }

    /**
     * create a InternalTime.
     *
     * @param hours   hours
     * @param minutes minutes
     * @param seconds seconds
     * @param nanos   nanos
     * @param scale   scale
     */
    public InternalTime(int hours, int minutes, int seconds, int nanos, int scale) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.scale = scale;
        this.nanos = nanos;
    }

    /**
     * convert InternalTime to Time.
     * @return Time value
     */
    public Time getTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
        cal.setLenient(false);
        cal.set(1970, 0, 1, hours, minutes, seconds);
        cal.set(Calendar.MILLISECOND, 0);
        long ms = (nanos / 1000000) + cal.getTimeInMillis();
        return new Time(ms);
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

    public boolean isZero() {
        return this.hours == 0 && this.minutes == 0 && this.seconds == 0 && this.nanos == 0;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
