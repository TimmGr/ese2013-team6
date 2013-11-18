package com.ese2013.mub;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ese2013.mub.util.translate.TranslationTask;
import com.memetix.mst.language.Language;

public class TranslateButtonListener implements OnClickListener{

	private MenuView menuView;


	public TranslateButtonListener(MenuView menuView) {
		// TODO Auto-generated constructor stub
		this.menuView = menuView;
	}

	@Override
	public void onClick(View arg0) {
		
		TextView title = (TextView) menuView.findViewById(R.id.title_textview);
		TranslationTask transTaskTitle = new TranslationTask(title, Language.GERMAN, Language.ENGLISH);
		transTaskTitle.execute();
		
		TextView desc = (TextView) menuView.findViewById(R.id.desc_textview);
		TranslationTask transTaskDesc = new TranslationTask(desc, Language.GERMAN, Language.ENGLISH);
		transTaskDesc.execute();
		
	}
	
	

}
