package com.baidao.android.wizardpager.library.model;

import android.support.v4.app.Fragment;

import com.baidao.android.wizardpager.library.ui.NumberFragment;


public class NumberPage extends TextPage {

	public NumberPage(ModelCallbacks callbacks, String title) {
		super(callbacks, title);
	}

	@Override
	public Fragment createFragment() {
		return NumberFragment.create(getKey());
	}

}
