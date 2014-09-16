package com.github.miao1007.konakanwallpaper.obverser;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by leon on 14-9-8.
 */
public class DownloadObserver extends ContentObserver {
    private long downid;
    //private Image image;
    //private StaggeredGridView gridView;
    private Handler handler;
    private Context context;
    private ProgressBar progressBar;
    //private int i;
    //private final int LISTVIEW_DOWNLOAD = 3;

//    public DownloadObserver(Handler handler, Context context,long downid,StaggeredGridView gridView,int i) {
//        super(handler);
//        this.handler = handler;
//        //this.image = image;
//        this.downid = downid;
//        this.context = context;
//        this.gridView = gridView;
//        this.i = i;
//    }


    public DownloadObserver(Handler handler, Context context, long downid , ProgressBar progressBar) {
        super(handler);
        this.downid = downid;
        this.handler = handler;
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    public void onChange(boolean selfChange) {
        //每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        //Log.w("onChangeID", String.valueOf(downid));
        super.onChange(selfChange);
        //实例化查询类，这里需要一个刚刚的downid
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downid);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //这个就是数据库查询啦
        Cursor cursor = downloadManager.query(query);
        while (cursor.moveToNext()) {
            int mDownload_so_far = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int mDownload_all = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            final int mProgress = (mDownload_so_far * 100) / mDownload_all;
            Log.w(getClass().getSimpleName(), String.valueOf(mProgress));
            //TODO：handler.sendMessage(xxxx),这样就可以更新UI了
            //image.setDownLoadProgress(mProgress);
            //REFERENCE:http://www.cnblogs.com/Couch-potato/archive/2012/12/11/2813460.html
            handler.sendEmptyMessage(0x123);
        }
        cursor.close();
    }
}