package com.ebs.banglalinkbangladhol.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.adapter.OnLoadMoreListener;
import com.ebs.banglalinkbangladhol.bean.Album;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    public AlbumFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;

    int current_page = 1;
    int numberOfColumns = 2;

    private ProgressBar splashbar;
    public String  albumCT, albumType, albumCategory, item;

    public String selectedCode, selectedCatname, selectedArtistName, selectedAlbumName,
            selectedPublisher, selectedRelease, selectedImage;
    private Integer selectedCount;

    public JSONObject albums, albums_more;

    public List<Album> albumList = new ArrayList<>();
    public List<Album> albumList2 = new ArrayList<>();

    ArrayList<HashMap<String, String>> albumItems = new ArrayList<HashMap<String, String>>();;

    public static String[] AitemsCodeArray, AitemsCatNameArray, AitemsArtistNameArray,
            AitemsAlbumNameArray, AitemsCpArray, AitemsReleaseArray, AitemsImageArray;
    public static Integer[] AiitemsCountArray;

    public DisplayImageOptions options;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    Context mContext;

    private NativeExpressAdView adView;
    private AdRequest request;

    public static final String FRAGMENT_TAG = "AlbumFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album2, container, false);

        splashbar = (ProgressBar) view.findViewById(R.id.splashAlbumProgress);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        //albumItems = new ArrayList<HashMap<String, String>>();
        mContext = getActivity();

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        mUserAdapter = new UserAdapter();

        try {

            Bundle args = getArguments();

            if (args != null) {

                albumCT = args.getString("albumCt"); // use
                albumType = args.getString("albumType"); // use
                albumCategory = args.getString("albumCategory"); //use
            }

            if(albumCategory != null){
                getActivity().setTitle(albumCategory);
            }

        } catch (Exception e) {

            Log.d("TAG", "Error in player service");

        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_img)
                .showImageForEmptyUri(R.drawable.no_img)
                .showImageOnFail(R.drawable.no_img).cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        adView = (NativeExpressAdView) view.findViewById(R.id.adView);
        request = new AdRequest.Builder().build();
        request.isTestDevice(getActivity());

        try {

            if(CheckUserInfo.getShowAdHomeStatus() == 1){
                adView.loadAd(request);
            }

        } catch (Exception ex) {

            Log.d("AD ERROR SeeAll", "getShowAdHomeStatus");
        }

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //Toast.makeText(getApplicationContext(), "onAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                //Toast.makeText(getApplicationContext(), "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                //Toast.makeText(getApplicationContext(), "onAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                //Toast.makeText(getApplicationContext(), "onAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "onAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                //Toast.makeText(getApplicationContext(), "onAdClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                //Toast.makeText(getApplicationContext(), "onAdImpression", Toast.LENGTH_SHORT).show();
            }
        });

        if(albumItems.size() == 0){

            Invoke();

        } else {

            mRecyclerView.setAdapter(mUserAdapter);
        }

        return view;
    }

    public void Invoke() {

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThread reqThread = new RequestThread();

            reqThread.start();

        } catch (Exception ex) {

            Log.i("TAG", "Requested Thread");
        }
    }

    public class RequestThread extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {

                    albums = ServerUtilities.requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(),
                            albumCT, albumType, current_page);

                } catch (Exception e) {

                    Log.d("TAG", "Error Occured");

                }

            }

            album_handler.sendEmptyMessage(0);

        }
    }

    Handler album_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if (albums != null) {

                    albumList = HTTPGateway.getDynamicAlbumList(albums);

                    if(albumList.size() == 0) return;

                    for (int i = 0; i < albumList.size(); i++) {

                        HashMap<String, String> map = new HashMap<String, String>();
                        Album tmpData = albumList.get(i);
                        map.put("albumcode_list", tmpData.getAlbumCode());
                        map.put("catname_list", tmpData.getCatname());
                        map.put("artistname_list", tmpData.getArtistname());
                        map.put("albumname_list", tmpData.getAlbumName());
                        map.put("cp_list", tmpData.getCp());
                        map.put("release_list", tmpData.getRelease());
                        map.put("count_list", String.valueOf(tmpData.getCount()));
                        map.put("image_list", tmpData.getAlbumImgUrl());
                        albumItems.add(map);
                    }

                    mRecyclerView.setAdapter(mUserAdapter);

                    mUserAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {

                            albumItems.add(null);
                            mUserAdapter.notifyItemInserted(albumItems.size() - 1);
                            //Load more data for reyclerview
                            InvokeMore();
                        }
                    });

                } else {

                    Toast.makeText(getActivity(),
                            "Couldn't Connect, Please Try Again", Toast.LENGTH_LONG).show();
                }

            } catch (Exception ex){
                Log.d("Error albumhandler", ex.toString());
            }

        }

    };

    public void InvokeMore() {

        try {

            //splashbar.setVisibility(View.VISIBLE);

            RequestThreadMore reqThread = new RequestThreadMore();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error in Requested Thread");
        }
    }

    public class RequestThreadMore extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {

                    current_page += 1;

                    albums_more = ServerUtilities.requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(),
                            albumCT, albumType, current_page);

                } catch (Exception ex) {

                    Log.d("TAG", "Error in data thread");
                }

            }

            handler_more.sendEmptyMessage(0);

        }
    }

    Handler handler_more = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(android.os.Message msg) {

            //splashbar.setVisibility(View.GONE);

            if (albums_more != null) {

                try {
                    // remove loading item
                    albumItems.remove(albumItems.size() - 1);
                    mUserAdapter.notifyItemRemoved(albumItems.size());

                    albumList2 = HTTPGateway.getDynamicAlbumList(albums_more);

                    if(albumList2.size() == 0){
                        Toast.makeText(getActivity(),"No More Album To Load", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (int i = 0; i < albumList2.size(); i++) {

                        HashMap<String, String> map = new HashMap<String, String>();
                        Album tmpData = albumList2.get(i);
                        map.put("albumcode_list", tmpData.getAlbumCode());
                        map.put("catname_list", tmpData.getCatname());
                        map.put("artistname_list", tmpData.getArtistname());
                        map.put("albumname_list", tmpData.getAlbumName());
                        map.put("cp_list", tmpData.getCp());
                        map.put("release_list", tmpData.getRelease());
                        map.put("count_list", String.valueOf(tmpData.getCount()));
                        map.put("image_list", tmpData.getAlbumImgUrl());
                        albumItems.add(map);
                    }

                    mUserAdapter.notifyDataSetChanged();
                    mUserAdapter.setLoaded();

                } catch (Exception ex) {
                    Log.d("TAG", "Error while handler_more ");
                }

            } else {


            }

        }
    };

    class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView image;

        public UserViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.gridText);
            image = (ImageView) itemView.findViewById(R.id.gridImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();

                    try {

                        HashMap<String, String> item = new HashMap<String, String>();
                        item = albumItems.get(pos);

                        selectedCode = item.get("albumcode_list");
                        selectedCatname = item.get("catname_list");
                        selectedArtistName = item.get("artistname_list");
                        selectedAlbumName = item.get("albumname_list");
                        selectedPublisher = item.get("cp_list");
                        selectedRelease = item.get("release_list");
                        selectedCount = Integer.parseInt(item.get("count_list"));
                        selectedImage = item.get("image_list");

                        if(albumType.contains("album")){

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
                            Bundle args = new Bundle();
                            args.putString("albumCode", selectedCode);
                            args.putString("albumName", selectedAlbumName);
                            args.putString("albumArtist", selectedArtistName);
                            args.putString("albumGenre", selectedCatname);
                            args.putString("albumPublisher", selectedPublisher);
                            args.putString("albumYear", selectedRelease);
                            args.putInt("albumCount", selectedCount);
                            args.putString("albumImage", selectedImage);
                            albumDetailsFragment.setArguments(args);
                            ft.replace(R.id.home_container, albumDetailsFragment, AlbumDetailsFragment.FRAGMENT_TAG);
                            ft.addToBackStack(AlbumDetailsFragment.FRAGMENT_TAG);
                            ft.commit();

                        } else {

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            PlaylistDetailsFragment playlistDetailsFragment = new PlaylistDetailsFragment();
                            Bundle args = new Bundle();
                            args.putString("albumCode", selectedCode);
                            args.putString("albumName", selectedAlbumName);
                            args.putString("albumArtist", selectedArtistName);
                            args.putString("albumGenre", selectedCatname);
                            args.putString("albumPublisher", selectedPublisher);
                            args.putString("albumYear", selectedRelease);
                            args.putInt("albumCount", selectedCount);
                            args.putString("albumImage", selectedImage);
                            playlistDetailsFragment.setArguments(args);
                            ft.replace(R.id.home_container, playlistDetailsFragment, PlaylistDetailsFragment.FRAGMENT_TAG);
                            ft.addToBackStack(PlaylistDetailsFragment.FRAGMENT_TAG);
                            ft.commit();
                        }

                    } catch (Exception ex){

                        Log.d("Error", "Album onitem click");

                    }
                }
            });
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private OnLoadMoreListener mOnLoadMoreListener;

        private boolean isLoading;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        public UserAdapter() {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return albumItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_user_item, parent, false);
                return new UserViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof UserViewHolder) {

                HashMap<String, String> item = new HashMap<String, String>();
                item = albumItems.get(position);

                UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.text.setText(item.get("albumname_list"));

                imageLoader.displayImage(item.get("image_list"), userViewHolder.image,
                        options, animateFirstListener);

            } else if (holder instanceof LoadingViewHolder) {

                //LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                //loadingViewHolder.progressBar.setIndeterminate(true);

                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;

                if(position == albumItems.size() - 1) {
                    //loadingViewHolder.progressBar.setVisibility(View.GONE);
                    loadingViewHolder.progressBar.setIndeterminate(false);

                } else {
                    loadingViewHolder.progressBar.setIndeterminate(true);
                }
            }
        }

        @Override
        public int getItemCount() {
            return albumItems == null ? 0 : albumItems.size();
        }

        public void setLoaded() {
            isLoading = false;
        }
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
