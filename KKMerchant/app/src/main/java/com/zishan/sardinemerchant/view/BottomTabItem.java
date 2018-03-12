package com.zishan.sardinemerchant.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zishan.sardinemerchant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建一个tab
 * Created by wislie on 2017/12/29.
 */

public class BottomTabItem extends LinearLayout {

    private int itemNumber;
    /**
     * 默认字体颜色
     */
    private int unSelectedColor = 0xff000000;
    /**
     * 选中时候的字体颜色
     */
    private int selectedColor = 0xffff0000;

    private int[] selectedImagesList;

    private int[] unSelectedImagesList;

    private int[] titles;

    private int selectedIndex;

    private List<View> mViewsList = new ArrayList<>();

    private TabSelectedListener mTabSelectedListener;

    public void setTabSelectedListener(TabSelectedListener tabSelectedListener) {
        this.mTabSelectedListener = tabSelectedListener;
    }

    public BottomTabItem(Context context) {
        this(context, null);
    }

    public BottomTabItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.BottomTabItem);
        itemNumber = attribute.getInt(R.styleable.BottomTabItem_item_num, itemNumber);
        selectedColor = attribute.getColor(R.styleable.BottomTabItem_item_text_selected_color, unSelectedColor);
        unSelectedColor = attribute.getColor(R.styleable.BottomTabItem_item_text_unselected_color, selectedColor);
        attribute.recycle();
        initLayout(context);
    }

    private void initLayout(Context context) {

        setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < itemNumber; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_bottom_tab, null);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTabSelected((Integer) view.getTag());
                }
            });
            view.setTag(i);
            mViewsList.add(view);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
            view.setLayoutParams(lp);
            addView(view);
        }
        setTabSelected(0);

    }

    //设置tab image
    public void setTabAttrs(int[] selectedImagesList, int[] unselectedImagesList, int[] titles){

        this.selectedImagesList = selectedImagesList;
        this.unSelectedImagesList = unselectedImagesList;
        this.titles = titles;
        setTabSelected(selectedIndex);
    }

    //设置选中后的tab
    private void setTabSelected(int index) {
        selectedIndex = index;
        for (int i = 0; i < itemNumber; i++) {

            View view = mViewsList.get(i);
            ImageView tab_icon = (ImageView) view.findViewById(R.id.tab_icon);
            TextView contentText = (TextView) view.findViewById(R.id.tab_title);
            if (index == i) {
                tab_icon.setSelected(true);
                contentText.setTextColor(selectedColor);
                if(selectedImagesList != null && i < selectedImagesList.length)
                tab_icon.setImageResource(selectedImagesList[i]);
                if(mTabSelectedListener != null){
                    mTabSelectedListener.select(i);
                }
            } else {
                tab_icon.setSelected(false);
                contentText.setTextColor(unSelectedColor);
                if(unSelectedImagesList != null && i < unSelectedImagesList.length)
                    tab_icon.setImageResource(unSelectedImagesList[i]);
            }

            if(titles != null && i < titles.length){
                contentText.setText(getResources().getString(titles[i]));
            }
        }
    }


    public interface TabSelectedListener{
        void select(int position);
    }
}
