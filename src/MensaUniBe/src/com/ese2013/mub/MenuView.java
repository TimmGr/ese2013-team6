package com.ese2013.mub;

import java.util.Locale;

import com.ese2013.mub.model.Menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuView extends LinearLayout {

	private TextView menuTitleText;
	private TextView menuDescView;

	public MenuView(Context context, String menuTitle, String menuDesc) {
		super(context);
		
		
		setOrientation(VERTICAL);
		setPadding(0, 0, 0, dimToPixels(R.dimen.menu_view_bottom_margin));

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.menu_view, this);

//		menuTitle = menuTitle.toUpperCase(Locale.getDefault());
		setTitle(menuTitle);
		setDescription(menuDesc);
		setUpTranslateButton();
	}

	public MenuView(Context context) {
		super(context);
	}

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void setTitle(String menuTitle) {
		menuTitleText = (TextView) findViewById(R.id.title_textview);
		menuTitleText.setText(menuTitle);
		menuTitleText.setBackgroundColor(getTitleColor(menuTitle));
	}
	
	private void setDescription(String menuDesc) {
		menuDescView = (TextView) findViewById(R.id.desc_textview);
		menuDescView.setText(menuDesc);
	}
	
	private void setUpTranslateButton() {
		// TODO Auto-generated method stub
		System.out.println("Button set up");
		Button translateButton = (Button) findViewById(R.id.translate_menu_button);
		translateButton.setOnClickListener(new TranslateButtonListener(this));
	}

	// TODO there should be a cleaner way to map titles to colors
	private int getTitleColor(String title) {
		if (title.contains("VEGI") || title.contains("VEGETARISCH"))
			return getResources().getColor(R.color.green);

		if (title.contains("EINFACH GUT") || title.contains("TAGESGERICHT") || title.contains("WARMES SCH�SSELGERICHT"))
			return getResources().getColor(R.color.yellow);

		return getResources().getColor(R.color.blue);
	}

	private int dimToPixels(int dim) {
		return (int) getResources().getDimension(dim);
	}
}
