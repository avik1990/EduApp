package com.app.eduapp.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.ScommentListBinding;
import com.app.eduapp.pojo.SCommentBean;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class StupostcommentsAdapter extends RecyclerView.Adapter<StupostcommentsAdapter.SimpleObjectHolder> {

  ScommentListBinding binding;
  List<SCommentBean.DiaryCommnetsList> item;
  ClickEventLisener clickEventLisener;

  public StupostcommentsAdapter(Context mContext, List<SCommentBean.DiaryCommnetsList> list , ClickEventLisener clickEventLisener) {
    super();
    this.item=list;
    this.clickEventLisener=clickEventLisener;

  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.scomment_list, parent, false);
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
    ScommentListBinding binding;

    public SimpleObjectHolder(ScommentListBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;
     /* itemView.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickEventLisener.clickTrigger(view,getLayoutPosition());
        }
      });*/
    }

    public void bindConnection(SCommentBean.DiaryCommnetsList obj) {
     binding.setExamName(obj);
    }


  }
}