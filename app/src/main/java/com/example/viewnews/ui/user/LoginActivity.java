package com.example.viewnews.ui.user;

import androidx.annotation.Nullable;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.viewnews.MainActivity;
import com.example.viewnews.R;
import com.example.viewnews.logic.ThreadPool;
import com.example.viewnews.logic.model.UInfo;
import com.example.viewnews.logic.model.User;
import com.example.viewnews.logic.model.UserInfoResponse;
import com.example.viewnews.logic.model.UserResponse;
import com.example.viewnews.logic.network.user.InfoNetWork;
import com.example.viewnews.logic.network.user.LoginNetWork;
import com.example.viewnews.BaseActivity;
import com.tencent.mmkv.MMKV;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LoginActivity extends BaseActivity {

    private MMKV mmkv;
    private EditText userAccount;
    private EditText userPwd;
    private Button loginBtn;
    private Button registerBtn;
    private ProgressBar progressBar;
    private UserResponse responseResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MMKV.initialize(this);

        userAccount = findViewById(R.id.login_userAccount);
        userPwd = findViewById(R.id.login_pwd);
        loginBtn = findViewById(R.id.login_on);
        registerBtn = findViewById(R.id.login_register);
        progressBar = findViewById(R.id.LoginProgressBar);//进度条控件
        progressBar.setVisibility(View.GONE);

        // 在点击编辑资料时，提醒先登录
        Intent intent = getIntent();
        String status = intent.getStringExtra("loginStatus");
        if(status != null) {
            Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numId = userAccount.getText().toString();
                String pwd = userPwd.getText().toString();
                // 先判断输入不能为空
                if(TextUtils.isEmpty(numId) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    if(progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);


                    final User loginUser = new User(numId,pwd);
                    Log.d("LoginActivity","numId is " + loginUser.getUserName());
                    Log.d("LoginActivity","password is " + loginUser.getPassword());

                    ThreadPool.getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            responseResult = null;
                            while (responseResult == null) {//不为null时，跳出循环，此时说明以成功进行一次网络请求
                                responseResult = LoginNetWork.sign_in(loginUser);
                                Log.d("LoginActivity","responseResult被正确置空");
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            Looper myLooper = Looper.myLooper();
                            if (myLooper == null) {//使用Loop保证在子线程可以使用Toast
                                Looper.prepare();
                                myLooper = Looper.myLooper();
                            }


                            if (responseResult.getStatus()) {//说明登录成功
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                                //向服务器发送请求，获取个人信息
                                UInfo result;
                                UserInfoResponse response = InfoNetWork.getInfo(loginUser.getUserName());
                                if(response.getData() != null){//这里是服务器成功返回的结果
                                    result =response.getData();
                                    saveOnHost(loginUser,result);
                                }
                                else {//这里是返回失败，需要用默认值替换的情况,说明还未在服务器上登记
                                    saveOnHost(loginUser);
                                }

                                //跳转到主函数
                                mmkv = MMKV.defaultMMKV();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {//说明登录失败
                                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                                Log.d("LoginActivity", "登陆失败");
                                //测试代码
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            if (myLooper != null) {//防止内存泄漏，关闭Looper
                                Looper.loop();
                                myLooper.quit();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (progressBar.getVisibility() == View.VISIBLE)//关闭进度条
                                        progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    });


                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //此处跳转到注册页面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                // 注册请求码是2
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if(resultCode == RESULT_OK) {
                    Toast.makeText(LoginActivity.this, data.getStringExtra("register_status"), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 登录界面销毁时存储账号
/*        String inputIdText = userAccount.getText().toString();
        save(inputIdText);
        System.out.println("活动毁灭之前是否传值" + inputIdText);*/
    }

    // 存储账号，方便下次启动app时，直接读取账号，并初始化数据
/*    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    //将登录的账号信息存进本地数据库中
    public void saveOnHost(User user){

        mmkv = MMKV.defaultMMKV();
        mmkv.encode("name",user.getUserName());
        mmkv.encode("password",user.getPassword());

        //性别
        mmkv.encode("Sex","待完善");
        //地区
        mmkv.encode("Location","待完善");
        //个性签名
        mmkv.encode("Signature","这个人很懒，TA什么也没留下");
        //昵称
        mmkv.encode("NickName","未设置");
        //图片路径
        mmkv.encode("Url","");
    }

    public void saveOnHost(User user,UInfo uInfo){
        mmkv = MMKV.defaultMMKV();
        mmkv.encode("name",user.getUserName());
        mmkv.encode("password",user.getPassword());

        //性别
        mmkv.encode("Sex",uInfo.getuDetail().getUserSex());
        //地区
        mmkv.encode("Location",uInfo.getuDetail().getUserLocation());
        //个性签名
        mmkv.encode("Signature",uInfo.getuDetail().getUserSignature());
        //昵称
        mmkv.encode("NickName",uInfo.getuDetail().getnickname());
        //图片路径
        mmkv.encode("Url",uInfo.getuDetail().getImageUrl());
    }
}
