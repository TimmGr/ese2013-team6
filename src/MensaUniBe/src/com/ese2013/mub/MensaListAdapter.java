package com.ese2013.mub;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        setFavoriteButtonListener(view, mensa);
        setMapButtonListener(view);
        
        TextView titleView = (TextView) view.findViewById(R.id.mensa_name_view);
        TextView adressView = (TextView) view.findViewById(R.id.mensa_adress_view);
        
        titleView.setText(mensa.getName());
        adressView.setText(mensa.getName() + "\n" + mensa.getStreet() + "\n" + mensa.getZip());
        setTextViewListener(adressView, mensa);
      
            
		return view;
	}
	public void setMapButtonListener(View view){
		ImageButton mapButton = (ImageButton) view.findViewById(R.id.map_button);
        mapButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View viewIn) {
            	//TODO: implement Button functionality
            	Toast t = Toast.makeText(context, "Not yet Implemented!", Toast.LENGTH_SHORT);
            	t.show();
            	}});
	}
	public void setFavoriteButtonListener(View view, Mensa mensa){
		  ImageButton favorites = (ImageButton) view.findViewById(R.id.mensa_list_fav_button);
		  if(mensa.isFavorite())
			  favorites.setBackgroundResource(R.drawable.ic_fav);
		  else
			  favorites.setBackgroundResource(R.drawable.ic_fav_grey);
	        favorites.setOnClickListener(new FavoriteButtonListener(mensa, favorites));
	}
	public void setTextViewListener(View view, Mensa mensa){
		view.setOnClickListener(new AddressTextListener(mensa, this));
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