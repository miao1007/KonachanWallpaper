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

package com.github.miao1007.konakanwallpaper.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.github.miao1007.konakanwallpaper.adapter.MyAdaper;
import com.github.miao1007.konakanwallpaper.model.Image;
import com.github.miao1007.konakanwallpaper.utils.DataUtils;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    //adapter
    MyAdaper adaper;
    StaggeredGridView gridView;
    List<Image> images = new ArrayList<Image>();
    SwipeRefreshLayout swipeRefreshLayout;

    //data
    int currentPage = 1;
    String currentTAGS = "";

    //handler
    Handler handler;
    final static int LISTVIEW_REFRESH = 1;
    final static int LISTVIEW_LOAD = 2;
    final static int LISVIEW_DOWNLOAD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initialView();
        initData();
        //handleSearch(getIntent());
    }

    //Initial View And Set Handler
    private void initialView() {
        adaper = new MyAdaper(MyActivity.this ,handler , images);
        //PullToReresh : in android.support.v4
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_my_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN);
        //Combined StaggeredGridView And Swing Animation
        gridView = (StaggeredGridView) findViewById(R.id.gridView);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adaper);
        swingBottomInAnimationAdapter.setAbsListView(gridView);
        swingBottomInAnimationAdapter.getViewAnimator().setAnimationDelayMillis(350);
        gridView.setAdapter(swingBottomInAnimationAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int currentPosition = i;
                final int firstVisible = gridView.getFirstVisiblePosition();
                Log.w(getClass().getSimpleName(), String.valueOf(firstVisible));
                Log.w(getClass().getSimpleName(), String.valueOf(currentPosition));
                Log.w("Onclick", String.valueOf(currentPosition - firstVisible));
                final Image image = images.get(currentPosition);
                //DialogWithChoice.createDialog(MyActivity.this, image);
                Log.w("Dialog",
                        String.valueOf(image.getFile_size()) + "/" +
                                String.valueOf(image.getSample_file_size()) + "/" +
                                String.valueOf(image.getJpeg_file_size()));
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            final String TAG = "RefreshGrid";
            private boolean isLastRow = false;
            private boolean isload = false;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (isLastRow == true) {
                    currentPage++;
                    loadDataInThread(LISTVIEW_LOAD, currentTAGS, String.valueOf(currentPage));
                }
                isLastRow = false;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                //i : recycled items , i2 : iems in visible , i3 : all items
                //Log.w("onScroll",i + "/" + i2 + "/" + i3);
                if ((i3 - i2 - i) <= 20) {
                    //Log.w(TAG, "isLastRow Set True");
                    isLastRow = true;
                }
            }
        });
        //当你创建一个新的Handler时候，默认情况下，它将关联到创建它的这个线程和该线程的消息队列
        //handler应该由处理消息的线程创建，当前的消息就是处理UI
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                List<Image> tmpimages = (List<Image>) message.obj;
                if (tmpimages.isEmpty()) {
                    Toast.makeText(MyActivity.this, getString(R.string.no_found), Toast.LENGTH_SHORT).show();
                } else {
                    switch (message.what) {
                        //On Refresh More
                        case LISTVIEW_REFRESH:
                            Log.w("Handle_UP", "imgs to add = " + Integer.toString(tmpimages.size()));
                            images.clear();
                            images.addAll(tmpimages);
                            adaper.notifyDataSetChanged();
                            break;
                        //On Load More
                        case LISTVIEW_LOAD:
                            Log.w("Handle_DN", "imgs to add = " + Integer.toString(tmpimages.size()));
                            images.addAll(tmpimages);
                            adaper.notifyDataSetChanged();
                            break;
                        case LISVIEW_DOWNLOAD:

                            adaper.notifyDataSetChanged();
                    break;
                }
                }
                Log.w("Handle_FI", "all imgs now = " + Integer.toString(images.size()));
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    public void loadDataInThread(final int LISTVIEW_STATE, final String tags, final String page) {
        //Toast.makeText(MyActivity.this, getString(R.string.toast_wait), Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(true);
        new Thread() {
            @Override
            public void run() {
                //这个消息对象会关联调用它的Handler对象
                handler.sendMessage(handler.obtainMessage(LISTVIEW_STATE, DataUtils.getDate(tags, page)));
            }
        }.start();
    }

    //Initial Data For First Use
    public void initData() {
        loadDataInThread(LISTVIEW_REFRESH, currentTAGS, "1");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        getMenuInflater().inflate(R.menu.my, menu);
        //真正的数据操作
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.w("Search", "onQueryTextSubmit : " + s);
                currentTAGS = s;
                currentPage = 1;
                loadDataInThread(LISTVIEW_REFRESH, currentTAGS, String.valueOf(currentPage));
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() > 0) {
                    Log.w("Search", "onQueryTextChange : " + s);
                    //TODO: void showSearchTips(s)

                }
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:
                return true;
            case R.id.menu_about: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        loadDataInThread(LISTVIEW_REFRESH,currentTAGS,"1");
    }
}

