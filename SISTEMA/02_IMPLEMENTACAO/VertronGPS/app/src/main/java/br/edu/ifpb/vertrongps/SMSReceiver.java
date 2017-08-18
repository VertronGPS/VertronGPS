package br.edu.ifpb.vertrongps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the message
        Bundle extras = intent.getExtras();

        // Set object message in android device
        SmsMessage[] smgs = null;

        // Content SMS message
        String infoSMS = "";

        if (extras != null){
            // Retrieve the SMS message received
            Object[] pdus = (Object[]) extras.get("pdus");
            smgs = new SmsMessage[pdus.length];

            for (int i=0; i<smgs.length; i++){
                smgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                infoSMS += smgs[i].getMessageBody().toString();
                infoSMS += "\n";
            }

            Toast.makeText(context, infoSMS, Toast.LENGTH_LONG).show();

        }

    }

}