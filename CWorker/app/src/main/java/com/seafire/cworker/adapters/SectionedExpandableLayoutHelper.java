package com.seafire.cworker.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.seafire.cworker.Bean.FreindBean;
import com.seafire.cworker.Bean.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bpncool on 2/23/2016.
 */
public class SectionedExpandableLayoutHelper implements SectionStateChangeListener {

    //data list
    private LinkedHashMap<Section, ArrayList<FreindBean.UsersBean>> mSectionDataMap = new LinkedHashMap<Section, ArrayList<FreindBean.UsersBean>>();
    private ArrayList<Object> mDataArrayList = new ArrayList<Object>();

    //section map
    //TODO : look for a way to avoid this
    private HashMap<String, Section> mSectionMap = new HashMap<String, Section>();

    //adapter
    private SectionedExpandableGridAdapter mSectionedExpandableGridAdapter;

    public SectionedExpandableLayoutHelper(Context context, RecyclerView recyclerView, ItemClickListener itemClickListener) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSectionedExpandableGridAdapter = new SectionedExpandableGridAdapter(context, mDataArrayList,
                itemClickListener, this);
        recyclerView.setAdapter(mSectionedExpandableGridAdapter);
    }

    public void notifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        mSectionedExpandableGridAdapter.notifyDataSetChanged();
    }

    public void addSection(String section, ArrayList<FreindBean.UsersBean> items) {
        Section newSection;
        mSectionMap.put(section, (newSection = new Section(section, items.size())));
        mSectionDataMap.put(newSection, items);
        notifyDataSetChanged();
    }

    public void removeAllData(){
        mSectionDataMap.clear();
        mSectionDataMap.clear();
        mDataArrayList.clear();
    }

    public void addItem(String section, FreindBean.UsersBean item) {
        mSectionDataMap.get(mSectionMap.get(section)).add(item);
    }

    public void removeItem(String section, FreindBean.UsersBean item) {
        mSectionDataMap.get(mSectionMap.get(section)).remove(item);
    }

    public void removeSection(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    private void generateDataList () {
        mDataArrayList.clear();
        for (Map.Entry<Section, ArrayList<FreindBean.UsersBean>> entry : mSectionDataMap.entrySet()) {
            Section key;
            mDataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                mDataArrayList.addAll(entry.getValue());
        }
    }

    @Override
    public void onSectionStateChanged(Section section, boolean isOpen) {
        section.isExpanded = isOpen;
        notifyDataSetChanged();
    }
}
