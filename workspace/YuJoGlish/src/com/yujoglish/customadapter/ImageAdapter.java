package com.yujoglish.customadapter;

import com.yujoglish.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {

	
		Context context;
		private int[] GalImages = new int[] {
		//R.drawable.one,
		//R.drawable.two,
		//R.drawable.three
				R.drawable.tuto1,
				R.drawable.tuto2,
				R.drawable.tuto3,
				R.drawable.tuto4,
				R.drawable.tuto5,
				R.drawable.tuto6,
				R.drawable.tuto7,
				R.drawable.tuto8,
				R.drawable.tuto9,
				R.drawable.tuto10
		};
		public ImageAdapter(Context context){
			this.context=context;
		}
		@Override
		public int getCount() {
			return GalImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(context);
			//int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageResource(GalImages[position]);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
		
	
}
