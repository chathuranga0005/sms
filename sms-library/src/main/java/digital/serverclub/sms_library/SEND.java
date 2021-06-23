package digital.serverclub.sms_library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.LENGTH_LONG;

public  class SEND extends AsyncTask<String ,Void,String> {
    Context c;
    String msg="";
    public SEND(Context ctx){c=ctx;}


    @Override
    protected String doInBackground(String... params) {

        String updateTeacher="https://slh.nebuloapps.com/SMS/serverclub-sms-api-master/send_sms.php";

        try {

            URL url = new URL(updateTeacher);

            String api_key = params[0];
            String mask = params[1];
            String mobile = params[2];
            String sms = params[3];

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String postData = URLEncoder.encode("api_key", "UTF-8") + "=" + URLEncoder.encode(api_key, "UTF-8") + "&"
                    + URLEncoder.encode("mask", "UTF-8") + "=" + URLEncoder.encode(mask, "UTF-8") + "&"
                    + URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&"
                    + URLEncoder.encode("sms", "UTF-8") + "=" + URLEncoder.encode(sms, "UTF-8");

            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }


    @Override
    protected void onPostExecute(String result) {


        ProgressDialog dialog;
        dialog = new ProgressDialog(c);
        dialog.setMessage("Sending...");
        dialog.show();

        new CountDownTimer(1000, 100) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                dialog.dismiss();
                String message = null;
                String user = null;
                try {
                    //token
                    JSONObject jo = new JSONObject(result);
                    message = jo.getString("message");
                    user = jo.getString("user");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                    alertDialogBuilder.setTitle("Message Status");
                    alertDialogBuilder.setMessage(user+"\n"+message);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.start();





    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
