package com.example.hongb_000.dictionaryows.PIII.PUpdate;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hongb_000.dictionaryows.PIII.DataController.GetDataFirebase;
import com.example.hongb_000.dictionaryows.PIII.DataController.UpdateData;
import com.example.hongb_000.dictionaryows.PIII.DataController.UpdateDataController;
import com.example.hongb_000.dictionaryows.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by phamn on 8/21/2015.
 */
public class UpdateActivity extends AppCompatActivity {
    ArrayList<UpdateData> list;
    ArrayList<UpdateData> listNew;
    ListView listViewUpdateData;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private Button startBtn;
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piii_updatequetions);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Thêm câu hỏi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final TextView tile = (TextView) findViewById(R.id.textView25);

        if (isInternetAvailable()) {

            final ProgressDialog ringProgressDialog =
                    ProgressDialog.show(this, "Xin đợi...", "Đang kiểm tra dữ liệu", true);
            ringProgressDialog.setCancelable(false);
            UpdateDataController updateDataController = new UpdateDataController(this);
            listNew = new ArrayList<UpdateData>();
            list = updateDataController.getUpdateDataSqlite();
            listViewUpdateData = (ListView) findViewById(R.id.listView2);

            final UpdateArrayAdapter arrayAdapter = new UpdateArrayAdapter(
                    this, R.layout.piii_update_customlistview, listNew,
                    new DownloadAction() {
                        @Override
                        public void successDownload(int id, Button button) {
                            System.out.println(id);
                            // button.setText("Đã tải");
                            // //button.setTextColor(Color.GREEN);
                            //button.setEnabled(false);

                        }

                        @Override
                        public void errorDownload(Button button) {
                            //   button.setText("Lỗi");
                            //  button.setTextColor(Color.RED);
                        }
                    }
            );

            listViewUpdateData.setAdapter(arrayAdapter);

            updateDataController.getUpdateData(new GetDataFirebase() {
                @Override
                public void setOnGetData(UpdateData data) {
                    ringProgressDialog.dismiss();
                    tile.setText("Chưa có câu hỏi mới");
                    Boolean stateValue = true;
                    for (int i = 0; i < list.size(); i++) {
                        UpdateData dataSql = list.get(i);
                        if (dataSql.getLink().equals(data.getLink())) {
                            stateValue = false;
                            break;
                        }
                    }
                    if (stateValue) {
                       // listNew.add(data);
                        //   arrayAdapter.notifyDataSetChanged();
                    }
                }
            });


        } else
            Toast.makeText(this, "Không có kết nối mạng. Xin kiểm tra lại", Toast.LENGTH_LONG).show();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startDownload(String url, DownloadAction downloadAction, int id, Button button) {
        new DownloadFileAsync(downloadAction, id, button).execute(url);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Tải dữ liệu...");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        DownloadAction downloadAction;
        int id;
        Button button;

        public DownloadFileAsync(DownloadAction downloadAction, int id, Button button) {
            this.downloadAction = downloadAction;
            this.id = id;
            this.button = button;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {

                String link = aurl[0];
                String fileName = link.split("/")[link.split("/").length - 1];
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());

                File folder = new File(Environment.getExternalStorageDirectory() + "/Odic");
                System.out.println(Environment.getExternalStorageDirectory() + "/Odic");
                if (!folder.exists()) {
                    folder.mkdir();
                }

                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Odic/" + fileName);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                downloadAction.errorDownload(button);
            }

            downloadAction.successDownload(id, button);
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    private class UpdateArrayAdapter extends ArrayAdapter {
        Activity context;
        int idLayout;
        ArrayList<UpdateData> list;
        DownloadAction downloadAction;

        public UpdateArrayAdapter(Activity context, int idLayout, ArrayList<UpdateData> list, DownloadAction downloadAction) {
            super(context, idLayout, list);
            this.list = list;
            this.context = context;
            this.idLayout = idLayout;
            this.downloadAction = downloadAction;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(idLayout, null);

            final UpdateData updateData = list.get(position);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView note = (TextView) convertView.findViewById(R.id.note);
            TextView quetions = (TextView) convertView.findViewById(R.id.quetions);
            final Button download = (Button) convertView.findViewById(R.id.tai);
            name.setText(updateData.getName());
            note.setText(updateData.getNote());
            quetions.setText(quetions.getText() + updateData.getNumberOfQuetions());
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDownload(updateData.getLink(), downloadAction, position, download);
                }
            });

            return convertView;
        }
    }

    private interface DownloadAction {
        public void successDownload(int id, Button button);

        public void errorDownload(Button button);

    }
}
