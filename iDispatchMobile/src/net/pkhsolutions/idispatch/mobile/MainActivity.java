package net.pkhsolutions.idispatch.mobile;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        bar.addTab(bar.newTab()
                .setText("Overview")
                .setTabListener(new TabListener<OverviewFragment>(this, "overview", OverviewFragment.class)));
        bar.addTab(bar.newTab()
                .setText("Status")
                .setTabListener(new TabListener<StatusFragment>(this, "status", StatusFragment.class)));
        bar.addTab(bar.newTab()
                .setText("Messages")
                .setTabListener(new TabListener<MessagesFragment>(this, "messages", MessagesFragment.class)));

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
    
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {

        private final Activity activity;
        private final String tag;
        private final Class<T> fragmentClass;
        private final Bundle args;
        private Fragment fragment;
        
        public TabListener(Activity activity, String tag, Class<T> fragmentClass) {
            this(activity, tag, fragmentClass, null);
        }
        
        public TabListener(Activity activity, String tag, Class<T> fragmentClass, Bundle args) {
            this.activity = activity;
            this.tag = tag;
            this.fragmentClass = fragmentClass;
            this.args = args;
            
            fragment = activity.getFragmentManager().findFragmentByTag(tag);
            if (fragment != null && !fragment.isDetached()) {
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                ft.detach(fragment);
                ft.commit();
            }
        }
        
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (fragment == null) {
                fragment = Fragment.instantiate(activity, fragmentClass.getName(), args);
                ft.add(android.R.id.content, fragment, tag);
            } else {
                ft.attach(fragment);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (fragment != null) {
                ft.detach(fragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }        
    }
}
