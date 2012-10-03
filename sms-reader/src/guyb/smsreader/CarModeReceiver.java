package guyb.smsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CarModeReceiver extends BroadcastReceiver
{

	// private static final String TAG = "CarModeReceiver";

	public static final String PREFS = "CarModeReceiver";
	public static final String KEY_CAR_MODE = "CarMode";

	public CarModeReceiver()
	{
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{

		// Log.v(TAG, intent.toString());

		SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREFS,
				Context.MODE_PRIVATE);

		Editor e = prefs.edit();

		if (intent.getAction() == "android.app.action.ENTER_CAR_MODE")
		{
			e.putBoolean(KEY_CAR_MODE, true);

			Intent t = new Intent(context, IncomingService.class);
			t.putExtra(KEY_CAR_MODE, true);

			context.startService(t);

		} else if (intent.getAction() == "android.app.action.EXIT_CAR_MODE")
		{
			e.putBoolean(KEY_CAR_MODE, false);

			context.stopService(new Intent(context, IncomingService.class));
		}

		e.commit();
	}
}
