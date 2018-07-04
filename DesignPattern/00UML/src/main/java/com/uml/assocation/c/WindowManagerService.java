package com.uml.assocation.c;

public class WindowManagerService {
    private static WindowManagerService sDefaultWindowManager;

    private WindowManagerService() {
    }

    public static WindowManagerService getInstance() {
        synchronized (WindowManagerService.class) {
            if (sDefaultWindowManager == null) {
                sDefaultWindowManager = new WindowManagerService();
            }
            return sDefaultWindowManager;
        }
    }
}
