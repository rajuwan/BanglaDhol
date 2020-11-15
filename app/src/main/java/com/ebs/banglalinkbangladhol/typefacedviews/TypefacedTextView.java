package com.ebs.banglalinkbangladhol.typefacedviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefacedTextView extends TextView {

	public TypefacedTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TypefacedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TypefacedTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/MyriadPro-Regular.ttf");

		setTypeface(tf);
	}

}
