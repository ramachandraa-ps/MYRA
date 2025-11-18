package com.myrafriend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Date and Time Utility Class
 * Provides date/time formatting and calculation methods
 */
public class DateTimeUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy";
    public static final String DISPLAY_TIME_FORMAT = "hh:mm a";
    public static final String DISPLAY_DATETIME_FORMAT = "MMM dd, yyyy hh:mm a";

    /**
     * Get current date in yyyy-MM-dd format
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
    }

    /**
     * Get current time in HH:mm:ss format
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(new Date());
    }

    /**
     * Get current datetime in yyyy-MM-dd HH:mm:ss format
     */
    public static String getCurrentDateTime() {
        return new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault()).format(new Date());
    }

    /**
     * Format date for display (e.g., "Jan 15, 2024")
     */
    public static String formatDateForDisplay(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());
            Date parsedDate = inputFormat.parse(date);
            return parsedDate != null ? outputFormat.format(parsedDate) : date;
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * Format time for display (e.g., "02:30 PM")
     */
    public static String formatTimeForDisplay(String time) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(DISPLAY_TIME_FORMAT, Locale.getDefault());
            Date parsedTime = inputFormat.parse(time);
            return parsedTime != null ? outputFormat.format(parsedTime) : time;
        } catch (ParseException e) {
            return time;
        }
    }

    /**
     * Format datetime for display
     */
    public static String formatDateTimeForDisplay(String datetime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(DISPLAY_DATETIME_FORMAT, Locale.getDefault());
            Date parsedDateTime = inputFormat.parse(datetime);
            return parsedDateTime != null ? outputFormat.format(parsedDateTime) : datetime;
        } catch (ParseException e) {
            return datetime;
        }
    }

    /**
     * Get date X days ago
     */
    public static String getDateDaysAgo(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.getTime());
    }

    /**
     * Get date X days from now
     */
    public static String getDateDaysFromNow(int daysFromNow) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysFromNow);
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.getTime());
    }

    /**
     * Get relative time string (e.g., "2 hours ago", "Just now")
     */
    public static String getRelativeTimeString(String datetime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
            Date pastDate = format.parse(datetime);
            if (pastDate == null) return datetime;

            long timeDiff = System.currentTimeMillis() - pastDate.getTime();

            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
            long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
            long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

            if (seconds < 60) {
                return "Just now";
            } else if (minutes < 60) {
                return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
            } else if (hours < 24) {
                return hours + (hours == 1 ? " hour ago" : " hours ago");
            } else if (days < 7) {
                return days + (days == 1 ? " day ago" : " days ago");
            } else {
                return formatDateForDisplay(datetime.split(" ")[0]);
            }
        } catch (ParseException e) {
            return datetime;
        }
    }

    /**
     * Check if date is today
     */
    public static boolean isToday(String date) {
        return date.equals(getCurrentDate());
    }

    /**
     * Check if date is in the past
     */
    public static boolean isPastDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date inputDate = format.parse(date);
            Date today = format.parse(getCurrentDate());
            return inputDate != null && today != null && inputDate.before(today);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Get day of week (e.g., "Monday")
     */
    public static String getDayOfWeek(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            Date parsedDate = inputFormat.parse(date);
            return parsedDate != null ? outputFormat.format(parsedDate) : "";
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * Calculate days between two dates
     */
    public static long getDaysBetween(String startDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date start = format.parse(startDate);
            Date end = format.parse(endDate);
            if (start != null && end != null) {
                long diff = end.getTime() - start.getTime();
                return TimeUnit.MILLISECONDS.toDays(diff);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
