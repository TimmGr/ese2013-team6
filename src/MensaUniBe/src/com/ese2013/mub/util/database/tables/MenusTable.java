package com.ese2013.mub.util.database.tables;

public class MenusTable extends AbstractTable {

	public static final String TABLE_MENUS = "menus";
	public static final String COL_ID = "id";
	public static final String COL_TITLE = "title";
	public static final String COL_DESC = "desc";
	public static final String COL_DATE = "date";
	public static final String COL_TITLE_TRANSLANTED = "title_translated";
	public static final String COL_DESC_TRANSLANTED = "desc_translated";
	
	
	private static final String TABLE_MENUS_CREATE = 
			"create table " + TABLE_MENUS + "(" + 
			COL_ID + " integer primary key AUTOINCREMENT, " + 
			COL_TITLE + " text not null, " + 
			COL_DESC + " text not null, " +
			COL_DATE + " text not null," +
			COL_TITLE_TRANSLANTED + " text," +
			COL_DESC_TRANSLANTED + " text," +
			"constraint uniqueConstraint unique (" + COL_TITLE + "," + COL_DESC + "," + COL_DATE + "));";
	
	public MenusTable() {
		super(TABLE_MENUS, TABLE_MENUS_CREATE);
	}
}
