package dev.cavi.reality.augmented.ar_sgi;

import android.app.Application;

import org.artoolkit.ar.base.assets.AssetHelper;

public class ARApplication extends Application {
    private static Application sInstance;

    public static Application getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ((ARApplication) sInstance).initializeInstance();
    }

    // Here we do one-off initialisation which should apply to all activities in the application.
    protected void initializeInstance() {
        // Unpack assets to cache directory so native library can read them.
        // N.B.: If contents of assets folder changes, be sure to increment the
        // versionCode integer in the AndroidManifest.xml file.
        AssetHelper assetHelper = new AssetHelper(getAssets());
        assetHelper.cacheAssetFolder(getInstance(), "Data");
    }
}