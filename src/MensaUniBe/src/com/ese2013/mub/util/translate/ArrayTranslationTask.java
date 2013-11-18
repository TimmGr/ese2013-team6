package com.ese2013.mub.util.translate;



import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ese2013.mub.DailyPlanFragment;
import com.ese2013.mub.util.OnTaskCompleted;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class ArrayTranslationTask extends AsyncTask<Void, Void, String[]>{

    

	private Language from;
	private Language to;

	private String[] texts= new String[1000];
	private String[] translated = new String[1000];
	private OnTaskCompleted listener;
	private ViewGroup container;
	private LinearLayout layout;
	private LayoutInflater inf;

	public ArrayTranslationTask( OnTaskCompleted listener, ViewGroup container, LinearLayout layout, LayoutInflater inf, String[] texts, Language from, Language to){
		this.listener=listener;
		this.container=container;
		this.layout=layout;
		this.inf =inf;
		Translate.setClientId("MensaUniBe");
	    Translate.setClientSecret("T35oR9q6ukB/GbuYAg4nsL09yRsp9j5afWjULfWfmuY=");
	    
	    this.texts = texts;
	    this.from = from;
	    this.to = to;
	    
	}

	@Override
	protected String[] doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		System.out.println("IM TASK!!!!!!!!!!!!!!!!!!!!titleAndDesc["+0+"]: " +texts[0]);
		try {
			System.out.println("First to be translated: " + this.texts[0]);
			this.translated = Translate.execute(this.texts, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return translated;
	}
	
	@Override
	protected void onPostExecute(String[] result) {
		super.onPostExecute(result);
		System.out.println("result[0]: " + result[0]);
		System.out.println("Translation of first: " + this.translated[0]);
		if (result[0]!=null){
			listener.onTaskCompleted(translated,container, layout, inf);
		}else{
			System.out.println("Translation returned empty array!");
		}
		
	}


}
