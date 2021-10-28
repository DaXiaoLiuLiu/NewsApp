package com.example.viewnews.ui.other;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.viewnews.BaseActivity;
import com.example.viewnews.R;


public class OtherSitesActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_sites);
        Button button1=(Button)findViewById(R.id.webSites_FengHuang_button);
        Button button2=(Button)findViewById(R.id.webSites_renmin_button);
        Button button3=(Button)findViewById(R.id.webSites_zgxinwen_button);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Uri uri=null;
        switch (view.getId()){
            case R.id.webSites_FengHuang_button:
                uri = Uri.parse("https://www.ifeng.com/");
                break;
            case R.id.webSites_renmin_button:
                uri = Uri.parse("http://www.people.com.cn/");
                break;
            case R.id.webSites_zgxinwen_button:
                uri = Uri.parse("https://www.chinanews.com/");
                break;
            default:
                break;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
