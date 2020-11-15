package com.ebs.banglalinkbangladhol.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.Account_Subscription_DataModel;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;
import com.google.android.material.snackbar.Snackbar;

public class AccountFragment extends Fragment {

	public AccountFragment() {

	}

	private String response;
	ProgressBar splashbar;

	private List<Account_Subscription_DataModel> sub_downloadData;

	private static String[] subDateArray, subPackArray, subPurposeArray, subAmountArray;

	private ListView subList;
	private TextView txtStatus, txtStatusText;
	private TableLayout subTable;
	private Animation anim;

	public static final String FRAGMENT_TAG = "AccountFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_account, container, false);

		getActivity().setTitle("My Account");

		splashbar = (ProgressBar) view.findViewById(R.id.accountProgress);
		subList = (ListView) view.findViewById(R.id.listview_sub);

		try {

			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.fragment_account_header,
					subList, false);

			txtStatus = (TextView) header.findViewById(R.id.top_header_subStatus);
			txtStatusText = (TextView) header.findViewById(R.id.top_header_subscription);
			subTable = (TableLayout) header.findViewById(R.id.table_sub);

			subList.addHeaderView(header, null, false);

		} catch (Exception ex) {

			Log.d("TAG", "Error in Requested Thread");
		}

		anim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

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

					response = ServerUtilities.requestForMyAccount(CheckUserInfo.getUserMsisdn());

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

				if (response != null && response.length() > 30) {
					// Account Subscription
					sub_downloadData = HTTPGateway
							.getDynamicAccount_Subscription_DataModel(response);

					if (CheckUserInfo.getUserPackText() != null) {

						txtStatus.setText(CheckUserInfo.getUserPackText());
						txtStatus.setAnimation(anim);

					}

					txtStatusText.setText("Subscription History of last 90 days");
					subTable.setVisibility(View.VISIBLE);

					if (sub_downloadData != null) {

						List<String> date_list = new ArrayList<String>();
						List<String> purpose_list = new ArrayList<String>();
						List<String> pack_list = new ArrayList<String>();
						List<String> amount_list = new ArrayList<String>();

						for (int i = 0; i < sub_downloadData.size(); i++) {

							Account_Subscription_DataModel tmpDataSub = sub_downloadData.get(i);

							date_list.add(tmpDataSub.getDate_time());
							purpose_list.add(tmpDataSub.getPurpose());
							pack_list.add(tmpDataSub.getCharge_type());
							amount_list.add(tmpDataSub.getCharge_amount());

						}

						subDateArray = date_list.toArray(new String[] {});
						subPurposeArray = purpose_list.toArray(new String[] {});
						subPackArray = pack_list.toArray(new String[] {});
						subAmountArray = amount_list.toArray(new String[] {});

						subList.setAdapter(new SuscriptionAdapter());

					}

				} else {

					Snackbar.make(getView(), "No Subscription History Found", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();

				}



			} catch (Exception ex) {

				Log.d("TAG", "Error in request thread");
			}

		}

	};

	public class SuscriptionAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView sub_time, sub_type, sub_amount;

		}

		@Override
		public int getCount() {

			return subDateArray.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {

			LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {

				view = mInflater
						.inflate(R.layout.account_subscription_single_row,
								parent, false);
				holder = new ViewHolder();

				holder.sub_time = (TextView) view.findViewById(R.id.sub_time);
				holder.sub_type = (TextView) view.findViewById(R.id.sub_type);
				holder.sub_amount = (TextView) view
						.findViewById(R.id.sub_amount);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.sub_time.setText(subDateArray[position]);
			holder.sub_type.setText(subPurposeArray[position] + " " + subPackArray[position]);

			String amount = subAmountArray[position];

			if(amount.length() > 1){
				holder.sub_amount.setText(subAmountArray[position]);
			}else{
				holder.sub_amount.setText("0");
			}

			return view;
		}
	}

}

