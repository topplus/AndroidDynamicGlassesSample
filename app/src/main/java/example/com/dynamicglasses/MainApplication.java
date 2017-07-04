package example.com.dynamicglasses;

import android.app.Application;

import com.tendcloud.tenddata.TCAgent;

import topplus.com.commonutils.Library;

/**
 * Created by ssbai on 2016/7/14.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Library.init(getApplicationContext(), "", "");
        TCAgent.init(this, "123C3B621BEE7E5EA2E76D95F8CB971C", "Dynamic_Sample");
        TCAgent.setReportUncaughtExceptions(true);
    }
}
