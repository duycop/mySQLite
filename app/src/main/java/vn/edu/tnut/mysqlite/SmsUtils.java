package vn.edu.tnut.mysqlite;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsUtils {
    private static final int MY_PERMISSIONS_REQUEST_SMS = 123;

    public static boolean checkSmsPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                // Quyền chưa được cấp, yêu cầu từ người dùng
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_SMS);

                return false;
            } else {
                // Quyền đã được cấp
                return true;
            }
        } else {
            // Đối với các thiết bị có phiên bản Android dưới 23, quyền được coi là đã được cấp khi cài đặt ứng dụng
            return true;
        }
    }
}
