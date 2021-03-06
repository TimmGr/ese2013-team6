package com.ese2013.mub.util;

import java.util.List;

import android.os.AsyncTask;

import com.ese2013.mub.model.Mensa;
import com.ese2013.mub.model.Model;
import com.ese2013.mub.util.database.MensaDataSource;

/**
 * This class saves asynchronously all data from the model using the DataManager
 * class.
 */
public class ModelSavingTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		MensaDataSource dataManager = MensaDataSource.getInstance();
		dataManager.open();
		List<Mensa> mensas = Model.getInstance().getMensas();
		dataManager.storeMensaList(mensas);
		dataManager.deleteMenus();
		for (Mensa m : mensas) {
			dataManager.storeWeeklyMenuplan(m);
		}
		dataManager.close();
		return null;
	}
}
