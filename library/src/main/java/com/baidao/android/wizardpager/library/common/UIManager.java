package com.baidao.android.wizardpager.library.common;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.baidao.android.wizardpager.library.model.Page;

/**
 * Created by bruce on 14-9-17.
 */
public class UIManager {

    public static void setActionBarTitle(Page page, Fragment fragment) {
        if (null == page) {
            return;
        }

        if (page.isShowTitleInActionBar()) {
            if (fragment.getUserVisibleHint()) {
                fragment.getActivity().getActionBar().setTitle(page.getTitle());
            }
        }
    }

    public static void setTitle(Page page, Fragment fragment, View rootView) {
        if (page.isShowTitleInActionBar()) {
            if (fragment.getUserVisibleHint()) {
                fragment.getActivity().getActionBar().setTitle(page.getTitle());
            }
        }
        if (page.isShowTitleInContent()) {
            ((TextView) rootView.findViewById(android.R.id.title)).setText(page.getTitle());
        } else {
            View view = rootView.findViewById(android.R.id.title);
            if (null != view) {
                view.setVisibility(View.GONE);
            }
        }
    }
}
