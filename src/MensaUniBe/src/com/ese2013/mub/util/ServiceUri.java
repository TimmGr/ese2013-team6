package com.ese2013.mub.util;

public class ServiceUri {
	private static final String TOKEN = "?tok=6112255ca02b3040711015bbbda8d955";
	public static final String GET_MENSAS = "http://mensa.xonix.ch/v1/mensas" + TOKEN;
	public static final String GET_WEEKLY_MENUPLAN = "http://mensa.xonix.ch/v1/mensas/:id/weeklyplan" + TOKEN;;
	public static final String GET_UPDATE_STATUS = "http://mensa.xonix.ch/v1/mensas/updates" + TOKEN;
}