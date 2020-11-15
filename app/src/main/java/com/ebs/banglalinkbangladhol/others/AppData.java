package com.ebs.banglalinkbangladhol.others;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;

import com.ebs.banglalinkbangladhol.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class AppData extends Application {
	public static final boolean DEVELOPER_MODE = false;

	@SuppressWarnings("unused")
	public void onCreate() {
		super.onCreate();
		if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}

		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.no_img_album)
				.showImageOnFail(R.drawable.no_img_album).resetViewBeforeLoading()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.defaultDisplayImageOptions(options)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}
}
