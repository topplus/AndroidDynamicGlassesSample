package example.com.dynamicglasses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import topplus.com.commonutils.util.AssetsHelper;
import topplus.com.dynamicglassvr.DynamicGlassTexture;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String[] mGlasses = {"1.gst", "2.gst",
            "3.gst", "4.gst", "5.gst"};
    private final static String[] mWheelContents = {
            "请选择一副眼镜",
            "1",
            "2",
            "3",
            "4",
            "5"
    };
    private SharedPreferences mShared;
    private DynamicGlassTexture mDynamicGlassTexture;
    private ListView mWheelView;
    private ArrayAdapter<String> mListAdapter;
    private int mCurrentPosition = 0;
    private MoveGestureDetector mMoveDetector;
    //监控和处理滑屏事件
    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mMoveDetector.onTouchEvent(event);
            return true;
        }
    };
    //眼镜在鼻梁上的角度
    private float mGlassNosePadPos = 0.2f;
    //眼镜的相对大小
    private float mGlassScale = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDynamicGlassTexture = (DynamicGlassTexture) findViewById(R.id.dynamicGlass);
        mDynamicGlassTexture.setOnTouchListener(mTouchListener);
        mWheelView = (ListView) findViewById(R.id.wheelview);
        mMoveDetector = new MoveGestureDetector(getApplicationContext(), new MoveListener());
        mListAdapter = new ArrayAdapter<String>(this, R.layout.list_item, Arrays.asList(mWheelContents)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                if (position == mCurrentPosition || position == 0) {
                    textView.setTextColor(Color.BLACK);
                } else {
                    textView.setTextColor(0xFFD3D3D3);
                }
                return textView;
            }
        };
        mWheelView.setAdapter(mListAdapter);
        mWheelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPosition = position;
                if (position > 0) {
                    int result = mDynamicGlassTexture.loadGlass(getFilesDir() + "/" + mGlasses[position - 1]);
                    if (result == 4) {
                        Toast.makeText(MainActivity.this, "授权失败，请检查您的授权码！", Toast.LENGTH_SHORT).show();
                    }
                }
                mListAdapter.notifyDataSetChanged();
            }
        });
        mShared = getSharedPreferences("staticGlassActivity", 0);
        boolean firstRun = mShared.getBoolean("firstRun", true);
        if (firstRun) {
            new CopyGlassTask().execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDynamicGlassTexture.pauseRender();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDynamicGlassTexture.startRender();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.multiview_btn:
                Intent intent = new Intent(MainActivity.this, MultiDynamicActivity.class);
                intent.putExtra("mGlassNosePadPos", mGlassNosePadPos);
                intent.putExtra("mGlassScale", mGlassScale);
                startActivity(intent);
                break;
//            case R.id.btn_transparent_view:
//                startActivity(new Intent(MainActivity.this, TransparentActivity.class));
//                break;
        }
    }

    //拷贝眼镜模型task
    private class CopyGlassTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (String glassName : mGlasses) {
                AssetsHelper.copyAssetFile(getApplicationContext(), glassName,
                        getFilesDir() + "/" + glassName, false);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            SharedPreferences.Editor editor = mShared.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
        }
    }

    //监控和处理滑屏事件
    private class MoveListener implements MoveGestureDetector.MoveListener {
        public boolean onScroll(int fingerCount, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                if (fingerCount == 1) {
                    if (distanceY > 0) {
                        mGlassNosePadPos += 0.02f;
                        if (mGlassNosePadPos > 1.0f)
                            mGlassNosePadPos = 1.0f;
                        mDynamicGlassTexture.setNosePadPos(mGlassNosePadPos);
                    } else if (distanceY < 0) {
                        mGlassNosePadPos -= 0.02f;
                        if (mGlassNosePadPos < 0f)
                            mGlassNosePadPos = 0f;
                        mDynamicGlassTexture.setNosePadPos(mGlassNosePadPos);
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onScale(float factor) {
            mGlassScale *= factor;
            mGlassScale = Math.max(0.001f, Math.min(mGlassScale, 1.0f));
            mDynamicGlassTexture.setGlassModelScale(mGlassScale);
            return true;
        }
    }

}
