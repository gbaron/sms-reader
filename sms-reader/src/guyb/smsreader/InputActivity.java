package guyb.smsreader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InputActivity extends Activity implements OnClickListener
{

	private TextView mText;
	private SpeechRecognizer sr;
	private static final String TAG = "MyStt3Activity";

	class listener implements RecognitionListener
	{
		public void onReadyForSpeech(Bundle params)
		{
			Log.d(TAG, "onReadyForSpeech");
		}

		public void onBeginningOfSpeech()
		{
			Log.d(TAG, "onBeginningOfSpeech");
		}

		public void onRmsChanged(float rmsdB)
		{
			Log.d(TAG, "onRmsChanged");
		}

		public void onBufferReceived(byte[] buffer)
		{
			Log.d(TAG, "onBufferReceived");
		}

		public void onEndOfSpeech()
		{
			Log.d(TAG, "onEndofSpeech");
		}

		public void onError(int error)
		{
			Log.d(TAG, "error " + error);
			mText.setText("error " + error);
		}

		public void onResults(Bundle results)
		{
			String str = new String();
			Log.d(TAG, "onResults " + results);
			ArrayList<String> data = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			for (int i = 0; i < data.size(); i++)
			{
				Log.d(TAG, "result " + data.get(i));
				str += data.get(i);
			}
			mText.setText("results: " + String.valueOf(data.size()));
		}

		public void onPartialResults(Bundle partialResults)
		{
			Log.d(TAG, "onPartialResults");
		}

		public void onEvent(int eventType, Bundle params)
		{
			Log.d(TAG, "onEvent " + eventType);
		}
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.btn_speak)
		{
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");

			intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
			sr.startListening(intent);
			Log.i("111111", "11111111");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		Button speakButton = (Button) findViewById(R.id.btn_speak);
		mText = (TextView) findViewById(R.id.textView1);
		speakButton.setOnClickListener(this);
		sr = SpeechRecognizer.createSpeechRecognizer(this);
		sr.setRecognitionListener(new listener());
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		sr.cancel();
		sr.destroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_input, menu);
		return true;
	}
}
