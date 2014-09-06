package com.github.miao1007.konakanwallpaper.utils;

import android.util.Log;

import com.github.miao1007.konakanwallpaper.model.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by leon on 14-9-1.
 */
public class DataUtils {

    final static String Endpint = "http://konachan.net";
    final static String GetCMD = "/post.json";
    final static String LIMIT_PAGE = "35";

    //Retrofit : A Fast And Easy Http API Providing Directly  "HttpGet Query"
    interface KonakanService {
        @GET(GetCMD)
        List<Image> searchByTagsAndPage(@Query("tags") String tags,@Query("page") String page,@Query("limit")String limit);
    }

    public static List<Image> getDate(String tags,String page) {
        List<Image> imagesToAdd = new ArrayList<Image>();
        try { //For Advanced Use, Please Refer To The Developer's Website : square.github.io/retrofit/
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Endpint)
                    .build();
            KonakanService service = restAdapter.create(KonakanService.class);
            imagesToAdd = service.searchByTagsAndPage(tags, page ,LIMIT_PAGE);
            if (imagesToAdd.isEmpty()){
                throw new NullPointerException();
            }

            //Safe Check :Move Image links which rating is not "s"
            //s : Safe , q : Questionable , e : Explicit
            Iterator<Image> iterator = imagesToAdd.iterator();
            while (iterator.hasNext()){

                Image image = iterator.next();
                if (!image.getRating().contains("s")){
                    iterator.remove();
                }
            }

            Log.w("DataUtils", "safe:" + imagesToAdd.size() + "/" + tags + "/" + page);
            return imagesToAdd;
        }catch (NullPointerException e){
            Log.e("DataUtils", "imagesToAdd is empty");
            return imagesToAdd;
        }
        catch (RetrofitError error){
            Log.e("DataUtils", "NETWORK CONNECT TIMEOUT!");
            return imagesToAdd;
        }
    }




}