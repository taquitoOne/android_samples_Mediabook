package org.aospstudio.webbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.aospstudio.webbrowser.components.*;
import org.aospstudio.webbrowser.ui.MeraSocial;
import com.google.android.material.circularreveal.CircularRevealRelativeLayout;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BrowserActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    final Context context = this;
    private View mCustomView;
    private CircularRevealRelativeLayout loadprogress, appheader, main_app, toolbar_web, notify_screen, notify_main, error_screen, settings_screen;
    private SwipeRefreshLayout swipeContainer;
    private NestedScrollView settingscreen, clearscreen;
    private AppCompatImageView backtostart_btn, forward_btn, refresh_btn, top_btn, moremenu_btn;
    private AppCompatTextView theme_btn, minimode_btn, desktopmode_btn, textView3, help_setting, close_setting, close_bg, pagename, retry, back, name, aboutapp, notifyText, restart, clearcache, notifyClose, clear_datas1, clear_datas2;
    private SwitchCompat javascript, minimode, desktopmode, themesetting;
    private WebView meraWeb;
    private CookieManager cookieManager;
    private String versionName = BuildConfig.VERSION_NAME;
    private String userid = Build.BOARD;
    private static final String PREFS = "sb_pref";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private final String username = "username", password = "password";
    private MediaPlayer mediaplayer;
    private static final String TAG = BrowserActivity.class.getSimpleName();
    private static final int REQUEST_CODE_LOLIPOP = 1;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @TargetApi(Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            setContentView(R.layout.v5_browserpage_main);

            Intent i = getIntent();
            String s2 = i.getStringExtra("name");
            pagename = findViewById(R.id.pageappname);
            pagename.setText(s2);
            loadprogress = findViewById(R.id.loadprogress);
            back = findViewById(R.id.back);
            retry = findViewById(R.id.retry);
            error_screen = findViewById(R.id.error_screen);
            settings_screen = findViewById(R.id.settings_screen);
            settingscreen = findViewById(R.id.settingscreen);
            clearscreen = findViewById(R.id.clearscreen);
            appheader = findViewById(R.id.appheader);
            main_app = findViewById(R.id.main_app);
            toolbar_web = findViewById(R.id.toolbarweb);
            notify_screen = findViewById(R.id.notify_screen);
            notify_screen = findViewById(R.id.notify_screen);
            notify_main = findViewById(R.id.notify_main);
            notify_main.setVisibility(View.GONE);
            notifyText = findViewById(R.id.notifyText);
            notifyClose = findViewById(R.id.notifyClose);
            notifyClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyText.setText("");
                    notify_main.setVisibility(View.GONE);
                }
            });

            swipeContainer = this.findViewById(R.id.swipeContainer);
            swipeContainer.setNestedScrollingEnabled(true);
            swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
            meraWeb = findViewById(R.id.meraWeb);
            WebSettings meraKit = meraWeb.getSettings();
            meraKit.setJavaScriptEnabled(true);
            meraKit.setJavaScriptCanOpenWindowsAutomatically(true);
            meraKit.setUserAgentString("Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/" + Build.ID + ") AppleWebKit/537.36 (KHTML, like Gecko) " + getString(R.string.chromium_version) + " Safari/537.36 " + getString(R.string.opr_version));
            meraKit.setMediaPlaybackRequiresUserGesture(true);
            meraKit.setUseWideViewPort(true);
            meraKit.setLoadWithOverviewMode(true);
            meraKit.setLoadsImagesAutomatically(true);
            meraKit.setDefaultFontSize(17);
            meraKit.setSupportZoom(true);
            meraKit.setBuiltInZoomControls(true);
            meraKit.setDisplayZoomControls(false);
            meraKit.setAppCacheEnabled(true);
            meraKit.setDatabaseEnabled(true);
            meraKit.setDomStorageEnabled(true);
            meraKit.setCacheMode(WebSettings.LOAD_DEFAULT);
            meraKit.setSaveFormData(true);
            meraKit.setSavePassword(true);
            meraKit.setGeolocationEnabled(true);
            meraKit.setGeolocationDatabasePath(getFilesDir().getPath());
            meraKit.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
            meraWeb.setWebChromeClient(new OperaChromeClient());
            meraWeb.setWebViewClient(new OperaClient());
            meraWeb.setDownloadListener(new OperaDownload());
            meraWeb.addJavascriptInterface(new WebInterface(this), "Android");
            meraWeb.setVerticalScrollBarEnabled(false);
            meraWeb.setHorizontalScrollBarEnabled(false);
            try{
                Bundle extras = getIntent().getExtras();
                String value = extras.getString("send_string");
                meraWeb.loadUrl(value);
            } catch (Exception e){
                if (getIntent() != null) {
                    Uri intentUri = getIntent().getData();
                    meraWeb.loadUrl(intentUri != null ? intentUri.toString() : null);
                }
            }

            if (savedInstanceState != null)
                ((WebView) findViewById(R.id.meraWeb)).restoreState(savedInstanceState);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                meraKit.setSafeBrowsingEnabled(true);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                meraWeb.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                meraWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            if (getIntent() != null) {
                Uri intentUri = getIntent().getData();
                meraWeb.loadUrl(intentUri != null ? intentUri.toString() : null);
            }

            backtostart_btn = findViewById(R.id.backtostart_btn);
            backtostart_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    meraWeb.loadUrl("");
                    meraWeb.destroy();
                    finish();
                }
            });

            forward_btn = findViewById(R.id.forward_btn);
            forward_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (meraWeb.canGoForward()) {
                        meraWeb.goForward();
                    }
                }
            });

            refresh_btn = findViewById(R.id.refresh_btn);
            refresh_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(meraWeb, "scrollY",
                            meraWeb.getScrollY(), 0);
                    anim.setDuration(500);
                    anim.start();
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            meraWeb.reload();
                        }
                    }, 600);
                }
            });

            top_btn = findViewById(R.id.top_btn);
            top_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(meraWeb, "scrollY",
                            meraWeb.getScrollY(), 0);
                    anim.setDuration(500);
                    anim.start();
                }
            });

            moremenu_btn = findViewById(R.id.moremenu_btn);
            moremenu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(BrowserActivity.this, v, R.style.MeraPureUI2PopupMenu);
                    popup.getMenuInflater().inflate(R.menu.browser_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.share) {
                                shareURL();
                            } else if (item.getItemId() == R.id.otherbrowser) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meraWeb.getUrl()));
                                startActivity(intent);
                            } else if (item.getItemId() == R.id.clearapp) {
                                settings_screen.setVisibility(View.VISIBLE);
                                close_bg.setBackground(getDrawable(R.drawable.ic_close_black_24dp));
                                settingscreen.setVisibility(View.GONE);
                                clearscreen.setVisibility(View.VISIBLE);
                                name = findViewById(R.id.name);
                                name.setText(R.string.clear_files);
                            } else if (item.getItemId() == R.id.settings) {
                                settings_screen.setVisibility(View.VISIBLE);
                                close_bg.setBackground(getDrawable(R.drawable.round_arrow_back_24));
                                settingscreen.setVisibility(View.VISIBLE);
                                clearscreen.setVisibility(View.GONE);
                                name = findViewById(R.id.name);
                                name.setText(R.string.settings_btn);
                            } else if (item.getItemId() == R.id.normal_restart) {
                                meraRestart();
                            } else if (item.getItemId() == R.id.quit) {
                                meraQuit();
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

            swipeContainer.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            meraWeb.reload();
                            swipeContainer.setRefreshing(false);
                        }
                    }
            );

            close_bg = findViewById(R.id.close_bg);
            close_setting = findViewById(R.id.close_setting);
            close_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settings_screen.setVisibility(View.GONE);
                    clearscreen.setVisibility(View.GONE);
                    settingscreen.setVisibility(View.VISIBLE);
                }
            });

            help_setting = findViewById(R.id.help_setting);
            help_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(BrowserActivity.this, v, R.style.MeraPureUI2PopupMenu);
                    popup.getMenuInflater().inflate(R.menu.help_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.privacy) {
                                String url = getString(R.string.appprivacy_url);
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            } else if (item.getItemId() == R.id.terms) {
                                String url = getString(R.string.appterms_url);
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

            mediaplayer = new MediaPlayer();
            mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaplayer.getAudioSessionId();

            themesetting = findViewById(R.id.themesetting);
            javascript = findViewById(R.id.javascript);
            minimode = findViewById(R.id.minimode);
            desktopmode = findViewById(R.id.desktopmode);

            clear_datas1 = findViewById(R.id.clear_datas1);
            clear_datas2 = findViewById(R.id.clear_datas2);

            themesetting.setOnCheckedChangeListener(this);
            javascript.setOnCheckedChangeListener(this);
            minimode.setOnCheckedChangeListener(this);
            desktopmode.setOnCheckedChangeListener(this);

            theme_btn = findViewById(R.id.theme_btn);
            minimode_btn = findViewById(R.id.minimode_btn);
            desktopmode_btn = findViewById(R.id.desktopmode_btn);

            if (getFromSP("themesetting")) {
                themesetting.setChecked(true);
                MeraSocial.getInstance().setIsNightModeEnabled(true);
            } else {
                themesetting.setChecked(false);
                MeraSocial.getInstance().setIsNightModeEnabled(false);
            }
            if (getFromSP("javascript")) {
                javascript.setChecked(true);
                meraKit.setJavaScriptEnabled(false);
                meraWeb.reload();
            } else {
                javascript.setChecked(false);
                meraKit.setJavaScriptEnabled(true);
                meraWeb.reload();
            }
            if (getFromSP("minimode")) {
                minimode.setChecked(true);
                appheader.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                minimode.setChecked(false);
                appheader.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            }
            if (getFromSP("desktopmode")) {
                desktopmode.setChecked(true);
                meraKit.setUseWideViewPort(true);
                meraKit.setLoadWithOverviewMode(true);
                meraWeb.clearCache(true);
                meraKit.setUserAgentString("Mozilla/5.0 (X11; CrOS x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " + getString(R.string.dsktp_chromium_version) + " Safari/537.36 " + getString(R.string.dsktp_opr_version));
                meraWeb.reload();
            } else {
                desktopmode.setChecked(false);
                meraKit.setUseWideViewPort(true);
                meraKit.setLoadWithOverviewMode(true);
                meraWeb.clearCache(true);
                meraKit.setUserAgentString("Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/" + Build.ID + ") AppleWebKit/537.36 (KHTML, like Gecko) " + getString(R.string.chromium_version) + " Safari/537.36 " + getString(R.string.opr_version));
                meraWeb.reload();
            }

            clear_datas1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.deleteDatabase("webview.db");
                    context.deleteDatabase("webviewCache.db");
                    meraWeb.clearCache(true);
                    meraWeb.clearFormData();
                    meraWeb.clearHistory();
                    Toast.makeText(getApplicationContext(), getString(R.string.clear_cdatas), Toast.LENGTH_LONG).show();
                }
            });

            clear_datas2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearCookies(context);
                    WebViewDatabase.getInstance(BrowserActivity.this).clearFormData();
                    Toast.makeText(getApplicationContext(), getString(R.string.clear_cdatas), Toast.LENGTH_LONG).show();
                }
            });

            clearcache = findViewById(R.id.clearcache);
            clearcache.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingscreen.setVisibility(View.GONE);
                    clearscreen.setVisibility(View.VISIBLE);
                    name.setText(R.string.clear_files);
                }
            });

            restart = findViewById(R.id.restart);
            restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    meraRestart();
                }
            });

        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder
                    .setTitle("aospstudio problem wizard")
                    .setMessage("Sorry, an unexpected error has occurred. In order to correct the problem, please send us SplashActivity report on the button below.")
                    .setCancelable(false)
                    .setPositiveButton("Send report", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:"));
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"issue@aospstudio.org"});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Issue Mera Social " + BuildConfig.VERSION_NAME);
                            intent.putExtra(Intent.EXTRA_TEXT, "Please do not delete this information and send it to us as it is.\n\nError: " + e);
                            startActivity(Intent.createChooser(intent, "Send issue"));
                        }
                    })
                    .setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            meraQuit();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.themesetting:
                saveInSp("themesetting", isChecked);
                break;
            case R.id.javascript:
                saveInSp("javascript", isChecked);
                break;
            case R.id.minimode:
                saveInSp("minimode", isChecked);
                break;
            case R.id.desktopmode:
                saveInSp("desktopmode", isChecked);
                break;
        }
    }

    public void saveInSp(String key, boolean value){
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getFromSP(String key){
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    private class OperaClient extends WebViewClient {
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(BrowserActivity.this);
            String message = "SSL Certificate error.";
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
            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {
            swipeContainer.setVisibility(View.GONE);
            error_screen.setVisibility(View.VISIBLE);
            back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            textView3 = findViewById(R.id.textView3);
            textView3.setText(description);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeContainer.setVisibility(View.VISIBLE);
                    error_screen.setVisibility(View.GONE);
                    meraWeb.clearCache(true);
                    meraWeb.reload();
                }
            });
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean value = false;
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            if (extension != null) {
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String mimeType = mime.getMimeTypeFromExtension(extension);
                if (mimeType != null) {
                    if (mimeType.toLowerCase().contains("jpg")
                            || extension.toLowerCase().contains("png")
                            || extension.toLowerCase().contains("svg")
                            || extension.toLowerCase().contains("bmp")
                            || extension.toLowerCase().contains("tiff")
                            || extension.toLowerCase().contains("mp4")
                            || extension.toLowerCase().contains("mkv")
                            || extension.toLowerCase().contains("mov")
                            || extension.toLowerCase().contains("avi")
                            || extension.toLowerCase().contains("3gp")
                            || extension.toLowerCase().contains("flac")
                            || extension.toLowerCase().contains("wav")
                            || extension.toLowerCase().contains("mp3")
                            || extension.toLowerCase().contains("aac")
                            || extension.toLowerCase().contains("pdf")
                            || extension.toLowerCase().contains("txt")
                            || extension.toLowerCase().contains("xml")
                            || extension.toLowerCase().contains("docx")
                            || extension.toLowerCase().contains("pptx")
                            || extension.toLowerCase().contains("xlsx")
                            || extension.toLowerCase().contains("doc")
                            || extension.toLowerCase().contains("ppt")
                            || extension.toLowerCase().contains("xls")
                            || extension.toLowerCase().contains("zip")
                            || extension.toLowerCase().contains("rar")
                            || extension.toLowerCase().contains("7z")
                            || extension.toLowerCase().contains("apk"))
                        try{
                            DownloadManager mdDownloadManager = (DownloadManager) BrowserActivity.this
                                    .getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(
                                    Uri.parse(url));
                            File destinationFile = new File(
                                    Environment.getExternalStorageDirectory(),
                                    getFileName(url));
                            request.setDescription(getString(R.string.app_download_file));
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationUri(Uri.fromFile(destinationFile));
                            mdDownloadManager.enqueue(request);
                            value = false;
                        } catch (ActivityNotFoundException e){
                            notifyText.setText(R.string.download_error_title);
                            notify_main.setVisibility(View.VISIBLE);
                        }
                }
                else if(url.startsWith("intent://")
                        ||url.startsWith("app://")
                        ||url.startsWith("file://")
                        ||url.startsWith("market://")
                        ||url.startsWith("linkedin://")
                        ||url.startsWith("fb-messenger://")
                        ||url.startsWith("skype://")
                        ||url.startsWith("whatsapp://")
                        ||url.startsWith("tg://")
                        ||url.startsWith("tel:")
                        ||url.startsWith("sms:")
                        ||url.startsWith("mailto:"))
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                    catch (ActivityNotFoundException e) {
                        notifyText.setText(R.string.other_links_error_title);
                        notify_main.setVisibility(View.VISIBLE);
                    }
                else{
                    view.loadUrl(url);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadprogress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadprogress.setVisibility(View.GONE);
        }
    }

    private class OperaChromeClient extends WebChromeClient {
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            request.grant(request.getResources());
        }
        private Activity mActivity = BrowserActivity.this;
        private CustomViewCallback mCustomViewCallback;
        private FrameLayout mFullscreenContainer;
        private final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            main_app.setVisibility(View.GONE);
            toolbar_web.setVisibility(View.GONE);
            Toast.makeText(context, R.string.close_fullscreen, Toast.LENGTH_LONG).show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            mFullscreenContainer = new FrameLayout(mActivity);
            mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
            mCustomView = view;
            setFullscreen(true);
            mCustomViewCallback = callback;
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            }
            main_app.setVisibility(View.VISIBLE);
            toolbar_web.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            setFullscreen(false);
            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            decor.removeView(mFullscreenContainer);
            mFullscreenContainer = null;
            mCustomView = null;
            mCustomViewCallback.onCustomViewHidden();
        }

        private void setFullscreen(boolean enabled) {
            Window win = mActivity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            if (enabled) {
                winParams.flags |= bits;

            } else {
                winParams.flags &= ~bits;
                if (mCustomView != null) {
                    mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
            win.setAttributes(winParams);
        }

        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;
            meraUploader();
            return true;
        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, true);
        }
    }

    private class OperaDownload implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
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
            notifyText.setText(R.string.app_downloading_file);
            notify_main.setVisibility(View.VISIBLE);
        }
    }

    private File meraPhotoName() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        String imageFileName = ".img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    private void meraUploader() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = meraPhotoName();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                Log.e(TAG, "Unable to create Image File", ex);
            }
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, R.string.app_picture_chooser);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, meraWeb.getUrl());
        return shareIntent;
    }

    private void shareURL() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, meraWeb.getUrl());
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_url)));
    }

    private void meraBassBooster() {
        BassBoost bassBoost = new BassBoost(0,0);
        bassBoost.setStrength((short) 1000);
        bassBoost.setEnabled(true);
        mediaplayer.attachAuxEffect(bassBoost.getId());
        mediaplayer.setAuxEffectSendLevel(1.0f);
        try {
            mediaplayer.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseVideo() {
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(meraWeb, (Object[]) null);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException cnfe) {
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
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
        pauseVideo();
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private String getFileName(String url) {
        String filenameWithoutExtension = "";
        filenameWithoutExtension = String.valueOf(System.currentTimeMillis());
        return filenameWithoutExtension;
    }

    private class FullscreenHolder extends FrameLayout {
        private FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_LOLIPOP:
                Uri[] results = null;
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        if (mCameraPhotoPath != null) {
                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
        }
    }

    private void meraRestart() {
        new AlertDialog.Builder(BrowserActivity.this)
                .setTitle(R.string.restart_title)
                .setMessage(R.string.restart_message)
                .setPositiveButton(R.string.restart_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BrowserActivity.this, SplashActivity.class);
                        mediaplayer.stop();
                        meraWeb.loadUrl("");
                        meraWeb.destroy();
                        finishAndRemoveTask();
                        moveTaskToBack(true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (meraWeb.canGoBack()) {
            meraWeb.goBack();
            settings_screen.setVisibility(View.GONE);
            clearscreen.setVisibility(View.GONE);
            settingscreen.setVisibility(View.VISIBLE);
        } else {
            settings_screen.setVisibility(View.GONE);
            clearscreen.setVisibility(View.GONE);
            settingscreen.setVisibility(View.VISIBLE);
        }
        settings_screen.setVisibility(View.GONE);
        clearscreen.setVisibility(View.GONE);
        settingscreen.setVisibility(View.VISIBLE);
    }

    private void meraQuit() {
        meraWeb.loadUrl("");
        meraWeb.destroy();
        mediaplayer.stop();
        finish();
        moveTaskToBack(true);
    }
}
