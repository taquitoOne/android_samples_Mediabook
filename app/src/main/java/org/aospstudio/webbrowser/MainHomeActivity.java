package org.aospstudio.webbrowser;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.circularreveal.CircularRevealRelativeLayout;

public class MainHomeActivity extends AppCompatActivity {

    final Context context = this;
    private AppCompatButton facebook, messenger, instagram, twitter, tumblr, pinterest, linkedin, workplace, myspace, flickr, periscope, meetup, weibo, badoo, twoo, quora, wikipedia, wattpad, reddit, medium, askfm, livejournal, soundcloud, lastfm, vk, twitch, dailymotion, youtube;
    private AppCompatImageView quit_btn;
    private CircularRevealRelativeLayout notify_screen, notify_main;
    private AppCompatTextView notifyText, notifyClose, privacymenu, termsmenu;

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.v5_startingpage_main);
            meraPermission();

            quit_btn = findViewById(R.id.quit_btn);
            quit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    moveTaskToBack(true);
                }
            });

            privacymenu = findViewById(R.id.privacymenu);
            privacymenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = getString(R.string.appprivacy_url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            termsmenu = findViewById(R.id.termsmenu);
            termsmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = getString(R.string.appterms_url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            notify_screen = findViewById(R.id.notify_screen);
            notify_screen = findViewById(R.id.notify_screen);
            notify_main = findViewById(R.id.notify_main);
            notify_main.setVisibility(View.VISIBLE);
            notifyText = findViewById(R.id.notifyText);
            notifyText.setText(R.string.welcome_mediabook);
            notifyClose = findViewById(R.id.notifyClose);
            notifyClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyText.setText("");
                    notify_main.setVisibility(View.GONE);
                }
            });

            facebook = findViewById(R.id.facebook);
            messenger = findViewById(R.id.messenger);
            instagram = findViewById(R.id.instagram);
            twitter = findViewById(R.id.twitter);
            tumblr = findViewById(R.id.tumblr);
            pinterest = findViewById(R.id.pinterest);
            linkedin = findViewById(R.id.linkedin);
            workplace = findViewById(R.id.workplace);
            myspace = findViewById(R.id.myspace);
            flickr = findViewById(R.id.flickr);
            periscope = findViewById(R.id.periscope);
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
            soundcloud = findViewById(R.id.soundcloud);
            lastfm = findViewById(R.id.lastfm);
            vk = findViewById(R.id.vk);
            twitch = findViewById(R.id.twitch);
            dailymotion = findViewById(R.id.dailymotion);
            youtube = findViewById(R.id.youtube);

            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://facebook.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_facebook);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            messenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://facebook.com/messages/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_messenger);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            instagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://instagram.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_instagram);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://mobile.twitter.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_twitter);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            tumblr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.tumblr.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_tumblr);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            pinterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://pinterest.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_pinterest);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            linkedin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.linkedin.com/uas/login?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_linkedin);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            workplace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://work.facebook.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_workplace);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            myspace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://myspace.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_myspace);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            flickr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.flickr.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_flickr);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            periscope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.pscp.tv/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_periscope);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            meetup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://secure.meetup.com/login/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_meetup);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            weibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://m.weibo.cn/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_weibo);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            badoo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://badoo.com/landing/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_badoo);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            twoo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.twoo.com/#login?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_twoo);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            quora.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.quora.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_quora);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            wikipedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://wikipedia.org/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_wiki);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            wattpad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://wattpad.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_wattpad);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            reddit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.reddit.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_reddit);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            medium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://medium.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_medium);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            askfm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://ask.fm/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_askfm);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            livejournal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.livejournal.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_livejournal);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            soundcloud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://soundcloud.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_soundcloud);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            lastfm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.last.fm/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_lastfm);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            vk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://vk.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_vk);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            twitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://www.twitch.tv/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_twitch);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            dailymotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://dailymotion.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_dailymotion);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });
            youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "https://youtube.com/?utm_source=merasocial";
                    String s2 = getString(R.string.app_name_youtube);
                    Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                    i.putExtra("send_string", s);
                    i.putExtra("name", s2);
                    startActivity(i);
                }
            });

        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder
                    .setTitle("aospstudio problem wizard")
                    .setMessage("Sorry, an unexpected error has occurred. In order to correct the problem, please send us SplashActivity report on the facebook below.")
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
                            finish();
                            System.exit(0);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void meraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 0);
    }
}
