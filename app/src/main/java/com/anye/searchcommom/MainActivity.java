package com.anye.searchcommom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.anye.lsearchview.BCallBack;
import com.anye.lsearchview.SCallBack;
import com.anye.lsearchview.LSearchView;

public class MainActivity extends AppCompatActivity {

    private LSearchView mLSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLSearchView = findViewById(R.id.act_search_view);

        mLSearchView.setbCallBack(new BCallBack() {
            @Override
            public void BackAction() {
                finish();
            }
        });
        mLSearchView.setsCallBack(new SCallBack() {
            @Override
            public void SearchAction(String text) {
                Toast.makeText(getApplicationContext(),"开始搜索："+ text,Toast.LENGTH_LONG).show();
            }
        });
    }
}
