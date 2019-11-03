package com.aospstudio.android.apps.mediabook.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aospstudio.android.apps.mediabook.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class BuyActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    MaterialButton buyapp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v6_buypro_main);
        toolbar = findViewById(R.id.buytoolbar);
        toolbar.setNavigationOnClickListener(v -> finishAndRemoveTask());

        buyapp_btn = findViewById(R.id.buyapp_btn);
        //buyapp_btn.setAlpha(.5f);
        //buyapp_btn.setText(getResources().getString(R.string.buy_btn_disabled));
        //buyapp_btn.setEnabled(false);
        buyapp_btn.setOnClickListener(v -> {
            String url = "https://play.google.com/store/apps/details?id=org.aospstudio.socialbrowser.pro";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }
}
