package com.app.eduapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.eduapp.R;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

public class MatrixTableAdapterStatusPending<T> extends BaseTableAdapter {

    private final static int WIDTH_DIP = 107;
    private final static int HEIGHT_DIP = 55;

    private final Context context;

    private T[][] table;

    private final int width;
    private final int height;

    public MatrixTableAdapterStatusPending(Context context) {
        this(context, null);
    }

    public MatrixTableAdapterStatusPending(Context context, T[][] table) {
        this.context = context;
        Resources r = context.getResources();

        width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP, r.getDisplayMetrics()));
        height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_DIP, r.getDisplayMetrics()));

        setInformation(table);
    }

    public void setInformation(T[][] table) {
        this.table = table;
    }

    @Override
    public int getRowCount() {
        return table.length - 1;
    }

    @Override
    public int getColumnCount() {
        return table[0].length - 1;
    }

    @Override
    public View getView(final int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(context);
            ((TextView) convertView).setGravity(Gravity.CENTER);
        }

        if (table[row + 1][column + 1] != null) {
            ((TextView) convertView).setText(Html.fromHtml(table[row + 1][column + 1].toString()));
        } else {
            ((TextView) convertView).setText(" - ");
        }

        if (row < 0) {
            ((TextView) convertView).setTextColor(context.getResources().getColor(R.color.white));
            convertView.setBackgroundColor(context.getResources().getColor(R.color.table_header));
        }

        if (row >= 0) {
            if ((row) % 2 == 0) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.light_pink));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.light_green_table));
            }
			
/*			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent=new Intent(context,ActivityGeneralCaseDetails.class);
					intent.putExtra("position", String.valueOf(row));
					context.startActivity(intent);
					
					
				}
			});*/

        }

        return convertView;
    }

    @Override
    public int getHeight(int row) {
        return height;
    }

    @Override
    public int getWidth(int column) {
        return width;
    }

    @Override
    public int getItemViewType(int row, int column) {
        return row;
    }

    @Override
    public int getViewTypeCount() {

        if (table.length != 0)
            return table.length;

        return 1;
    }
}
