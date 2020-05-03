package com.app.eduapp.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.eduapp.R;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.databinding.ReportCardRowBinding;
import com.app.eduapp.pojo.ReportCardBean;

import java.util.List;


/**
 * Created by user2 on 15-05-2018.
 */

public class ReportCardAdapter extends RecyclerView.Adapter<ReportCardAdapter.SimpleObjectHolder> {

    ReportCardRowBinding binding;
    List<ReportCardBean> item;
    Context context;
    ClickEventLisener clickEventLisener;

    public ReportCardAdapter(Context mContext, List<ReportCardBean> list, ClickEventLisener clickEventLisener) {
        super();
        this.item = list;
        this.context = mContext;
    }

    @NonNull
    @Override
    public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.report_card_row, parent, false);
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
        ReportCardRowBinding binding;

        public SimpleObjectHolder(ReportCardRowBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bindConnection(ReportCardBean obj) {
            binding.setReportCard(obj);
        }
    }
}