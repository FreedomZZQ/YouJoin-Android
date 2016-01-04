package me.zq.youjoin.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends BaseFragment {


    @Bind(R.id.plugin_list)
    ListView pluginList;
    @Bind(R.id.add_plugin_fab)
    FloatingActionButton addPluginFab;

    public PluginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plugin, container, false);
        ButterKnife.bind(this, view);

        TextView emptyView = new TextView(getActivity());
        emptyView.setText(getString(R.string.hint_nodata));
        ViewGroup parentView = (ViewGroup) pluginList.getParent();
        parentView.addView(emptyView, 2);
        pluginList.setEmptyView(emptyView);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
