package wxplus.opengles2forandroid.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * @author WangXiaoPlus
 * @date 2018/4/24
 */

public class CommonUtils {
    public static final String TAG = CommonUtils.class.getSimpleName();

    /**
     * 获取本APP中所有的已注册的Activity
     * @return
     */
    public static ArrayList<ActivityInfo> getAppActivityList(Context context) {
        ArrayList<ActivityInfo> atyInfoList = null;
        try {
            String pkgName = context.getApplicationContext().getPackageName();
            PackageInfo pkgInfo = context.getApplicationContext().getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
            if (pkgInfo.activities != null && pkgInfo.activities.length > 0) {
                atyInfoList = new ArrayList<>(pkgInfo.activities.length);
                for (int i = 0; i < pkgInfo.activities.length; i++) {
                    atyInfoList.add(pkgInfo.activities[i]);
                }
            }
        } catch (Exception e) {
            GLog.e(TAG, "getAppActivityList, e = " + e);
        }
        return atyInfoList;
    }
}
