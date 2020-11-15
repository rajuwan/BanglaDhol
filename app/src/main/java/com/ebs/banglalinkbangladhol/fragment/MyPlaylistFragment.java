package com.ebs.banglalinkbangladhol.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.PlaylistItem;
import com.ebs.banglalinkbangladhol.model.ActionClick;
import com.ebs.banglalinkbangladhol.others.DBHelperNew;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaylistFragment extends Fragment {


    public MyPlaylistFragment() {
        // Required empty public constructor
    }

    ProgressBar splashbar;

    private static ArrayList<PlaylistItem> allPlaylist = new ArrayList<PlaylistItem>();
    private RecyclerView rv_playlist;
    private PlaylistAdapter  playlistAdapter;
    private TextView tv_noplaylist;
    private DBHelperNew dbHelper;
    FragmentActivity activity;

    int id = 0;

    public static final String FRAGMENT_TAG = "MyPlaylistFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity  = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_playlist, container, false);

        initViews(view);

        dbHelper = new DBHelperNew(getActivity());

        Invoke();

        return view;
    }

    public void Invoke(){

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThread reqThread = new RequestThread();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error while retreving device data ");

        }

    }

    public class RequestThread extends Thread {
        @Override
        public void run() {

            synchronized (this) {

                try {

                    allPlaylist = new ArrayList<PlaylistItem>();
                    allPlaylist.clear();
                    allPlaylist = dbHelper.getAllPlaylist();

                } catch (Exception ex) {

                    Log.d("TAG", "Error in request thread");
                }

            }
            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if (allPlaylist.size() > 0) {

                    playlistAdapter = new PlaylistAdapter(getActivity());
                    rv_playlist.setAdapter(playlistAdapter);

                } else {

                    tv_noplaylist.setVisibility(View.VISIBLE);
                    playlistAdapter.notifyDataSetChanged();

                }


            } catch (Exception ex) {

                Log.d("TAG", "Error in request thread");
            }

        }

    };

    public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {

        private Context mContext;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public ImageView thumbnail, overflow;

            public MyViewHolder(View view) {
                super(view);

                title = (TextView) view.findViewById(R.id.card_playlist_title);
                overflow = (ImageView) view.findViewById(R.id.overflow);
            }
        }

        public PlaylistAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.playlist_card_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final PlaylistItem obj = allPlaylist.get(position);

            holder.title.setText(obj.getPlaylistTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        ActionClick.goToPlaylistById(activity, obj.getId(), obj.getPlaylistTitle());

                    } catch (Exception ex) {
                        // TODO: handle exception
                    }

                }
            });


            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow);
                    id = obj.getId();
                }
            });
        }

        /**
         * Showing popup menu when tapping on 3 dots
         */
        private void showPopupMenu(View view) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_album, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
            popup.show();
        }

        /**
         * Click listener for popup menu items
         */
        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            public MyMenuItemClickListener() {
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.action_delete_playlist:

                        try {

                            boolean deleted = ActionClick.deletePlaylist(id);

                            if(deleted){
                                Invoke();
                            }

                        } catch (Exception ex) {
                            // TODO: handle exception
                        }

                        return true;

                    default:
                }
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return allPlaylist.size();
        }
    }

    public void initViews(View view){

        splashbar = (ProgressBar) view.findViewById(R.id.splashPlaylistProgress);
        rv_playlist = (RecyclerView) view.findViewById(R.id.recycler_view_playlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_playlist.setLayoutManager(mLayoutManager);
        rv_playlist.setItemAnimator(new DefaultItemAnimator());

        tv_noplaylist = (TextView) view.findViewById(R.id.tv_no_playlist);

    }

}
