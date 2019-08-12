package com.app.eduapp.adapter.noticeadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.pojo.Notice;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

public class NoticeExpAdapter extends ExpandableRecyclerAdapter<Notice.NoticeData, Notice.NoticeList, GroupParentHolder, NoticeChildHolder> {
    private LayoutInflater mInflater;
    private List<Notice.NoticeData> mRecipeList;
    private static final int PARENT_NORMAL = 1;
    private static final int CHILD_NORMAL = 3;
    Context mContext;

    public NoticeExpAdapter(Context mContext, @NonNull List<Notice.NoticeData> parentList) {
        super(parentList);
        this.mContext=mContext;
        mRecipeList = parentList;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public GroupParentHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView;
        switch (viewType) {
            default:
            case PARENT_NORMAL:
                recipeView = mInflater.inflate(R.layout.group_item, parentViewGroup, false);
                break;
        }
        return new GroupParentHolder(recipeView);
    }

    @NonNull
    @Override
    public NoticeChildHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView;
        switch (viewType) {
            default:
            case CHILD_NORMAL:
                ingredientView = mInflater.inflate(R.layout.child_item, childViewGroup, false);
                break;
        }
        return new NoticeChildHolder(ingredientView,mContext);
    }

    @Override
    public void onBindParentViewHolder(@NonNull GroupParentHolder parentViewHolder, int parentPosition, @NonNull Notice.NoticeData parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull NoticeChildHolder childViewHolder, int parentPosition, int childPosition, @NonNull Notice.NoticeList child) {
        childViewHolder.bind(child);
    }

    @Override
    public int getParentViewType(int parentPosition) {
        return PARENT_NORMAL;
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        return CHILD_NORMAL;
    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == PARENT_NORMAL;
    }
}
