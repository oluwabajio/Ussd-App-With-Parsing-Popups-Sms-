package tingtel.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;

public class RequestPermissionActivity extends AppCompatActivity {

    FancyButton btnRequestPermission;

    int REQUEST_PHONE_STATE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permission);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      initViews();

    }

    private void initViews() {

        btnRequestPermission = (FancyButton) findViewById(R.id.btn_requestPermission);

        btnRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }

    private void requestPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //ask permission
            ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, RECEIVE_SMS, CALL_PHONE}, REQUEST_PHONE_STATE);

        } else {

            Intent intent = new Intent(RequestPermissionActivity.this, MainActivity.class);
            startActivity(intent);

        }

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PHONE_STATE) {

            Intent intent = new Intent(RequestPermissionActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

}
