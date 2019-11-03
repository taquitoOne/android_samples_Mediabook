package com.aospstudio.android.apps.mediabook;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MediabookHomeActivity extends AppCompatActivity {

    final Context context = this;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.v6_layout_splash);
            i = new Intent(getApplicationContext(), MediabookActivity.class);
            if (getIntent() != null) {
                Uri intentUri = getIntent().getData();
                if (intentUri != null) {
                    i.putExtra("send_string", intentUri.toString());
                }
            }
            startHeavyProcessing();
        } catch (Exception e){
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

    private void startHeavyProcessing() {
        LongOperation k = new LongOperation();
        k.execute("");
    }

    @SuppressLint("StaticFieldLeak")
    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            startActivity(i);
            finish();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
