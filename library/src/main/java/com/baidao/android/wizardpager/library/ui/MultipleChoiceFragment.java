/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidao.android.wizardpager.library.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.baidao.android.wizardpager.library.R;
import com.baidao.android.wizardpager.library.common.UIManager;
import com.baidao.android.wizardpager.library.model.MultipleFixedChoicePage;
import com.baidao.android.wizardpager.library.model.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MultipleChoiceFragment<T extends Page> extends ListFragment {
    protected static final String ARG_KEY = "key";

    protected PageFragmentCallbacks mCallbacks;
    protected String mKey;
    protected List<String> mChoices;
    protected T mPage;

    protected int contentLayout = R.layout.fragment_page;

    public static MultipleChoiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MultipleChoiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (T) mCallbacks.onGetPage(mKey);

        initChoices();
    }

    protected void initChoices() {
        MultipleFixedChoicePage fixedChoicePage = (MultipleFixedChoicePage) mPage;
        mChoices = new ArrayList<String>();
        for (int i = 0; i < fixedChoicePage.getOptionCount(); i++) {
            mChoices.add(fixedChoicePage.getOptionAt(i));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        UIManager.setActionBarTitle(mPage, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentLayout(), container, false);
        UIManager.setTitle(mPage, this, rootView);

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(getAdapter());
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        restoreState(listView);

        return rootView;
    }

    protected void restoreState(final ListView listView) {
        // Pre-select currently selected items.
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> selectedItems = mPage.getData().getStringArrayList(
                        Page.SIMPLE_DATA_KEY);
                if (selectedItems == null || selectedItems.size() == 0) {
                    return;
                }

                Set<String> selectedSet = new HashSet<String>(selectedItems);

                for (int i = 0; i < mChoices.size(); i++) {
                    if (selectedSet.contains(mChoices.get(i))) {
                        listView.setItemChecked(i, true);
                    }
                }
            }
        });
    }

    protected ArrayAdapter<String> getAdapter() {
        return new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice,
                android.R.id.text1,
                mChoices);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SparseBooleanArray checkedPositions = getListView().getCheckedItemPositions();
        ArrayList<String> selections = new ArrayList<String>();
        for (int i = 0; i < checkedPositions.size(); i++) {
            if (checkedPositions.valueAt(i)) {
                selections.add(getListAdapter().getItem(checkedPositions.keyAt(i)).toString());
            }
        }

        mPage.getData().putStringArrayList(Page.SIMPLE_DATA_KEY, selections);
        mPage.notifyDataChanged();
    }

    public int getContentLayout() {
        return R.layout.fragment_page;
    }

    public MultipleChoiceFragment setContentLayout(int contentLayout) {
        this.contentLayout = contentLayout;
        return this;
    }
}
