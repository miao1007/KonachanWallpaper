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

package com.github.miao1007.konakanwallpaper.obverser;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by leon on 14-9-6.
 */
public class DownloadObserver extends ContentObserver
{
    private long mAppDownloadId = 0;
    private Handler handler;
    Uri downloadsContentUri = Uri.parse("content://konakan/" + mAppDownloadId);

    public DownloadObserver(Handler handler)
    {
        super(handler);
        this.handler = handler;
    }

    public DownloadObserver(long appDownloadId, Handler handler)
    {
        super(handler);
        this.handler = handler;
        mAppDownloadId = appDownloadId;
    }


    @Override
    public void onChange(boolean selfChange)
    {
        super.onChange(selfChange);
        // Do some work on this row with the id
        Log.w("Download","onchange");

    }
}