package me.zq.youjoin.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.activity.PluginActivity;
import me.zq.youjoin.activity.PluginDownloadActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends BaseFragment {


    @Bind(R.id.plugin_list)
    ListView pluginList;
    @Bind(R.id.add_plugin_fab)
    FloatingActionButton addPluginFab;
    @Bind(R.id.start)
    Button startButton;

    public PluginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plugin, container, false);
        ButterKnife.bind(this, view);

//        TextView emptyView = new TextView(getActivity());
//        emptyView.setText(getString(R.string.hint_nodata));
//        ViewGroup parentView = (ViewGroup) pluginList.getParent();
//        parentView.addView(emptyView, 2);
//        pluginList.setEmptyView(emptyView);

        addPluginFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginDownloadActivity.actionStart(getActivity());
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginActivity.actionStart(getActivity());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
