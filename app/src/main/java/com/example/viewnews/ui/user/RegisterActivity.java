package com.example.viewnews.ui.user;

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

import com.example.viewnews.R;
import com.example.viewnews.logic.ThreadPool;
import com.example.viewnews.logic.model.User;
import com.example.viewnews.logic.model.UserResponse;
import com.example.viewnews.logic.network.user.LoginNetWork;
import com.example.viewnews.BaseActivity;


import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends BaseActivity {

    private EditText reg_userAccount;
    private EditText reg_userPwd;
    private EditText reg_confirm_userPwd;
    private Button registerBtn;
    private ProgressBar progressBar;
    private UserResponse responseResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_userAccount = findViewById(R.id.register_userAccount);//账号
        reg_userPwd = findViewById(R.id.register_pwd);//密码
        reg_confirm_userPwd = findViewById(R.id.confirm_pwd);//确认密码
        registerBtn = findViewById(R.id.register_click);
        progressBar = findViewById(R.id.RegisterProgressBar);
        progressBar.setVisibility(View.GONE);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 首先验证输入是否为空
                String userId = reg_userAccount.getText().toString();
                String userPwd = reg_userPwd.getText().toString();
                String secondPwd = reg_confirm_userPwd.getText().toString();//读取信息


                if(TextUtils.isEmpty(userId) || TextUtils.isEmpty(userPwd) || TextUtils.isEmpty(secondPwd)) {
                    // 判断字符串是否为null或者""
                    Toast.makeText(RegisterActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    // 判断两次输入的密码是否匹配，匹配则写入数据库，并且结束当前活动，自动返回登录界面
                    if(userPwd.equals(secondPwd)) {

                        if(progressBar.getVisibility() == View.GONE)
                            progressBar.setVisibility(View.VISIBLE);

                        Log.d("LoginActivity","numId is " + userId);
                        final User registerUser = new User(userId,userPwd);

                        ThreadPool.getExecutor().execute(new Runnable() {
                            @Override
                            public void run() {

                                responseResult = LoginNetWork.sign_up(registerUser) ;

                                Looper myLooper = Looper.myLooper();
                                if (myLooper == null) {//使用Loop保证在子线程可以使用Toast
                                    Looper.prepare();
                                    myLooper = Looper.myLooper();
                                }


                                if (responseResult.getStatus()) {//说明注册成功
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("register_status", "注册成功");
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {//说明注册失败
                                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                                    Log.d("LoginActivity",  responseResult.getMessage());
                                    //测试代码
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (progressBar.getVisibility() == View.VISIBLE)//关闭进度条
                                                progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                if (myLooper != null) {//防止内存泄漏，关闭Looper
                                    Looper.loop();
                                    myLooper.quit();
                                }

                            }
                        });

                    } else {
                        Toast.makeText(RegisterActivity.this, "两次输入的密码不匹配", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
