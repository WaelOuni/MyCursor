package app.stage.sifast.floatingview.Service;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.stage.sifast.floatingview.R;

public class ServiceFloating extends Service {

    public static int ID_NOTIFICATION = 2018;

    private WindowManager windowManager;
    private ImageView chatHead;
    private PopupWindow pwindo;

    boolean mHasDoubleClicked = false;
    long lastPressTime;
    long lastReleaseTime;
    long pressTime;
    private Boolean _enable = true;
    ArrayList<String> myArray;
    List listCity;
    WindowManager.LayoutParams params;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*Toast toast;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());*/
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        chatHead = new ImageView(this);
        chatHead.setClickable(true);
        chatHead.setImageResource(R.drawable.floating4);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FIRST_SUB_WINDOW,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        windowManager.addView(chatHead, params);
        try {

            chatHead.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int clics;
                    int x;
                    int y;
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            // Get current time in nano seconds.
                            pressTime = System.currentTimeMillis();
                            // If double click...
                            if (pressTime - lastPressTime <= 300) {
                                //ServiceFloating.this.stopSelf();
                                mHasDoubleClicked = true;
                            } else {     // If not double click....
                                mHasDoubleClicked = false;
                            }

                            lastPressTime = pressTime;
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            Log.i("{x,y}=", event.getX() + " " + event.getY());
                            //	Toast.makeText(getApplicationContext(), "hiiii", Toast.LENGTH_LONG).show();
                            clics = 1;
                            break;

                        case MotionEvent.ACTION_UP:

                            clics = 0;
                            x = params.x;
                            y = params.y;
                            lastReleaseTime = System.currentTimeMillis();
                            if (lastReleaseTime - pressTime <= 300) {


                                //	Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                                windowManager.removeViewImmediate(chatHead);
                                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                params = new WindowManager.LayoutParams(
                                        WindowManager.LayoutParams.WRAP_CONTENT,
                                        WindowManager.LayoutParams.WRAP_CONTENT,
                                        WindowManager.LayoutParams.TYPE_PHONE,
                                        WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
                                        PixelFormat.TRANSLUCENT);
                                params.gravity = Gravity.TOP | Gravity.LEFT;
                                params.x = x;
                                params.y = y;
                                //new InjectEvent().execute(chatHead);
                                windowManager.addView(chatHead, params);
                                //timer.scheduleAtFixedRate(m, 0, 100);

                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                windowManager.removeViewImmediate(chatHead);
                                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                params = new WindowManager.LayoutParams(
                                        WindowManager.LayoutParams.WRAP_CONTENT,
                                        WindowManager.LayoutParams.WRAP_CONTENT,
                                        WindowManager.LayoutParams.TYPE_PHONE,
                                        WindowManager.LayoutParams.FIRST_SUB_WINDOW,
                                        PixelFormat.TRANSLUCENT);
                                params.gravity = Gravity.TOP | Gravity.LEFT;
                                params.x = (initialX + (int) (event.getRawX() - initialTouchX));
                                params.y = (initialY + (int) (event.getRawY() - initialTouchY));
                                //new InjectEvent().execute(chatHead);
                                windowManager.addView(chatHead, params);
                            }

                            break;

                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(chatHead, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }

        chatHead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //		initiatePopupWindow(chatHead);
                _enable = false;
            }
        });
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {


        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
    }


    class InjectEvent extends AsyncTask<View, Void, View> {
        MotionEvent m1;

        @Override
        protected View doInBackground(View... params) {
            m1 = MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN, params[0].getX(), params[0].getY(), 0);

            return params[0];
        }

        @Override
        protected void onPostExecute(View o) {
            super.onPostExecute(o);
            if (m1.getAction() == MotionEvent.ACTION_DOWN) {
                //	windowManager.removeViewImmediate(o);
                o.performClick();
                o.dispatchTouchEvent(m1);
            }
        }
    }
/*
	Thread thread = new Thread(){
		@Override
		public void run(){
			Instrumentation m_Instrumentation = new Instrumentation();

			m_Instrumentation.sendPointerSync(MotionEvent.obtain(
					SystemClock.uptimeMillis(),
					SystemClock.uptimeMillis(),
					MotionEvent.ACTION_DOWN,posx, posy, 0));
			m_Instrumentation.sendPointerSync(MotionEvent.obtain(
					SystemClock.uptimeMillis(),
					SystemClock.uptimeMillis(),
					MotionEvent.ACTION_UP,width*4/5,height, 0));
		}
	};*/


/*
	private void initiatePopupWindow(View anchor) {
		try {
			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			ListPopupWindow popup = new ListPopupWindow(this);
			popup.setAnchorView(anchor);
			popup.setWidth((int) (display.getWidth()/(1.5)));
			//ArrayAdapter<String> arrayAdapter =
			//new ArrayAdapter<String>(this,R.layout.list_item, myArray);
			popup.setAdapter(new CustomAdapter(getApplicationContext(), R.layout.row, listCity));
			popup.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id3) {
					//Log.w("tag", "package : "+apps.get(position).pname.toString());
					Intent i;
					PackageManager manager = getPackageManager();
					try {
						i = manager.getLaunchIntentForPackage(apps.get(position).pname.toString());
						if (i == null)
							throw new PackageManager.NameNotFoundException();
						i.addCategory(Intent.CATEGORY_LAUNCHER);
						startActivity(i);
					} catch (PackageManager.NameNotFoundException e) {

					}
				}
			});
			popup.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/


}