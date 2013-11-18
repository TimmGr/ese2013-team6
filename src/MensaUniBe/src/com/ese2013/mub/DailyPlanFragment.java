package com.ese2013.mub;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ese2013.mub.model.DailyMenuplan;
import com.ese2013.mub.model.Day;
import com.ese2013.mub.model.Mensa;
import com.ese2013.mub.model.Menu;
import com.ese2013.mub.model.Model;

public class DailyPlanFragment extends Fragment {
	private Day day;
	private List<Mensa> mensas;

	public DailyPlanFragment() {
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public static DailyPlanFragment newInstance(Day day) {
		DailyPlanFragment frag = new DailyPlanFragment();
		frag.setDay(day);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_home_scrollable_content, container, false);
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.section_linear_layout);
		
		if (HomeFragment.getShowAllByDay()) {
			mensas = Model.getInstance().getMensas();
		} else {
			mensas = Model.getInstance().getFavoriteMensas();
		}
		
		
		if (Model.getInstance().noMensasLoaded())
			return rootView; // hacky fix for the case when app is recreated
								// due screen rotation, needs to be handled
								// through proper state management and so
								// on.
		
		/* Date of the displayed day in Favorites View */
		
		SimpleDateFormat df = new SimpleDateFormat( "dd. MMMM yyyy", Locale.getDefault());
		TextView textDateOfDayOfWeek = new TextView(container.getContext());
		textDateOfDayOfWeek.setText(day.format(df));
		LayoutInflater inf = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout.addView(textDateOfDayOfWeek);
		if(mensas.isEmpty()) {
				TextView noFavoriteMensasChosen = new TextView(container.getContext());
				noFavoriteMensasChosen.setText(R.string.no_favorite_mensa);
				layout.addView(noFavoriteMensasChosen);
		}
		for (Mensa mensa : mensas) {
			
				RelativeLayout rel = (RelativeLayout)inf.inflate(R.layout.daily_section_title_bar, null);
				TextView text = (TextView) rel.getChildAt(0);
				text.setText(mensa.getName());
				
				DailyMenuplan d = mensa.getMenuplan().getDailymenuplan(day);
				
				LinearLayout menuLayout = new LinearLayout(container.getContext());
				menuLayout.setOrientation(LinearLayout.VERTICAL);
				
				setUpFavoriteButton(rel, mensa);
				setUpMapButton(rel, mensa);
//				setUpTranslateButton(rel, mensa);
				
				for (Menu menu : d.getMenus()) {
					menuLayout.addView(new MenuView(container.getContext(), menu));
				}
				if (HomeFragment.getShowAllByDay()) 
					menuLayout.setVisibility(View.GONE);
				
				rel.setOnClickListener(new ToggleListener(menuLayout, container.getContext()));
				
				layout.addView(rel);
				layout.addView(menuLayout);
		}
		return rootView;
	}
	

	public void setUpFavoriteButton(RelativeLayout rel, Mensa mensa){
		ImageButton favorite = (ImageButton)rel.getChildAt(1);
		
		favorite.setImageResource((mensa.isFavorite()) ? R.drawable.ic_fav : R.drawable.ic_fav_grey);

		favorite.setOnClickListener(new FavoriteButtonListener(mensa, favorite, this));
	}
	public void setUpMapButton(RelativeLayout rel, Mensa mensa){
		ImageButton map = (ImageButton)rel.getChildAt(2);
		map.setOnClickListener(new MapButtonListener(mensa, this));
		map.setImageResource(R.drawable.ic_map);
	}
//	private void setUpTranslateButton(RelativeLayout rel, Mensa mensa) {
//		Button translate = (Button) rel.getChildAt(3);
//		translate.setOnClickListener(new TranslateButtonListener(mensa, this));
//		
//		
//	}
	public void refreshFavoriteView() {
		((DrawerMenuActivity) getActivity()).refreshHomeActivity();
		
	}
	@Override
	public void onPause(){
		super.onPause();
	}
	@Override
	public void onResume(){
		super.onResume();
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}