package com.app.eduapp.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.databinding.RowResultBinding;
import com.app.eduapp.pojo.ResultSubjectsList;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class ResultSubjectsListAdapter extends RecyclerView.Adapter<ResultSubjectsListAdapter.SimpleObjectHolder> {

  RowResultBinding binding;
  List<ResultSubjectsList.ResultSubjectsListData> item;


  public ResultSubjectsListAdapter(Context mContext, List<ResultSubjectsList.ResultSubjectsListData> list) {
    super();
    this.item=list;

  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil
        .inflate(LayoutInflater.from(parent.getContext()), R.layout.row_result, parent, false);
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

  public class SimpleObjectHolder extends RecyclerView.ViewHolder {
    RowResultBinding binding;

    public SimpleObjectHolder(RowResultBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;
    }

    public void bindConnection(ResultSubjectsList.ResultSubjectsListData obj) {
     binding.setResultlist(obj);
    }
  }
}