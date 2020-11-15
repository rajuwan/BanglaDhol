package com.ebs.banglalinkbangladhol.typefacedviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class TypefacedBoldItTextView extends TextView {

	public TypefacedBoldItTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TypefacedBoldItTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TypefacedBoldItTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/MyriadPro-SemiboldIt.ttf");

		setTypeface(tf);
	}

}
