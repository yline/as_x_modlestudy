package com.view.animate.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

public class EasyTransition {
    public static final String EASY_TRANSITION_OPTIONS = "easy_transition_options";
    public static final long DEFAULT_TRANSITION_ANIM_DURATION = 1000;

    public static void startActivity(Activity activity, Intent intent, View... views) {
        intent.putParcelableArrayListExtra(EASY_TRANSITION_OPTIONS, genViewAttrs(views));
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void enter(Activity activity, long duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        Intent intent = activity.getIntent();
        ArrayList<ViewAttrs> attrs = intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runEnterAnimation(activity, attrs, duration, interpolator, listener);
    }

    public static void exit(Activity activity, long duration, TimeInterpolator interpolator) {
        Intent intent = activity.getIntent();
        ArrayList<ViewAttrs> attrs = intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runExitAnimation(activity, attrs, duration, interpolator);
    }

    public static ArrayList<ViewAttrs> genViewAttrs(View... views) {
        if (null == views) {
            return new ArrayList<>();
        }

        ArrayList<ViewAttrs> attrs = new ArrayList<>();
        for (View view : views) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            attrs.add(new EasyTransition.ViewAttrs(
                    view.getId(),
                    location[0], location[1],
                    view.getWidth(), view.getHeight()
            ));
        }

        return attrs;
    }

    public static void runEnterAnimation(Activity activity, ArrayList<ViewAttrs> attrs,
                                         final long duration, final TimeInterpolator interpolator, final Animator.AnimatorListener listener) {
        if (null == attrs || attrs.size() == 0) {
            return;
        }

        for (final ViewAttrs attr : attrs) {
            final View view = activity.findViewById(attr.id);
            if (null == view) {
                continue;
            }

            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    view.setPivotX(0);
                    view.setPivotY(0);
                    view.setScaleX(attr.width / view.getWidth());
                    view.setScaleY(attr.height / view.getHeight());
                    view.setTranslationX(attr.startX - location[0]); // xDelta
                    view.setTranslationY(attr.startY - location[1]); // yDelta

                    view.animate().scaleX(1).scaleY(1)
                            .translationX(0).translationY(0)
                            .setDuration(duration)
                            .setInterpolator(interpolator)
                            .setListener(listener);
                    return true;
                }
            });
        }
    }

    public static void runExitAnimation(final Activity activity, ArrayList<ViewAttrs> attrs,
                                        long duration, TimeInterpolator interpolator) {
        if (null == attrs || attrs.size() == 0) {
            return;
        }

        for (final ViewAttrs attr : attrs) {
            View view = activity.findViewById(attr.id);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            view.setPivotX(0);
            view.setPivotY(0);

            view.animate()
                    .scaleX(attr.width / view.getWidth())
                    .scaleY(attr.height / view.getHeight())
                    .translationX(attr.startX - location[0])
                    .translationY(attr.startY - location[1])
                    .setInterpolator(interpolator)
                    .setDuration(duration);
        }

        activity.findViewById(attrs.get(0).id).postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }, duration);
    }

    public static class ViewAttrs implements Parcelable {
        public int id;
        public float startX;
        public float startY;
        public float width;
        public float height;

        public ViewAttrs(int id, float startX, float startY, float width, float height) {
            this.id = id;
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeFloat(this.startX);
            dest.writeFloat(this.startY);
            dest.writeFloat(this.width);
            dest.writeFloat(this.height);
        }

        public static final Parcelable.Creator<ViewAttrs> CREATOR = new Parcelable.Creator<ViewAttrs>() {
            @Override
            public ViewAttrs createFromParcel(Parcel source) {
                int id = source.readInt();
                float startX = source.readFloat();
                float startY = source.readFloat();
                float width = source.readFloat();
                float height = source.readFloat();
                return new ViewAttrs(id, startX, startY, width, height);
            }

            @Override
            public ViewAttrs[] newArray(int size) {
                return new ViewAttrs[size];
            }
        };
    }
}
