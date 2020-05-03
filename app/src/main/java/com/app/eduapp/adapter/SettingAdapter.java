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
import com.app.eduapp.databinding.SettingStudentSwitchBinding;
import com.app.eduapp.pojo.StudentNameBean;

import java.util.List;


/**
 * Created by user2 on 15-05-2018.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SimpleObjectHolder> {

  SettingStudentSwitchBinding binding;
  List<StudentNameBean> item;
  ClickEventLisener clickEventLisener;
  Context mContext;

  public SettingAdapter(Context mContext, List<StudentNameBean> list, ClickEventLisener clickEventLisener) {
    super();
    this.item=list;
    this.mContext=mContext;
    this.clickEventLisener=clickEventLisener;

  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil
        .inflate(LayoutInflater.from(parent.getContext()), R.layout.setting_student_switch, parent, false);
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
    SettingStudentSwitchBinding binding;

    public SimpleObjectHolder(SettingStudentSwitchBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;

      itemView.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        //  clickEventLisener.clickTrigger(view,getLayoutPosition());
        }
      });
    }
    public void onSwitchClicked(View view) {
      if (view==binding.noIv){
        StudentNameBean studentNameBean= (StudentNameBean) view.getTag();//here you get all details of clicked item...
        binding.noIv.setImageResource(R.drawable.no_active);
        binding.yesIv.setImageResource(R.drawable.yes_deactive);
        //save value in sharedpreference
      }
      else if (view==binding.yesIv){
        //save value in sharedpreference
        binding.yesIv.setImageResource(R.drawable.yes_active);
        binding.noIv.setImageResource(R.drawable.no_deactive);
      }

    }

    public void bindConnection(StudentNameBean obj) {
      SimpleObjectHolder offerViewHoder = new SimpleObjectHolder(binding);
      binding.setHandlers(offerViewHoder);
      binding.setStudentbean(obj);

    }
  }
}