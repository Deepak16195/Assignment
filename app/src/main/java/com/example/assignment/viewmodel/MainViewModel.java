package com.example.assignment.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.assignment.BR;
import com.example.assignment.adapter.MyVideoAdapter;
import com.example.assignment.database.History;
import com.example.assignment.model.VideoDetails;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends BaseObservable {

    private MyVideoAdapter adapter;
    private List<VideoDetails> data;
    Context mContext;
    Realm realm;

    public MainViewModel(Context context) {
        data = new ArrayList<>();
        adapter = new MyVideoAdapter(context);
        mContext = context;
        realm = Realm.getDefaultInstance();
    }


    public void getVideoList(Cursor cursor) {
        int column_index_data, column_id, thum;
        String absolutePathOfImage = null;
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        RealmResults<History> bookMarks = realm.where(History.class).findAll();
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("column_id", cursor.getString(column_id));
            Log.e("thum", cursor.getString(thum));

            VideoDetails obj_model = new VideoDetails();
            obj_model.setName("Mark As Bookmark");
            obj_model.setSelected(false);
            if (bookMarks.size() > 0) {
                for (History mHistory : bookMarks) {
                    if (mHistory.getPathId().equalsIgnoreCase(cursor.getString(column_id))) {
                        obj_model.setName("Bookmark Saved");
                        obj_model.setSelected(true);
                    }
                }
            }
            obj_model.setVideo_path(absolutePathOfImage);
            obj_model.setVideo_thumb(column_id + ""); //cursor.getString(thum)
            data.add(obj_model);
        }
        notifyPropertyChanged(BR.data);
    }

    @Bindable
    public List<VideoDetails> getData() {
        return this.data;
    }

    @Bindable
    public MyVideoAdapter getAdapter() {
        return this.adapter;
    }

}
