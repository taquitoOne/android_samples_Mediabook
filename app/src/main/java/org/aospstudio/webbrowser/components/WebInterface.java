package org.aospstudio.webbrowser.components;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebInterface {
    private Context mContext;
    private WebView meraWeb;

    /** Instantiate the interface and set the context */
    public WebInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void playSound(){
        meraWeb.playSoundEffect(0);
    }

    @JavascriptInterface
    public void playSound(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void pauseSound(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public int getAndroidVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    @JavascriptInterface
    public void showAndroidVersion(String versionName) {
        Toast.makeText(mContext, versionName, Toast.LENGTH_SHORT).show();
    }

    /** Show BrowserActivity toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }
}
