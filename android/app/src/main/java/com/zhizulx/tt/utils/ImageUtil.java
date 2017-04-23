package com.zhizulx.tt.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.zhizulx.tt.ui.activity.HomePageActivity;
import com.zhizulx.tt.ui.helper.PhotoHelper;

import java.io.File;

/**
 * @Description 图片处理
 * @author Nana
 * @date 2014-8-4
 *
 */
public class ImageUtil {
    private static Logger logger = Logger.getLogger(ImageUtil.class);

	public static Bitmap getBigBitmapForDisplay(String imagePath,
			Context context) {
		if (null == imagePath || !new File(imagePath).exists())
			return null;
		try {
			int degeree = PhotoHelper.readPictureDegree(imagePath);
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			if (bitmap == null)
				return null;
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
			float scale = bitmap.getWidth() / (float) dm.widthPixels;
			Bitmap newBitMap = null;
			if (scale > 1) {
				newBitMap = zoomBitmap(bitmap, (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale));
				bitmap.recycle();
				Bitmap resultBitmap = PhotoHelper.rotaingImageView(degeree, newBitMap);
				return resultBitmap;
			}
			Bitmap resultBitmap = PhotoHelper.rotaingImageView(degeree, bitmap);
			return resultBitmap;
		} catch (Exception e) {
			logger.e(e.getMessage());
			return null;
		}
	}

	private static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (null == bitmap) {
			return null;
		}
		try {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) width / w);
			float scaleHeight = ((float) height / h);
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
			return newbmp;
		} catch (Exception e) {
			logger.e(e.getMessage());
			return null;
		}
	}

	public static void GlideRoundAvatar(final Context ctx, String url, final ImageView avatar) {
		Glide.with(ctx).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(avatar) {
			@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable =
						RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
				circularBitmapDrawable.setCircular(true);
				avatar.setImageDrawable(circularBitmapDrawable);
			}
		});
	}

	public static void GlideRoundRectangleAvatar(final Context ctx, String url, final ImageView avatar) {
		Glide.with(ctx).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(avatar) {
			@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable =
						RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
				circularBitmapDrawable.setCornerRadius(15);
				avatar.setImageDrawable(circularBitmapDrawable);
			}
		});
	}

    public static void GlideAvatar(final Context ctx, String url, final ImageView avatar) {
        Glide.with(ctx).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(avatar));
    }
}
