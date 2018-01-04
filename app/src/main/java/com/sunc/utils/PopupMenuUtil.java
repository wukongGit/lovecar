package com.sunc.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sunc.car.lovecar.R;
import com.sunc.view.DrawableDivider;

import java.util.List;

public class PopupMenuUtil {

    /**
     * @param activity
     * @param view
     * @param itemList
     * @param listener
     */
    public static void showPopupMenu(final Activity activity, View view, final List<String> itemList, OnPopupMenuItemClick listener) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popView = inflater.inflate(R.layout.layout_popup_menu, null);

        final PopupWindow window = new PopupWindow(popView,
                DimenUtils.INSTANCE.dp2px(activity, 135.0f),
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 点击窗口外区域消失.兼容部分手机
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    window.dismiss();
                    return true;
                }
                return false;
            }
        });

        window.showAsDropDown(view);

        RecyclerView mList = (RecyclerView) popView.findViewById(R.id.list);
        mList.setHasFixedSize(true);
        mList.addItemDecoration(new DrawableDivider(new DrawableDivider.DrawableProvider() {
            @Override
            public Drawable dividerDrawable(int position, RecyclerView parent) {
                return activity.getResources().getDrawable(R.drawable.divider);
            }

            @Override
            public int dividerSize(int position, RecyclerView parent) {
                if (position <= 0) {
                    return 0;
                }
                if (position >= itemList.size()) {
                    return 0;
                }
                return DimenUtils.INSTANCE.dp2px(activity, 0.5f);
            }
        }));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(mLayoutManager);
        mList.setItemAnimator(new DefaultItemAnimator());

        final PopupMenuAdapter mAdapter = new PopupMenuAdapter(activity, itemList, listener, new PopupMenuUtil.OnPopupMenuItemClick() {
            @Override
            public void onItemClick(int position) {
                window.dismiss();
            }
        });
        mList.setAdapter(mAdapter);
    }

    public interface OnPopupMenuItemClick {
        void onItemClick(int position);
    }

    public static class PopupMenuAdapter extends RecyclerView.Adapter<PopupMenuAdapter.ItemHolder> {

        private Activity mActivity;
        private List<String> mList;
        private OnPopupMenuItemClick mListener;
        private OnPopupMenuItemClick mPopupListener;

        public PopupMenuAdapter(@NonNull Activity activity, @NonNull List<String> list,
                                @NonNull OnPopupMenuItemClick listener,
                                @NonNull OnPopupMenuItemClick popupistener) {
            this.mActivity = activity;
            this.mList = list;
            this.mListener = listener;
            this.mPopupListener = popupistener;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(mActivity);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.height = DimenUtils.INSTANCE.dp2px(mActivity, 60.0f);
            tv.setLayoutParams(params);
            tv.setTextColor(mActivity.getResources().getColor(R.color.textColor));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
            tv.setBackgroundColor(Color.TRANSPARENT);
            tv.setGravity(Gravity.CENTER);
            return new ItemHolder(tv);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, final int position) {
            holder.tv.setText(mList.get(position));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position);
                    mPopupListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public static class ItemHolder extends RecyclerView.ViewHolder {
            public TextView tv;

            public ItemHolder(TextView itemView) {
                super(itemView);
                tv = itemView;
            }
        }
    }

}
