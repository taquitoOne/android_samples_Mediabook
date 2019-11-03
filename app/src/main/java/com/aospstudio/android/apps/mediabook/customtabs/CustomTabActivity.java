package com.aospstudio.android.apps.mediabook.customtabs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.webkit.WebViewClientCompat;

import com.aospstudio.android.apps.mediabook.MediabookActivity;
import com.aospstudio.android.apps.mediabook.R;
import com.aospstudio.android.apps.mediabook.components.MeraWeb;
import com.aospstudio.android.apps.mediabook.service.BackgroundService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class CustomTabActivity extends AppCompatActivity {

    final Context context = this;
    private MaterialToolbar toolbar;
    private MeraWeb meraWeb;
    private RelativeLayout browser_screen;
    private ContentLoadingProgressBar progressBar;
    private static final String TAG = MediabookActivity.class.getSimpleName();
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;

    @TargetApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.v6_customtabs_main);
            toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(v -> finishAndRemoveTask());
            progressBar = findViewById(R.id.progressBar);
            browser_screen = findViewById(R.id.browser_screen);

            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.forward) {
                    if (meraWeb.canGoForward()) {
                        meraWeb.goForward();
                    }
                } else if (item.getItemId() == R.id.refresh) {
                    meraWeb.reload();
                } else if (item.getItemId() == R.id.share) {
                    shareURL();
                } else if (item.getItemId() == R.id.clipboard) {
                    String url = meraWeb.getUrl();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", url);
                    clipboard.setPrimaryClip(clip);
                } else if (item.getItemId() == R.id.otherbrowser) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(meraWeb.getUrl())));
                } else if (item.getItemId() == R.id.print) {
                    createWebPrintJob(meraWeb);
                }
                return true;
            });

            meraWeb = findViewById(R.id.meraWeb);
            WebSettings meraKit = meraWeb.getSettings();
            meraKit.setSupportMultipleWindows(false);
            meraKit.setSupportZoom(true);
            meraKit.setBuiltInZoomControls(true);
            meraKit.setDisplayZoomControls(false);
            meraWeb.setVerticalScrollBarEnabled(true);
            meraWeb.setHorizontalScrollBarEnabled(true);
            meraWeb.setWebViewClient(new WebViewClientCompat() {
                public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                    final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                    String message = getString(R.string.ssl_error);
                    switch (error.getPrimaryError()) {
                        case SslError.SSL_UNTRUSTED:
                            message = getString(R.string.ssl_untrusted);
                            break;
                        case SslError.SSL_EXPIRED:
                            message = getString(R.string.ssl_expired);
                            break;
                        case SslError.SSL_IDMISMATCH:
                            message = getString(R.string.ssl_idmismatch);
                            break;
                        case SslError.SSL_NOTYETVALID:
                            message = getString(R.string.ssl_notypevalid);
                            break;
                    }
                    message += " " + getString(R.string.ssl_dialog_message);

                    builder.setTitle(getString(R.string.ssl_dialog_title));
                    builder.setMessage(message);
                    builder.setPositiveButton(R.string.okay_btn, (dialog, which) -> handler.proceed());
                    builder.setNegativeButton(R.string.cancel_btn, (dialog, which) -> handler.cancel());
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("market://")
                            || url.startsWith("intent://")
                            || url.startsWith("linkedin://")
                            || url.startsWith("fb-messenger://")
                            || url.startsWith("sharesample://")
                            || url.startsWith("skype://")
                            || url.startsWith("whatsapp://")
                            || url.startsWith("twitter://")
                            || url.startsWith("dlive://")
                            || url.startsWith("tg://")
                            || url.startsWith("tel:")
                            || url.startsWith("sms:")
                            || url.startsWith("mailto:")
                            || url.startsWith("googledrive://")
                            || url.startsWith("https://m.me/"))
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            return true;
                        } catch (ActivityNotFoundException e) {
                            Snackbar
                                    .make(browser_screen, getString(R.string.error_intent), 7000)
                                    .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                                    .setTextColor(getResources().getColor(R.color.settings_text_color))
                                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                    .setAction(getString(R.string.close_btn), v -> dismissSnackbar())
                                    .show();
                        }
                    else {
                        view.loadUrl(url);
                        return true;
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (url.startsWith("market://")
                            || url.startsWith("intent://")
                            || url.startsWith("linkedin://")
                            || url.startsWith("fb-messenger://")
                            || url.startsWith("sharesample://")
                            || url.startsWith("skype://")
                            || url.startsWith("whatsapp://")
                            || url.startsWith("twitter://")
                            || url.startsWith("dlive://")
                            || url.startsWith("tg://")
                            || url.startsWith("tel:")
                            || url.startsWith("sms:")
                            || url.startsWith("mailto:")
                            || url.startsWith("googledrive://")
                            || url.startsWith("https://m.me/"))
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            meraWeb.loadUrl("");
                            finish();
                        } catch (ActivityNotFoundException e) {
                            Snackbar
                                    .make(browser_screen, getString(R.string.error_intent), 7000)
                                    .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                                    .setTextColor(getResources().getColor(R.color.settings_text_color))
                                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                    .setAction(getString(R.string.close_btn), v -> dismissSnackbar())
                                    .show();
                        }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            meraWeb.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
                String filename = URLUtil.guessFileName(url, contentDisposition, mimeType);

                new MaterialAlertDialogBuilder(context)
                        .setTitle(getString(R.string.download_dialog_title))
                        .setMessage(filename + " " + getString(R.string.download_dialog_msg))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.download_dialog_ok), (dialog, which) -> {
                            try {
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                request.setMimeType(mimeType);
                                String cookies = CookieManager.getInstance().getCookie(url);
                                request.addRequestHeader("cookie", cookies);
                                request.addRequestHeader("User-Agent", userAgent);
                                request.setDescription("Downloading file...");
                                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                dm.enqueue(request);
                                Snackbar
                                        .make(browser_screen, getString(R.string.app_downloading_file), Snackbar.LENGTH_LONG)
                                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                                        .setTextColor(getResources().getColor(R.color.settings_text_color))
                                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                        .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                                        .show();
                            } catch (Exception e) {
                                Snackbar
                                        .make(browser_screen, getString(R.string.error_download), Snackbar.LENGTH_LONG)
                                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                                        .setTextColor(getResources().getColor(R.color.settings_text_color))
                                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                        .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                                        .show();
                            }
                        })
                        .setNegativeButton(getString(R.string.download_dialog_cancel),
                                (dialog, which) -> {
                                }).create().show();
            });
            meraWeb.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress == 100) {
                        toolbar.setTitle(meraWeb.getTitle());
                    }
                    progressBar.setProgress(meraWeb.getProgress());
                    progressBar.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
                }

                @Override
                public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle(url)
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok,
                                    (dialog, which) -> result.confirm()).setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel()).create().show();
                    return true;
                }

                private View mCustomView;
                private WebChromeClient.CustomViewCallback mCustomViewCallback;
                protected FrameLayout mFullscreenContainer;
                private int mOriginalOrientation;
                private int mOriginalSystemUiVisibility;

                public Bitmap getDefaultVideoPoster() {
                    if (mCustomView == null) {
                        return null;
                    }
                    return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
                }

                public void onHideCustomView() {
                    ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
                    this.mCustomView = null;
                    getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                    setRequestedOrientation(this.mOriginalOrientation);
                    this.mCustomViewCallback.onCustomViewHidden();
                    this.mCustomViewCallback = null;
                }

                public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
                    if (this.mCustomView != null) {
                        onHideCustomView();
                        return;
                    }
                    Toast.makeText(context, getString(R.string.close_fullscreen), Toast.LENGTH_LONG).show();
                    this.mCustomView = paramView;
                    this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                    this.mOriginalOrientation = getRequestedOrientation();
                    this.mCustomViewCallback = paramCustomViewCallback;
                    ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                    getWindow().getDecorView().setSystemUiVisibility(3846);
                    mCustomView.setBackgroundColor(getResources().getColor(android.R.color.black));
                }

                @SuppressLint("IntentReset")
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                    if (mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                    }
                    mFilePathCallback = filePathCallback;

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(pickIntent, "");
                    chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, R.string.app_picture_chooser);
                    startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                    return true;
                }

                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    callback.invoke(origin, true, false);
                }
            });

            Bundle extras = getIntent().getExtras();
            String value = extras.getString("send_url");
            meraWeb.loadUrl(value);

        } catch (Exception e) {
            // BUG SEND REPORT
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle(getResources().getString(R.string.app_name) + " has stopped working");
            builder.setView(R.layout.mpui_bug_detect_main);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();

            AppCompatButton bugscreen_info = (AppCompatButton) dialog.findViewById(R.id.bug_screen_info);
            AppCompatButton bugscreen_close = (AppCompatButton) dialog.findViewById(R.id.bug_screen_close);
            AppCompatButton bugscreen_copy = (AppCompatButton) dialog.findViewById(R.id.bug_screen_copy);
            AppCompatButton bugscreen_send = (AppCompatButton) dialog.findViewById(R.id.bug_screen_send);
            bugscreen_info.setOnClickListener(buginfo -> {
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).addCategory(Intent.CATEGORY_DEFAULT).setData(Uri.parse("package:" + getPackageName()));
                startActivity(i);
            });
            bugscreen_close.setOnClickListener(bugclose -> {
                finish();
                System.exit(0);
            });
            bugscreen_copy.setOnClickListener(bugcopy -> {
                Toast.makeText(context, "If the error code has been copied to the clipboard, you can now report this error code.", Toast.LENGTH_LONG).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", "Code: " + e);
                clipboard.setPrimaryClip(clip);
            });
            bugscreen_send.setOnClickListener(bugsend -> {
                Toast.makeText(context, "We know, insects are very bad. We need reports to solve them. Please paste the error code you copied to the clipboard into our Google feedback group.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://groups.google.com/forum/#!newtopic/aospstudio-feedback")));
            });
        }
    }

    private void shareURL() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, meraWeb.getUrl());
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_url)));
    }

    private void createWebPrintJob(MeraWeb meraWeb) {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                meraWeb.createPrintDocumentAdapter();

        String jobName = getString(R.string.app_name);

        if (printManager != null) {
            printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());
        }
    }

    private void dismissSnackbar() {
        Snackbar.make(browser_screen, "", Snackbar.LENGTH_LONG).dismiss();
    }

    private void startService(View view) {
        startService(new Intent(this, BackgroundService.class));
    }

    private void stopService(View view) {
        stopService(new Intent(this, BackgroundService.class));
    }

    @Override
    protected void onResume() {
        if (meraWeb != null) {
            meraWeb.onResume();
            meraWeb.resumeTimers();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (meraWeb != null) {
            meraWeb.onPause();
        }
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        meraWeb.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        meraWeb.restoreState(state);
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }

    private String getFileName(String url) {
        String filenameWithoutExtension = "";
        filenameWithoutExtension = String.valueOf(System.currentTimeMillis());
        return filenameWithoutExtension;
    }

    @Override
    public void onBackPressed() {
        if (meraWeb.canGoBack()) {
            meraWeb.goBack();
        } else {
            meraWeb.loadUrl("");
            meraWeb.clearHistory();
            meraWeb.clearCache(true);
            meraWeb.destroy();
            finishAndRemoveTask();
        }
    }
}
