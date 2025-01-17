package com.app.whosnextapp.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;

import com.app.whosnextapp.customVideoViews.OnVideoTrimListener;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static void startTrim(@NonNull File src, @NonNull String dst, long startMs, long endMs,
                                 @NonNull OnVideoTrimListener callback) throws IOException {
        File file = new File(dst);
        file.getParentFile().mkdirs();
        Log.d(TAG, "Generated file path " + dst);
        generateVideo(src, file, startMs, endMs, callback);
    }

    private static void generateVideo(@NonNull File src, @NonNull File dst, long startMs,
                                      long endMs, @NonNull OnVideoTrimListener callback) throws IOException {
        // NOTE: Switched to using FileDataSourceViaHeapImpl since it does not use memory mapping (VM).
        // Otherwise we get OOM with large movie files.
        Movie movie = MovieCreator.build(new FileDataSourceImpl(src.getAbsolutePath()));

        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        // remove all tracks we will create new tracks from the old

        double startTime1 = startMs / 1000;
        double endTime1 = endMs / 1000;

        boolean timeCorrected = false;

        // Here we try to find a track that has sync samples. Since we can only start decoding
        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
        // such a frame
        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    // This exception here could be a false positive in case we have multiple tracks
                    // with sync samples at exactly the same positions. E.g. a single movie containing
                    // multiple qualities of the same video (Microsoft Smooth Streaming file)

                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
//                startTime1 = correctTimeToSyncSample(track, startTime1, false);
//                endTime1 = correctTimeToSyncSample(track, endTime1, true);
                timeCorrected = true;
            }
        }

        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            double lastTime = -1;
            long startSample1 = -1;
            long endSample1 = -1;
            for (int i = 0; i < track.getSampleDurations().length; i++) {
                long delta = track.getSampleDurations()[i];


                if (currentTime > lastTime && currentTime <= startTime1) {
                    // current sample is still before the new starttime
                    startSample1 = currentSample;
                }
                if (currentTime > lastTime && currentTime <= endTime1) {
                    // current sample is after the new start time and still before the new endtime
                    endSample1 = currentSample;
                }
                lastTime = currentTime;
                currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
            movie.addTrack(new AppendTrack(new CroppedTrack(track, startSample1, endSample1)));
        }

        Container out = new DefaultMp4Builder().build(movie);

        FileOutputStream fos = new FileOutputStream(dst);
        FileChannel fc = fos.getChannel();
        out.writeContainer(fc);

        fc.close();
        fos.close();
        callback.getResult(Uri.parse(dst.toString()));
    }

    static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    /*To delete cache file from system after video or audio thumbnail upload*/
    public static void deleteInternalCacheFiles(Context context) {
        File[] cacheFiles = context.getCacheDir().listFiles();
        for (File file : cacheFiles) {
            file.delete();
        }
    }

    /*To create thumbnail file from video or audio*/
    public static File createFileFromResource(Context context, Drawable drawable, String fileName) {
        File f;
        try {
            f = new File(context.getCacheDir() + File.separator + fileName);
            //Convert bitmap to byte array
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmap_data = bos.toByteArray();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmap_data);
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            f = null;
        }
        return f;
    }

    public static String createFile(String path, String filename, String extension) {
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) {
            externalRootDir += "/";
        }
        String parentDir = externalRootDir + path;
        File parentDirFile = new File(parentDir);
        if (!parentDirFile.exists())
            parentDirFile.mkdirs();
        return parentDir + filename + extension;
    }

    public static String createFilePath(String path, String filename, String extension) {
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) {
            externalRootDir += "/";
        }
        String parentDir = externalRootDir + path;
        return parentDir + filename + extension;
    }

    /*Post Height Calculation*/
    public static Integer getHeight(int imgHeight, int imgWidth, Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        return screenWidth * imgHeight / imgWidth;
    }
}
