package me.zq.youjoin.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.activity.PublishActivity;
import me.zq.youjoin.widget.recycler.RecyclerItemClickListener;

public class TweetsFragment extends Fragment {

    @Bind(R.id.tweets_recycler_list)
    RecyclerView tweetsRecyclerList;
    @Bind(R.id.add_tweets_fab)
    FloatingActionButton addTweetsFab;

    public TweetsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yj_fragment_tweets, container, false);

        ButterKnife.bind(this, view);

        addTweetsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishActivity.actionStart(getActivity());
            }
        });

        tweetsRecyclerList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        tweetsRecyclerList.setLayoutManager(layoutManager);
        tweetsRecyclerList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));
        tweetsRecyclerList.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

//    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
//        private List<TweetInfo> mTweets = new ArrayList<>();
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder{
//
//        }
//    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
//            Book book = mAdapter.getBook(position);
//            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
//            intent.putExtra("book", book);
//
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                            view.findViewById(R.id.ivBook), getString(R.string.transition_book_img));
//
//            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
