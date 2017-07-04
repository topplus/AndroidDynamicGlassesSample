package example.com.dynamicglasses;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import topplus.com.dynamicglassvr.DynamicGlassTexture;


public class MultiDynamicActivity extends AppCompatActivity {
    DynamicGlassTexture dynamicGlassTexture0, dynamicGlassTexture1,
            dynamicGlassTexture2, dynamicGlassTexture3;
    View mLoadingView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mLoadingView.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_dynamic);
        mLoadingView = findViewById(R.id.loading_view);
        dynamicGlassTexture0 = (DynamicGlassTexture) findViewById(R.id.dynamic0);
        dynamicGlassTexture1 = (DynamicGlassTexture) findViewById(R.id.dynamic1);
        dynamicGlassTexture2 = (DynamicGlassTexture) findViewById(R.id.dynamic2);
        dynamicGlassTexture3 = (DynamicGlassTexture) findViewById(R.id.dynamic3);
        Intent intent = getIntent();
        float mGlassNosePadPos = intent.getFloatExtra("mGlassNosePadPos", 0.2f);
        float mGlassScale = intent.getFloatExtra("mGlassScale", 0.5f);
        dynamicGlassTexture0.setNosePadPos(mGlassNosePadPos);
        dynamicGlassTexture0.setGlassModelScale(mGlassScale);
        dynamicGlassTexture0.loadGlass(getFilesDir() + "/" + MainActivity.mGlasses[1]);
        dynamicGlassTexture1.setNosePadPos(mGlassNosePadPos);
        dynamicGlassTexture1.setGlassModelScale(mGlassScale);
        dynamicGlassTexture1.loadGlass(getFilesDir() + "/" + MainActivity.mGlasses[2]);
        dynamicGlassTexture2.setNosePadPos(mGlassNosePadPos);
        dynamicGlassTexture2.setGlassModelScale(mGlassScale);
        dynamicGlassTexture2.loadGlass(getFilesDir() + "/" + MainActivity.mGlasses[3]);
        dynamicGlassTexture3.setNosePadPos(mGlassNosePadPos);
        dynamicGlassTexture3.setGlassModelScale(mGlassScale);
        int result = dynamicGlassTexture3.loadGlass(getFilesDir() + "/" + MainActivity.mGlasses[4]);
        if (result == 4) {
            Toast.makeText(this, "授权失败，请检查您的授权码！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoadingView.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(1, 1500);
    }
}
