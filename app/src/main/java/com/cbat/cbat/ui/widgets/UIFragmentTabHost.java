package com.cbat.cbat.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cbat.cbat.util.GlobalClass;

import java.util.ArrayList;

/**
 * 自定义FragmentTabHost,在源码的基础上，增加了一个动画设置
 *
 * @author begreat
 * 
 * @date 2016-5-12 下午12:53:10
 */
public class UIFragmentTabHost extends TabHost implements TabHost.OnTabChangeListener {
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	private FrameLayout mRealTabContent;
	private Context mContext;
	private FragmentManager mFragmentManager;
	private int mContainerId;
	private OnTabChangeListener mOnTabChangeListener;
	private TabInfo mLastTab;
	private boolean mAttached;
	private int currentTab = 0;
	ImageView share;
	ImageView timeFilter;

	protected static final int NO_ANIM = -1;

	private int forward_anim_in = NO_ANIM;
	private int forward_anim_out = NO_ANIM;
	private int back_anim_in = NO_ANIM;
	private int back_anim_out = NO_ANIM;



	static final class TabInfo {
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;
		private Fragment fragment;

		TabInfo(String _tag, Class<?> _class, Bundle _args) {
			tag = _tag;
			clss = _class;
			args = _args;
		}
	}

	static class DummyTabFactory implements TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	static class SavedState extends BaseSavedState {
		String curTab;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			curTab = in.readString();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeString(curTab);
		}

		@Override
		public String toString() {
			return "FragmentTabHost.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " curTab=" + curTab + "}";
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	public UIFragmentTabHost(Context context) {
		// Note that we call through to the version that takes an AttributeSet,
		// because the simple Context construct can result in a broken object!
		super(context, null);
		initFragmentTabHost(context, null);
	}

	public UIFragmentTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFragmentTabHost(context, attrs);
	}

	private void initFragmentTabHost(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, new int[] { android.R.attr.inflatedId }, 0, 0);
		mContainerId = a.getResourceId(0, 0);
		a.recycle();

		super.setOnTabChangedListener(this);
	}

	/**
	 * 设置前进动画：即进入当前Fragment右侧的目标Fragment
	 *
	 * @param anim_in
	 *            目标Fragment进入的动画
	 * @param anim_out
	 *            当前Fragment退出的动画
	 */
	public void setForwardAnim(int anim_in, int anim_out) {
		this.forward_anim_in = anim_in;
		this.forward_anim_out = anim_out;
	}

	/**
	 * 设置后退动画：即进入当前Fragment左侧的目标Fragment
	 *
	 * @param anim_in
	 *            目标Fragment进入的动画
	 * @param anim_out
	 *            当前Fragment退出的动画
	 */
	public void setBackAnim(int anim_in, int anim_out) {
		this.back_anim_in = anim_in;
		this.back_anim_out = anim_out;
	}

	private void ensureHierarchy(Context context) {
		// If owner hasn't made its own view hierarchy, then as a convenience
		// we will construct a standard one here.
		if (findViewById(android.R.id.tabs) == null) {
			LinearLayout ll = new LinearLayout(context);
			ll.setOrientation(LinearLayout.VERTICAL);
			addView(ll, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			TabWidget tw = new TabWidget(context);
			tw.setId(android.R.id.tabs);
			tw.setOrientation(TabWidget.HORIZONTAL);
			ll.addView(tw, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

			FrameLayout fl = new FrameLayout(context);
			fl.setId(android.R.id.tabcontent);
			ll.addView(fl, new LinearLayout.LayoutParams(0, 0, 0));

			mRealTabContent = fl = new FrameLayout(context);
			mRealTabContent.setId(mContainerId);
			ll.addView(fl, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		}
	}

	/**
	 * @deprecated Don't call the original TabHost setup, you must instead call {@link #setup(Context, FragmentManager)} or
	 *             {@link #setup(Context, FragmentManager, int)}.
	 */
	@Override
	@Deprecated
	public void setup() {
		throw new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
	}

	public void setup(Context context, FragmentManager manager) {
		ensureHierarchy(context); // Ensure views required by super.setup()
		super.setup();
		mContext = context;
		mFragmentManager = manager;
		ensureContent();
	}

	public void setup(Context context, FragmentManager manager, int containerId) {
		ensureHierarchy(context); // Ensure views required by super.setup()
		super.setup();
		mContext = context;
		mFragmentManager = manager;
		mContainerId = containerId;
		ensureContent();
		mRealTabContent.setId(containerId);

		// We must have an ID to be able to save/restore our state. If
		// the owner hasn't set one at this point, we will set it ourself.
		if (getId() == View.NO_ID) {
			setId(android.R.id.tabhost);
		}
	}

	private void ensureContent() {
		if (mRealTabContent == null) {
			mRealTabContent = findViewById(mContainerId);
			if (mRealTabContent == null) {
				throw new IllegalStateException("No tab content FrameLayout found for id " + mContainerId);
			}
		}
	}

	@Override
	public void setOnTabChangedListener(OnTabChangeListener l) {
		mOnTabChangeListener = l;
	}

	public void addTab(TabSpec tabSpec, Class<?> clss, Bundle args) {
		tabSpec.setContent(new DummyTabFactory(mContext));
		String tag = tabSpec.getTag();

		TabInfo info = new TabInfo(tag, clss, args);

		if (mAttached) {
			// If we are already attached to the window, then check to make
			// sure this tab's fragment is inactive if it exists. This shouldn't
			// normally happen.
			info.fragment = mFragmentManager.findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction ft = mFragmentManager.beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}
		}

		mTabs.add(info);
		addTab(tabSpec);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		String currentTab = getCurrentTabTag();

		// Go through all tabs and make sure their fragments match
		// the correct state.
		FragmentTransaction ft = null;
		for (int i = 0; i < mTabs.size(); i++) {
			TabInfo tab = mTabs.get(i);
			tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
			if (tab.fragment != null && !tab.fragment.isDetached()) {
				if (tab.tag.equals(currentTab)) {
					// The fragment for this tab is already there and
					// active, and it is what we really want to have
					// as the current tab. Nothing to do.
					mLastTab = tab;
				} else {
					// This fragment was restored in the active state,
					// but is not the current tab. Deactivate it.
					if (ft == null) {
						ft = mFragmentManager.beginTransaction();
					}
					ft.detach(tab.fragment);//old
                    //ft.hide(tab.fragment);//new
				}
			}
		}
		
		if (isInEditMode()){
		    return;
        }

		// We are now ready to go. Make sure we are switched to the
		// correct tab.
		mAttached = true;
		ft = doTabChanged(currentTab, ft);
		if (ft != null) {
			ft.commit();
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mAttached = false;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.curTab = getCurrentTabTag();
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		setCurrentTabByTag(ss.curTab);
	}

	@Override
	public void onTabChanged(String tabId) {
		if (mAttached) {
			FragmentTransaction ft = doTabChanged(tabId, null);
			if (ft != null) {
				ft.commit();
			}
		}
		if (mOnTabChangeListener != null) {
			mOnTabChangeListener.onTabChanged(tabId);
		}
	}

	private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {


		TabInfo newTab = null;

		int desTab = 0;// added

		for (int i = 0; i < mTabs.size(); i++) {
			TabInfo tab = mTabs.get(i);
			if (tab.tag.equals(tabId)) {
				//Log.d("TAb main1",tabId);
				newTab = tab;
				desTab = i;// added
				GlobalClass.selectedMainTab=desTab;
				Log.d("Main Tab Selected","desTab" +desTab);
			}
		}

		if (newTab == null) {
			throw new IllegalStateException("No tab known for tag " + tabId);
		}
		if (mLastTab != newTab) {
			if (ft == null) {
				ft = mFragmentManager.beginTransaction();
			}
			// ----------------------------------------------------------------------
			// 添加动画
			if (desTab > currentTab) {// 前进
				if (forward_anim_in != NO_ANIM && forward_anim_out != NO_ANIM) {
					ft.setCustomAnimations(forward_anim_in, forward_anim_out);
				}
			} else if (desTab < currentTab) {// 后退
				if (back_anim_in != NO_ANIM && back_anim_out != NO_ANIM) {
					ft.setCustomAnimations(back_anim_in, back_anim_out);
				}
			}

			currentTab = desTab;
			// -----------------------------------------------------------------------

			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
                ensureTabSingle(newTab);
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(mContext, newTab.clss.getName(), newTab.args);
					ft.add(mContainerId, newTab.fragment, newTab.tag);
					ft.addToBackStack(tabId);
				} else {
					ft.attach(newTab.fragment);// old
					//ft.show(newTab.fragment);// new
				}
			}

			mLastTab = newTab;
		}
		return ft;
	}
	
	private void ensureTabSingle(TabInfo tabInfo){
        
        if (tabInfo == null){
            return ;
        }
        
        tabInfo.fragment = mFragmentManager.findFragmentByTag(tabInfo.tag);
        
    }
}