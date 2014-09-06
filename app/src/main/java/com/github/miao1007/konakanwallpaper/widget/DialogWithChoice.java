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

package com.github.miao1007.konakanwallpaper.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.github.miao1007.konakanwallpaper.model.Image;
import com.github.miao1007.konakanwallpaper.utils.DownloadTask;

/**
 * Created by leon on 14-9-4.
 */
public class DialogWithChoice  {

    static int currentChoice = 0;


    static public void createDialog(final Context context, final Image image){

        CharSequence[] imageResolution = {
                image.getStringResolution(image.getWidth(),image.getHeight()) + "(推荐)",
                image.getStringResolution(image.getJpeg_width(),image.getJpeg_height())+ "(中等)",
                image.getStringResolution(image.getSample_width(),image.getSample_height())+ "(最低)",
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("选择分辨率")
                .setSingleChoiceItems(imageResolution,0,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.w("Dialog", "click/" + String.valueOf(i));
                        currentChoice = i;
                    }
                }).setNegativeButton("下载到SD卡啦", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //i = -2
                        //You must create a instance before use it in static class
                        DownloadTask downloadTask = new DownloadTask(context);
                        downloadTask.execute(image);

                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    // i = -1
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.w("Dialog", "NButton/" + String.valueOf(i));
                    }
                });


        builder.create().show();
    }

}
