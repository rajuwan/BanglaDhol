package com.ebs.banglalinkbangladhol.adapter;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.model.DrawerItem;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;

	public CustomDrawerAdapter(Context context, int layoutResourceID, List<DrawerItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.ItemName = (TextView) view
					.findViewById(R.id.drawer_itemName);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

			drawerHolder.title = (TextView) view.findViewById(R.id.drawerTitle);

			drawerHolder.headerLayout = (LinearLayout) view
					.findViewById(R.id.headerLayout);
			drawerHolder.itemLayout = (LinearLayout) view
					.findViewById(R.id.itemLayout);

			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();

		}

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

		if (dItem.getTitle() != null) {

			drawerHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.title.setText(dItem.getTitle());
			drawerHolder.icon.setImageResource(dItem.getImgResID());

		} else {

			drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.ItemName.setText(dItem.getItemName());
			drawerHolder.ItemName.setTypeface(null, Typeface.NORMAL);
			drawerHolder.icon.setImageResource(dItem.getImgResID());

		}

		if (((ListView) parent).isItemChecked(position)) {
			drawerHolder.ItemName.setTextColor(Color.parseColor("#FFFFFF"));
			drawerHolder.ItemName.setTypeface(null, Typeface.BOLD);
			drawerHolder.icon.setVisibility(View.VISIBLE);

		} else {
			drawerHolder.ItemName.setTextColor(Color.parseColor("#DDDDDB"));
			drawerHolder.ItemName.setTypeface(null, Typeface.NORMAL);
			drawerHolder.icon.setVisibility(View.VISIBLE);
			//drawerHolder.icon.setVisibility(View.INVISIBLE);
		}

		return view;
	}

	private static class DrawerItemHolder {
		TextView ItemName, title;
		ImageView icon;
		LinearLayout headerLayout, itemLayout;

	}
}