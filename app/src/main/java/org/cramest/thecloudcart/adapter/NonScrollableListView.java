package org.cramest.thecloudcart.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by cremaluca on 18/05/2017.
 */

public class NonScrollableListView extends ListView {

    public NonScrollableListView(Context context) {
        super(context);
    }
    public NonScrollableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NonScrollableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Non ho idea di cosa faccia, so che l'ho copiato da internet e funziona
        int heightMeasureSpec_custom = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}