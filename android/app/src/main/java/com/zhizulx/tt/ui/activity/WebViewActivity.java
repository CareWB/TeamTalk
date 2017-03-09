package com.zhizulx.tt.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhizulx.tt.R;
import com.zhizulx.tt.ui.base.TTBaseFragmentActivity;

public class WebViewActivity extends  TTBaseFragmentActivity{
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.web_view_activity);
        webView = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setDomStorageEnabled(true);
        // 设置支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
//启动缓存
        webView.getSettings().setAppCacheEnabled(true);
        //设置缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //加载网页
        webView.loadUrl("http://m.ctrip.com/webapp/hotel/hoteldetail/890106.html");
        //webView.loadUrl("http://hotels.ctrip.com/hotel/6112181.html");

        //webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        //在当前的浏览器中响应
        webView.setWebViewClient(new WebViewClient() {
/*            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }*/

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("onPageStarted", "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                Log.e("shouldOverrideKeyEvent", "shouldOverrideKeyEvent");
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e("onLoadResource", "onLoadResource");
                super.onLoadResource(view, url);
            }
        });
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            refreshHtmlContent(html);
        }
    }


    private void refreshHtmlContent(final String html){
        Log.i("网页内容",html);
/*        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //解析html字符串为对象
                Document document = Jsoup.parse(html);
                //通过类名获取到一组Elements，获取一组中第一个element并设置其html
                Elements elements = document.getElementsByClass("js_back_to_lastpage");
                if (!elements.isEmpty()) {
                    //elements.get(0).attr("disabled", "disabled");
                }

                //elements.get(0).html("<p>加载完成</p>");

*//*                //通过ID获取到element并设置其src属性
                Element element = document.getElementById("imageView");
                element.attr("src","file:///android_asset/dragon.jpg");

                //通过类名获取到一组Elements，获取一组中第一个element并设置其文本
                elements = document.select("p.hint");
                elements.get(0).text("您好，我是龙猫军团！");*//*

                //获取进行处理之后的字符串并重新加载
                String body = document.toString();
                webView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
            }
        },5000);*/

        //解析html字符串为对象
        Document document = Jsoup.parse(html);
        //通过类名获取到一组Elements，获取一组中第一个element并设置其html
        //Elements elements = document.getElementsByClass("js_back_to_lastpage");
        Elements elements = document.getElementsByClass("dt-btn-mod");
        if (!elements.isEmpty()) {
            elements.get(0).attr("style", "display:none");
            Log.e("web", "got it");
        }

        //获取进行处理之后的字符串并重新加载
        String body = document.toString();
        webView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
    }
}
