package com.app.eduapp.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.databinding.RowExamRoutinListBinding;
import com.app.eduapp.pojo.ExamSubjects;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class ExamRoutineAdapter extends RecyclerView.Adapter<ExamRoutineAdapter.SimpleObjectHolder> {

  RowExamRoutinListBinding binding;
  List<ExamSubjects.ExamSubjectsList> item;

  public ExamRoutineAdapter(Context mContext, List<ExamSubjects.ExamSubjectsList> list) {
    super();
    this.item=list;
  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_exam_routin_list, parent, false);
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
    RowExamRoutinListBinding binding;

    public SimpleObjectHolder(RowExamRoutinListBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;
    }

    public void bindConnection(ExamSubjects.ExamSubjectsList obj) {
     binding.setRoutine(obj);
    }
  }
}