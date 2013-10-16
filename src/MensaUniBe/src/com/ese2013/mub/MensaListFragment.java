package com.ese2013.mub;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



public class MensaListFragment extends Fragment {
	private MensaListAdapter adapter;
	private ListView mensaListView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		adapter = new MensaListAdapter(getActivity(), android.R.layout.simple_list_item_1);

		View view = inflater.inflate(R.layout.fragment_mensa_list, container,
				false);
		
		mensaListView = (ListView) view.findViewById(R.id.mensa_view_layout);
		mensaListView.setAdapter(adapter);
		adapter.fill();

		return view;
	}
}