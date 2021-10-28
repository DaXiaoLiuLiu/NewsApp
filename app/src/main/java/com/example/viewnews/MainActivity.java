package com.example.viewnews;
/*
 * @Author Lxf
 * @Date 2021/9/13 16:35
 * @Description 登录功能部分准备使用mmkv存储，而不是使用SQL-Lite
 * 需要废弃的layout：item_layout01 03 news_list
 *
 * @Since version-1.0
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.viewnews.ui.GlideHelper;
import com.example.viewnews.ui.news.NewsPageAdapter;
import com.example.viewnews.ui.other.OtherSitesActivity;
import com.example.viewnews.ui.other.TopicActivity;
import com.example.viewnews.ui.user.LoginActivity;
import com.example.viewnews.ui.user.UserDetailActivity;
import com.example.viewnews.ui.news.collection.UserFavoriteActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TabLayout tabLayout;

    private ViewPager2 viewPager2;

    private List<String> list;


    private TextView userNickName, userSignature;

    private ImageView userAvatar;

    // 采用静态变量来存储当前登录的账号
    public static String currentUserId;
    // 记录读者账号，相当于Session来使用
    private String currentUserNickName, currentSignature, currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化MMkv
        MMKV.initialize(this);

        toolbar = findViewById(R.id.toolbar);

        //注意：只需第一次创建或升级本地数据库，第二次运行就注释掉
        //Toast.makeText(MainActivity.this, "创建数据库成功", Toast.LENGTH_LONG).show();

        //获取抽屉布局实例
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //获取菜单控件实例
        navigationView = (NavigationView) findViewById(R.id.nav_design);

        //无法直接通过findViewById方法获取header布局id
        View v = navigationView.getHeaderView(0);

        CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.icon_image);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager2 = (ViewPager2)findViewById(R.id.viewPager2);

        list = new ArrayList<>();
    }

    //在活动由不可见变为可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();


        System.out.println("当前MainActivity活动又被加载onStart");
        //设置标题栏的logo
        //toolbar.setLogo(R.drawable.icon);
        //设置标题栏标题
        toolbar.setTitle("看点新闻");
        //设置自定义的标题栏实例
        setSupportActionBar(toolbar);
        //获取到ActionBar的实例
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //通过HomeAsUp来让导航按钮显示出来
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置Indicator来添加一个点击图标（默认图标是一个返回的箭头）
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //设置默认选中第一个
        navigationView.setCheckedItem(R.id.nav_edit);
        //设置菜单项的监听事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //逻辑页面处理
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_edit://编辑资料

                        Intent Intent = new Intent(MainActivity.this, UserDetailActivity.class);
                        startActivity(Intent);

                        break;
                    case R.id.nav_articles://编辑文章

                        Toast.makeText(MainActivity.this,"功能没做好捏!",Toast.LENGTH_LONG).show();
/*                        Intent editIntent = new Intent(MainActivity.this, ArticleActivity.class);
                        startActivity(editIntent);*/
                        break;
                    case R.id.nav_favorite://文章收藏

                        Intent loveIntent = new Intent(MainActivity.this, UserFavoriteActivity.class);
                        startActivity(loveIntent);
                        break;
                    case R.id.nav_clear_cache://清楚缓存
                        // 清除缓存
                        // Toast.makeText(MainActivity.this,"你点击了清除缓存，下步实现把",Toast.LENGTH_SHORT).show();
                        clearCacheData();
                        break;
                    case R.id.nav_switch://账号切换界面
                        // 切换账号
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        // 登录请求码是1
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.nav_other_sites://第三方跳转区
                        Intent otherSitesIntent=new Intent(MainActivity.this, OtherSitesActivity.class);
                        startActivity(otherSitesIntent);
                        break;
                    case R.id.nav_topic://自编辑文章区
                        Intent topicIntent=new Intent(MainActivity.this, TopicActivity.class);
                        startActivity(topicIntent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //设置tab标题
        list.add("头条");
        list.add("社会");
        list.add("国内");
        list.add("国际");
        list.add("娱乐");
        list.add("体育");
        list.add("军事");
        list.add("科技");
        list.add("财经");

        //表示ViewPager（默认）预加载一页
        //viewPager.setOffscreenPageLimit(1);
        /*
            当fragment不可见时, 可能会将fragment的实例也销毁(执行 onDestory, 是否执行与setOffscreenPageLimit 方法设置的值有关).
            所以内存开销会小些, 适合多fragment的情形.
            具体讲解查看：https://blog.csdn.net/StrongerCoder/article/details/70158836
            https://blog.csdn.net/Mr_LiaBill/article/details/48749807
        */

        viewPager2.setOffscreenPageLimit(1);
        viewPager2.setAdapter(new NewsPageAdapter(this,list));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int i) {
                Log.d("MainActivity","tab is " + list.get(i));
                tab.setText(list.get(i));
            }
        }).attach();

    }

    // 解析、展示图片
    private void diplayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath) && imagePath.length() != 0) {
  
            GlideHelper helper = new GlideHelper(getApplicationContext());
            helper.setGilde(imagePath,userAvatar);
        } else {
            userAvatar.setImageResource(R.drawable.login_fail);
        }
    }




    public void clearCacheData() {
        // 缓存目录为 /data/user/0/com.example.viewnews/cache
        File file = new File(MainActivity.this.getCacheDir().getPath());
        System.out.println("缓存目录为：" + MainActivity.this.getCacheDir().getPath());
        String cacheSize = null;
        try {
            cacheSize = DataCleanManager.getCacheSize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("缓存大小为：" + cacheSize);
        new MaterialDialog.Builder(MainActivity.this)
                .title("提示")
                .content("当前缓存大小一共为" + cacheSize + "。确定要删除所有缓存？离线内容及其图片均会被清除。")
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        // dialog 此弹窗实例共享父实例
                        System.out.println("点击了啥内容：" + which);
                        // 没起作用
                        DataCleanManager.cleanInternalCache(MainActivity.this);
                        Toast.makeText(MainActivity.this, "成功清除缓存。", Toast.LENGTH_SHORT).show();
                    }
                })
                .negativeText("取消")
                .show();

    }

    //加载标题栏的菜单布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //获取toolbar菜单项
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //监听标题栏的菜单item的选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //R.id.home修改导航按钮的点击事件为打开侧滑
            case android.R.id.home://
                //打开侧滑栏，注意要与xml中保持一致START
                View v = navigationView.getHeaderView(0);
                userNickName = v.findViewById(R.id.text_nickname);
                userSignature = v.findViewById(R.id.text_signature);
                userAvatar = v.findViewById(R.id.icon_image);
                MMKV mmkv = MMKV.defaultMMKV();
                currentUserId = mmkv.decodeString("name","");//获取当前账号id
                if (!TextUtils.isEmpty(currentUserId)) {
                    //List<UserInfo> userInfos = LitePal.where("userAccount = ?", currentUserId).find(UserInfo.class);
                    userNickName.setText(mmkv.decodeString("NickName",""));
                    userSignature.setText(mmkv.decodeString("Signature",""));
                    currentImagePath = mmkv.decodeString("Url","");
                    //System.out.println("主界面初始化数据：" + userInfos);
                    diplayImage(currentImagePath);
                } else {
                    userNickName.setText("请先登录");
                    userSignature.setText("请先登录");
                    userAvatar.setImageResource(R.drawable.no_login_avatar);
                }
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.userFeedback:
                //填写用户反馈
                new MaterialDialog.Builder(MainActivity.this)
                        .title("用户反馈")
                        .inputRangeRes(1, 50, R.color.colorBlack)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(null, null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                System.out.println("反馈的内容为：" + input);
                                Toast.makeText(MainActivity.this, "反馈成功！反馈内容为：" + input, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .positiveText("确定")
                        .negativeText("取消")
                        .show();
                break;

            case R.id.userExit:
                SweetAlertDialog mDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("您是否要退出？")
                        .setCustomImage(null)
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                ActivityCollector.finishAll();
                            }
                        });
                mDialog.show();
                break;
            default:
        }
        return true;
    }


}