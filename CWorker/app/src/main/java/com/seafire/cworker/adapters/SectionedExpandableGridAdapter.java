package com.seafire.cworker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.seafire.cworker.Bean.FreindBean;
import com.seafire.cworker.Bean.Item;
import com.seafire.cworker.R;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lenovo on 2/23/2016.
 */
public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;

    //listeners
    private final ItemClickListener mItemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.layout_section;
    private static final int VIEW_TYPE_ITEM = R.layout.layout_item; //TODO : change this

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                           ItemClickListener itemClickListener,
                                          SectionStateChangeListener sectionStateChangeListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof Section;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM :
                final FreindBean.UsersBean item = (FreindBean.UsersBean) mDataArrayList.get(position);
                holder.itemTextView.setText(item.getName());
                Glide.with(mContext).load(item.getFace())
                        .placeholder(R.drawable.ic_launcher_round)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(holder.itemHeadIcon);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(item);
                    }
                });
                break;
            case VIEW_TYPE_SECTION :
                final Section section = (Section) mDataArrayList.get(position);
                holder.sectionTextView.setText(section.getName());
                holder.sectionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mItemClickListener.itemClicked(section);
                        section.isExpanded = !section.isExpanded;
                        mSectionStateChangeListener.onSectionStateChanged(section, section.isExpanded);
                        if (section.isExpanded){
                            holder.sectionImageView.setPivotX(holder.sectionImageView.getWidth()/2);
                            holder.sectionImageView.setPivotY(holder.sectionImageView.getHeight()/2);//支点在图片中心
                            holder.sectionImageView.setRotation(90);
                        }else{
                            holder.sectionImageView.setPivotX(holder.sectionImageView.getWidth()/2);
                            holder.sectionImageView.setPivotY(holder.sectionImageView.getHeight()/2);//支点在图片中心
                            holder.sectionImageView.setRotation(-90);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;

        //for section
        TextView sectionTextView;
        View sectionView;
        ImageView sectionImageView;

        //for item
        TextView itemTextView;
        ImageView itemHeadIcon;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                itemTextView = (TextView) view.findViewById(R.id.text_item);
                itemHeadIcon = (ImageView) view.findViewById(R.id.head_icon);
            } else {
                sectionTextView = (TextView) view.findViewById(R.id.text_section);
                sectionView = view.findViewById(R.id.section_view);
                sectionImageView = (ImageView) view.findViewById(R.id.group_arrow);
            }
        }
    }
}
