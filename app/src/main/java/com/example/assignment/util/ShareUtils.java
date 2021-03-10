package com.example.assignment.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import androidx.core.content.FileProvider;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;
import java.io.InputStream;

public class ShareUtils {

    public static Uri getUri(Context context, String path) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getVideoContentUri(context, new File(path));
        } else {
            fileUri = Uri.fromFile(new File(path));
        }
        return fileUri;
    }


    public static boolean fileExists(final Context context, final Uri uri) {
        if ("file".equals(uri.getScheme())) {
            final File file = new File(uri.getPath());
            return file.exists();
        } else {
            try {
                final InputStream inputStream = context.getContentResolver().openInputStream(uri);
                inputStream.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        try {
            if (uri.getScheme().equals("content")) {
                try (Cursor cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        final int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (columnIndex > -1)
                            result = cursor.getString(columnIndex);
                    }
                }
            }
            if (result == null) {
                result = uri.getPath();
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
            if (result.indexOf(".") > 0)
                result = result.substring(0, result.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Uri getVideoContentUri(Context context, File file) {
        String filePath = file.getAbsolutePath();
        Cursor cursor =
                context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA +
                                "=?",
                        new String[]{filePath}, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Uri baseUri = Uri.parse("content://media/external/video/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }
            cursor.close();
        }
        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , values);
        }

        if (uri == null) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        }
        return uri;
    }

}
