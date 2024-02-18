// Import necessary packages and classes.
package com.reflect.reflect;

// Import AppCompatActivity from the androidx.appcompat library.
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Import Dialog and ProgressDialog from the android.app library.
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.Manifest;

import com.google.android.gms.common.data.DataHolder;

import java.util.Calendar;

// Define a class named BaseActivity that extends AppCompatActivity.
public class BaseActivity extends AppCompatActivity {

    // Declare a private variable progressDialog of type Dialog.
    private Dialog progressDialog;

    // Define a constant for the permission request code.
    private static final int PERMISSION_REQUEST_CODE = 1001;

    // Override the onCreate method of AppCompatActivity.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate progressDialog as a new Dialog with the current context.
        progressDialog = new Dialog(this);
        // Set the layout for the progressDialog using a custom layout file named "custom_progress_dialog".
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        // Ensure that the progressDialog is not cancellable by pressing the back button.
        progressDialog.setCancelable(false);

        // Check for notification permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {
            // Request the missing permissions.
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_CODE
            );
            return;
        }

        // Create the notification channel if it doesn't exist.
        createNotificationChannel();

        Intent intent = new Intent(this, DashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "reminders")
                .setSmallIcon(R.drawable.baseline_calendar_today_24)
                .setContentTitle("Daily Reminder")
                .setContentText("Did you Reflect on your day today?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Get the NotificationManagerCompat and show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());

        scheduleDailyNotification();
    }

    // Define a method to schedule the daily notification.
    private void scheduleDailyNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 4);

        // Create an intent to be fired at the specified time.
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Get the AlarmManager service and set the notification to be triggered daily.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminders";
            String description = "Daily Reminders for mood tracking.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("reminders", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Define a method named showProgressDialog, accessible to subclasses.
    protected void showProgressDialog() {
        // Check if progressDialog is not null, then show the progressDialog.
        if (progressDialog != null)
            progressDialog.show();
    }

    // Define a method named hideProgress, accessible to subclasses.
    protected void hideProgress() {
        // Check if progressDialog is not null and is currently showing, then dismiss it.
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
