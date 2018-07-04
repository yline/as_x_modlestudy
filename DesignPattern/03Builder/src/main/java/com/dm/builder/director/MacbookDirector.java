package com.dm.builder.director;

import com.dm.builder.builder.Builder;
import com.dm.builder.product.Computer;

/**
 * 负责构建Computer
 */
public class MacbookDirector {
    private Builder mBuilder;

    public MacbookDirector(Builder builder) {
        this.mBuilder = builder;
    }

    /**
     * 组装的过程
     */
    public void construct(Computer computer, String board, String display) {
        mBuilder.buildBoard(computer, board);
        mBuilder.buildDisplay(computer, display);
        mBuilder.buildOS(computer);
    }
}
