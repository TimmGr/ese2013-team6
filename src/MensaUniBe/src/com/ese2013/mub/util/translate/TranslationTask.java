package com.ese2013.mub.util.translate;



import android.os.AsyncTask;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TranslationTask extends AsyncTask<Void, Void, Void>{

    
	private String translated;
	private Language from;
	private Language to;
	private String stringToTranslate;
	private TextView textView;

	public TranslationTask( TextView toTranslate, Language from, Language to){

		Translate.setClientId("MensaUniBe");
	    Translate.setClientSecret("T35oR9q6ukB/GbuYAg4nsL09yRsp9j5afWjULfWfmuY=");
	    
	    this.textView = toTranslate;
	    this.stringToTranslate = (String) toTranslate.getText();
	    this.from = from;
	    this.to = to;
	    
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		
		try {
			System.out.println("Translation of: " + this.stringToTranslate);
			this.translated = Translate.execute(this.stringToTranslate, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		System.out.println("Translation of: " + this.translated);
		textView.setText(translated);
//		listener.onTaskCompleted();
	}


}
