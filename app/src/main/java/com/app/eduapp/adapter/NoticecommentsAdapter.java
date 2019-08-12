package com.app.eduapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.NcommentListBinding;
import com.app.eduapp.databinding.ScommentListBinding;
import com.app.eduapp.pojo.NCommentBean;
import com.app.eduapp.pojo.SCommentBean;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class NoticecommentsAdapter extends RecyclerView.Adapter<NoticecommentsAdapter.SimpleObjectHolder> {

  NcommentListBinding binding;
  List<NCommentBean.NoticeCommnetsList> item;
  ClickEventLisener clickEventLisener;

  public NoticecommentsAdapter(Context mContext, List<NCommentBean.NoticeCommnetsList> list , ClickEventLisener clickEventLisener) {
    super();
    this.item=list;
    this.clickEventLisener=clickEventLisener;

  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ncomment_list, parent, false);
    return new SimpleObjectHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull SimpleObjectHolder holder, int position) {
    holder.bindConnection(item.get(position));
  }

  @Override
  public int getItemCount() {
    return item.size();
  }

  public class SimpleObjectHolder extends RecyclerView.ViewHolder{
    NcommentListBinding binding;

    public SimpleObjectHolder(NcommentListBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;
     /* itemView.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickEventLisener.clickTrigger(view,getLayoutPosition());
        }
      });*/
    }

    public void bindConnection(NCommentBean.NoticeCommnetsList obj) {
     binding.setExamName(obj);
    }


  }
}