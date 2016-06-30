package com.example.library.image;

import com.baijiahulian.common.image.ImageOptions;
import com.example.library.R;

/**
 * Created by houlijiang on 15/12/16.
 *
 * 图片加载的通用工具类
 *
 *
 * ImageLoader.displayImage(url, CommonImageView view, FImageLoaderUtils.createDefaultOptions());
 */
public class FImageLoaderUtils {

    /**
     * 创建一个默认的图片加载配置
     */
    public static ImageOptions createDefaultOptions() {
        ImageOptions.Builder builder = new ImageOptions.Builder();

        builder.setImageOnEmpty(R.drawable.f_ic_empty);
        builder.setImageOnFail(R.drawable.f_ic_img_fail);
        builder.setImageOnLoading(R.drawable.f_ic_img_placeholder);
        return builder.build();
    }
}