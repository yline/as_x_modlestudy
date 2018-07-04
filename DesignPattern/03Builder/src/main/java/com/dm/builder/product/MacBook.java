package com.dm.builder.product;

import com.dm.builder.builder.Builder;
import com.dm.builder.builder.MacBookBuilder;

public class MacBook extends Computer {
    public MacBook() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setOS() {
        mOS = "Mac OS X 10.10";
    }

    public static Builder getBuilder() {
        return new MacBookBuilder();
    }
}
