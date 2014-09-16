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

package com.github.miao1007.konakanwallpaper.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.github.miao1007.konakanwallpaper.model.Image;

/**
 * Created by leon on 14-9-4.
 */
public class DownloadTask extends AsyncTask<Image, Integer, Boolean> {
    private Context context;

    public DownloadTask(Context context) {
        this.context = context;
    }


    @Override
    protected Boolean doInBackground(Image... image) {
//        final OkHttpClient client = new OkHttpClient();
//        final String image_url = image[0].getFile_url();
//        final int bufferSize = 102400;
//        try {
//
//            final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), image[0].getTags() + ".jpg");
//
//
//            final Request request = new Request.Builder()
//                    .url(image_url)
//                    .build();
//            int progress = 0;
//            for (int i = 0; i < 10; i++) {
//                progress = i;
//                publishProgress(i);
//                Thread.sleep(100);
//            }
//
//            client.newCall(request).enqueue(new Callback() {
//
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override public void onResponse(Response response) throws IOException {
//
//                    Log.w("okhttp","Start Download");
//                    InputStream inputStream = response.body().byteStream();
//                    FileOutputStream outputStream = new FileOutputStream(file);
//
//
//                    //缓冲文件输入流
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                    //缓冲文件输出流
//                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
//
//                    int i = 0;
//                    //缓冲区的大小
//                    byte[] buffer = new byte[bufferSize];
//
//                    while(true) {
//                        if(bufferedInputStream.available() < bufferSize) {
//                            Log.w("okhttp","pprogressI" + inputStream.available());
//                            while((i = bufferedInputStream.read()) != -1) {
//                                bufferedOutputStream.write(i);
//                            }
//                            break;
//                        } else {
//                            Log.w("okhttp","pprogressInput/" + inputStream.available());
//                            Log.w("okhttp", "pprogressBuff/" + bufferedInputStream.available());
//                            bufferedInputStream.read(buffer);
//                            bufferedOutputStream.write(buffer);
//                        }
//                    }
//                    bufferedOutputStream.flush();
//                    bufferedInputStream.close();
//                    bufferedOutputStream.close();
//                    Log.w("okhttp","DownloadDone");
//                }
//            });
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(image[0].getFile_url()))
                    .setDestinationInExternalPublicDir("Konachan", image[0].getTags() + ".jpeg")
                    .setTitle(image[0].getTags())
                    .setDescription("下载到SD卡啦")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                    .setMimeType("image/jpeg");
            long downid = downloadManager.enqueue(request);
            //register Content Observer
//            context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, new DownloadObserver(handler, context, downid, holder.progressBar));

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        //ViewStub也是个view，自己本身能够被创建，但是他包含的布局文件是不会被加载的，即不会被inflate，所以也就不会消耗太多内存

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);

    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
