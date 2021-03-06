package com.ese2013.mub.util.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ese2013.mub.model.DailyMenuplan;
import com.ese2013.mub.model.Day;
import com.ese2013.mub.model.Mensa;
import com.ese2013.mub.model.Menu;
import com.ese2013.mub.model.WeeklyMenuplan;
import com.ese2013.mub.util.database.tables.FavoritesTable;
import com.ese2013.mub.util.database.tables.MensasTable;
import com.ese2013.mub.util.database.tables.MenusMensasTable;
import com.ese2013.mub.util.database.tables.MenusTable;

/**
 * Manages storing and loading data from the Mensa SQLite database.
 */
public class MensaDataSource {
	private SQLiteDatabase database;
	private SqlDatabaseHelper dbHelper;
	private static SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
	private static MensaDataSource instance;

	private MensaDataSource() {
	}

	public static MensaDataSource getInstance() {
		if (instance == null)
			instance = new MensaDataSource();
		return instance;
	}

	/**
	 * Should be called before the MensaDataSource is used.
	 * 
	 * @param context
	 *            Context in which the DataSource is used, i.e. the main
	 *            activity. Must not be null.
	 */
	public void init(Context context) {
		dbHelper = new SqlDatabaseHelper(context);
	}

	/**
	 * Opens the database, must be called before using the database. Caller must
	 * also call close() after using the database to avoid a resource leak.
	 * 
	 * @throws SQLException
	 *             If the database cannot be created or read.
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Closes the database. Must be called after using the database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Stores the given list of mensas
	 * 
	 * @param mensas
	 *            The List of Mensa objects to be stored. Must not be null and
	 *            must not contain null.
	 */
	public void storeMensaList(List<Mensa> mensas) {
		for (Mensa m : mensas)
			storeMensa(m);
	}

	private void storeMensa(Mensa m) {
		ContentValues values = new ContentValues();
		values.put(MensasTable.COL_ID, m.getId());
		values.put(MensasTable.COL_NAME, m.getName());
		values.put(MensasTable.COL_STREET, m.getStreet());
		values.put(MensasTable.COL_ZIP, m.getZip());
		values.put(MensasTable.COL_LON, m.getLongitude());
		values.put(MensasTable.COL_LAT, m.getLatitude());
		values.put(MensasTable.COL_TIMESTAMP, m.getTimestamp());
		database.replace(MensasTable.TABLE_MENSAS, null, values);
	}

	/**
	 * Loads the list of mensas from the database
	 * 
	 * @return List of Mensas.
	 */
	public List<Mensa> loadMensaList() {
		List<Mensa> mensas = new ArrayList<Mensa>();
		Cursor c = database.rawQuery("SELECT * FROM " + MensasTable.TABLE_MENSAS, null);
		final int POS_ID = c.getColumnIndex(MensasTable.COL_ID);
		final int POS_NAME = c.getColumnIndex(MensasTable.COL_NAME);
		final int POS_STREET = c.getColumnIndex(MensasTable.COL_STREET);
		final int POS_ZIP = c.getColumnIndex(MensasTable.COL_ZIP);
		final int POS_LON = c.getColumnIndex(MensasTable.COL_LON);
		final int POS_LAT = c.getColumnIndex(MensasTable.COL_LAT);
		final int POS_TIMESTAMP = c.getColumnIndex(MensasTable.COL_TIMESTAMP);
		c.moveToFirst();
		do {
			Mensa.MensaBuilder builder = new Mensa.MensaBuilder();
			int mensaId = c.getInt(POS_ID);
			builder.setId(mensaId);
			builder.setName(c.getString(POS_NAME));
			builder.setStreet(c.getString(POS_STREET));
			builder.setZip(c.getString(POS_ZIP));
			builder.setLongitude(c.getDouble(POS_LON));
			builder.setLatitude(c.getDouble(POS_LAT));
			builder.setIsFavorite(isInFavorites(mensaId));
			builder.setTimestamp(c.getInt(POS_TIMESTAMP));
			mensas.add(builder.build());
		} while (c.moveToNext());
		c.close();
		return mensas;
	}

	/**
	 * Updates the table of favorite mensas using the given mensa list. Every
	 * mensa which is a favorite mensa gets stored in the favorites table.
	 * 
	 * @param mensas
	 *            List of mensas to update the favorites table.
	 */
	public void storeFavorites(List<Mensa> mensas) {
		database.delete(FavoritesTable.TABLE_FAV_MENSAS, null, null);
		for (Mensa m : mensas) {
			if (m.isFavorite()) {
				ContentValues values = new ContentValues();
				values.put(MensasTable.COL_ID, m.getId());
				database.insert(FavoritesTable.TABLE_FAV_MENSAS, null, values);
			}
		}
	}

	/**
	 * Checks if a given mensa id belongs to a favorite mensa.
	 * 
	 * @param mensaId
	 *            Int mensa id to be checked.
	 * @return true if the mensa id belongs to a favorite mensa.
	 */
	public boolean isInFavorites(int mensaId) {
		Cursor c = database.rawQuery("select * from " + FavoritesTable.TABLE_FAV_MENSAS + " where " + MensasTable.COL_ID
				+ "=" + mensaId, null);
		return c.getCount() != 0;
	}

	/**
	 * Returns the update time stamp for a given mensa id.
	 * 
	 * @param mensaId
	 *            Int id of the mensa to retrieve the time stamp of.
	 * @return Time stamp of the given mensa (represented by the id).
	 */
	public int getMensaTimestamp(int mensaId) {
		Cursor c = database.rawQuery("select " + MensasTable.COL_TIMESTAMP + " from " + MensasTable.TABLE_MENSAS + " where "
				+ MensasTable.COL_ID + "=" + mensaId, null);
		c.moveToFirst();
		return c.getInt(0);
	}

	/**
	 * Stores the given Mensa's weekly menu plan to the database. Caller must
	 * assure that only weekly menu plans of the same week are stored in the
	 * database.
	 * 
	 * @param mensa
	 *            The Mensa the weekly plan belongs to. Must not be null
	 */
	public void storeWeeklyMenuplan(Mensa mensa) {
		WeeklyMenuplan plan = mensa.getMenuplan();
		for (DailyMenuplan d : plan)
			for (Menu m : d.getMenus())
				storeMenu(m, mensa);
	}

	/**
	 * Stores the given Menu in the MenuTable and also stores a pair of Menu Id
	 * and Mensa Id in the MenusMensa Table.
	 * 
	 * @param menu Menu to be stored. Must not be null.
	 * @param mensa Mensa which the Menu belongs to. Must not be null.
	 */
	private void storeMenu(Menu menu, Mensa mensa) {
		String date = menu.getDate().format(fm);
		
		//query the database to check if the menu is already stored, and if so, retrieve the id.
		Cursor c = database.rawQuery("select " + MenusTable.COL_ID + " from " + MenusTable.TABLE_MENUS + " where "
				+ MenusTable.COL_TITLE + "='" + menu.getTitle() + "' and " + MenusTable.COL_DESC + "='" + menu.getDescription()
				+ "' and " + MenusTable.COL_DATE + "='" + date + "'", null);
		int id;
		if (c.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(MenusTable.COL_TITLE, menu.getTitle());
			values.put(MenusTable.COL_DESC, menu.getDescription());
			values.put(MenusTable.COL_DATE, date);
			values.put(MenusTable.COL_TITLE_TRANSLANTED, menu.getTitleTranslated());
			values.put(MenusTable.COL_DESC_TRANSLANTED, menu.getDescriptionTranslated());
			database.insert(MenusTable.TABLE_MENUS, null, values);
			c = database.rawQuery("select last_insert_rowid()", null);
			c.moveToFirst();
			id = c.getInt(0);
		} else {
			c.moveToFirst();
			id = c.getInt(c.getColumnIndex(MenusTable.COL_ID));
		}

		ContentValues values2 = new ContentValues();
		values2.put(MenusTable.COL_ID, id);
		values2.put(MensasTable.COL_ID, mensa.getId());
		database.replace(MenusMensasTable.TABLE_MENUS_MENSAS, null, values2);
	}

	/**
	 * Loads the weekly menu plan of a given mensa, represented by the mensa id.
	 * 
	 * @param mensaId
	 *            Id of the mensa.
	 * @return WeeklyMenuplan of the given mensa.
	 */
	public WeeklyMenuplan loadMenuplan(int mensaId) {
		String query = "select * from " + MenusTable.TABLE_MENUS + " inner join " + MenusMensasTable.TABLE_MENUS_MENSAS
				+ " on " + MenusTable.TABLE_MENUS + "." + MenusTable.COL_ID + " = " + MenusMensasTable.TABLE_MENUS_MENSAS
				+ "." + MenusTable.COL_ID + " where " + MenusMensasTable.TABLE_MENUS_MENSAS + "." + MensasTable.COL_ID
				+ " = " + mensaId + " ;";
		Cursor c = database.rawQuery(query, null);
		final int POS_TITLE = c.getColumnIndex(MenusTable.COL_TITLE);
		final int POS_DESC = c.getColumnIndex(MenusTable.COL_DESC);
		final int POS_DATE = c.getColumnIndex(MenusTable.COL_DATE);
		final int POS_TITLE_TRANSLANTED = c.getColumnIndex(MenusTable.COL_TITLE_TRANSLANTED);
		final int POS_DESC_TRANSLANTED = c.getColumnIndex(MenusTable.COL_DESC_TRANSLANTED);
		WeeklyMenuplan p = new WeeklyMenuplan();
		c.moveToFirst();
		do {
			try {
				Menu.MenuBuilder builder = new Menu.MenuBuilder();
				builder.setTitle(c.getString(POS_TITLE));
				builder.setDescription(c.getString(POS_DESC));
				builder.setDate(new Day(fm.parse(c.getString(POS_DATE))));
				builder.setTitleTranslated(c.getString(POS_TITLE_TRANSLANTED));
				builder.setDescriptionTranslated(c.getString(POS_DESC_TRANSLANTED));
				p.add(builder.build());
			} catch (ParseException e) {
				throw new AssertionError("Database did not save properly");
			}
		} while (c.moveToNext());
		return p;
	}

	/**
	 * Returns the week of the stored menus.
	 * 
	 * @return Number of week of the menus.
	 */
	public int getWeekOfStoredMenus() {
		Cursor c = database.query(MenusTable.TABLE_MENUS, new String[] { MenusTable.COL_DATE }, null, null,
				MenusTable.COL_DATE, null, null);

		final int POS_DATE = c.getColumnIndex(MenusTable.COL_DATE);
		c.moveToFirst();
		int minWeek = Integer.MAX_VALUE;
		do {
			try {
				String dateString = c.getString(POS_DATE);
				Calendar cal = Calendar.getInstance(Locale.GERMAN);
				cal.setTime(fm.parse(dateString));
				int week = cal.get(Calendar.WEEK_OF_YEAR);
				minWeek = week < minWeek ? week : minWeek;
			} catch (ParseException e) {
				throw new AssertionError("Database did not save properly");
			}
		} while (c.moveToNext());
		return minWeek;
	}

	/**
	 * Deletes all menus from the database.
	 */
	public void deleteMenus() {
		database.delete(MenusTable.TABLE_MENUS, null, null);
		database.execSQL("delete from sqlite_sequence where name='" + MenusTable.TABLE_MENUS + "';");
		database.delete(MenusMensasTable.TABLE_MENUS_MENSAS, null, null);
	}

	/**
	 * Completely clears the whole database.
	 */
	public void cleanUpAllTables() {
		database.delete(MensasTable.TABLE_MENSAS, null, null);
		database.delete(FavoritesTable.TABLE_FAV_MENSAS, null, null);
		database.delete(MenusTable.TABLE_MENUS, null, null);
		database.delete(MenusMensasTable.TABLE_MENUS_MENSAS, null, null);
	}
}