package com.example.viewnews.ui.user;
/*
 * @Author Lxf
 * @Date 2021/9/17 16:04
 * @Description This is description of class
 * @Since version-1.0
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.viewnews.MainActivity;
import com.example.viewnews.R;
import com.example.viewnews.logic.ThreadPool;
import com.example.viewnews.logic.model.UInfo;
import com.example.viewnews.logic.model.UserInfoResponse;
import com.example.viewnews.logic.network.user.InfoNetWork;
import com.example.viewnews.BaseActivity;
import com.example.viewnews.ui.GlideHelper;
import com.tencent.mmkv.MMKV;

import java.util.Calendar;

public class UserDetailActivity extends BaseActivity {
    private static final String DEFAULT = "未设置";
    private ImageView userAvatar;

    private Toolbar detailToolbar;

    private String userId;//用户账号

    public static final int CHOOSE_USER_AVATAR = 11;
    public static final int CHOOSE_USER_PROFILE = 12;

    private UInfo userInfo;

    private Button button;

    private MMKV mmkv;
    private GlideHelper helper;

    // 定义线性布局
    private LinearLayout layout_avatar, layout_nickname, layout_sex, layout_local, layout_signature;

    private TextView showNickName, showSex, showLocation, showSignature;

    //private Calendar calendar;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        MMKV.initialize(this);
        mmkv = MMKV.defaultMMKV();

        detailToolbar = findViewById(R.id.userData_toolbar);
        detailToolbar.setTitle("个人信息");
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }
        layout_avatar = findViewById(R.id.lay_avatar);
        layout_nickname = findViewById(R.id.lay_nickname);

        layout_sex = findViewById(R.id.lay_sex);
        //layout_birth = findViewById(R.id.lay_birthday);
        layout_local = findViewById(R.id.lay_birthday);
        layout_signature = findViewById(R.id.lay_signature);

        userAvatar = findViewById(R.id.user_avatar);

        showNickName = findViewById(R.id.show_name);
        showSex = findViewById(R.id.show_sex);

        showLocation = findViewById(R.id.show_birthday);
        showSignature = findViewById(R.id.show_sign);

        button = findViewById(R.id.infoBtn);
        button.setText("保存");//这里应该改成硬解码

        progressBar = findViewById(R.id.progress);

        userId = mmkv.decodeString("name","");
        initData(userId);
        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
                //上传数据的唯一入口
                userInfo = new UInfo();//创建userInfo
                userInfo.setName(mmkv.decodeString("name",DEFAULT));
                userInfo.setuDetail(new UInfo.UDetail());
                userInfo.getuDetail().setNickName(mmkv.decodeString("NickName",DEFAULT));
                userInfo.getuDetail().setUserSex(mmkv.decodeString("Sex",DEFAULT));
                userInfo.getuDetail().setUserLocation(mmkv.decodeString("Location",DEFAULT));
                userInfo.getuDetail().setUserSignature(mmkv.decodeString("Signature",DEFAULT));
                userInfo.getuDetail().setImageUrl(mmkv.decodeString("Url",DEFAULT));

                ThreadPool.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Looper myLooper = Looper.myLooper();
                        if (myLooper == null) {//使用Loop保证在子线程可以使用Toast
                            Looper.prepare();
                            myLooper = Looper.myLooper();
                        }

                        UserInfoResponse response = InfoNetWork.setInfo(userInfo);
                        if(response.getStatus()){//设置成功
                            Log.d("UserDetailActivity"," success in create: " + response.getMessage());
                            Toast.makeText(UserDetailActivity.this,"保存成功",Toast.LENGTH_LONG).show();
                        }
                        else{//设置失败
                            Log.d("UserDetailActivity"," fail in create: " + response.getMessage());
                            Toast.makeText(UserDetailActivity.this,"保存失败",Toast.LENGTH_LONG).show();
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressBar.getVisibility() == View.VISIBLE)
                                    progressBar.setVisibility(View.GONE);
                            }
                        });

                        if (myLooper != null) {//防止内存泄漏，关闭Looper
                            Looper.loop();
                            myLooper.quit();
                        }

                    }
                });

            }
        });

    }

    // 初始化数据
    private void initData(String userId) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                UserInfoResponse response = InfoNetWork.getInfo(userId);
                if(response.getStatus()){//请求成功
                    userInfo = response.getData();
                    mmkv.encode("Sex",userInfo.getuDetail().getUserSex());
                    //地区
                    mmkv.encode("Location",userInfo.getuDetail().getUserLocation());
                    //个性签名
                    mmkv.encode("Signature",userInfo.getuDetail().getUserSignature());
                    //昵称
                    mmkv.encode("NickName",userInfo.getuDetail().getnickname());
                    //图片路径
                    mmkv.encode("Url",userInfo.getuDetail().getImageUrl());
                    Log.d("UserDetailActivity",response.getMessage());
                }
                else {//请求失败
                    Log.d("UserDetailActivity",response.getMessage());
                }

            }
        }).start();


        showNickName.setText(mmkv.decodeString("NickName","未设置"));
        showSex.setText(mmkv.decodeString("Sex","未设置"));
        showLocation.setText(mmkv.decodeString("Location","未设置"));
        showSignature.setText(mmkv.decodeString("Signature","未设置"));
        String curImagePath = mmkv.decodeString("Url","");
        helper = new GlideHelper(getApplicationContext());
        helper.setGilde(curImagePath,userAvatar);
        //diplayImage(curImagePath);
    }

    // 在活动由不可见变为可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();
        layout_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent,CHOOSE_USER_PROFILE);


      /*          if (ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserDetailActivity.this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }*/
            }
        });
        layout_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(UserDetailActivity.this)
                        .title("修改昵称")
                        .inputRangeRes(2, 8, R.color.colorBlack)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("请输入要修改的昵称", mmkv.decodeString("NickName","未设置"), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // CharSequence的值是可读可写序列，而String的值是只读序列。
                                //Toast.makeText(UserDetailActivity.this, input, Toast.LENGTH_SHORT).show();

                                System.out.println(input.toString());
                                // 重新设置值，当前活动被销毁时才保存到数据库
                                mmkv.encode("NickName",input.toString());
                                showNickName.setText(input.toString());
                            }
                        })
                        .positiveText("确定")
                        .show();

            }
        });
        layout_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] contentSex = new String[]{"男", "女"};
                new MaterialDialog.Builder(UserDetailActivity.this)
                        .title("修改性别")
                        .items(contentSex)
                        .itemsCallbackSingleChoice(mmkv.decodeString("Sex","未设置").equals("女") ? 1 : 0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                System.out.println("选择哪一个" + which);
                                System.out.println("选择的内容是" + text);

                                mmkv.encode("Sex",text.toString());
                                showSex.setText(text.toString());
                                return true;
                            }
                        })
                        .show();
            }
        });

        layout_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialDialog.Builder(UserDetailActivity.this)
                        .title("选择地区")
                        .inputRangeRes(1, 38, R.color.colorBlack)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("请输入要修改的地区信息", mmkv.decodeString("Location","未设置"), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                mmkv.encode("Location",input.toString());
                                showLocation.setText(input.toString());
                            }
                        })
                        .positiveText("确定")
                        .show();
            }
        });

        layout_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(UserDetailActivity.this)
                        .title("修改个性签名")
                        .inputRangeRes(1, 38, R.color.colorBlack)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("请输入要修改的个性签名", mmkv.decodeString("Signature","未设置"), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                System.out.println(input.toString());

                                mmkv.encode("Signature",input.toString());
                                showSignature.setText(input.toString());
                            }
                        })
                        .positiveText("确定")
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://返回主界面时保存，这里是不准备保存
                // 保存更改的数据,即向服务器发送请求

                if(progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);

                Intent intent = new Intent(UserDetailActivity.this,MainActivity.class);
                userInfo = new UInfo();//创建userInfo
                userInfo.setName(mmkv.decodeString("name",DEFAULT));
                userInfo.setuDetail(new UInfo.UDetail());
                userInfo.getuDetail().setNickName(mmkv.decodeString("NickName",DEFAULT));
                userInfo.getuDetail().setUserSex(mmkv.decodeString("Sex",DEFAULT));
                userInfo.getuDetail().setUserLocation(mmkv.decodeString("Location",DEFAULT));
                userInfo.getuDetail().setUserSignature(mmkv.decodeString("Signature",DEFAULT));
                userInfo.getuDetail().setImageUrl(mmkv.decodeString("Url",DEFAULT));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserInfoResponse response = InfoNetWork.setInfo(userInfo);
                        if(response.getStatus()){//设置成功
                            Log.d("UserDetailActivity"," success in create: " + response.getMessage());
                        }
                        else{//设置失败
                            Log.d("UserDetailActivity"," fail in create: " + response.getMessage());
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressBar.getVisibility() == View.VISIBLE) {
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                    UserDetailActivity.this.finish();
                                }
                            }
                        });

                    }
                }).start();


                break;
        }
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_USER_PROFILE:
                if(resultCode == Activity.RESULT_OK && data != null){
                    Uri uri = data.getData();
                    if(uri != null){
                        userAvatar = findViewById(R.id.user_avatar);
                        GlideHelper helper = new GlideHelper(getApplicationContext());

                        String uri_s = uri.toString();
                        helper.setGilde(uri_s,userAvatar);
                        MMKV.defaultMMKV().encode("Url",uri_s);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"图片选择错误",Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("UserDetailActivity","个人信息页面被销毁。。。。。。。。。。");
    }



}
