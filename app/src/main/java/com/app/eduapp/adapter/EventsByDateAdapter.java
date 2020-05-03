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
import com.app.eduapp.databinding.EventListBinding;
import com.app.eduapp.pojo.EventsByDate;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class EventsByDateAdapter extends RecyclerView.Adapter<EventsByDateAdapter.SimpleObjectHolder> {

  EventListBinding binding;
  List<EventsByDate.EventsList> item;
  ClickEventLisener clickEventLisener;

  public EventsByDateAdapter(Context mContext, List<EventsByDate.EventsList> list, ClickEventLisener clickEventLisener) {
    super();
    this.item=list;
    this.clickEventLisener=clickEventLisener;

  }

  @NonNull
  @Override
  public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    binding = DataBindingUtil
        .inflate(LayoutInflater.from(parent.getContext()), R.layout.event_list, parent, false);
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
    EventListBinding binding;

    public SimpleObjectHolder(EventListBinding itemView) {
      super(itemView.getRoot());
      binding = itemView;

      itemView.getRoot().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickEventLisener.clickTrigger(view,getLayoutPosition());
        }
      });
    }

    public void bindConnection(EventsByDate.EventsList obj) {
     binding.setEventName(obj);
    }
  }
}