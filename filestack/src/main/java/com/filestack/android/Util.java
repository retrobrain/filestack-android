package com.filestack.android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;
import android.widget.TextView;

import com.filestack.Client;
import com.filestack.Config;
import com.filestack.Sources;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

class Util {
    private static final Map<Integer, SourceInfo> SOURCES = new ArrayMap<>();

    private static Client client;
    private static Selection.SimpleSaver selectionSaver;

    static {
        SOURCES.put(R.id.nav_camera_picture, new SourceInfo(
                Sources.CAMERA,
                R.drawable.ic_source_camera_photo,
                R.string.source_camera_photo,
                R.color.theme_source_camera));

        SOURCES.put(R.id.nav_camera_movie, new SourceInfo(
                Sources.CAMERA,
                R.drawable.ic_source_camera_video,
                R.string.source_camera_video,
                R.color.theme_source_camera));

        SOURCES.put(R.id.nav_device, new SourceInfo(
                Sources.DEVICE,
                R.drawable.ic_source_device,
                R.string.source_device,
                R.color.theme_source_device));

        SOURCES.put(R.id.nav_gallery, new SourceInfo(
                Sources.DEVICE,
                R.drawable.ic_source_device,
                R.string.source_gallery,
                R.color.theme_source_device));

        SOURCES.put(R.id.nav_facebook, new SourceInfo(
                Sources.FACEBOOK,
                R.drawable.ic_source_facebook,
                R.string.source_facebook,
                R.color.theme_source_facebook));

        SOURCES.put(R.id.google_photos, new SourceInfo(
                Sources.GOOGLE_PHOTOS,
                R.drawable.ic_source_google_photos,
                R.string.source_google_photos,
                R.color.theme_source_google_photos));

        SOURCES.put(R.id.instagram, new SourceInfo(
                Sources.INSTAGRAM,
                R.drawable.ic_source_instagram,
                R.string.source_instagram,
                R.color.theme_source_instagram));

        SOURCES.put(R.id.amazon_drive, new SourceInfo(
                Sources.AMAZON_DRIVE,
                R.drawable.ic_source_amazon_drive,
                R.string.source_amazon_drive,
                R.color.theme_source_amazon_drive));

        SOURCES.put(R.id.box, new SourceInfo(
                Sources.BOX,
                R.drawable.ic_source_box,
                R.string.source_box,
                R.color.theme_source_box));

        SOURCES.put(R.id.dropbox, new SourceInfo(
                Sources.DROPBOX,
                R.drawable.ic_source_dropbox,
                R.string.source_dropbox,
                R.color.theme_source_dropbox));

        SOURCES.put(R.id.google_drive, new SourceInfo(
                Sources.GOOGLE_DRIVE,
                R.drawable.ic_source_google_drive,
                R.string.source_google_drive,
                R.color.theme_source_google_drive));

        SOURCES.put(R.id.onedrive, new SourceInfo(
                Sources.ONEDRIVE,
                R.drawable.ic_source_onedrive,
                R.string.source_onedrive,
                R.color.theme_source_onedrive));

        SOURCES.put(R.id.github, new SourceInfo(
                Sources.GITHUB,
                R.drawable.ic_source_github,
                R.string.source_github,
                R.color.theme_source_github));

        SOURCES.put(R.id.gmail, new SourceInfo(
                Sources.GMAIL,
                R.drawable.ic_source_gmail,
                R.string.source_gmail,
                R.color.theme_source_gmail));
    }
    
    static SourceInfo getSourceInfo(int id) {
        return SOURCES.get(id);
    }

    static void textViewReplace(TextView view, String target, String replacement) {
        String text = view.getText().toString();
        text = text.replace(target, replacement);
        view.setText(text);
    }

    static String trimLastPathSection(String path) {
        String[] sections = path.split("/");
        String newPath = "/";
        for (int i = 1; i < sections.length - 1; i++) {
            newPath += sections[i] + "/";
        }
        return newPath;
    }

    static Selection.SimpleSaver getSelectionSaver() {
        if (selectionSaver == null) {
            selectionSaver = new Selection.SimpleSaver();
        }
        return selectionSaver;
    }

    static String getPathFromMediaUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri,  projection, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    static File createPictureFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";
        // Store in normal camera directory
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    static File createMovieFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "MP4_" + timeStamp + "_";
        // Store in normal camera directory
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return File.createTempFile(fileName, ".mp4", storageDir);
    }

    static void addMediaToGallery(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    static void initializeClient(Config config, String sessionToken) {
        client = new Client(config);
        client.setSessionToken(sessionToken);
    }

    static Client getClient() {
        return client;
    }
}
