package wxplus.opengles2forandroid;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wxplus.opengles2forandroid.utils.CommonUtils;
import wxplus.opengles2forandroid.utils.GLog;

public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.app_name);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(rootView);
        ArrayList<ActivityInfo> appActivityList = CommonUtils.getAppActivityList(this);
        LinearLayout layoutContent = rootView.findViewById(R.id.layout_content);
        if (appActivityList != null && appActivityList.size() > 0) {
            layoutContent.removeAllViews();
            for (int i = 0; i < appActivityList.size(); i++) {
                ActivityInfo activityInfo = appActivityList.get(i);
                if (activityInfo.name.endsWith(".MainActivity")) {
                    continue;
                }
                RelativeLayout menuItemView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_main_list_item, null);
                TextView titleView = menuItemView.findViewById(R.id.title);
                titleView.setText(activityInfo.name.replaceAll(".*\\.", ""));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startActivity(Class.forName(activityInfo.name));
                        } catch (ClassNotFoundException e) {
                            GLog.e(TAG, "onCreate, e = " + e);
                        }
                    }
                });
                layoutContent.addView(menuItemView);
            }
        }
        startActivity(OpenGL_01_Color.class);
    }

    @Override
    public int layoutResId() {
        return 0;
    }

    @Override
    public GLSurfaceView.Renderer createGlViewRenderer() {
        return null;
    }
}
