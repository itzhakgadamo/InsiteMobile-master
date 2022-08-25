package elfar.insitemobile.Settings;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import elfar.insitemobile.R;

public class CheckUpdates extends Fragment {

    private boolean packageReadyToInstall = false;
    private Activity mActivity;
    private int STORAGE_PERMISSION_CODE = 1;
    public CheckUpdates() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmet.
        View v = inflater.inflate(R.layout.fragment_checkupdates, container, false);
        final Button btnCheckUpdates = v.findViewById(R.id.btnCheckUpdates);
        final ProgressBar pbPercentage = v.findViewById(R.id.pbDownloadPercent);
        final ProgressBar pbCircle = v.findViewById(R.id.pbDownload);
        final TextView tvPercentage = v.findViewById((R.id.tvPercentage));

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }

        btnCheckUpdates.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!packageReadyToInstall) {

                    //Download Update
                    String urlDownload = "http://84.94.103.254:901/uploadapk/downloadupdate";
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDownload));
                    request.setDescription("Insite Update");
                    request.setTitle("InsiteMobileUpdate");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "InsiteMobile.apk");
                    String root = Environment.getExternalStorageDirectory().toString();
                    File apk = new File(root + "/download/" + "InsiteMobile.apk");
                    apk.delete();


                    try {
                        final DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        final long downloadId = manager.enqueue(request);

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {

                                    boolean downloading = true;

                                    while (downloading) {
                                        DownloadManager.Query q = new DownloadManager.Query();
                                        q.setFilterById(downloadId);

                                        final Cursor cursor = manager.query(q);
                                        cursor.moveToFirst();
                                        int bytes_downloaded = cursor.getInt(cursor
                                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                            downloading = false;
                                            packageReadyToInstall = true;
                                            mActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    btnCheckUpdates.setText("Install Update");
                                                }
                                            });
                                        }

                                        final int dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);

                                        mActivity.runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                if (dl_progress == 0) {
                                                    pbCircle.setVisibility(View.VISIBLE);
                                                    tvPercentage.setText("");
                                                } else {
                                                    pbPercentage.setVisibility(View.VISIBLE);
                                                    pbPercentage.setProgress(dl_progress);
                                                    tvPercentage.setText(dl_progress + "%");
                                                    pbCircle.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });

                                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_PAUSED) {
                                            downloading = false;
                                            mActivity.runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    pbCircle.setVisibility(View.INVISIBLE);
                                                    tvPercentage.setText("Download Failed");
                                                }
                                            });
                                        }
                                        String status = statusMessage(cursor);
                                        cursor.close();
                                    }

                                } catch (Exception e) {
                                    //AppManager.appendLogz(e.toString());
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        requestStoragePermission();
                    }
                }
                //After Update Downloaded
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "InsiteMobile.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            }
        });
        return v;
    }

    private String statusMessage(Cursor c) {
        String msg = "???";

        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;

            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return (msg);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(mActivity)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to download the update file to the device")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

}
