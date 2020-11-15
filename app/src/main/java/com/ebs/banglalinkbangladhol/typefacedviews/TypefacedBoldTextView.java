package com.ebs.banglalinkbangladhol.typefacedviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefacedBoldTextView extends TextView {

	public TypefacedBoldTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TypefacedBoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TypefacedBoldTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/MyriadPro-Semibold.ttf");

		setTypeface(tf);
	}

}
