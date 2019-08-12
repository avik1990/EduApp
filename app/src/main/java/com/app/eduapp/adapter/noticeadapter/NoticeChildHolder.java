package com.app.eduapp.adapter.noticeadapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.app.eduapp.NoticeDetails;
import com.app.eduapp.R;
import com.app.eduapp.pojo.Notice;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

class NoticeChildHolder extends ChildViewHolder {

    private TextView mIngredientTextView;
    Context mContext;

    public NoticeChildHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        this.mContext=mContext;
        mIngredientTextView = itemView.findViewById(R.id.tv_childname);
    }

    public void bind(@NonNull final Notice.NoticeList ingredient) {
        mIngredientTextView.setText(ingredient.getNoticeTitle());
        mIngredientTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EdUtils.showToastShort(mContext,ingredient.getNoticeID());
                Intent i=new Intent(mContext, NoticeDetails.class);
                i.putExtra("notice_id",ingredient.getNoticeID());
                mContext.startActivity(i);
            }
        });
    }
}

