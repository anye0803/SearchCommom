package com.anye.lsearchview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 *
 * Created by anye on 18-1-31.
 */

public class SRecordsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private DCallBack mDCallBack;
    public SRecordsAdapter(Context context,List<String> datas,DCallBack dCallBack) {
        this.mContext = context;
        this.mDatas = datas;
        this.mDCallBack = dCallBack;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.l_search_record, null);
            viewHolder.mName = view.findViewById(R.id.l_search_record_name);
            viewHolder.mDelete = view.findViewById(R.id.l_search_record_delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mName.setText(mDatas.get(i));
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDCallBack.DeleteAction(mDatas.get(i));
            }
        });
        return view;
    }

    class ViewHolder{
        TextView mName;
        ImageView mDelete;
    }
}
