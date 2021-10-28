package com.example.viewnews.ui.news;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.viewnews.R;
import com.example.viewnews.logic.ThreadPool;
import com.example.viewnews.logic.dao.NewsData;
import com.example.viewnews.logic.dao.NewsCache;
import com.example.viewnews.ui.other.RecyclerViewAdapter;
import com.example.viewnews.BaseActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

//若需要启用Javascript，则抑制其警告
@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {

    private WebView webView;

    private Toolbar navToolbar, commentToolBar;

    private String urlData, pageUniquekey, pageTtile;

    private Boolean isfavFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV.initialize(this);

        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webView);
        navToolbar = (Toolbar) findViewById(R.id.toolbar_webView);
        commentToolBar = (Toolbar) findViewById(R.id.toolbar_webComment);
        //将底部评论框的toolbar放在主界面上
        findViewById(R.id.toolbar_webComment).bringToFront();


    }

    //活动由不可见变为可见时调用
    @Override
    protected void onStart() {
        super.onStart();
        // 获取html页面的连接
        urlData = getIntent().getStringExtra("pageUrl");
        pageUniquekey = getIntent().getStringExtra("uniquekey");
        pageTtile = getIntent().getStringExtra("news_title");
        String type = getIntent().getStringExtra("type");

        System.out.println("当前新闻id为：" + pageUniquekey);
        System.out.println("当前新闻标题为：" + pageTtile);

        //设置点赞图标
        MMKV mmkv = MMKV.defaultMMKV();
        String id = mmkv.decodeString("name","");
        ThreadPool.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //用id作为类型去查找，找到了,就说明被收藏了
                List<NewsData> dataList = NewsCache.getInstance(getApplicationContext()).newsDao().
                        selectFavNews(id,pageUniquekey);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MenuItem u = commentToolBar.getMenu().getItem(2);
                        if(dataList.size() > 0) {
                            u.setIcon(R.drawable.ic_star_border_favourite_yes);
                            isfavFlag = true;
                        } else {
                            u.setIcon(R.drawable.ic_star_border_favourite_no);
                            isfavFlag = false;
                        }
                    }
                });
            }
        });

        // 通过WebView中的getSettings方法获得一个WebSettings对象
        WebSettings settings = webView.getSettings();

        // 详细讲解：https://www.jianshu.com/p/0d7d429bd216
        // 开启javascript：h5页要一般都有js,设置为true才允许h5页面执行js，但开启js非常耗内存，经常会导致oom，
        // 为了解决这个问题，可以在onStart方法中开启，在onStop中关闭。
        settings.setJavaScriptEnabled(true);

        //设置支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具。若为false，则该WebView不可缩放
        settings.setBuiltInZoomControls(true);
        // 设置允许js弹出alert对话框
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 设置webview推荐使用的窗口，使html界面自适应屏幕
        // 原因讲解：https://blog.csdn.net/SCHOLAR_II/article/details/80614486
        settings.setUseWideViewPort(true);
        // 设置WebView底层的布局算法，参考LayoutAlgorithm#NARROW_COLUMNS，将会重新生成WebView布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        // 设置缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);
        // 隐藏webview缩放按钮
        settings.setDisplayZoomControls(false);
        // 加载网页连接
        webView.loadUrl(urlData);

        // Toolbar通过setSupportActionBar(toolbar) 被修饰成了actionbar。
        setSupportActionBar(commentToolBar);
        // 设置菜单栏标题
        navToolbar.setTitle("看点新闻");
        setSupportActionBar(navToolbar);
        commentToolBar.inflateMenu(R.menu.tool_webbottom);
        commentToolBar.setTitle("感谢观看");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 页面开始加载时就去查看收藏表中是否有对应的记录，组合键（账号和新闻号）
                //List<NewsCollectBean> beanList = LitePal.where("userIdNumer = ? AND newsId = ?", MainActivity.currentUserId == null ? "" : MainActivity.currentUserId, pageUniquekey).find(NewsCollectBean.class);
                // 获取收藏按钮
/*                MMKV mmkv = MMKV.defaultMMKV();
                String id = mmkv.decodeString("name","");
                List<NewsData> dataList = NewsCache.getInstance(getApplicationContext()).newsDao().
                        selectFavNews(id,pageUniquekey);

                MenuItem u = commentToolBar.getMenu().getItem(2);
                if(dataList.size() > 0) {
                    u.setIcon(R.drawable.ic_star_border_favourite_yes);
                } else {
                    u.setIcon(R.drawable.ic_star_border_favourite_no);
                }*/

            }

            // 在页面加载结束时调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 通过查看每个新闻的网页发现网页广告的div样式的选择器为body > div.top-wrap.gg-item.J-gg-item 然后去除这个样式，使其加载网页时去掉广告
                view.loadUrl("javascript:function setTop1(){document.querySelector('body > div.top-wrap.gg-item.J-gg-item').style.display=\"none\";}setTop1();");
                view.loadUrl("javascript:function setTop4(){document.querySelector('body > a.piclick-link').style.display=\"none\";}setTop4();");
                view.loadUrl("javascript:function setTop2(){document.querySelector('#news_check').style.display=\"none\";}setTop2();");
                view.loadUrl("javascript:function setTop3(){document.querySelector('body > div.articledown-wrap gg-item J-gg-item').style.display=\"none\";}setTop3();");
            }

            // 重写此方法可以让webView处理https请求。
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 默认的处理方式，WebView变成空白页
                // handler.cancel();
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
            }
        });

        // 重写执行执行去广告的js代码
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // 去除加载热点新闻
                view.loadUrl("javascript:function setTop1(){document.querySelector('body > div.top-wrap.gg-item.J-gg-item').style.display=\"none\";}setTop1();");
                view.loadUrl("javascript:function setTop4(){document.querySelector('body > a.piclick-link').style.display=\"none\";}setTop4();");
                view.loadUrl("javascript:function setTop2(){document.querySelector('#news_check').style.display=\"none\";}setTop2();");
                view.loadUrl("javascript:function setTop3(){document.querySelector('body > div.articledown-wrap gg-item J-gg-item').style.display=\"none\";}setTop3();");
            }
        });

        commentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.news_share:
                        // 这里有bug，点击之后没反应
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_SUBJECT, urlData);
                        // 分享的文本类型
                        intent.setType("text/plain");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getTitle()));
                        break;
                    case R.id.news_collect:
                        //下一步实现点击收藏功能，以及用户查看收藏功能
                        String id = MMKV.defaultMMKV().decodeString("name","");

                        if(!TextUtils.isEmpty(id)) {
                            // 先去查询一下是否有收藏过，然后加载每条新闻的时候查看是否已经被收藏，若被收藏，则将收藏按钮背景色设置为红色，否则为白色
                            MenuItem u = commentToolBar.getMenu().getItem(2);
                            ThreadPool.getExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        NewsCache cache = NewsCache.getInstance(getApplicationContext());
                                        if(isfavFlag){//已经收藏，点击取消收藏
                                            //点击取消收藏后，就可以直接把该新闻从本地数据库中删除
                                            cache.newsDao().deleteFav(id,pageUniquekey);
                                            isfavFlag = false;
                                        }
                                        else {//还未收藏，点击添加收藏
                                            List<NewsData> list = cache.newsDao().selectFavNews(type,pageUniquekey);
                                            if(list.size() > 0){
                                                cache.newsDao().deleteFav(type,pageUniquekey);//先删后添加
                                                NewsData data = list.get(0);
                                                data.setCategory(id);
                                                List<NewsData> newlist = new ArrayList<>();
                                                newlist.add(data);
                                                cache.newsDao().updateNews(newlist);
                                                isfavFlag = true;
                                            }
                                            else {//这种是出错了的情况，返回的列表数量为0，查找失败
                                                Log.d("WebActivity","list size is 0");
                                            }
                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(isfavFlag){//收藏
                                                    u.setIcon(R.drawable.ic_star_border_favourite_yes);
                                                    Toast.makeText(WebActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                                                }
                                                else {//没收藏
                                                    u.setIcon(R.drawable.ic_star_border_favourite_no);
                                                    Toast.makeText(WebActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });


                        }
                        else {
                            Toast.makeText(WebActivity.this, "请先登录后再收藏！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.news_like:
                        break;
                        //ToDo 点击点赞后将数据传回后台并设置点赞图标为news_liked
                    case R.id.news_comment:
                        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(WebActivity.this);
                        View dialogView= LayoutInflater.from(WebActivity.this).inflate(R.layout.dialog_bottomsheet,null);
                        //RecyclerView初始化
                        //ToDo 利用后台传回的评论数据来初始化list，此处先暂时用循环代替
                        List list = new ArrayList<>();
                        for (int i=0;i<10;i++){
                            list.add("这是第"+i+"个测试");
                        }
                        RecyclerViewAdapter adapter=new RecyclerViewAdapter(WebActivity.this,list);
                        LinearLayoutManager manager = new LinearLayoutManager(WebActivity.this);
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        RecyclerView recyclerView=dialogView.findViewById(R.id.dialog_bottomsheet_rv_lists);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        //评论提交Button初始化
                        Button button=dialogView.findViewById(R.id.dialog_button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(WebActivity.this);
                                builder.setTitle("请输入评论");
                                builder.setIcon(R.drawable.news_comment);
                                builder.setView(new EditText(WebActivity.this));
                                builder.setPositiveButton("提交",null);
                                builder.setNegativeButton("取消",null);
                                builder.show();
                            }
                        });

                        bottomSheetDialog.setContentView(dialogView);
                        bottomSheetDialog.show();
                        break;
                        //Todo 点击评论后跳转
                }
                return true;
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置返回图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }
    }

    // 活动不可见时关闭脚本，否则可能会导致oom
    @Override
    protected void onStop() {
        super.onStop();
        webView.getSettings().setJavaScriptEnabled(false);
    }

    // 此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。 返回true则显示该menu, false则不显示;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_webview, menu);

        // SearchManager提供全局搜索服务
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // 通过MenuItem得到SearchView
        // SearchView searchView = (SearchView) menu.findItem(R.id.news_search).getActionView();

        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // 搜索框文字变化监听
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 在文字改变的时候回调，query是改变之后的文字
                Toast.makeText(WebActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            //文字提交的时候回调
            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(WebActivity.this, newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 左上角的id
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                // 结束当前活动
                WebActivity.this.finish();
                break;
            case R.id.news_setting:
                Toast.makeText(this, "夜间模式", Toast.LENGTH_SHORT).show();
                break;
            case R.id.news_feedback:
                Toast.makeText(this, "举报！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}