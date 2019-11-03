package com.aospstudio.android.apps.mediabook;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.webkit.WebViewClientCompat;

import com.aospstudio.android.apps.mediabook.components.MeraWeb;
import com.aospstudio.android.apps.mediabook.customtabs.CustomTabActivity;
import com.aospstudio.android.apps.mediabook.pages.BuyActivity;
import com.aospstudio.android.apps.mediabook.service.BackgroundService;
import com.aospstudio.android.apps.mediabook.theme.ThemeHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;

import java.util.Stack;

public class MediabookActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    final Context context = this;
    private View editappsSheet, mCustomView;
    private MaterialToolbar toolbar_home, toolbar_browser, toolbar_more, toolbar_settings, toolbar_clear;
    private RelativeLayout home, browser, browserStartPage, meraWebUp, more, errorPage, settingsPage, clearPage;
    private NestedScrollView homeNested, moreNested;
    private BottomNavigationView navigation;
    private View appheader;

    private MediaPlayer clickPlayer, clickPlayer2;
    private SwipeRefreshLayout swipeContainer;
    private ContentLoadingProgressBar progressBar;
    private static MeraWeb meraWeb;
    private CookieManager cookieManager;
    private static MaterialCheckBox chckfacebook, chckmessenger, chckinstagram, chckigdirect, chcktwitter, chcktumblr, chckpinterest, chcklinkedin, chckworkplace, chckmyspace, chckflickr, chckperiscope, chckmeetup, chckweibo, chckbadoo, chcktwoo, chckquora, chckwikipedia, chckwattpad, chckreddit, chckmedium, chckaskfm, chcklivejournal, chckspotify, chckytmusic, chcksoundcloud, chcklastfm, chckvk, chcktwitch, chckdlive, chckdailymotion, chckyoutube;
    private SwitchMaterial mutesound, apptheme, locationpermit, storagepermit, javascript, cookies, privacymode, navigation_labels, hidetoolbar, nosleep;
    private MaterialTextView hidesocial, versionname, webkitname, errorTxt3, proversion, help_setting, close_setting, close_bg, pagename, back, name, notifyText, restart, fastrestart, clearcache, notifyClose, clear_datas1, clear_datas2, clear_setting;
    private AppCompatButton retry, settings_more, clearapp_more, help_more, restart_more, buypro_more, facebook, messenger, instagram, igdirect, twitter, tumblr, pinterest, linkedin, workplace, myspace, flickr, periscope, meetup, weibo, badoo, twoo, quora, wikipedia, wattpad, reddit, medium, askfm, livejournal, spotify, ytmusic, soundcloud, lastfm, vk, twitch, dlive, dailymotion, youtube;
    private static ExtendedFloatingActionButton editapps, upweb;
    private BottomSheetDialog editAppsSheetDialog, sheet;
    private TextView title, msg;
    private Button button_close, button_ok;

    private static final String PREFS = "sb_pref";
    private SharedPreferences prefs, hideprefs;
    private SharedPreferences.Editor editor;
    private static Stack<Integer> pageHistory;

    private String versionName = BuildConfig.VERSION_NAME;
    private String userid = Build.BOARD;

    private static final String TAG = MediabookActivity.class.getSimpleName();
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;

    @SuppressWarnings("deprecation")
    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility", "WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.v6_layout_all);

            // BUTTON CLICK SOUNDS
            clickPlayer = MediaPlayer.create(context, R.raw.click);

            pageHistory = new Stack<>();

            toolbar_home = findViewById(R.id.toolbar_home);
            toolbar_home.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.buypro) {
                    clickPlayer.start();
                    startActivity(new Intent(context, BuyActivity.class));
                }
                return true;
            });
            toolbar_home.setTitle(getString(R.string.bottom_home));

            toolbar_browser = findViewById(R.id.toolbar_browser);
            toolbar_browser.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.incognito) {
                    privacymode = findViewById(R.id.privacy_btn);
                    privacymode.setChecked(!privacymode.isChecked());
                } else if (item.getItemId() == R.id.share) {
                    clickPlayer.start();
                    shareURL();
                } else if (item.getItemId() == R.id.print) {
                    clickPlayer.start();
                    createWebPrintJob(meraWeb);
                }
                return true;
            });

            toolbar_more = findViewById(R.id.toolbar_more);

            toolbar_settings = findViewById(R.id.toolbar_settings);
            toolbar_settings.setNavigationOnClickListener(v -> {
                toolbar_more.setVisibility(View.VISIBLE);
                settingsPage.setVisibility(View.GONE);
                clearPage.setVisibility(View.GONE);
            });

            toolbar_clear = findViewById(R.id.toolbar_clear);
            toolbar_clear.setNavigationOnClickListener(v -> {
                toolbar_more.setVisibility(View.VISIBLE);
                clearPage.setVisibility(View.GONE);
                clearPage.setVisibility(View.GONE);
            });

            // WEBVIEW
            progressBar = findViewById(R.id.progressBarV6);
            swipeContainer = this.findViewById(R.id.swipeContainer);
            swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
            meraWeb = findViewById(R.id.meraWeb);
            WebSettings meraKit = meraWeb.getSettings();
            meraKit.setSupportMultipleWindows(true);
            meraKit.setSupportZoom(true);
            meraKit.setBuiltInZoomControls(true);
            meraKit.setDisplayZoomControls(false);
            meraWeb.setWebChromeClient(new MediabookChromeClient());
            meraWeb.setWebViewClient(new MediabookWebClient());
            meraWeb.setDownloadListener(new MediabookDownloadService());
            meraWeb.setVerticalScrollBarEnabled(true);
            meraWeb.setHorizontalScrollBarEnabled(true);
            meraWeb.setHapticFeedbackEnabled(false);

            swipeContainer.setOnRefreshListener(() -> {
                        meraWeb.reload();
                        swipeContainer.setRefreshing(false);
                    }
            );

            meraWebUp = findViewById(R.id.meraWebUp);
            upweb = findViewById(R.id.upweb);
            upweb.setVisibility(View.GONE);
            upweb.setOnClickListener(v -> {
                clickPlayer.start();
                ObjectAnimator anim2 = ObjectAnimator.ofInt(meraWeb, "scrollY", meraWeb.getScrollY(), 0);
                anim2.setDuration(500);
                anim2.start();
            });

            meraWeb.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (meraWeb.getScrollY() == 0) {
                    swipeContainer.setEnabled(true);
                } else {
                    swipeContainer.setEnabled(false);
                }
                if (scrollY > oldScrollY) {
                    upweb.hide();
                }
                if (scrollY < oldScrollY) {
                    upweb.show();
                }
                if (scrollY == 0) {
                    upweb.hide();
                }
            });

            // NAVIGATION
            navigation = findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(this);

            // ONE HAND MODE & PAGES
            appheader = findViewById(R.id.appheader);
            appheader.setVisibility(View.GONE);
            home = findViewById(R.id.page1);
            browser = findViewById(R.id.page2);
            more = findViewById(R.id.page3);
            homeNested = findViewById(R.id.homeNested);
            moreNested = findViewById(R.id.moreNested);

            if (homeNested != null) {
                homeNested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY > oldScrollY) {
                        editapps.hide();
                    }
                    if (scrollY < oldScrollY) {
                        editapps.show();
                    }
                });
            }

            // SETTINGS & CLEAR PAGES
            settingsPage = findViewById(R.id.pageSettings);
            clearPage = findViewById(R.id.pageClear);

            // ERROR PAGE
            browserStartPage = findViewById(R.id.browserPageStarting);
            errorPage = findViewById(R.id.pageError);
            errorTxt3 = findViewById(R.id.errorTxt3);
            retry = findViewById(R.id.retry);

            // SETTINGS APP
            javascript = findViewById(R.id.javascript_btn);
            apptheme = findViewById(R.id.apptheme_btn);
            hidesocial = findViewById(R.id.hidesocial_btn);
            navigation_labels = findViewById(R.id.navigation_label_btn);
            hidetoolbar = findViewById(R.id.hide_toolbar_btn);
            privacymode = findViewById(R.id.privacy_btn);
            mutesound = findViewById(R.id.mutesounds_btn);
            nosleep = findViewById(R.id.nosleep_btn);
            locationpermit = findViewById(R.id.location_btn);
            storagepermit = findViewById(R.id.storage_btn);
            cookies = findViewById(R.id.cookies_btn);

            // CLEAR APP;
            clearcache = findViewById(R.id.clearcache);
            clear_setting = findViewById(R.id.clear_setting);
            clear_datas1 = findViewById(R.id.clear_datas1);
            clear_datas2 = findViewById(R.id.clear_datas2);

            // VERSIONS;
            versionname = findViewById(R.id.versionname);
            webkitname = findViewById(R.id.webkitname);

            // WEBKIT & APP VERSION
            versionname.setText(getString(R.string.version_title) + ": " + getString(R.string.app_version));
            webkitname.setText(getString(R.string.webkit_title) + ": " + getString(R.string.opr_version));

            // APPLICATION SETTINGS
            javascript.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("javascript", javascript.isChecked());
                meraKit.setJavaScriptEnabled(!javascript.isChecked());
            });
            apptheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("apptheme", apptheme.isChecked());
                ThemeHelper.applyTheme(apptheme.isChecked() ? ThemeHelper.DARK_MODE : ThemeHelper.LIGHT_MODE);
                if (pageHistory.size() > 1) {
                    pageHistory.pop();
                    navigation.setSelectedItemId(pageHistory.lastElement());
                }
            });
            navigation_labels.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("navigationlabels", navigation_labels.isChecked());
                if (navigation_labels.isChecked()) {
                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
                } else {
                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                }
            });
            hidetoolbar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("hidetoolbar", hidetoolbar.isChecked());
                toolbar_browser.setVisibility(hidetoolbar.isChecked() ? View.GONE : View.VISIBLE);
            });

            hidesocial.setOnClickListener(v -> editapps.callOnClick());

            privacymode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("privacymode", privacymode.isChecked());
                meraWeb.getSettings().setAppCacheEnabled(!privacymode.isChecked());
                meraKit.setCacheMode(privacymode.isChecked() ? WebSettings.LOAD_NO_CACHE : WebSettings.LOAD_DEFAULT);
                if (privacymode.isChecked()) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                    meraWeb.clearHistory();
                    meraWeb.clearCache(true);
                    meraWeb.clearFormData();
                    WebViewDatabase.getInstance(context).clearFormData();
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
                meraWeb.getSettings().setSavePassword(!privacymode.isChecked());
                meraWeb.getSettings().setSaveFormData(!privacymode.isChecked());
                getWindow().setStatusBarColor(getColor(privacymode.isChecked() ? R.color.incognito_colorDark : R.color.colorPrimaryDark));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    getWindow().setNavigationBarColor(getColor(privacymode.isChecked() ? R.color.incognito_colorDark : R.color.colorPrimaryDark));
                }
                toolbar_browser.setBackgroundColor(getColor(privacymode.isChecked() ? R.color.incognito_color : R.color.colorBackground));
                navigation.setBackgroundColor(getColor(privacymode.isChecked() ? R.color.incognito_color : R.color.colorBackground));
                appheader.setBackgroundColor(getColor(privacymode.isChecked() ? R.color.incognito_color : R.color.colorBackground));
            });
            mutesound.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("mutesound", mutesound.isChecked());
            });
            nosleep.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("nosleep", nosleep.isChecked());
                if (nosleep.isChecked()) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });
            locationpermit.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("locationpermit", locationpermit.isChecked());
                if (locationpermit.isChecked()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                        }, 100);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                        }, 100);
                    }
                }
                meraKit.setGeolocationEnabled(locationpermit.isChecked());
            });
            storagepermit.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("storagepermit", storagepermit.isChecked());

                if (storagepermit.isChecked()) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    }, 101);
                }
                meraKit.setDomStorageEnabled(storagepermit.isChecked());
                meraKit.setAppCacheEnabled(storagepermit.isChecked());
                meraKit.setAppCachePath(context.getFilesDir().getAbsolutePath() + "/cache");
                meraKit.setDatabaseEnabled(storagepermit.isChecked());
                meraKit.setDatabasePath(context.getFilesDir().getAbsolutePath() + "/databases");
                meraKit.setAllowFileAccess(storagepermit.isChecked());
                meraKit.setAllowContentAccess(storagepermit.isChecked());
                meraKit.setAllowFileAccessFromFileURLs(storagepermit.isChecked());
                meraKit.setAllowUniversalAccessFromFileURLs(storagepermit.isChecked());
            });
            cookies.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchClickSound();
                saveAppSettings("thirdycookies", cookies.isChecked());
                CookieSyncManager.createInstance(context);
                CookieSyncManager.getInstance().startSync();
                CookieManager.getInstance().setAcceptThirdPartyCookies(meraWeb, !cookies.isChecked());
                CookieManager.getInstance().setAcceptCookie(true);
            });

            // APPLICATION CLEAR SETTINGS
            clear_setting.setOnClickListener(v -> {
                switchClickSound();
                SharedPreferences settings = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
                settings.edit().clear().apply();
                javascript.setChecked(false);
                apptheme.setChecked(false);
                navigation_labels.setChecked(false);
                hidetoolbar.setChecked(false);
                privacymode.setChecked(false);
                mutesound.setChecked(false);
                nosleep.setChecked(false);
                locationpermit.setChecked(false);
                storagepermit.setChecked(false);
                cookies.setChecked(false);
                Snackbar
                        .make(browser, getString(R.string.title_csetting), 4500)
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .setTextColor(getResources().getColor(R.color.settings_text_color))
                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                        .setAnchorView(navigation)
                        .show();
            });
            clearcache.setOnClickListener(v -> clearapp_more.callOnClick());

            clear_datas1.setOnClickListener(v -> {
                clearClickSound();
                context.deleteDatabase("webview.db");
                context.deleteDatabase("webviewCache.db");
                meraWeb.clearCache(true);
                meraWeb.clearFormData();
                meraWeb.clearHistory();
                Snackbar
                        .make(browser, getString(R.string.snack_cleared), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .setTextColor(getResources().getColor(R.color.settings_text_color))
                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                        .setAnchorView(navigation)
                        .show();
            });
            clear_datas2.setOnClickListener(v -> {
                clearClickSound();
                clearCookies(context);
                WebViewDatabase.getInstance(context).clearFormData();
                Snackbar
                        .make(browser, getString(R.string.snack_cleared), Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .setTextColor(getResources().getColor(R.color.settings_text_color))
                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                        .setAnchorView(navigation)
                        .show();
            });

            // MORE MENU
            settings_more = findViewById(R.id.settings_more);
            settings_more.setOnClickListener(v -> {
                clickPlayer.start();
                toolbar_more.setVisibility(View.GONE);
                clearPage.setVisibility(View.GONE);
                settingsPage.setVisibility(View.VISIBLE);
            });
            clearapp_more = findViewById(R.id.clearapp_more);
            clearapp_more.setOnClickListener(v -> {
                clickPlayer.start();
                toolbar_more.setVisibility(View.GONE);
                settingsPage.setVisibility(View.GONE);
                clearPage.setVisibility(View.VISIBLE);
            });
            restart_more = findViewById(R.id.restart_more);
            restart_more.setOnClickListener(v -> {
                clickPlayer.start();
                meraRestart();
            });
            help_more = findViewById(R.id.help_more);
            help_more.setOnClickListener(v -> {
                clickPlayer.start();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://aospstudiom.com/contact")));
            });
            buypro_more = findViewById(R.id.buypro_more);
            buypro_more.setVisibility(View.GONE);
            buypro_more.setOnClickListener(v -> {
                clickPlayer.start();
                startActivity(new Intent(context, BuyActivity.class));
            });

            // ALL APPS
            facebook = findViewById(R.id.facebook);
            messenger = findViewById(R.id.messenger);
            instagram = findViewById(R.id.instagram);
            igdirect = findViewById(R.id.direct);
            twitter = findViewById(R.id.twitter);
            tumblr = findViewById(R.id.tumblr);
            pinterest = findViewById(R.id.pinterest);
            linkedin = findViewById(R.id.linkedin);
            workplace = findViewById(R.id.workplace);
            myspace = findViewById(R.id.myspace);
            flickr = findViewById(R.id.flickr);
            periscope = findViewById(R.id.periscope);
            vk = findViewById(R.id.vk);
            meetup = findViewById(R.id.meetup);
            weibo = findViewById(R.id.weibo);
            badoo = findViewById(R.id.badoo);
            twoo = findViewById(R.id.twoo);
            quora = findViewById(R.id.quora);
            wikipedia = findViewById(R.id.wikipedia);
            wattpad = findViewById(R.id.wattpad);
            reddit = findViewById(R.id.reddit);
            medium = findViewById(R.id.medium);
            askfm = findViewById(R.id.askfm);
            livejournal = findViewById(R.id.livejournal);
            spotify = findViewById(R.id.spotify);
            spotify.setVisibility(View.GONE);
            soundcloud = findViewById(R.id.soundcloud);
            lastfm = findViewById(R.id.lastfm);
            twitch = findViewById(R.id.twitch);
            dlive = findViewById(R.id.dlive);
            dailymotion = findViewById(R.id.dailymotion);
            youtube = findViewById(R.id.youtube);
            ytmusic = findViewById(R.id.ytmusic);

            facebook.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://facebook.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_facebook));
            });
            messenger.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://facebook.com/messages/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_messenger));
            });
            instagram.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://instagram.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_instagram));
            });
            igdirect.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://instagram.com/direct/inbox/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_igdirect));
            });
            twitter.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://mobile.twitter.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_twitter));
            });
            tumblr.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.tumblr.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_tumblr));
            });
            pinterest.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://pinterest.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_pinterest));
            });
            linkedin.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.linkedin.com/uas/login?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_linkedin));
            });
            workplace.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://work.workplace.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_workplace));
            });
            myspace.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://myspace.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_myspace));
            });
            flickr.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.flickr.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_flickr));
            });
            periscope.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.pscp.tv/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_periscope));
            });
            vk.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://vk.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_vk));
            });
            meetup.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://secure.meetup.com/login/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_meetup));
            });
            weibo.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://m.weibo.cn/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_weibo));
            });
            badoo.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://badoo.com/landing/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_badoo));
            });
            twoo.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.twoo.com/#login?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_twoo));
            });
            quora.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.quora.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_quora));
            });
            wikipedia.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://wikipedia.org/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_wiki));
            });
            wattpad.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://wattpad.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_wattpad));
            });
            reddit.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.reddit.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_reddit));
            });
            medium.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://medium.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_medium));
            });
            askfm.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://ask.fm/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_askfm));
            });
            livejournal.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.livejournal.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_livejournal));
            });
            spotify.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://open.spotify.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_spotify));
            });
            ytmusic.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://music.youtube.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_ytmusic));
            });
            soundcloud.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://soundcloud.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_soundcloud));
            });
            lastfm.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.last.fm/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_lastfm));
            });
            twitch.setOnClickListener(v -> {
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://www.twitch.tv/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_twitch));
            });
            dlive.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://dlive.tv/m/home?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_dlive));
            });
            dailymotion.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://dailymotion.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_dailymotion));
            });
            youtube.setOnClickListener(v -> {
                clickPlayer.start();
                navigation.setSelectedItemId(R.id.browser);
                meraWeb.loadUrl("https://youtube.com/?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb");
                toolbar_browser.setTitle(getString(R.string.app_name_youtube));
            });

            // HIDE APPS SHEET
            editapps = findViewById(R.id.editapps);
            editAppsSheetDialog = new BottomSheetDialog(context);
            editAppsSheetDialog.setContentView(R.layout.v6_sheet_editapps);

            editapps.setOnClickListener(v -> {
                clickPlayer.start();
                chckfacebook = editAppsSheetDialog.findViewById(R.id.chckfacebook);
                chckmessenger = editAppsSheetDialog.findViewById(R.id.chckmessenger);
                chckinstagram = editAppsSheetDialog.findViewById(R.id.chckinstagram);
                chckigdirect = editAppsSheetDialog.findViewById(R.id.chckdirect);
                chcktwitter = editAppsSheetDialog.findViewById(R.id.chcktwitter);
                chcktumblr = editAppsSheetDialog.findViewById(R.id.chcktumblr);
                chckpinterest = editAppsSheetDialog.findViewById(R.id.chckpinterest);
                chcklinkedin = editAppsSheetDialog.findViewById(R.id.chcklinkedin);
                chckworkplace = editAppsSheetDialog.findViewById(R.id.chckworkplace);
                chckmyspace = editAppsSheetDialog.findViewById(R.id.chckmyspace);
                chckflickr = editAppsSheetDialog.findViewById(R.id.chckflickr);
                chckperiscope = editAppsSheetDialog.findViewById(R.id.chckperiscope);
                chckvk = editAppsSheetDialog.findViewById(R.id.chckvk);
                chckmeetup = editAppsSheetDialog.findViewById(R.id.chckmeetup);
                chckweibo = editAppsSheetDialog.findViewById(R.id.chckweibo);
                chckbadoo = editAppsSheetDialog.findViewById(R.id.chckbadoo);
                chcktwoo = editAppsSheetDialog.findViewById(R.id.chcktwoo);
                chckquora = editAppsSheetDialog.findViewById(R.id.chckquora);
                chckwikipedia = editAppsSheetDialog.findViewById(R.id.chckwiki);
                chckwattpad = editAppsSheetDialog.findViewById(R.id.chckwattpad);
                chckreddit = editAppsSheetDialog.findViewById(R.id.chckreddit);
                chckmedium = editAppsSheetDialog.findViewById(R.id.chckmedium);
                chckaskfm = editAppsSheetDialog.findViewById(R.id.chckaskfm);
                chcklivejournal = editAppsSheetDialog.findViewById(R.id.chcklivejournal);
                chckspotify = editAppsSheetDialog.findViewById(R.id.chckspotify);
                chcksoundcloud = editAppsSheetDialog.findViewById(R.id.chcksoundcloud);
                chcklastfm = editAppsSheetDialog.findViewById(R.id.chcklastfm);
                chcktwitch = editAppsSheetDialog.findViewById(R.id.chcktwitch);
                chckdlive = editAppsSheetDialog.findViewById(R.id.chckdlive);
                chckdailymotion = editAppsSheetDialog.findViewById(R.id.chckdailymotion);
                chckyoutube = editAppsSheetDialog.findViewById(R.id.chckyoutube);
                chckytmusic = editAppsSheetDialog.findViewById(R.id.chckytmusic);
                chckfacebook.setChecked(isHideApp("chckfacebook"));
                chckmessenger.setChecked(isHideApp("chckmessenger"));
                chckinstagram.setChecked(isHideApp("chckinstagram"));
                chckigdirect.setChecked(isHideApp("chckigdirect"));
                chcktwitter.setChecked(isHideApp("chcktwitter"));
                chcktumblr.setChecked(isHideApp("chcktumblr"));
                chckpinterest.setChecked(isHideApp("chckpinterest"));
                chcklinkedin.setChecked(isHideApp("chcklinkedin"));
                chckworkplace.setChecked(isHideApp("chckworkplace"));
                chckmyspace.setChecked(isHideApp("chckmyspace"));
                chckflickr.setChecked(isHideApp("chckflickr"));
                chckperiscope.setChecked(isHideApp("chckperiscope"));
                chckvk.setChecked(isHideApp("chckvk"));
                chckmeetup.setChecked(isHideApp("chckmeetup"));
                chckweibo.setChecked(isHideApp("chckweibo"));
                chckbadoo.setChecked(isHideApp("chckbadoo"));
                chcktwoo.setChecked(isHideApp("chcktwoo"));
                chckquora.setChecked(isHideApp("chckquora"));
                chckwikipedia.setChecked(isHideApp("chckwiki"));
                chckwattpad.setChecked(isHideApp("chckwattpad"));
                chckreddit.setChecked(isHideApp("chckreddit"));
                chckmedium.setChecked(isHideApp("chckmedium"));
                chckaskfm.setChecked(isHideApp("chckaskfm"));
                chcklivejournal.setChecked(isHideApp("chcklivejournal"));
                //chckspotify.setChecked(isHideApp("chckspotify"));
                chcksoundcloud.setChecked(isHideApp("chcksoundcloud"));
                chcklastfm.setChecked(isHideApp("chcklastfm"));
                chcktwitch.setChecked(isHideApp("chcktwitch"));
                chckdlive.setChecked(isHideApp("chckdlive"));
                chckdailymotion.setChecked(isHideApp("chckdailymotion"));
                chckyoutube.setChecked(isHideApp("chckyoutube"));
                chckytmusic.setChecked(isHideApp("chckytmusic"));

                editAppsSheetDialog.show();

                chckfacebook.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckfacebook", chckfacebook.isChecked());
                    facebook.setVisibility(chckfacebook.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckmessenger.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckmessenger", chckmessenger.isChecked());
                    messenger.setVisibility(chckmessenger.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckinstagram.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckinstagram", chckinstagram.isChecked());
                    instagram.setVisibility(chckinstagram.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckigdirect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckigdirect", chckigdirect.isChecked());
                    igdirect.setVisibility(chckigdirect.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcktwitter.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcktwitter", chcktwitter.isChecked());
                    twitter.setVisibility(chcktwitter.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcktumblr.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcktumblr", chcktumblr.isChecked());
                    tumblr.setVisibility(chcktumblr.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckpinterest.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckpinterest", chckpinterest.isChecked());
                    pinterest.setVisibility(chckpinterest.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcklinkedin.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcklinkedin", chcklinkedin.isChecked());
                    linkedin.setVisibility(chcklinkedin.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckworkplace.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckworkplace", chckworkplace.isChecked());
                    workplace.setVisibility(chckworkplace.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckmyspace.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckmyspace", chckmyspace.isChecked());
                    myspace.setVisibility(chckmyspace.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckflickr.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckflickr", chckflickr.isChecked());
                    flickr.setVisibility(chckflickr.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckperiscope.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckperiscope", chckperiscope.isChecked());
                    periscope.setVisibility(chckperiscope.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckvk.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckvk", chckvk.isChecked());
                    vk.setVisibility(chckvk.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckmeetup.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckmeetup", chckmeetup.isChecked());
                    meetup.setVisibility(chckmeetup.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckweibo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckweibo", chckweibo.isChecked());
                    weibo.setVisibility(chckweibo.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckbadoo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckbadoo", chckbadoo.isChecked());
                    badoo.setVisibility(chckbadoo.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcktwoo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcktwoo", chcktwoo.isChecked());
                    twoo.setVisibility(chcktwoo.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckquora.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckquora", chckquora.isChecked());
                    quora.setVisibility(chckquora.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckwikipedia.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckwiki", chckwikipedia.isChecked());
                    wikipedia.setVisibility(chckwikipedia.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckwattpad.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckwattpad", chckwattpad.isChecked());
                    wattpad.setVisibility(chckwattpad.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckreddit.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckreddit", chckreddit.isChecked());
                    reddit.setVisibility(chckreddit.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckmedium.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckmedium", chckmedium.isChecked());
                    medium.setVisibility(chckmedium.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckaskfm.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckaskfm", chckaskfm.isChecked());
                    askfm.setVisibility(chckaskfm.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcklivejournal.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcklivejournal", chcklivejournal.isChecked());
                    livejournal.setVisibility(chcklivejournal.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckspotify.setVisibility(View.GONE);
                chckspotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckspotify", chckspotify.isChecked());
                    spotify.setVisibility(chckspotify.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckytmusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckytmusic", chckytmusic.isChecked());
                    ytmusic.setVisibility(chckytmusic.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcksoundcloud.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcksoundcloud", chcksoundcloud.isChecked());
                    soundcloud.setVisibility(chcksoundcloud.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcklastfm.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcklastfm", chcklastfm.isChecked());
                    lastfm.setVisibility(chcklastfm.isChecked() ? View.GONE : View.VISIBLE);
                });
                chcktwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chcktwitch", chcktwitch.isChecked());
                    twitch.setVisibility(chcktwitch.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckdlive.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckdlive", chckdlive.isChecked());
                    dlive.setVisibility(chckdlive.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckdailymotion.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckdailymotion", chckdailymotion.isChecked());
                    dailymotion.setVisibility(chckdailymotion.isChecked() ? View.GONE : View.VISIBLE);
                });
                chckyoutube.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    switchClickSound();
                    saveHideApp("chckyoutube", chckyoutube.isChecked());
                    youtube.setVisibility(chckyoutube.isChecked() ? View.GONE : View.VISIBLE);
                });
            });

            // GET DEEPLINK URLS
            try {
                Bundle extras = getIntent().getExtras();
                String value = extras.getString("send_string");
                meraWeb.loadUrl(value);
            } catch (Exception e) {
                if (getIntent() != null) {
                    Uri intentUri = getIntent().getData();
                    meraWeb.loadUrl(intentUri != null ? intentUri.toString() : null);
                } else {
                    Toast.makeText(context, getString(R.string.error_intent), Toast.LENGTH_SHORT).show();
                }
            }

            // LOAD SETTINGS & PAGE HISTORY
            hideHidenButton();
            settingsShowPrefs();
            pageHistory.push(R.id.home);
            clickPlayer.setVolume(mutesound.isChecked() ? 0 : 1, mutesound.isChecked() ? 0 : 1);
        } catch (Exception e) {
            // BUG SEND REPORT
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle(getResources().getString(R.string.app_name) + " has stopped working");
            builder.setView(R.layout.mpui_bug_detect_main);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();

            AppCompatButton bugscreen_info = dialog.findViewById(R.id.bug_screen_info);
            AppCompatButton bugscreen_close = dialog.findViewById(R.id.bug_screen_close);
            AppCompatButton bugscreen_copy = dialog.findViewById(R.id.bug_screen_copy);
            AppCompatButton bugscreen_send = dialog.findViewById(R.id.bug_screen_send);
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

    // TOUCH SOUNDS
    private void switchClickSound() {
        clickPlayer2 = MediaPlayer.create(context, R.raw.switchclick);
        clickPlayer2.setVolume(mutesound.isChecked() ? 0 : 1, mutesound.isChecked() ? 0 : 1);
        clickPlayer2.start();
    }

    private void clearClickSound() {
        clickPlayer2 = MediaPlayer.create(context, R.raw.trash);
        clickPlayer2.setVolume(mutesound.isChecked() ? 0 : 1, mutesound.isChecked() ? 0 : 1);
        clickPlayer2.start();
    }

    // NAVIGATION SELECT
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        clickPlayer.start();
        if (pageHistory.lastElement() != item.getItemId()) {
            pageHistory.push(item.getItemId());
        }
        home.setVisibility(View.GONE);
        browser.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        switch (item.getItemId()) {
            case R.id.home:
                toolbar_home.setTitle(getString(R.string.bottom_home));
                home.setVisibility(View.VISIBLE);
                break;
            case R.id.browser:
                browser.setVisibility(View.VISIBLE);
                break;
            case R.id.more:
                more.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

    // SETTING HIDE
    private void hideHidenButton() {
        facebook.setVisibility(!isHideApp("chckfacebook") ? View.VISIBLE : View.GONE);
        messenger.setVisibility(!isHideApp("chckmessenger") ? View.VISIBLE : View.GONE);
        instagram.setVisibility(!isHideApp("chckinstagram") ? View.VISIBLE : View.GONE);
        igdirect.setVisibility(!isHideApp("chckigdirect") ? View.VISIBLE : View.GONE);
        twitter.setVisibility(!isHideApp("chcktwitter") ? View.VISIBLE : View.GONE);
        tumblr.setVisibility(!isHideApp("chcktumblr") ? View.VISIBLE : View.GONE);
        pinterest.setVisibility(!isHideApp("chckpinterest") ? View.VISIBLE : View.GONE);
        linkedin.setVisibility(!isHideApp("chcklinkedin") ? View.VISIBLE : View.GONE);
        workplace.setVisibility(!isHideApp("chckworkplace") ? View.VISIBLE : View.GONE);
        myspace.setVisibility(!isHideApp("chckmyspace") ? View.VISIBLE : View.GONE);
        flickr.setVisibility(!isHideApp("chckflickr") ? View.VISIBLE : View.GONE);
        periscope.setVisibility(!isHideApp("chckperiscope") ? View.VISIBLE : View.GONE);
        vk.setVisibility(!isHideApp("chckvk") ? View.VISIBLE : View.GONE);
        meetup.setVisibility(!isHideApp("chckmeetup") ? View.VISIBLE : View.GONE);
        weibo.setVisibility(!isHideApp("chckweibo") ? View.VISIBLE : View.GONE);
        badoo.setVisibility(!isHideApp("chckbadoo") ? View.VISIBLE : View.GONE);
        twoo.setVisibility(!isHideApp("chcktwoo") ? View.VISIBLE : View.GONE);
        quora.setVisibility(!isHideApp("chckquora") ? View.VISIBLE : View.GONE);
        wikipedia.setVisibility(!isHideApp("chckwiki") ? View.VISIBLE : View.GONE);
        wattpad.setVisibility(!isHideApp("chckwattpad") ? View.VISIBLE : View.GONE);
        reddit.setVisibility(!isHideApp("chckreddit") ? View.VISIBLE : View.GONE);
        medium.setVisibility(!isHideApp("chckmedium") ? View.VISIBLE : View.GONE);
        askfm.setVisibility(!isHideApp("chckaskfm") ? View.VISIBLE : View.GONE);
        livejournal.setVisibility(!isHideApp("chcklivejournal") ? View.VISIBLE : View.GONE);
        //spotify.setVisibility(!isHideApp("chckspotify")? View.VISIBLE : View.GONE);
        ytmusic.setVisibility(!isHideApp("chckytmusic") ? View.VISIBLE : View.GONE);
        soundcloud.setVisibility(!isHideApp("chcksoundcloud") ? View.VISIBLE : View.GONE);
        lastfm.setVisibility(!isHideApp("chcklastfm") ? View.VISIBLE : View.GONE);
        twitch.setVisibility(!isHideApp("chcktwitch") ? View.VISIBLE : View.GONE);
        dlive.setVisibility(!isHideApp("chckdlive") ? View.VISIBLE : View.GONE);
        dailymotion.setVisibility(!isHideApp("chckdailymotion") ? View.VISIBLE : View.GONE);
        youtube.setVisibility(!isHideApp("chckyoutube") ? View.VISIBLE : View.GONE);
    }

    // SETTING APP
    private void settingsShowPrefs() {
        mutesound.setChecked(getAppSettings("mutesound"));
        javascript.setChecked(getAppSettings("javascript"));
        apptheme.setChecked(getAppSettings("apptheme"));
        navigation_labels.setChecked(getAppSettings("navigationlabels"));
        hidetoolbar.setChecked(getAppSettings("hidetoolbar"));
        privacymode.setChecked(getAppSettings("privacymode"));
        nosleep.setChecked(getAppSettings("nosleep"));
        locationpermit.setChecked(getAppSettings("locationpermit"));
        storagepermit.setChecked(getAppSettings("storagepermit"));
        cookies.setChecked(getAppSettings("thirdycookies"));
    }

    // SAVES
    public void saveHideApp(String key, boolean value) {
        hideprefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        editor = hideprefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    private boolean isHideApp(String key) {
        hideprefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return hideprefs.getBoolean(key, false);
    }

    private void saveAppSettings(String key, boolean value) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean getAppSettings(String key) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    // SNACKBAR DISMISS
    private void dismissSnackbar() {
        clickPlayer.start();
        Snackbar.make(home, "", Snackbar.LENGTH_LONG).dismiss();
    }

    private void createWebPrintJob(MeraWeb meraWeb) {
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter =
                meraWeb.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name);
        if (printManager != null) {
            printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
        }
    }

    // PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && requestCode == 100) {
            locationpermit.setChecked(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            saveAppSettings("locationpermit", locationpermit.isChecked());
        } else if (grantResults.length > 0 && requestCode == 101) {
            storagepermit.setChecked(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            saveAppSettings("storagepermit", storagepermit.isChecked());
        }
    }

    //WEB CLIENTS
    private class MediabookWebClient extends WebViewClientCompat {
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
            builder.setPositiveButton(R.string.okay_btn, (dialog, which) -> {
                clickPlayer.start();
                handler.proceed();
            });
            builder.setNegativeButton(R.string.cancel_btn, (dialog1, which) -> {
                clickPlayer.start();
                handler.cancel();
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onReceivedError(WebView view, int errorCod, String description, String failingUrl) {
            errorPage.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
            errorTxt3.setText(description);
            retry.setOnClickListener(v -> {
                clickPlayer.start();
                swipeContainer.setVisibility(View.VISIBLE);
                errorPage.setVisibility(View.GONE);
                meraWeb.clearCache(true);
                meraWeb.reload();
            });
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
                    || url.startsWith("geo:")
                    || url.startsWith("googledrive://")
                    || url.startsWith("https://m.me/"))
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    return true;
                } catch (ActivityNotFoundException e) {
                    Snackbar
                            .make(browser, getString(R.string.error_intent), 7000)
                            .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                            .setTextColor(getResources().getColor(R.color.settings_text_color))
                            .setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                            .setAnchorView(navigation)
                            .show();
                }
            else {
                Uri uri = Uri.parse(url);
                if (uri.getHost() != null && uri.getHost().contains("facebook.com")
                        || uri.getHost().contains("messenger.com")
                        || uri.getHost().contains("instagram.com")
                        || uri.getHost().contains("twitter.com")
                        || uri.getHost().contains("tumblr.com")
                        || uri.getHost().contains("pinterest.com")
                        || uri.getHost().contains("linkedin.com")
                        || uri.getHost().contains("workplace.com")
                        || uri.getHost().contains("myspace.com")
                        || uri.getHost().contains("flickr.com")
                        || uri.getHost().contains("pscp.tv")
                        || uri.getHost().contains("vk.com")
                        || uri.getHost().contains("badoo.com")
                        || uri.getHost().contains("twoo.com")
                        || uri.getHost().contains("meetup.com")
                        || uri.getHost().contains("quora.com")
                        || uri.getHost().contains("weibo.com")
                        || uri.getHost().contains("weibo.cn")
                        || uri.getHost().contains("wikipedia.org")
                        || uri.getHost().contains("wattpad.com")
                        || uri.getHost().contains("reddit.com")
                        || uri.getHost().contains("medium.com")
                        || uri.getHost().contains("ask.fm")
                        || uri.getHost().contains("livejournal.com")
                        || uri.getHost().contains("soundcloud.com")
                        || uri.getHost().contains("lastfm.com")
                        || uri.getHost().contains("twitch.tv")
                        || uri.getHost().contains("dlive.tv")
                        || uri.getHost().contains("dailymotion.com")
                        || uri.getHost().contains("youtube.com")
                        || uri.getHost().contains("music.youtube.com")
                        || uri.getHost().contains("accounts.google.com")) {
                    return false;
                }
                startActivity(new Intent(getApplicationContext(), CustomTabActivity.class).putExtra("send_url", url));
                return true;
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            navigation.setSelectedItemId(R.id.browser);
            browserStartPage.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private class MediabookChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(meraWeb.getProgress());
            progressBar.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg) {
            MeraWeb meraWeb = new MeraWeb(context);
            view.addView(meraWeb);
            MeraWeb.WebViewTransport transport = (MeraWeb.WebViewTransport) resultMsg.obj;
            transport.setWebView(meraWeb);
            resultMsg.sendToTarget();
            meraWeb.setWebViewClient(new WebViewClientCompat() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    startActivity(new Intent(getApplicationContext(), CustomTabActivity.class).putExtra("send_url", url + "?utm_source=mediabook&utm_medium=link&utm_campaign=meraweb"));
                    return true;
                }
            });
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            sheet = new BottomSheetDialog(context);
            sheet.setContentView(R.layout.v6_sheet_main);
            sheet.setCancelable(false);
            title = sheet.findViewById(R.id.title);
            title.setText(url);
            msg = sheet.findViewById(R.id.message);
            msg.setText(message);
            button_close = sheet.findViewById(R.id.button_close);
            button_close.setText(android.R.string.cancel);
            button_close.setOnClickListener(v -> {
                clickPlayer.start();
                result.cancel();
                sheet.dismiss();
            });
            button_ok = sheet.findViewById(R.id.button_ok);
            button_ok.setText(android.R.string.ok);
            button_ok.setOnClickListener(v -> {
                clickPlayer.start();
                result.confirm();
                sheet.dismiss();
            });
            sheet.show();
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
            home.setVisibility(View.VISIBLE);
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
            home.setVisibility(View.GONE);
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
    }

    private class MediabookDownloadService implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
            String filename = URLUtil.guessFileName(url, contentDisposition, mimeType);
            new MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.download_dialog_title))
                    .setMessage(filename + " " + getString(R.string.download_dialog_msg))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.download_dialog_ok), (dialog, which) -> {
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
                                .make(browser, getString(R.string.app_downloading_file), Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                                .setTextColor(getResources().getColor(R.color.settings_text_color))
                                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                                .setAnchorView(navigation)
                                .show();
                    })
                    .setNegativeButton(getString(R.string.download_dialog_cancel), (dialog, which) -> {
                    })
                    .create().show();
        }
    }

    // CLEAR COOKIES
    private static void clearCookies(Context context) {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    // URL SHARE
    private void shareURL() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, meraWeb.getUrl());
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_url)));
    }

    // SERVICES
    private void startService(View view) {
        startService(new Intent(context, BackgroundService.class));
    }

    private void stopService(View view) {
        stopService(new Intent(context, BackgroundService.class));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == Activity.RESULT_OK && data != null) {
            String dataString = data.getDataString();
            if (dataString != null) {
                mFilePathCallback.onReceiveValue(new Uri[]{Uri.parse(dataString)});
            }
        } else {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = null;
        return;
    }

    // SAVE WEBVIEW STATE
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        meraWeb.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    // RESTORE WEBVIEW STATE
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        meraWeb.restoreState(state);
        super.onRestoreInstanceState(state);
    }

    // NORMAL & SHOCK RESTART SHEETS
    private void meraRestart() {
        sheet = new BottomSheetDialog(context);
        sheet.setContentView(R.layout.v6_sheet_main);
        title = sheet.findViewById(R.id.title);
        title.setText(getString(R.string.restart_title));
        msg = sheet.findViewById(R.id.message);
        msg.setText(getString(R.string.restart_message));
        button_close = sheet.findViewById(R.id.button_close);
        button_close.setText(getString(R.string.cancel_btn));
        button_close.setOnClickListener(v -> {
            clickPlayer.start();
            sheet.dismiss();
        });
        button_ok = sheet.findViewById(R.id.button_ok);
        button_ok.setText(getString(R.string.restart_button));
        button_ok.setOnClickListener(v -> {
            clickPlayer.start();
            Intent intent = (new Intent(context, MediabookActivity.class));
            meraWeb.loadUrl("");
            meraWeb.destroy();
            finishAndRemoveTask();
            moveTaskToBack(true);
            startActivity(intent);
        });
        sheet.show();
    }

    private void meraShockRestart() {
        sheet = new BottomSheetDialog(context);
        sheet.setContentView(R.layout.v6_sheet_main);
        title = sheet.findViewById(R.id.title);
        title.setText(getString(R.string.shock_title));
        msg = sheet.findViewById(R.id.message);
        msg.setText(getString(R.string.shock_msg));
        button_close = sheet.findViewById(R.id.button_close);
        button_close.setText(getString(R.string.cancel_btn));
        button_close.setOnClickListener(v -> {
            clickPlayer.start();
            sheet.dismiss();
        });
        button_ok = sheet.findViewById(R.id.button_ok);
        button_ok.setText(getString(R.string.shock_btn));
        button_ok.setOnClickListener(v -> {
            clickPlayer.start();
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
            meraWeb.clearCache(true);
            meraWeb.clearFormData();
            meraWeb.clearHistory();
            meraWeb.reload();
            sheet.dismiss();
            Snackbar
                    .make(browser, getString(R.string.shock_notify), 6000)
                    .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                    .setTextColor(getResources().getColor(R.color.settings_text_color))
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .setAction(getString(R.string.notify_del), v1 -> dismissSnackbar())
                    .setAnchorView(navigation)
                    .show();
        });
        sheet.show();
    }

    // APP BACK BUTTON
    @Override
    public void onBackPressed() {
        clickPlayer.start();
        if (navigation.getSelectedItemId() == R.id.home) {
            meraWeb.destroy();
            super.onBackPressed();
        } else if (navigation.getSelectedItemId() == R.id.browser) {
            if (meraWeb.canGoBack()) {
                meraWeb.goBack();
            } else {
                if (pageHistory.size() > 1) {
                    pageHistory.pop();
                    navigation.setSelectedItemId(pageHistory.lastElement());
                }
            }
        } else {
            if (pageHistory.size() > 1) {
                pageHistory.pop();
                navigation.setSelectedItemId(pageHistory.lastElement());
            }
        }
    }
}
