/*
 * Copyright (c) 2014 Miao1007
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.miao1007.konakanwallpaper.receiver;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by leon on 14-9-5.
 */
public class DownloadReceiver extends BroadcastReceiver {
    final static String MINETYPE = "image/jpeg";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        long downId = bundle.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        Log.w(getClass().getSimpleName(),String.valueOf(downId));
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri filepath = downloadManager.getUriForDownloadedFile(downId);
        Log.w(getClass().getSimpleName(),filepath.toString());
        if (intent.getAction() == DownloadManager.ACTION_DOWNLOAD_COMPLETE){
            //start the activity in new task opening the picture
            Intent activityIntent = new Intent(Intent.ACTION_VIEW);
            activityIntent.setDataAndType(filepath, MINETYPE);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(activityIntent);
            } catch (ActivityNotFoundException ex) {
                Log.d(getClass().getName(), "no activity for " + MINETYPE, ex);
            }
        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
            try {
                Intent dm = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                dm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dm);
            } catch (ActivityNotFoundException ex){
                Log.d(getClass().getName(), "no activity for " + MINETYPE, ex);
            }
        }

    }

}
