package com.example.assignment.viewmodel;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.assignment.database.History;
import com.example.assignment.model.VideoDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class DataItemViewModel extends BaseObservable {

    private VideoDetails dataModel;
    Realm realm;

    public DataItemViewModel(VideoDetails dataModel) {
        this.dataModel = dataModel;
    }

    @Bindable
    public String getTitle() {
        return dataModel.getName();
    }
    @Bindable
    public String getPath() {
        return dataModel.getVideo_thumb();
    }

    public void saveBookmark(String path) {
        realm = Realm.getDefaultInstance();
        if (realm != null) {
            realm.beginTransaction();
        }
        History mHistory = realm.createObject(History.class);
        mHistory.setPathId(path);
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        mHistory.setStartTime(currentTime);
        realm.commitTransaction();
        dataModel.setName("Bookmark Saved");
        dataModel.setSelected(true);
        notifyChange();
    }
}
