package aaa.bbb.ccc.solidsnake.data;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;

import aaa.bbb.ccc.solidsnake.R;
import aaa.bbb.ccc.solidsnake.utils.Logger;

public class WebClient extends WebViewClient {
    private CookieManager mManager;
    private View mProgress;

    public WebClient(CookieManager manager, View progress) {
        this.mManager = manager;
        this.mProgress = progress;
    }

    public WebClient(CookieManager manager) {
        this.mManager = manager;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        this.hideProgress(false);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mManager.setAcceptCookie(true);
        this.hideProgress(true);
        view.loadUrl("javascript:AndroidFunction.showHTML(document.getElementsByTagName('html')[0].innerHTML)");
    }

    private void hideProgress(boolean b) {
        if (mProgress == null) return;
        mProgress.setVisibility(b ? View.GONE : View.VISIBLE);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Resources resources = view.getContext().getResources();
        String htmlData = String.format(resources.getString(R.string.error_wev_view_message), description);
        htmlData= resources.getString(R.string.error_web_view_left) + htmlData + resources.getString(R.string.error_web_view_left);
        view.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
        view.invalidate();
    }


    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        Logger.log("ssl " + error.toString());
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(R.string.notification_error_ssl_cert_invalid);
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}

