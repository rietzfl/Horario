package hft.wiinf.de.horario.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import hft.wiinf.de.horario.R;
import hft.wiinf.de.horario.TabActivity;
import hft.wiinf.de.horario.controller.FailedSMSController;
import hft.wiinf.de.horario.controller.PersonController;
import hft.wiinf.de.horario.model.FailedSMS;
import hft.wiinf.de.horario.model.Person;
import hft.wiinf.de.horario.utility.BundleUtility;

/**
 * try to send failed SMS again
 */
public class FailedSMSService extends JobService {

    /**
     * The Sms.
     */
    Bundle sms;
    /**
     * The Phone state, to check weather it is in service or not
     */
    int phone_state;

    /**
     * job will check if there is service and if the sim is ready (everything what needs to be checked for sending sms)
     * after that it will send the SMS and set a notification
     * @param params
     * @return boolean if the job has finished successfully
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_SERVICE_STATE);

        int simState = tm.getSimState();

        if ((phone_state == ServiceState.STATE_IN_SERVICE) && (simState == TelephonyManager.SIM_STATE_READY)) {
            sms = BundleUtility.toBundle(params.getExtras());
            FailedSMS failedSMS = new FailedSMS(sms.getString("message"), sms.getString("phoneNo"), sms.getLong("creatorID"), sms.getBoolean("accepted"));

            sendSMS(failedSMS);
            FailedSMSController.deleteFailedSMS(failedSMS.getMessage(), failedSMS.getCreatorID(), failedSMS.getPhoneNo());

            jobFinished(params, false);
            addNotification(sms.getString("phoneNo"), sms.getBoolean("accepted"), sms.getInt("id"), sms.getString("eventShortDesc"));
            return true;
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    /**
     * Send sms.
     *
     * @param failedSMS the failed sms
     */
    public void sendSMS(FailedSMS failedSMS) {
        try {
            String msg;
            Person me = PersonController.getPersonWhoIam();
            if (failedSMS.isAccepted()) {
                msg = ":Horario:" + failedSMS.getCreatorID() + ",1," + me.getName();
            } else {
                msg = ":Horario:" + failedSMS.getCreatorID() + ",0," + me.getName() + "," + failedSMS.getMessage();
            }
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(failedSMS.getPhoneNo(), null, msg, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PhoneStateListener mPhoneListener = new PhoneStateListener() {
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            phone_state = serviceState.getState();
            super.onServiceStateChanged(serviceState);
        }
    };

    /**
     * creates a notification for the user
     *
     * @param phoneNo,       a {@link String}  of the phone number
     * @param accepted,      a {@code boolean} of the acceptedState of the event
     * @param id,            a {@code int} of the id
     * @param eventShortDesc a {@link String} of the event description
     */
    private void addNotification(String phoneNo, boolean accepted, int id, String eventShortDesc) {
        String contentText;
        if (accepted) {
            contentText = "Zusage des Events \"" + eventShortDesc + "\" an: " + phoneNo;
        } else {
            contentText = "Absage des Events  \"" + eventShortDesc + "\" an: " + phoneNo;
        }

        Intent notificationIntent = new Intent(this, TabActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 26) {
            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // The id of the channel.
            String channel_id = String.valueOf(id);
            //The user-visible name of the channel.
            CharSequence name = "SMSSENT";
            // The user-visible description of the channel.
            String description = "SMS wurde erfolgreich versandt!";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channel_id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(false);
            manager.createNotificationChannel(mChannel);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext(), channel_id)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle("SMS wurde erfolgreich versandt!")
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                            .setContentText(contentText);
            builder.setContentIntent(contentIntent);
            manager.notify(id, builder.build());
        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, "")
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle("SMS wurde erfolgreich versandt!")
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                            .setContentText(contentText);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(id, builder.build());
        }
    }
}