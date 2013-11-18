package com.ese2013.mub.model;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import android.os.AsyncTask;

//import com.ese2013.mub.util.translate.TranslationTask;

/**
 * Stores all data which make up a menu. Always created using the
 * Menu.MenuBuilder class.
 */
public class Menu {
	private String title, description;
	private Day date;
	private String titleTranslated, descriptionTranslated;

	/**
	 * Creates a Menu from a given MenuBuilder. Is private to ensure that Menus
	 * are only created by using the build() method from the MenuBuilder class.
	 * 
	 * @param builder
	 *            MenuBuilder containing all information to instantiate a Menu.
	 *            Must not be null;
	 */
	private Menu(MenuBuilder builder) {
		this.title = builder.title;
		this.description = builder.description;
		this.date = builder.date;
		this.titleTranslated = builder.titleTranslated;
		this.descriptionTranslated = builder.descriptionTranslated;
		//
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Day getDate() {
		return date;
	}
	
	public String getTitleTranslated() {
		return titleTranslated;
	}

	public String getDescriptionTranslated() {
		return descriptionTranslated;
	}

	/**
	 * Converts the date when the menu is served to a string. This string
	 * depends on the Locale settings and should only be used for visual output.
	 * 
	 * @return String containing the date of the menu in a long format (e.g.
	 *         "Monday, 14. October 2013").
	 */
	public String getDateString() {
		return date.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other instanceof Menu) {
			Menu otherMenu = (Menu) other;
			if (otherMenu.getDate() == null ? this.date != null : !otherMenu.getDate().equals(this.date))
				return false;
			if (!otherMenu.getTitle().equals(this.title))
				return false;
			if (!otherMenu.getDescription().equals(this.description))
				return false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + title.hashCode();
		result = 31 * result + description.hashCode();
		result = 31 * result + date.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Menu { \n" + "  Title: " + title + "\n  Description: " + description + "\n  Date: " + getDateString()
				+ " \n }";
	}
	
	

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	/**
	 * Standard builder class used to construct Menu objects.
	 */
	public static class MenuBuilder {
		private static final String DEFAULT = "N//A";
		private String title = DEFAULT, description = DEFAULT;
		private String titleTranslated = DEFAULT, descriptionTranslated = DEFAULT;
		private Day date;

		public MenuBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public MenuBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public MenuBuilder setDescriptionTranslated(String descriptionTranslated) {
			this.descriptionTranslated = descriptionTranslated;
			return this;
		}
		
		public MenuBuilder setTitleTranslated(String titleTranslated) {
			this.titleTranslated = titleTranslated;
			return this;
		}

		public MenuBuilder setDate(Day date) {
			this.date = date;
			return this;
		}

		public Menu build() {
			return new Menu(this);
		}
	}
	
		
}
