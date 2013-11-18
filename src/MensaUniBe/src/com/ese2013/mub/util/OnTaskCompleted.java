package com.ese2013.mub.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public interface OnTaskCompleted{


	void onTaskCompleted(String[] translated, ViewGroup container,
			LinearLayout layout, LayoutInflater inf);
}