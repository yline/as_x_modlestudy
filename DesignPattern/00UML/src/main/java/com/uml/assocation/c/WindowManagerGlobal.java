package com.uml.assocation.c;

public class WindowManagerGlobal {
    private static IWindowManager sWindowManagerService;

    public static IWindowManager getWindowManagerService() {
        synchronized (WindowManagerGlobal.class) {
            if (sWindowManagerService == null) {
                sWindowManagerService = WindowManagerService.getInstance();
            }
            return sWindowManagerService;
        }
    }
}
