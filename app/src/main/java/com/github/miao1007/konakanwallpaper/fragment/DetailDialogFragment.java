package com.github.miao1007.konakanwallpaper.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.miao1007.konakanwallpaper.activity.R;
import com.github.miao1007.konakanwallpaper.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 14/9/19.
 */
public class DetailDialogFragment extends DialogFragment {

    Image image;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static public DetailDialogFragment newInstance(Image image) {
        DetailDialogFragment f = new DetailDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("image",image);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = (Image)getArguments().getSerializable("image");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_dialog, container ,false);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView_preview);
        ListView listView = (ListView)v.findViewById(R.id.listView_reso);
        Button button_yes = (Button)v.findViewById(R.id.btn_yes);
        Button button_no = (Button)v.findViewById(R.id.btn_no);
        Picasso.with(getActivity()).load(image.getPreview_url()).into(imageView);
        List<String> urls = new ArrayList<String>();
        urls.add(image.getStringResolution(image.getSample_width(),image.getSample_height()));
        urls.add(image.getStringResolution(image.getJpeg_width(),image.getJpeg_height()));
        urls.add(image.getStringResolution(image.getWidth(),image.getHeight()) + "(推荐)");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,urls);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO.....
            }
        });
        return v;
    }
}
