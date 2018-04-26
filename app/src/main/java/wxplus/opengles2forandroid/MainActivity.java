package wxplus.opengles2forandroid;

import android.content.pm.ActivityInfo;
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
    }


    // onclick start
    public void click_simple_color(View view) {
        startActivity(OpenGL_01_Simple_Color.class);
    }
    public void click_simple_texture(View view) {
        startActivity(OpenGL_02_Simple_Texture.class);
    }
    public void click_simple_object(View view) {
        startActivity(OpenGL_03_Simple_Object.class);
    }
    public void click_sky_box(View view) {
        startActivity(OpenGL_04_Skybox.class);
    }
    public void click_height_map(View view) {
        startActivity(OpenGL_05_HeightMap.class);
    }
}
