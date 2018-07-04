package com.uml.assocation.b;

public class WindowManagerGlobal {
    private static WindowManagerGlobal sDefaultWindowManager;

    private WindowManagerGlobal() {
    }

    public static WindowManagerGlobal getInstance() {
        synchronized (WindowManagerGlobal.class) {
            if (sDefaultWindowManager == null) {
                sDefaultWindowManager = new WindowManagerGlobal();
            }
            return sDefaultWindowManager;
        }
    }
}
