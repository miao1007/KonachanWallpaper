package com.github.miao1007.konakanwallpaper.fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.github.miao1007.konakanwallpaper.activity.R;
import com.github.miao1007.konakanwallpaper.adapter.MyAdaper;
import com.github.miao1007.konakanwallpaper.model.Image;
import com.github.miao1007.konakanwallpaper.utils.DataUtils;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 14/9/17.
 */
public class PicListviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;


    List<Image> images = new ArrayList<Image>();

    MyAdaper adaper;
    StaggeredGridView staggeredGridView;

    //data
    int currentPage = 1;
    String currentTAGS = "";

    //handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            List<Image> tmpimages = (List<Image>) message.obj;
            if (tmpimages.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.no_found), Toast.LENGTH_SHORT).show();
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
    final static int LISTVIEW_REFRESH = 1;
    final static int LISTVIEW_LOAD = 2;
    final static int LISVIEW_DOWNLOAD = 3;

    public PicListviewFragment() {
        Log.w(getClass().getSimpleName(),"onConstruct");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(getClass().getSimpleName(),"onCreateView");
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_piclist, container ,false);
        adaper = new MyAdaper(getActivity() , handler, images);
        //staggeredGridView.setAdapter(adaper);
        staggeredGridView = (StaggeredGridView)view.findViewById(R.id.gridView);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adaper);
        swingBottomInAnimationAdapter.setAbsListView(staggeredGridView);
        swingBottomInAnimationAdapter.getViewAnimator().setAnimationDelayMillis(350);
        staggeredGridView.setAdapter(swingBottomInAnimationAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.activity_my_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN);
        staggeredGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w(getClass().getSimpleName(),"onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w(getClass().getSimpleName(),"onActivityCreated");
        initData();
    }

    public void initData() {
        loadDataInThread(LISTVIEW_REFRESH, currentTAGS, "1");
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



    @Override
    public void onRefresh() {
        loadDataInThread(LISTVIEW_REFRESH,currentTAGS,"1");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.fragment_picpreview_menu, menu);
        //真正的数据操作
        //restoreActionBar();
        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
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
                    currentTAGS = s;
                    currentPage = 1;
                    loadDataInThread(LISTVIEW_REFRESH, currentTAGS, String.valueOf(currentPage));

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return true;
    }
}
