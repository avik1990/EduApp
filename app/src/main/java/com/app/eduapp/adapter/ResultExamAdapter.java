package com.app.eduapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.RexamListBinding;
import com.app.eduapp.pojo.ExamResultListBean;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class ResultExamAdapter extends RecyclerView.Adapter<ResultExamAdapter.SimpleObjectHolder> {

  RexamListBinding binding;
  List<ExamResultListBean.ExamResultList> item;
  ClickEventLisener clickEventLisener;

  public ResultExamAdapter(Context mContext, List<ExamResultListBean.ExamResultList> list, ClickEventLisener clickEventLisener) {
    super();
    this.item=list;
    this.clickEventLisener=clickEventLisener;
  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil
        .inflate(LayoutInflater.from(parent.getContext()), R.layout.rexam_list, parent, false);
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
    RexamListBinding binding;

    public SimpleObjectHolder(RexamListBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;

      itemView.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickEventLisener.clickTrigger(view,getLayoutPosition());
        }
      });
    }

    public void bindConnection(ExamResultListBean.ExamResultList obj) {
     binding.setExamName(obj);
    }

  }
}