package com.anye.lsearchview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿QQ浏览器搜索
 * Created by anye on 18-1-30.
 */

public class LSearchView extends LinearLayout {

    private SHistorySQLiteOpenHelper mHelper;
    private SQLiteDatabase db;
    private SCallBack sCallBack;
    private BCallBack bCallBack;
    private DCallBack dCallBack;

    private LClearEditText lClearEditText;
    private TextView mCancel;
    private TextView mClearAll;

    private ListView mListView;
    private BaseAdapter mAdapter;

    private List<String> mDatas;

    public LSearchView(Context context) {
        super(context);
        init();
    }

    public LSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 1. 初始化组件
        initView();

        // 实例化数据库
        mHelper = new SHistorySQLiteOpenHelper(getContext());

        queryData("");

        mClearAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllData();
                queryData("");
            }
        });

        /**
         * 监听更换后的搜索按钮
         * 点击时，搜索事件
         */
        lClearEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (sCallBack != null) {
                        sCallBack.SearchAction(lClearEditText.getText().toString());
                    }
//                    Toast.makeText(getContext(), "需要搜索的是：" + lClearEditText.getText().toString(), Toast.LENGTH_LONG).show();
                    findAndInsertDate(lClearEditText.getText().toString().trim());
                }
                return false;
            }
        });

        lClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = lClearEditText.getText().toString();
                mCancel.setText(TextUtils.isEmpty(name) ? getResources().getString(R.string.l_search_cancel)
                        : getResources().getString(R.string.l_search_hint));
                queryData(name);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = mDatas.get(i);
                sCallBack.SearchAction(name);
            }
        });

        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = lClearEditText.getText().toString().trim();
                if (bCallBack != null && TextUtils.isEmpty(name)) {
                    bCallBack.BackAction();
                } else if (sCallBack != null) {
                    sCallBack.SearchAction(name);
                    findAndInsertDate(name);
                }

            }
        });
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.l_search_view, this);

        lClearEditText = findViewById(R.id.l_search_et);
        mCancel = findViewById(R.id.l_search_cancel);
        mClearAll = findViewById(R.id.l_search_clear);
        mListView = findViewById(R.id.l_search_listview);

        setdCallBack(new DCallBack() {
            @Override
            public void DeleteAction(String name) {
                deleteDataByName(name);
                queryData("");
            }
        });
    }

    public void setsCallBack(SCallBack sCallBack) {
        this.sCallBack = sCallBack;
    }

    public void setbCallBack(BCallBack bCallBack) {
        this.bCallBack = bCallBack;
    }


    private void setdCallBack(DCallBack dCallBack) {
        this.dCallBack = dCallBack;
    }

    /**
     * 查询数据库看是否存在重复数据显
     * １、重复
     * ２、不重复则插入数据更新
     *
     * @param tempName
     */
    private void findAndInsertDate(String tempName) {
        boolean hasData = find(tempName);
        if (!hasData) {
            insertData(tempName);
            queryData("");
        }
    }

    /**
     * 查询数据库更新历史显示
     *
     * @param tempName
     */
    private void queryData(String tempName) {
        mDatas = findData(tempName);
        mAdapter = new SRecordsAdapter(getContext(), mDatas, dCallBack);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetInvalidated();

        if (TextUtils.isEmpty(tempName) && mDatas.size() != 0) {
            mClearAll.setVisibility(VISIBLE);
        } else {
            mClearAll.setVisibility(INVISIBLE);
        }
    }

    /**
     * 模糊查询数据库
     *
     * @param tempName
     */
    private List<String> findData(String tempName) {
        List<String> datas = new ArrayList<>();
        db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from records where name like '%" + tempName + "%' order by id desc", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            datas.add(name);
        }
        cursor.close();
        db.close();
        return datas;
    }


    /**
     * 插入数据库
     *
     * @param name
     */
    private void insertData(String name) {
        db = mHelper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + name + "')");
        db.close();
    }

    /**
     * 删除表里的所有数据
     */
    private void deleteAllData() {
        db = mHelper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
        mClearAll.setVisibility(INVISIBLE);
    }

    /**
     * 根据name删除数据
     *
     * @param name
     */
    private void deleteDataByName(String name) {
        db = mHelper.getReadableDatabase();
        db.execSQL("delete from records where name=?", new Object[]{name});
        db.close();
    }

    /**
     * 查询数据库中是否已经存在数据
     *
     * @param name
     */
    private boolean find(String name) {
        db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from records where name=?",
                new String[]{name});
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }

}
