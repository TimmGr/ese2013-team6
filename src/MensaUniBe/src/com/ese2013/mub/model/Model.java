package com.ese2013.mub.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.widget.Toast;

import com.ese2013.mub.util.ModelCreationTask;
import com.ese2013.mub.util.ModelSavingTask;
import com.ese2013.mub.util.Observable;
import com.ese2013.mub.util.database.MensaDataSource;

/**
 * Manages the loading and storing of the whole model. This class holds the list
 * of all Mensas. It also initializes and updates this list. If the list of
 * Mensas is updated, all Observers are notified (e.g. GUI classes).
 */
public class Model extends Observable {
	private List<Mensa> mensas = new ArrayList<Mensa>();
	private static Model instance;
	private Context context;
	private MensaDataSource dataSource;

	public Model(Context context) {
		Model.instance = this;
		this.context = context;
		init();
	}

	private void init() {
		dataSource = MensaDataSource.getInstance();
		dataSource.init(context);
		ModelCreationTask task = new ModelCreationTask();
		task.execute();
		
	}

	public List<Mensa> getMensas() {
		return mensas;
	}

	public List<Mensa> getFavoriteMensas() {
		List<Mensa> ret = new ArrayList<Mensa>(3);
		for (Mensa m : mensas)
			if (m.isFavorite())
				ret.add(m);
		return ret;
	}

	public static Model getInstance() {
		return instance;
	}

	public boolean noMensasLoaded() {
		return mensas.size() == 0;
	}
	
	public boolean favoritesExist() {
		for (Mensa m : mensas)
			if (m.isFavorite())
				return true;
		return false;
	}

	public void saveFavorites() {
		dataSource.open();
		dataSource.storeFavorites(mensas);
		dataSource.close();
	}

	public void onCreationFinished(ModelCreationTask task) {
		Toast.makeText(context, context.getString(task.getStatusMsgResource()), Toast.LENGTH_LONG).show();
		if (task.wasSuccessful()) {
			mensas = task.getMensas();
			if (task.hasDownloadedNewData())
				saveModel();
		}
		notifyChanges();
	}

	private void saveModel() {
		ModelSavingTask savingTask = new ModelSavingTask();
		savingTask.execute();
	}
}
