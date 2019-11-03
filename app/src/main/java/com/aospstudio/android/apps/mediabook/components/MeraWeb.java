package com.aospstudio.android.apps.mediabook.components;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.webkit.WebViewCompat;

import com.aospstudio.android.apps.mediabook.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MeraWeb extends WebView {

    protected final List<String> mPermittedHostnames = new LinkedList<>();
    protected final Map<String, String> mHttpHeaders = new HashMap<>();

    @TargetApi(Build.VERSION_CODES.Q)
    public MeraWeb(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    public MeraWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
    public MeraWeb(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    public void setWebChromeClient(WebChromeClient client) {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/" + Build.ID + ") AppleWebKit/537.36 (KHTML, like Gecko) " + getResources().getString(R.string.chromium_version) + " Safari/537.36 " + getResources().getString(R.string.opr_version));
        getSettings().setMediaPlaybackRequiresUserGesture(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        getSettings().setSaveFormData(true);
        getSettings().setSavePassword(true);
        getSettings().setGeolocationEnabled(true);
        getSettings().setGeolocationDatabasePath(getContext().getFilesDir().getPath());
        getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        setSoundEffectsEnabled(true);
        playSoundEffect(0);
        setInitialScale(1);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus(View.FOCUSABLES_ALL);
        setScrollBarFadeDuration(1500);
        setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSettings().setSafeBrowsingEnabled(true);
            setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_YES);
        }
        PackageInfo webViewPackageInfo = WebViewCompat.getCurrentWebViewPackage(getContext());
        Log.d("MeraWeb", "MeraWeb version: " + webViewPackageInfo.versionName);
        super.setWebChromeClient(client);
    }

    public void addHttpHeader(final String name, final String value) {
        mHttpHeaders.put(name, value);
    }

    public void addPermittedHostname(String hostname) {
        mPermittedHostnames.add(hostname);
    }

    public void addPermittedHostnames(Collection<? extends String> collection) {
        mPermittedHostnames.addAll(collection);
    }

    public List<String> getPermittedHostnames() {
        return mPermittedHostnames;
    }

    public void removePermittedHostname(String hostname) {
        mPermittedHostnames.remove(hostname);
    }

    public void clearPermittedHostnames() {
        mPermittedHostnames.clear();
    }

    @Override
    public void loadUrl(final String url, Map<String, String> additionalHttpHeaders) {
        if (additionalHttpHeaders == null) {
            additionalHttpHeaders = mHttpHeaders;
        } else if (mHttpHeaders.size() > 0) {
            additionalHttpHeaders.putAll(mHttpHeaders);
        }

        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadUrl(final String url) {
        if (mHttpHeaders.size() > 0) {
            super.loadUrl(url, mHttpHeaders);
        } else {
            super.loadUrl(url);
        }
    }

    public void loadUrl(String url, final boolean preventCaching) {
        if (preventCaching) {
            url = makeUrlUnique(url);
        }

        loadUrl(url);
    }

    public void loadUrl(String url, final boolean preventCaching, final Map<String, String> additionalHttpHeaders) {
        if (preventCaching) {
            url = makeUrlUnique(url);
        }

        loadUrl(url, additionalHttpHeaders);
    }

    protected static String makeUrlUnique(final String url) {
        StringBuilder unique = new StringBuilder();
        unique.append(url);

        if (url.contains("?")) {
            unique.append('&');
        }
        else {
            if (url.lastIndexOf('/') <= 7) {
                unique.append('/');
            }
            unique.append('?');
        }

        unique.append(System.currentTimeMillis());
        unique.append('=');
        unique.append(1);
        return unique.toString();
    }

    public boolean isPermittedUrl(final String url) {
        if (mPermittedHostnames.size() == 0) {
            return true;
        }

        final Uri parsedUrl = Uri.parse(url);
        final String actualHost = parsedUrl.getHost();
        if (actualHost == null) {
            return false;
        }

        if (!actualHost.matches("^[a-zA-Z0-9._!~*')(;:&=+$,%\\[\\]-]*$")) {
            return false;
        }

        final String actualUserInformation = parsedUrl.getUserInfo();
        if (actualUserInformation != null && !actualUserInformation.matches("^[a-zA-Z0-9._!~*')(;:&=+$,%-]*$")) {
            return false;
        }
        for (String expectedHost : mPermittedHostnames) {
            if (actualHost.equals(expectedHost) || actualHost.endsWith("." + expectedHost)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @deprecated use `isPermittedUrl` instead
     */
    protected boolean isHostnameAllowed(final String url) {
        return isPermittedUrl(url);
    }
}
