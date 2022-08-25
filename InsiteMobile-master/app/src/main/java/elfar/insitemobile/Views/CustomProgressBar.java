package elfar.insitemobile.Views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import elfar.insitemobile.TinyDB;

public class CustomProgressBar extends ProgressBar {
    private static final long DEFAULT_DELAY = 500;
    private static final long DEFAULT_DURATION = 1000;
    private static final String TAG = "CustomProgressBar";
    private static final int PERMISSION_SEND_SMS = 123;
    final String phoneListDBKey = "emergency_phone_numbers";
    final String dbKeySmsEmergencyMessage = "key_sms_emergency_message";
    private ObjectAnimator animation;
    private TinyDB tinydb = new TinyDB(getContext());
    private int counter = 0;


    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public synchronized void setProgress(float progress) {
        super.setProgress((int) progress);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if (progress == this.getMax() && counter == 0) {
            try {
                SmsManager sms = SmsManager.getDefault();
                String messageToSent = "Emergency situation";
                if (tinydb.getListString(phoneListDBKey) != null) {
                    if (!tinydb.getString(dbKeySmsEmergencyMessage).equals("")) {
                        messageToSent = tinydb.getString(dbKeySmsEmergencyMessage);
                    }
                    for (String object : tinydb.getListString(phoneListDBKey)) {
                        sms.sendTextMessage(object, null, messageToSent, null, null);
                    }
                    Toast.makeText(getContext(), "SMS Sent", Toast.LENGTH_SHORT).show();
                    counter = 1;
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "No permision to send sms", Toast.LENGTH_SHORT).show();
            }
        }

        if (progress == 0) {
            counter = 0;
        }
    }

    public void startLcpProgressAnim(int progressTo) {
        startLcpProgressAnim(DEFAULT_DELAY, progressTo);
    }

    public void startLcpProgressAnim(long duration, int progressTo) {
        animation = ObjectAnimator.
                ofInt(this, "progress",
                        this.getProgress(), progressTo);
        animation.setDuration(duration);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public void stopLcpProgressAnim() {
        if (animation != null) {
            animation.cancel();
            animation = ObjectAnimator.
                    ofInt(this, "progress",
                            this.getProgress(), 0);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }
    }

}