package com.ese2013.mub.util.database.tables;

public class MenusMensasTable extends AbstractTable {
	
	public static final String TABLE_MENUS_MENSAS = "menusMensas";
	
	private static final String TABLE_MENUS_MENSAS_CREATE = 
			"create table " + TABLE_MENUS_MENSAS + "(" + 
			MenusTable.COL_ID + " integer not null references " + MenusTable.TABLE_MENUS + "(" + MenusTable.COL_ID + ") on delete cascade," + 
			MensasTable.COL_ID + " integer not null references " + MensasTable.TABLE_MENSAS + "(" + MensasTable.COL_ID + ") on delete cascade," +
			"primary key(" + MenusTable.COL_ID + ","+ MensasTable.COL_ID + "));";
	
	public MenusMensasTable() {
		super(TABLE_MENUS_MENSAS, TABLE_MENUS_MENSAS_CREATE);
	}

}
