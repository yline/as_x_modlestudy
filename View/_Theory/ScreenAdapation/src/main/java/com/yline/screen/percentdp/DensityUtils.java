package com.yline.screen.percentdp;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/**
 * preview预览设置：
 * 已知：屏幕宽度 width px，屏幕高度 height px，dpi总数：x
 * 则：(x / 160) : width = 屏幕尺寸 / (width^2 + height^2)^(1/2)
 * <p>
 * 屏幕的尺寸 = (x / 160) * (1 + (height / width)^2)^(1/2)
 */
public class DensityUtils {
    public static void setCustomDensity(@NonNull Activity activity, @NonNull Application application) {
        setCustomDensityInner(activity, application, 360);
    }

    private static float sNonCompatDensity;
    private static float sNonCompatScaleDensity;

    /**
     * @param activity    当前页面 上下文
     * @param application app 上下文
     * @param width       屏幕宽度，总dp数
     */
    private static void setCustomDensityInner(@NonNull Activity activity, @NonNull final Application application, @IntRange(from = 100) int width) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaleDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (null != newConfig && newConfig.fontScale > 0) {
                        sNonCompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / width;
        final float targetScaleDensity = targetDensity * (sNonCompatScaleDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        appDisplayMetrics.scaledDensity = targetScaleDensity;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
        activityDisplayMetrics.scaledDensity = targetScaleDensity;
    }
}
