package org.twinone.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;

import wewe.app.moboost.R;

public class ProcessGalleryFile extends AsyncTask<Void, Void, Bitmap> {

    private static int WIDTH = 80;
    private static int HEIGHT = 80;

    ImageLoader mImageLoader;

    ImageView photoHolder;
    TextView durationHolder;
    MediaType type;
    String filePath;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
Context ct;

    public ProcessGalleryFile(ImageView photoHolder, TextView durationHolder, String filePath, MediaType type,Context ct) {
        HEIGHT = WIDTH = (int) photoHolder.getContext().getResources().getDimension(R.dimen.thumbnail_width);
        this.filePath = filePath;
        this.durationHolder = durationHolder;
        this.photoHolder = photoHolder;
        this.type = type;
        this.ct =ct;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        Bitmap bmp = null;
        Log.d(getClass().getSimpleName(), "" + Thread.getAllStackTraces().keySet().size());

        String pathh = filePath;
        MediaType tt =type;
        if (type != MediaType.PHOTO || type==MediaType.PHOTO) {
            try {
                String path = filePath;
                mImageLoader = ImageLoader.getInstance();
                mImageLoader.init(ImageLoaderConfiguration.createDefault(ct));
                bmp = ImageLoader.getInstance().getMemoryCache().get(Uri.fromFile(new File(filePath)).toString() + "_");
            } catch (Exception e) {
                Log.e(ProcessGalleryFile.class.getSimpleName(), "" + e);
            }
            if (bmp == null) {
                try {
                    bmp = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
                    if (bmp != null) {
                        mImageLoader = ImageLoader.getInstance();
                        mImageLoader.init(ImageLoaderConfiguration.createDefault(ct));
                        ImageLoader.getInstance().getMemoryCache().put(Uri.fromFile(new File(filePath)).toString() + "_", bmp);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception when rotating thumbnail for gallery", e);
                } catch (OutOfMemoryError e) {
                    Log.e(ProcessGalleryFile.class.getSimpleName(), "" + e);
                }
            }
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (type == MediaType.PHOTO) {
            durationHolder.setVisibility(View.GONE);
            ImageAware aware = new ImageViewAware(photoHolder) {

                @Override
                public int getWidth() {
                    return WIDTH;
                }

                @Override
                public int getHeight() {
                    return HEIGHT;
                }
            };
            mImageLoader = ImageLoader.getInstance();
            mImageLoader.init(ImageLoaderConfiguration.createDefault(ct));

            File file = new File(filePath);

            final String uri1 = Uri.fromFile(file).toString();
            final String decoded1 = Uri.decode(uri1);
            mImageLoader.getInstance().displayImage(decoded1, aware, ImageOption.GALLERY_OPTIONS.getDisplayImageOptions());
        } else {
            durationHolder.setText(Utils.getDurationMark(filePath, retriever));
            durationHolder.setVisibility(View.VISIBLE);
            photoHolder.setImageBitmap(result);
        }
    }


    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ProcessGalleryFile)) return false;
        ProcessGalleryFile file = (ProcessGalleryFile) o;
        return filePath != null && file.filePath != null && filePath.equals(file.filePath);
    }


    public ImageLoader getImageLoader(Context ct) {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
            mImageLoader.init(ImageLoaderConfiguration.createDefault(ct));
        }
        return this.mImageLoader;
    }
}


