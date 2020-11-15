package com.ebs.banglalinkbangladhol.adapter;

import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.MainActivity;
import com.ebs.banglalinkbangladhol.model.ActionClick;
import com.ebs.banglalinkbangladhol.model.SectionDataModel;
import com.ebs.banglalinkbangladhol.util.CirclePageIndicator;
import com.ebs.banglalinkbangladhol.util.PageIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SectionDataModel> dataList;
    private FragmentActivity activity;
    public static View footer;

    int currentPage = 0;
    int NUM_PAGES = 0;

    public static final int ITEM_TYPE_HORIZONTAL = 0;
    public static final int ITEM_TYPE_VERTICAL_GRID = 1;
    public static final int ITEM_TYPE_VERTICAL_LIST = 2;
    public static final int ITEM_TYPE_SLIDER = 3;
    public static final int ITEM_TYPE_PROMO = 4;
    public static final int ITEM_TYPE_FOOTER = 5;

    public RecyclerViewDataAdapter() {

    }

    public RecyclerViewDataAdapter(FragmentActivity activity, ArrayList<SectionDataModel> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == ITEM_TYPE_HORIZONTAL) {

            View horizontalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
            return new ItemHorizontalRowHolder(horizontalView);

        } else if (viewType == ITEM_TYPE_VERTICAL_GRID) {

            View verticalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_2, null);
            return new ItemVerticalRowHolder(verticalView);

        } else if (viewType == ITEM_TYPE_VERTICAL_LIST) {

            View verticalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_2, null);
            return new ItemVerticalRowHolder(verticalView);

        } else if (viewType == ITEM_TYPE_SLIDER) {

            View sliderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home, null);
            return new ItemSliderHolder(sliderView);

        } else if (viewType == ITEM_TYPE_PROMO) {

            View promoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_promo, null);
            return new ItemPromoHolder(promoView);

        } else if (viewType == ITEM_TYPE_FOOTER) {

            View footerView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_footer, null);
            return new ItemFooterHolder(footerView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder itemRowHolder, final int i) {

        //final int itemType = getItemViewType(i);

        final String catCode = dataList.get(i).getCatCode();

        final String sectionName = dataList.get(i).getHeaderTitle();

        final int contentType = dataList.get(i).getContentType();

        final int contentViewType = dataList.get(i).getContentViewType();

        final ArrayList<HashMap<String, String>> menuItems = dataList.get(i).getSectionData();

        if (contentViewType == 1) { // horizontal

            if(menuItems.size() == 0){

                ((ItemHorizontalRowHolder)itemRowHolder).itemTitle.setVisibility(View.GONE);
                ((ItemHorizontalRowHolder)itemRowHolder).btnMore.setVisibility(View.GONE);
                ((ItemHorizontalRowHolder)itemRowHolder).recycler_view_list.setVisibility(View.GONE);

            } else{

                ((ItemHorizontalRowHolder)itemRowHolder).itemTitle.setVisibility(View.VISIBLE);
                ((ItemHorizontalRowHolder)itemRowHolder).btnMore.setVisibility(View.VISIBLE);
                ((ItemHorizontalRowHolder)itemRowHolder).recycler_view_list.setVisibility(View.VISIBLE);
            }

            ((ItemHorizontalRowHolder)itemRowHolder).itemTitle.setText(sectionName);

            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(activity,
                    menuItems, 1, contentType);

            //((ItemHorizontalRowHolder)itemRowHolder).recycler_view_list.setNestedScrollingEnabled(false);
            //((ItemHorizontalRowHolder)itemRowHolder).recycler_view_list.setHasFixedSize(true);
            ((ItemHorizontalRowHolder)itemRowHolder).recycler_view_list.setAdapter(itemListDataAdapter);

            ((ItemHorizontalRowHolder)itemRowHolder).recycler_view_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));


            ((ItemHorizontalRowHolder)itemRowHolder).btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(contentType == 1){
                        ActionClick.goToContentSeeAllFragment(activity, sectionName, catCode, 0);
                    } else if (contentType == 2){
                        ActionClick.goToAlbumSeeAllFragment(activity, catCode, "album", sectionName);
                    } else if (contentType == 3){
                        ActionClick.goToAlbumSeeAllFragment(activity, catCode, "playlist", sectionName);
                    }

                }
            });

        } else if (contentViewType == 2) { // vertical grid

            if(menuItems.size() == 0){

                ((ItemVerticalRowHolder)itemRowHolder).itemTitle.setVisibility(View.GONE);
                ((ItemVerticalRowHolder)itemRowHolder).btnMore.setVisibility(View.GONE);
                ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setVisibility(View.GONE);

            } else {

                ((ItemVerticalRowHolder)itemRowHolder).itemTitle.setVisibility(View.VISIBLE);
                ((ItemVerticalRowHolder)itemRowHolder).btnMore.setVisibility(View.VISIBLE);
                ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setVisibility(View.VISIBLE);
            }

            ((ItemVerticalRowHolder)itemRowHolder).itemTitle.setText(sectionName);

            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(activity,
                    menuItems, 2, contentType);

            //((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setNestedScrollingEnabled(false);
            //((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setHasFixedSize(true);

            ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setLayoutManager(new GridLayoutManager(activity, 2));

            ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setAdapter(itemListDataAdapter);

            ((ItemVerticalRowHolder)itemRowHolder).btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(contentType == 1){
                        ActionClick.goToContentSeeAllFragment(activity, sectionName, catCode, 0);
                    } else if (contentType == 2){
                        ActionClick.goToAlbumSeeAllFragment(activity, catCode, "album", sectionName);
                    } else if (contentType == 3){
                        ActionClick.goToAlbumSeeAllFragment(activity, catCode, "playlist", sectionName);
                    }

                }
            });


        } else if(contentViewType == 3){ // vertical list

            if(menuItems.size() == 0){

                ((ItemVerticalRowHolder)itemRowHolder).itemTitle.setVisibility(View.GONE);
                ((ItemVerticalRowHolder)itemRowHolder).btnMore.setVisibility(View.GONE);
                ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setVisibility(View.GONE);

            } else {

                ((ItemVerticalRowHolder)itemRowHolder).itemTitle.setVisibility(View.VISIBLE);
                ((ItemVerticalRowHolder)itemRowHolder).btnMore.setVisibility(View.VISIBLE);
                ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setVisibility(View.VISIBLE);
            }

            ((ItemVerticalRowHolder)itemRowHolder).itemTitle.setText(sectionName);

            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(activity,
                    menuItems, 3, contentType);

            //((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setNestedScrollingEnabled(false);
            //((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setHasFixedSize(true);

            ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setLayoutManager(new LinearLayoutManager(activity));

            ((ItemVerticalRowHolder)itemRowHolder).recycler_view_list.setAdapter(itemListDataAdapter);

            ((ItemVerticalRowHolder)itemRowHolder).btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(contentType == 1){
                        ActionClick.goToContentSeeAllFragment(activity, sectionName, catCode, 0);
                    } else if (contentType == 2){
                        ActionClick.goToAlbumSeeAllFragment(activity, catCode, "album", sectionName);
                    } else if (contentType == 3){
                        ActionClick.goToAlbumSeeAllFragment(activity, catCode, "playlist", sectionName);
                    }

                }
            });

        } else if(contentViewType == 4){ // slider

            ((ItemSliderHolder)itemRowHolder).mViewPager.setAdapter(new ImageSlideAdapterNew(activity, menuItems));

            ((ItemSliderHolder)itemRowHolder).mIndicator.setViewPager(((ItemSliderHolder)itemRowHolder).mViewPager);

            NUM_PAGES = menuItems.size();

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    ((ItemSliderHolder)itemRowHolder).mViewPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 5000, 5000);

            // Pager listener over indicator
            ((ItemSliderHolder)itemRowHolder).mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }
                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {}

                @Override
                public void onPageScrollStateChanged(int pos) {}
            });

        } else if(contentViewType == 5){ // promo

            if(menuItems.get(i).get("show_list").contains("1")){

                ((ItemPromoHolder)itemRowHolder).iv_promo.setVisibility(View.VISIBLE);

                Picasso.get().load(menuItems.get(i).get("url_list")).placeholder(R.drawable.no_img).
                        error(R.drawable.no_img).into(((ItemPromoHolder)itemRowHolder).iv_promo);

                ((ItemPromoHolder)itemRowHolder).iv_promo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            ActionClick.goToPromoFragment(activity, menuItems.get(i).get("url_list"),
                                    menuItems.get(i).get("text_list"));

                        } catch (Exception ex){

                        }

                    }
                });

            }

        } else if(contentViewType == 6){ // footer


        }

    }

    @Override
    public int getItemViewType(int position) {

        final int contentViewType = dataList.get(position).getContentViewType();

        if (contentViewType == 1) {
            return ITEM_TYPE_HORIZONTAL;
        } else if(contentViewType == 2){
            return ITEM_TYPE_VERTICAL_GRID;
        } else if(contentViewType == 3){
            return ITEM_TYPE_VERTICAL_LIST;
        } else if(contentViewType == 4){
            return ITEM_TYPE_SLIDER;
        } else if(contentViewType == 5){
            return ITEM_TYPE_PROMO;
        } else if(contentViewType == 6){
            return ITEM_TYPE_FOOTER;
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }


    public class ItemHorizontalRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected Button btnMore;


        public ItemHorizontalRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.btnMore= (Button) view.findViewById(R.id.btnMore);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.recycler_view_list.setHasFixedSize(true);
        }

    }

    public class ItemVerticalRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected Button btnMore;


        public ItemVerticalRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list_2);
            this.recycler_view_list.setHasFixedSize(true);
            this.btnMore= (Button) view.findViewById(R.id.btnMore);
        }

    }

    public class ItemSliderHolder extends RecyclerView.ViewHolder {

        protected ViewPager mViewPager;
        protected PageIndicator mIndicator;

        public ItemSliderHolder(View view) {
            super(view);

            mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
            mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

        }

    }

    public class ItemPromoHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_promo;

        public ItemPromoHolder(View view) {
            super(view);
            iv_promo = (ImageView) view.findViewById(R.id.promoBanner);
        }

    }

    public class ItemFooterHolder extends RecyclerView.ViewHolder {

        //public View footer;

        public ItemFooterHolder(View view) {
            super(view);
            footer = (View) view.findViewById(R.id.footer_blank);

            if(MainActivity.hasPlayList){

                footer.setVisibility(View.INVISIBLE);

            }
        }

    }

}