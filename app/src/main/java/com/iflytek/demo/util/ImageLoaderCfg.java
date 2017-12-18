package com.iflytek.demo.util;

import com.iflytek.demo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageLoaderCfg {
	public static  DisplayImageOptions default_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_default)
			.showImageForEmptyUri(R.drawable.ic_default)
			.showImageOnFail(R.drawable.ic_default).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(1000)).build();
//	.displayer(new RoundedBitmapDisplayer(20)).build();
}
