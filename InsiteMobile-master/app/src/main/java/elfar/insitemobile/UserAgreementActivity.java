package elfar.insitemobile;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class UserAgreementActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement_acticity);
        Toolbar toolbar = findViewById(R.id.MainActivityToolbar);
        WebView myWebView = findViewById(R.id.webview);
        Button btnAgree = findViewById(R.id.btnAgree);

        setSupportActionBar(toolbar);
        myWebView.loadUrl("file:///android_asset/termsfeed-html-terms-service-english.html");

        btnAgree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSharedPreferences("UserAgreement", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
                requestStoragePermission();

            }
        });
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(UserAgreementActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        startActivity(new Intent(UserAgreementActivity.this, MainActivity.class));
    }
}
