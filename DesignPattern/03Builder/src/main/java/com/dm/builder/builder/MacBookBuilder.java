package com.dm.builder.builder;

import com.dm.builder.director.MacbookDirector;
import com.dm.builder.product.Computer;
import com.dm.builder.product.MacBook;

/**
 * 负责构建Computer
 */
public class MacBookBuilder extends Builder {
    public MacBookBuilder() {
    }

    @Override
    public void buildBoard(Computer computer, String board) {
        computer.setBoard(board);
    }

    @Override
    public void buildDisplay(Computer computer, String display) {
        computer.setDisplay(display);
    }

    @Override
    public void buildOS(Computer computer) {
        computer.setOS();
    }

    @Override
    public Computer create(String board, String display) {
        Computer computer = new MacBook();

        // 将各个模块组装的作用
        MacbookDirector pcDirector = new MacbookDirector(this);

        // 封装构建过程,4核、内存2G、Mac系统
        pcDirector.construct(computer, board, display);

        return computer;
    }
}
