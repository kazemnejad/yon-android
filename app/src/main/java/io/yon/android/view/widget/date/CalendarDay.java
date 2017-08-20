package io.yon.android.view.widget.date;

import io.yon.android.util.calendar.PersianCalendar;

public class CalendarDay {
        private PersianCalendar mPersianCalendar;
        public int year;
        public int month;
        public int day;

        public CalendarDay() {
            setTime(System.currentTimeMillis());
        }

        public CalendarDay(long timeInMillis) {
            setTime(timeInMillis);
        }

        public CalendarDay(PersianCalendar calendar) {
            year = calendar.getPersianYear();
            month = calendar.getPersianMonth();
            day = calendar.getPersianDay();
        }

        public CalendarDay(int year, int month, int day) {
            setDay(year, month, day);
        }

        public void set(CalendarDay date) {
            year = date.year;
            month = date.month;
            day = date.day;
        }

        public void setDay(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        private void setTime(long timeInMillis) {
            if (mPersianCalendar == null) {
                mPersianCalendar = new PersianCalendar();
            }
            mPersianCalendar.setTimeInMillis(timeInMillis);
            month = mPersianCalendar.getPersianMonth();
            year = mPersianCalendar.getPersianYear();
            day = mPersianCalendar.getPersianDay();
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }
    }