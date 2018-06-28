package com.uml.composition.c;

public class Session {
    SurfaceSession mSurfaceSession;

    public Session() {
    }

    void windowAddedLocked() {
        if (mSurfaceSession == null) {
            mSurfaceSession = new SurfaceSession();
        }
    }

    void killSessionLocked() {
        if (mSurfaceSession != null) {
            mSurfaceSession = null;
        }
    }
}
