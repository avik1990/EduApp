package com.app.eduapp.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.ExamListBinding;
import com.app.eduapp.pojo.ExamListBean;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class ExamNameAdapter extends RecyclerView.Adapter<ExamNameAdapter.SimpleObjectHolder> {

  ExamListBinding binding;
  List<ExamListBean.ExamList> item;
  ClickEventLisener clickEventLisener;

  public ExamNameAdapter(Context mContext, List<ExamListBean.ExamList> list, ClickEventLisener clickEventLisener) {
    super();
    this.item=list;
    this.clickEventLisener=clickEventLisener;

  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil
        .inflate(LayoutInflater.from(parent.getContext()), R.layout.exam_list, parent, false);
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
    ExamListBinding binding;

    public SimpleObjectHolder(ExamListBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;

      itemView.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickEventLisener.clickTrigger(view,getLayoutPosition());
        }
      });
    }

    public void bindConnection(ExamListBean.ExamList obj) {
     binding.setExamName(obj);

    }


  }
}