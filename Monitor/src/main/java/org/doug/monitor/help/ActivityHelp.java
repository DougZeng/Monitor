/* 
 * 2010-2017 (C) Antonio Redondo
 * http://antonioredondo.com
 * http://github.com/AntonioRedondo/AnotherMonitor
 *
 * Code under the terms of the GNU General Public License v3.
 *
 */

package org.doug.monitor.help;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.doug.monitor.R;
import org.doug.monitor.base.Constans;

public class ActivityHelp extends Activity {

	private BroadcastReceiver receiverFinish = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};





	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		final Resources res = getResources();
		
		if (Build.VERSION.SDK_INT >= 19) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			
			float sSW = res.getConfiguration().smallestScreenWidthDp, sD = res.getDisplayMetrics().density;
			
			LinearLayout l = (LinearLayout) findViewById(R.id.LParent);
			int statusBarHeight = res.getDimensionPixelSize(res.getIdentifier(Constans.sbh, Constans.dimen, Constans.android));
			int navigationBarHeight = 0;
			
			if (!ViewConfiguration.get(this).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)
					&& (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || sSW > 560)) {
				getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
				navigationBarHeight = res.getDimensionPixelSize(res.getIdentifier(Constans.nbh, Constans.dimen, Constans.android));
				if (navigationBarHeight == 0)
					navigationBarHeight = (int) (48*sD);
				FrameLayout nb = (FrameLayout) findViewById(R.id.LNavigationBar);
				nb.setVisibility(View.VISIBLE);
				((FrameLayout.LayoutParams) nb.getLayoutParams()).height = navigationBarHeight;
			}
			
			l.setPadding(0, statusBarHeight, 0, navigationBarHeight);
		}
		
		// http://stackoverflow.com/questions/4790746/links-in-textview
		Linkify.addLinks((TextView) findViewById(R.id.TVHelpText), Linkify.WEB_URLS);
	}





	@Override
	public void onStart() {
		super.onStart();
		registerReceiver(receiverFinish, new IntentFilter(Constans.actionFinishActivity));
	}





	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiverFinish);
	}
}