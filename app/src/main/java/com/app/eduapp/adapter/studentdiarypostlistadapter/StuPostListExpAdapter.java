package com.app.eduapp.adapter.studentdiarypostlistadapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.pojo.StudentPostList;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

public class StuPostListExpAdapter extends ExpandableRecyclerAdapter<StudentPostList.DiaryData, StudentPostList.DiaryList, DiaryPostParentHolder, DiaryPostChildHolder> {
    private LayoutInflater mInflater;
    private List<StudentPostList.DiaryData> mRecipeList;
    private static final int PARENT_NORMAL = 1;
    private static final int CHILD_NORMAL = 3;
    Context mContext;

    public StuPostListExpAdapter(Context mContext, @NonNull List<StudentPostList.DiaryData> parentList) {
        super(parentList);
        this.mContext=mContext;
        mRecipeList = parentList;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public DiaryPostParentHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView;
        switch (viewType) {
            default:
            case PARENT_NORMAL:
                recipeView = mInflater.inflate(R.layout.group_item_transparent, parentViewGroup, false);
                break;
        }
        return new DiaryPostParentHolder(recipeView);
    }

    @NonNull
    @Override
    public DiaryPostChildHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView;
        switch (viewType) {
            default:
            case CHILD_NORMAL:
                ingredientView = mInflater.inflate(R.layout.child_item_blue, childViewGroup, false);
                break;
        }
        return new DiaryPostChildHolder(ingredientView,mContext);
    }

    @Override
    public void onBindParentViewHolder(@NonNull DiaryPostParentHolder parentViewHolder, int parentPosition, @NonNull StudentPostList.DiaryData parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull DiaryPostChildHolder childViewHolder, int parentPosition, int childPosition, @NonNull StudentPostList.DiaryList child) {
        childViewHolder.bind(child);
    }

  /*  @Override
    public void onBindParentViewHolder(@NonNull DiaryPostParentHolder parentViewHolder, int parentPosition, @NonNull Notice.NoticeData parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull DiaryPostChildHolder childViewHolder, int parentPosition, int childPosition, @NonNull Notice.NoticeList child) {
        childViewHolder.bind(child);
    }*/

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
