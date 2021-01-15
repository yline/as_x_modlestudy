package com.yline.ninepatch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.drawable.NinePatchDrawable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class NinePatchBuilder {
    private int width, height;
    private Bitmap bitmap;
    private Resources resources;
    private ArrayList<Integer> xRegions = new ArrayList<Integer>();
    private ArrayList<Integer> yRegions = new ArrayList<Integer>();

    public NinePatchBuilder(Resources resources, Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        this.bitmap = bitmap;
        this.resources = resources;
    }

    public NinePatchBuilder addXRegion(int x, int width) {
        xRegions.add(x);
        xRegions.add(x + width);
        return this;
    }

    public NinePatchBuilder addXRegionPoints(int x1, int x2) {
        xRegions.add(x1);
        xRegions.add(x2);
        return this;
    }

    public NinePatchBuilder addXRegion(float xPercent, float widthPercent) {
        int xtmp = (int) (xPercent * this.width);
        xRegions.add(xtmp);
        xRegions.add(xtmp + (int) (widthPercent * this.width));
        return this;
    }

    public NinePatchBuilder addXRegionPoints(float x1Percent, float x2Percent) {
        xRegions.add((int) (x1Percent * this.width));
        xRegions.add((int) (x2Percent * this.width));
        return this;
    }

    public NinePatchBuilder addXCenteredRegion(int width) {
        int x = (int) ((this.width - width) / 2);
        xRegions.add(x);
        xRegions.add(x + width);
        return this;
    }

    public NinePatchBuilder addXCenteredRegion(float widthPercent) {
        int width = (int) (widthPercent * this.width);
        int x = (int) ((this.width - width) / 2);
        xRegions.add(x);
        xRegions.add(x + width);
        return this;
    }

    public NinePatchBuilder addYRegion(int y, int height) {
        yRegions.add(y);
        yRegions.add(y + height);
        return this;
    }

    public NinePatchBuilder addYRegionPoints(int y1, int y2) {
        yRegions.add(y1);
        yRegions.add(y2);
        return this;
    }

    public NinePatchBuilder addYRegion(float yPercent, float heightPercent) {
        int ytmp = (int) (yPercent * this.height);
        yRegions.add(ytmp);
        yRegions.add(ytmp + (int) (heightPercent * this.height));
        return this;
    }

    public NinePatchBuilder addYRegionPoints(float y1Percent, float y2Percent) {
        yRegions.add((int) (y1Percent * this.height));
        yRegions.add((int) (y2Percent * this.height));
        return this;
    }

    public NinePatchBuilder addYCenteredRegion(int height) {
        int y = (int) ((this.height - height) / 2);
        yRegions.add(y);
        yRegions.add(y + height);
        return this;
    }

    public NinePatchBuilder addYCenteredRegion(float heightPercent) {
        int height = (int) (heightPercent * this.height);
        int y = (int) ((this.height - height) / 2);
        yRegions.add(y);
        yRegions.add(y + height);
        return this;
    }

    public byte[] buildChunk() {
        if (xRegions.size() == 0) {
            xRegions.add(0);
            xRegions.add(width);
        }
        if (yRegions.size() == 0) {
            yRegions.add(0);
            yRegions.add(height);
        }

        int NO_COLOR = 1;//0x00000001;
        int COLOR_SIZE = 9;//could change, may be 2 or 6 or 15 - but has no effect on output
        int arraySize = 1 + 2 + 4 + 1 + xRegions.size() + yRegions.size() + COLOR_SIZE;
        ByteBuffer byteBuffer = ByteBuffer.allocate(arraySize * 4).order(ByteOrder.nativeOrder());
        byteBuffer.put((byte) 1);//was translated
        byteBuffer.put((byte) xRegions.size());//divisions x
        byteBuffer.put((byte) yRegions.size());//divisions y
        byteBuffer.put((byte) COLOR_SIZE);//color size

        //skip
        byteBuffer.putInt(0);
        byteBuffer.putInt(0);

        //padding -- always 0 -- left right top bottom
        byteBuffer.putInt(0);
        byteBuffer.putInt(0);
        byteBuffer.putInt(0);
        byteBuffer.putInt(0);

        //skip
        byteBuffer.putInt(0);

        for (int rx : xRegions)
            byteBuffer.putInt(rx); // regions left right left right ...
        for (int ry : yRegions)
            byteBuffer.putInt(ry);// regions top bottom top bottom ...

        for (int i = 0; i < COLOR_SIZE; i++)
            byteBuffer.putInt(NO_COLOR);

        return byteBuffer.array();
    }

    public NinePatch buildNinePatch() {
        byte[] chunk = buildChunk();
        if (bitmap != null)
            return new NinePatch(bitmap, chunk, null);
        return null;
    }

    public NinePatchDrawable build() {
        NinePatch ninePatch = buildNinePatch();
        if (ninePatch != null)
            return new NinePatchDrawable(resources, ninePatch);
        return null;
    }
}
