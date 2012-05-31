/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.pkhsolutions.idispatch.mobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author peholmst
 */
public class OverviewFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview, container, false);

        // TODO Complete this method!

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
