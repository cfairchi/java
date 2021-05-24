package com.csf.java.utils;

public class TimeSpan {
    private int m_Days;
    private int m_Hours;
    private int m_Minutes;
    private int m_Seconds;

    public int Days() {
        return this.m_Days;
    }

    public void SetDays(int d) {
        this.m_Days = d;
    }

    public int Hours() {
        return this.m_Hours;
    }

    public void SetHours(int h) {
        this.m_Hours = h;
    }

    public int Minutes() {
        return this.m_Minutes;
    }

    public void SetMinutes(int m) {
        this.m_Minutes = m;
    }

    public int Seconds() {
        return this.m_Seconds;
    }

    public void SetSeconds(int s) {
        this.m_Seconds = s;
    }

    public TimeSpan(int d, int h, int m, int s) {
        this.m_Days = d;
        this.m_Hours = h;
        this.m_Minutes = m;
        this.m_Seconds = s;
    }

    public TimeSpan(int h, int m, int s) {
        this.m_Days = 0;
        this.m_Hours = h;
        this.m_Minutes = m;
        this.m_Seconds = s;
    }

    public Boolean Equals(TimeSpan ts) {
        return this.Days() == ts.Days() && this.Hours() == ts.Hours() && this.Minutes() == ts.Minutes() && this.Seconds() == ts.Seconds();
    }

    public TimeSpan Add(TimeSpan ts) {
        int s = this.Seconds() + ts.Seconds();
        int m = this.Minutes() + ts.Minutes();
        int h = this.Hours() + ts.Hours();
        int d = this.Days() + ts.Days();

        if (s > 59) {
            s -= 60;
            m += 1;
        }
        if (m > 59) {
            m -= 60;
            h += 1;
        }
        if (h > 23) {
            h -= 24;
            d += 1;
        }

        return new TimeSpan(d, h, m, s);

    }

    public TimeSpan Subtract(TimeSpan ts) {
        int s1 = this.Seconds();
        int m1 = this.Minutes() * 60;
        int h1 = this.Hours() * 60 * 60;
        int d1 = this.Days() * 24 * 60 * 60;

        int s2 = ts.Seconds();
        int m2 = ts.Minutes() * 60;
        int h2 = ts.Hours() * 60 * 60;
        int d2 = ts.Days() * 24 * 60 * 60;

        int sd = (s1 + m1 + h1 + d1) - (s2 + m2 + h2 + d2);

        int d = sd / (int) (24 * 60 * 60);
        sd -= (d * (24 * 60 * 60));
        int h = sd / (int) (60 * 60);
        sd -= (h * (60 * 60));
        int m = sd / 60;
        int s = sd - (m * 60);

        return new TimeSpan(d, h, m, s);

    }

    public int TotalHours() {
        return (this.Days() * 24) + this.Hours();
    }

    public int TotalMinutes() {
        return (((this.Days() * 24) + this.Hours()) * 60) + this.Minutes();
    }

    public int TotalSeconds() {
        return (((((this.Days() * 24) + this.Hours()) * 60) + this.Minutes()) * 60) + this.Seconds();
    }

    @Override
    public String toString() {
        if (this.Days() != 0) {
            return String.format("%d:%02d:%02d:%02d", this.Days(), this.Hours(), this.Minutes(), this.Seconds());
        } else {
            return String.format("%02d:%02d:%02d", this.Hours(), this.Minutes(), this.Seconds());
        }
    }

}
