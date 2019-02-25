package com.yline.finger.manager;

public abstract class OnAuthCallback {
    public void onAuthError(int errorCode, CharSequence errString) {
    }

    public void onAuthFailed() {
    }

    public void onAuthHelp(int helpCode, CharSequence helpString) {
    }

    public void onAuthSucceeded() {
    }
}
