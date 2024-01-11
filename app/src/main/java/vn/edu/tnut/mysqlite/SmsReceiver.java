package vn.edu.tnut.mysqlite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "56kmt_SmsReceiver";
    private static final String API_URL = "https://tms.tnut.edu.vn/56kmt/sms.php";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d(TAG, "onReceive: có tin đến");
        //showToast(context,"onReceive được gọi");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                //showToast(context,"onReceive tồn tại Object[] pdus ");
                for (Object pdu : pdus) {
                    try {
                        //showToast(context, "có 1 sms ...");
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                        String messageBody = smsMessage.getMessageBody();
                        //showToast(context, "messageBody=" + messageBody);
                        String senderPhoneNumber = smsMessage.getOriginatingAddress();
                        //showToast(context, "senderNumber=" + senderPhoneNumber);
                        // Lấy thời gian gửi
                        long timestampMillis = smsMessage.getTimestampMillis();
                        Date date = new Date(timestampMillis);
                        String timeSent = date.toString();
                        showToast(context,"timeSent="+timeSent);
                        // Lấy thông tin nhà mạng (MCC + MNC)
                        String serviceCenterAddress = smsMessage.getServiceCenterAddress();
                        //showToast(context,"serviceCenterAddress="+serviceCenterAddress);
                        //showToast(context, "SMS from: " + senderPhoneNumber + "\nMessage: " + messageBody + "\nserviceCenterAddress: " + serviceCenterAddress);
                        // Gọi phương thức để xử lý nội dung SMS và gửi tới API
                        processSmsMessage( messageBody, senderPhoneNumber, serviceCenterAddress);
                    }catch (Exception ex){
                        showToast(context,"Exception: "+ex.toString());
                    }
                }
            }
        }
    }


    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
//    private void processSmsMessageXXX(Context context, String messageBody, String senderPhoneNumber, String serviceCenterAddress, String smsTime) {
//        // Thực hiện các xử lý cần thiết và gửi thông tin tới API qua URL.
//
//            OkHttpClient client = new OkHttpClient();
//            Log.d(TAG, "processSmsMessage: tạo xong OkHttpClient client ");
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("body", messageBody)
//                    .add("number", senderPhoneNumber)
//                    .add("center", serviceCenterAddress)
//                    .add("time", smsTime)
//                    .build();
//            Log.d(TAG, "processSmsMessage: tạo xong RequestBody requestBody ");
//            Request request = new Request.Builder()
//                    .url(API_URL)
//                    .post(requestBody)
//                    .build();
//            Log.d(TAG, "processSmsMessage: tạo xong Request request ");
//
//        // Sử dụng enqueue() để thực hiện yêu cầu mạng bất đồng bộ
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // Xử lý lỗi khi thực hiện yêu cầu
//                e.printStackTrace();
//                Log.e(TAG, "Error during API request");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                // Kiểm tra mã trạng thái HTTP (200 là thành công)
//                if (response.isSuccessful()) {
//                    // Xử lý phản hồi thành công (nếu cần)
//                    Log.d(TAG, "API request successful");
//                } else {
//                    // Xử lý phản hồi không thành công (nếu cần)
//                    Log.e(TAG, "API request failed. Code: " + response.code() + ", Message: " + response.message());
//                }
//            }
//        });
//    }

//    private void processSmsMessage(Context context, String messageBody, String senderPhoneNumber, String serviceCenterAddress, String smsTime) {
//        HttpURLConnection urlConnection = null;
//        try {
//            // Tạo URL với endpoint của API
//            URL url = new URL(API_URL);
//
//            // Mở kết nối HTTP
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            urlConnection.setDoOutput(true);
//
//            // Tạo dữ liệu để gửi
//            String postData = "messageBody=" + messageBody +
//                    "&senderPhoneNumber=" + senderPhoneNumber +
//                    "&serviceCenterAddress=" + serviceCenterAddress;
//
//            // Gửi dữ liệu
//            OutputStream outputStream = urlConnection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//            writer.write(postData);
//            writer.flush();
//            writer.close();
//            outputStream.close();
//
//            // Lấy mã phản hồi từ API
//            int responseCode = urlConnection.getResponseCode();
//
//            // Kiểm tra mã phản hồi (200 là thành công)
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // Xử lý phản hồi thành công (nếu cần)
//                Log.d(TAG, "API request successful OK ROI OK ROI");
//            } else {
//                // Xử lý phản hồi không thành công (nếu cần)
//                Log.e(TAG, "API request failed. Code: " + responseCode);
//            }
//        } catch (Exception e) {
//            // Xử lý lỗi khi thực hiện yêu cầu
//            e.printStackTrace();
//            Log.e(TAG, "Error during API request: "+e.toString());
//        } finally {
//            // Đảm bảo đóng kết nối khi kết thúc
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
//    }

    private void processSmsMessage(String messageBody, String senderPhoneNumber, String serviceCenterAddress) {
        new ApiRequestTask().execute(messageBody, senderPhoneNumber, serviceCenterAddress);
    }

    private static class ApiRequestTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String messageBody = params[0];
            String senderPhoneNumber = params[1];
            String serviceCenterAddress = params[2];

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("messageBody", messageBody)
                    .add("senderPhoneNumber", senderPhoneNumber)
                    .add("serviceCenterAddress", serviceCenterAddress)
                    .build();

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                // Kiểm tra mã trạng thái HTTP (200 là thành công)
                if (response.isSuccessful()) {
                    // Xử lý phản hồi thành công (nếu cần)
                    Log.d(TAG, "API request successful");
                } else {
                    // Xử lý phản hồi không thành công (nếu cần)
                    Log.e(TAG, "API request failed. Code: " + response.code() + ", Message: " + response.message());
                }
            } catch (IOException e) {
                // Xử lý lỗi IO khi thực hiện yêu cầu
                e.printStackTrace();
                Log.e(TAG, "IO Exception during API request");
            }

            return null;
        }
    }
}
