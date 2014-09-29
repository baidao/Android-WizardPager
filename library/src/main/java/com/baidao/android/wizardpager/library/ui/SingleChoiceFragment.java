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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidao.android.wizardpager.library.R;
import com.baidao.android.wizardpager.library.common.UIManager;
import com.baidao.android.wizardpager.library.model.Page;
import com.baidao.android.wizardpager.library.model.SingleFixedChoicePage;

import java.util.ArrayList;
import java.util.List;


public class SingleChoiceFragment<T extends Page> extends ListFragment {
    protected static final String ARG_KEY = "key";

    protected PageFragmentCallbacks mCallbacks;
    protected List<String> mChoices;
    protected String mKey;
    protected T mPage;
    protected int contentLayout = R.layout.fragment_page;

    public static SingleChoiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        SingleChoiceFragment fragment = new SingleChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SingleChoiceFragment() {
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
        SingleFixedChoicePage fixedChoicePage = (SingleFixedChoicePage) mPage;
        mChoices = new ArrayList<String>(fixedChoicePage.getOptionCount());
        for (int i = 0; i < fixedChoicePage.getOptionCount(); i++) {
            mChoices.add(fixedChoicePage.getOptionAt(i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentLayout(), container, false);
        UIManager.setTitle(mPage, this, rootView);

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(getAdapter());
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        restoreState(listView);

        return rootView;
    }

    protected ArrayAdapter<String> getAdapter() {
        return new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                mChoices);
    }

    protected void restoreState(final ListView listView) {
        // Pre-select currently selected item.
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String selection = mPage.getData().getString(Page.SIMPLE_DATA_KEY);
                for (int i = 0; i < mChoices.size(); i++) {
                    if (mChoices.get(i).equals(selection)) {
                        listView.setItemChecked(i, true);
                        break;
                    }
                }
            }
        });
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
        mPage.getData().putString(Page.SIMPLE_DATA_KEY,
            getListAdapter().getItem(position).toString());
        mPage.notifyDataChanged();
    }

    public int getContentLayout() {
        return contentLayout;
    }

    public SingleChoiceFragment setContentLayout(int contentLayout) {
        this.contentLayout = contentLayout;
        return this;
    }
}
