package vn.edu.tnut.mysqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DataDAO dataDAO;
    final String hotens[] = {"Hùng", "Dũng", "Tuấn", "Tú"};

    void init_db() {
        //nếu lần đầu chạy app: thì bổ xung vài row mặc định
        if (dataDAO.getDataCount() == 0) {
            for (String hoten : hotens)
                dataDAO.insertData(hoten);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataDAO = new DataDAO(this);
        dataDAO.open();

        //nếu lần đầu chạy app: thì bổ sung vài row mặc định
        init_db();

        //list all ngay
        listAllrow(null);

        Boolean ok = SmsUtils.checkSmsPermission(this);
        showToast(this,ok?"check quyền sms: ok":"chưa có quyền với sms");
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() { //khi app bị die
        super.onDestroy();       //gọi để huỷ lớp bố
        dataDAO.close();         //giải phóng con trỏ ghi file sqlite
    }

    public void insert1row(View view) {
        //lấy họ tên nhập trên form
        EditText txtHoTen = findViewById(R.id.txtHoTen);
        String hoten = txtHoTen.getText().toString();

        //gọi dataDAO để insert
        dataDAO.insertData(hoten);

        //view kq luôn
        listAllrow(view);
    }

    public void listAllrow(View view) {
        //gọi dataDAO để lấy mọi row -> string -> GUI
        String strAllRow = dataDAO.getAllDataAsString();

        //lấy đối tượng trên giao diện chuẩn bị hiển thị
        TextView txtKQ = findViewById(R.id.txtKQ);

        //gán text cho nó để hiển thị
        txtKQ.setText(strAllRow);
    }
}