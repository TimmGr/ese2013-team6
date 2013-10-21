package com.ese2013.mub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ese2013.mub.model.DailyMenuplan;
import com.ese2013.mub.model.Mensa;
import com.ese2013.mub.model.Menu;
import com.ese2013.mub.model.Model;
import com.ese2013.mub.model.Observer;

public class MenusByMensaViewFragment extends Fragment implements Observer {
	private FragmentStatePagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;

	private boolean showByDay = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menusbymensa_view, container, false);
		if (showByDay) {
			sectionsPagerAdapter = new MenuSectionsPagerAdapter(getChildFragmentManager());
		} else {
			sectionsPagerAdapter = new MensaSectionsPagerAdapter(getChildFragmentManager());
		}
		viewPager = (ViewPager) view.findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		Model.getInstance().addObserver(this);
		return view;
	}

	@Override
	public void onNotifyChanges() {
		sectionsPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Model.getInstance().removeObserver(this);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class MensaSectionsPagerAdapter extends FragmentStatePagerAdapter {
		private ArrayList<Mensa> mensas = Model.getInstance().getMensas();

		public MensaSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Instantiates to fragment which is currently displayed
		 */
		@Override
		public Fragment getItem(int position) {
			return WeeklyPlanFragment.newInstance(mensas.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return mensas.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mensas.get(position).getName();
		}

		@Override
		public void notifyDataSetChanged() {
			mensas = Model.getInstance().getMensas();
			super.notifyDataSetChanged();
		}

	}

	/**
	 * This fragment displays the weekly menu plan for the given mensa.
	 */
	public static class WeeklyPlanFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private Mensa mensa;

		public WeeklyPlanFragment() {
		}

		/**
		 * Maybe it would be better to send the mensa via a Bundle. Depends on
		 * the implementation of the mensa class.
		 */
		public void setMensa(Mensa mensa) {
			this.mensa = mensa;
		}

		public static WeeklyPlanFragment newInstance(Mensa mensa) {
			WeeklyPlanFragment frag = new WeeklyPlanFragment();
			frag.setMensa(mensa);
			return frag;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home_scrollable_content, container, false);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.section_linear_layout);
			if (Model.getInstance().noMensasLoaded())
				return rootView; // hacky fix for the case when app is recreated
									// due screen rotation, needs to be handled
									// through proper state management and so
									// on.

			for (DailyMenuplan d : mensa.getMenuplan()) {
				TextView text = new TextView(container.getContext());
				text.setText(d.getDateString());
				layout.addView(text);

				for (Menu menu : d.getMenus()) {
					layout.addView(new MenuView(container.getContext(), menu.getTitle(), menu.getDescription()));
				}
			}
			return rootView;
		}
	}

	public class MenuSectionsPagerAdapter extends FragmentStatePagerAdapter {
		private ArrayList<Date> days;
		
		public MenuSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			if (Model.getInstance().noMensasLoaded())
				days = new ArrayList<Date>();
			else
				days = new ArrayList<Date>(Model.getInstance().getMensas().get(0).getMenuplan().getDays());
		}

		/**
		 * Instantiates to fragment which is currently displayed
		 */
		@Override
		public Fragment getItem(int position) {
			return DailyPlanFragment.newInstance(days.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return days.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
	        Calendar c = Calendar.getInstance();
	        c.setTime(days.get(position));
	        int numberOfWeek = c.get(Calendar.DAY_OF_WEEK);
	        String dayOfWeek = "";
	        switch(numberOfWeek){
		        case 1 : dayOfWeek = "Monday";
		        break;
		        case 2 : dayOfWeek = "Tuesday";
		        break;
		        case 3 : dayOfWeek = "Wednesday";
		        break;
		        case 4 : dayOfWeek = "Thursday";
		        break;
		        case 5 : dayOfWeek = "Friday";
		        break;
		        default : dayOfWeek = "";
		        break;
	        }
			return dayOfWeek;
		}

		@Override
		public void notifyDataSetChanged() {
			days = new ArrayList<Date>(Model.getInstance().getMensas().get(0).getMenuplan().getDays());
			super.notifyDataSetChanged();
		}

	}

	public static class DailyPlanFragment extends Fragment {
		private Date day;
		private ArrayList<Mensa> mensas;

		public DailyPlanFragment() {
		}

		public void setDay(Date day) {
			this.day = day;
		}

		public static DailyPlanFragment newInstance(Date day) {
			DailyPlanFragment frag = new DailyPlanFragment();
			frag.setDay(day);
			return frag;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			mensas = Model.getInstance().getFavoriteMensas();
			View rootView = inflater.inflate(R.layout.fragment_home_scrollable_content, container, false);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.section_linear_layout);
			if (Model.getInstance().noMensasLoaded())
				return rootView; // hacky fix for the case when app is recreated
									// due screen rotation, needs to be handled
									// through proper state management and so
									// on.
			Log.d("CALL", "BEFORE LOOP");
			
			/* Date of the displayed day in Favorites View */
			TextView textDateOfDayOfWeek = new TextView(container.getContext());
			textDateOfDayOfWeek.setText(day.toString());
			
			for (Mensa mensa : mensas) {
					DailyMenuplan d = mensa.getMenuplan().getDailymenuplan(day);
					TextView text = new TextView(container.getContext());
					text.setText(mensa.getName());
					layout.addView(text);
					Log.d("CALL", "IN SE LOOP");
					for (Menu menu : d.getMenus()) {
						layout.addView(new MenuView(container.getContext(), menu.getTitle(), menu.getDescription()));
					}
			}
			return rootView;
		}
	}
}
