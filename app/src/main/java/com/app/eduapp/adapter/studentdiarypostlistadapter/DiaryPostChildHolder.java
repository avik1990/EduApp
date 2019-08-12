package com.app.eduapp.adapter.studentdiarypostlistadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.eduapp.R;
import com.app.eduapp.DiaryPostDetails;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.StudentPostList;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

class DiaryPostChildHolder extends ChildViewHolder {
    private TextView mIngredientTextView, tv_showindiarylist;
    Context mContext;
    RelativeLayout v_icon, v_main;

    public DiaryPostChildHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        mIngredientTextView = itemView.findViewById(R.id.tv_childname);
        tv_showindiarylist = itemView.findViewById(R.id.tv_showindiarylist);
        v_icon = itemView.findViewById(R.id.v_icon);
        v_main = itemView.findViewById(R.id.v_main);

        if (EdUtils.get_userType(mContext).equalsIgnoreCase("TCH")) {
            tv_showindiarylist.setVisibility(View.VISIBLE);
        } else {
            tv_showindiarylist.setVisibility(View.GONE);
        }
    }

    public void bind(@NonNull final StudentPostList.DiaryList ingredient) {
        mIngredientTextView.setText(ingredient.getPostHeading());
        tv_showindiarylist.setText(ingredient.getShowInDiaryList());

        if (ingredient.getDiaryApplicationBy().equalsIgnoreCase("PRN")) {
            v_main.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_lightblue));
        } else {
            v_main.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_deepblue));
        }


        v_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DiaryPostDetails.class);
                i.putExtra("DiaryPostID", ingredient.getDiaryPostID());
                i.putExtra("SectionID", ingredient.getSectionID());
                i.putExtra("SubjectsID", ingredient.getSubjectsID());
                i.putExtra("ClassID", ingredient.getClassID());
                mContext.startActivity(i);
            }
        });
    }
}

