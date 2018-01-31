package com.anye.lsearchview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *
 * Created by anye on 18-1-31.
 */

public class LSearchListView extends ListView {

    public LSearchListView(Context context) {
        super(context);
    }

    public LSearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LSearchListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //通过复写其onMeasure方法、达到对ScrollView适配的效果
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
