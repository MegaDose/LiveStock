package com.bongi.emobilepastoralism.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bongi.emobilepastoralism.R;
import com.bongi.emobilepastoralism.app.AppController;
import com.bongi.emobilepastoralism.data.NotificationItem;

import java.util.List;


public class NotificationsListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<NotificationItem> NotificationItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public NotificationsListAdapter(Activity activity, List<NotificationItem> NotificationItems) {
		this.activity = activity;
		this.NotificationItems = NotificationItems;
	}

	@Override
	public int getCount() {
		return NotificationItems.size();
	}

	@Override
	public Object getItem(int location) {
		return NotificationItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.notification_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView mail = (TextView) convertView.findViewById(R.id.mail);
		
		TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView amp = (TextView) convertView.findViewById(R.id.amp);
		TextView file = (TextView) convertView.findViewById(R.id.file);
        TextView type = (TextView) convertView.findViewById(R.id.type);
		TextView details = (TextView) convertView.findViewById(R.id.details);
        TextView read = (TextView) convertView.findViewById(R.id.read);
		TextView count = (TextView) convertView.findViewById(R.id.messcount);
		TextView messsent = (TextView) convertView.findViewById(R.id.messsent);
		LinearLayout messblock =(LinearLayout) convertView.findViewById(R.id.countmess);
		
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.notepadPic);


		NotificationItem item = NotificationItems.get(position);


        amp.setText("&"+String.valueOf((item.getAmp())));
		id.setText(String.valueOf((item.getId())));
        file.setText(String.valueOf(item.getFile()));
        type.setText(String.valueOf(item.getType()));
        read.setText(String.valueOf(item.getRead()));

        details.setText(item.getDetails());
		name.setText(item.getName());
		mail.setText(item.getMial());
		read.setText(String.valueOf(item.getRead()));
		count.setText(String.valueOf(item.getCount()));

		if(item.getCount()==0)
		{
			messsent.setVisibility(View.VISIBLE);
			messblock.setVisibility(View.GONE);
		}else{
			messsent.setVisibility(View.GONE);
			messblock.setVisibility(View.VISIBLE);
		}
		

		// Converting timestamp into x ago format
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Long.parseLong((item.getTimeStamp())),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

		timestamp.setText(timeAgo);



		// user profile pic
		profilePic.setImageUrl(item.getNotepadPic(), imageLoader);



		return convertView;
	}

}
