package wxplus.opengles2forandroid;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hi on 2017/10/29.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;

    protected GLSurfaceView mGlView;
    protected GLSurfaceView.Renderer mRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setTitle(getClass().getSimpleName());
        mGlView = new GLSurfaceView(this);
        // Request an OpenGL ES 2.0 compatible context.
        mGlView.setEGLContextClientVersion(2);
        mGlView.setRenderer(mRenderer = createGlViewRenderer());
        // Check if the system supports OpenGL ES 2.0.
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        final boolean supportsEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));
        setContentView(mGlView);
    }

    public void startActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }

    public abstract GLSurfaceView.Renderer createGlViewRenderer();
}
