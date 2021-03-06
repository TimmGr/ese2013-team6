package com.ese2013.mub;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ese2013.mub.model.Mensa;
import com.ese2013.mub.model.Model;

public class MensaListAdapter extends BaseAdapter{
	private static Context context;
	private List<Mensa> menus = new ArrayList<Mensa>();
	private LayoutInflater inflater;
	private MensaListFragment target;
	
	public MensaListAdapter(Context ctx, int resource, MensaListFragment target) {
		super();
		context = ctx;
		this.target = target;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) 
        	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Mensa mensa = menus.get(position);
        view = inflater.inflate(R.layout.mensa_row, null);
        
        setUpFavoriteButton(view, mensa);
        setUpMapButton(view, mensa);
        setUpAdressTextView(view, mensa);
        
        TextView titleView = (TextView) view.findViewById(R.id.mensa_name_view);
        titleView.setText(mensa.getName());    
		return view;
	}
	public void setUpMapButton(View view, final Mensa mensa){
		ImageButton mapButton = (ImageButton) view.findViewById(R.id.mensa_list_map_button);
		mapButton.setImageResource(R.drawable.ic_map);
        mapButton.setOnClickListener(new MapButtonListener(mensa, target));
	}
	public void setUpFavoriteButton(View view, Mensa mensa){
		ImageButton favorite = (ImageButton) view.findViewById(R.id.mensa_list_fav_button);
		favorite.setImageResource((mensa.isFavorite()) ? R.drawable.ic_fav : R.drawable.ic_fav_grey);
	    favorite.setOnClickListener(new FavoriteButtonListener(mensa, favorite));
	}
	public void setUpAdressTextView(View view, Mensa mensa){
        TextView adressView = (TextView) view.findViewById(R.id.mensa_adress_view);
        adressView.setText(mensa.getName() + "\n" + mensa.getStreet() + "\n" + mensa.getZip());
		adressView.setOnClickListener(new AddressTextListener(mensa, this));
	}
	@Override
	public int getCount() {
		return menus.size();
	}
	@Override
	public Mensa getItem(int position) {
		return menus.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public void fill(){
		menus = Model.getInstance().getMensas();
	}
	public void sendListToMenusIntent(Mensa mensa) {
		target.sendListToMenusIntent(mensa);
		
	}
}