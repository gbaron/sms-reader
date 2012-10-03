package guyb.smsreader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class IncomingService extends Service
{

	private static final String SMS_RECEIVED_INTENT = "android.provider.Telephony.SMS_RECEIVED";

	private static final int NOTIF_ID = 535;

	public static boolean isRunning = false;

	private SMSReceiver smsReceiver;
	private GoogleAnalyticsTracker tracker;

	public IncomingService()
	{
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate()
	{
		super.onCreate();

		int icon = R.drawable.ic_launcher;
		CharSequence contentTitle = this.getResources().getString(R.string.app_name);
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, contentTitle, when);

		Context context = getApplicationContext();

		CharSequence contentText = "Incoming messages will be played";

		Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		startForeground(NOTIF_ID, notification);

		if (smsReceiver == null)
		{
			smsReceiver = new SMSReceiver();

			this.registerReceiver(smsReceiver, new IntentFilter(SMS_RECEIVED_INTENT));
		}

		isRunning = true;

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession("UA-35090171-1", 20, getApplicationContext());

		tracker.trackPageView("ServiceStarted");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if (smsReceiver != null)
		{
			this.unregisterReceiver(smsReceiver);
		}
		smsReceiver = null;
		isRunning = false;

		tracker.trackPageView("ServiceStopped");
		tracker.stopSession();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		if (intent != null && intent.hasExtra(CarModeReceiver.KEY_CAR_MODE)
				&& intent.getBooleanExtra(CarModeReceiver.KEY_CAR_MODE, false))
		{
			tracker.trackPageView("ServiceStarted/CarMode");
		} else if (intent != null && intent.hasExtra(MainActivity.MANUAL_KEY)
				&& intent.getBooleanExtra(MainActivity.MANUAL_KEY, false))
		{
			tracker.trackPageView("ServiceStarted/Manual");
		}

		return Service.START_STICKY;
	}

	public class SMSReceiver extends BroadcastReceiver
	{
		// private static final String TAG = "SMSReceiver";

		public SMSReceiver()
		{
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{

			// Log.v(TAG,
			// "IncomingService.SMSReceiver:onReceive "
			// + intent.getAction());

			if (intent.getAction() == SMS_RECEIVED_INTENT)
			{

				tracker.trackEvent("Action", "Message", "Played", 0);

				Intent i = new Intent(intent);
				i.setClass(context, SpeechService.class);
				context.startService(i);

			}
		}

	}

}
