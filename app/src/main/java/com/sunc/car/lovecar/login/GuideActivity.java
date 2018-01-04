package com.sunc.car.lovecar.login;

import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sunc.base.BaseBindingActivity;
import com.sunc.car.lovecar.R;
import com.sunc.car.lovecar.databinding.ViewRecyclerBinding;
import com.sunc.utils.SerializeUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class GuideActivity extends BaseBindingActivity<ViewRecyclerBinding> {
    public static final String PARAM_TITLE = "PARAM_TITLE";
    public static final String PARAM_FILE = "PARAM_FILE";

    @NotNull
    @Override
    public ViewRecyclerBinding createDataBinding(@Nullable Bundle savedInstanceState) {
        return DataBindingUtil.setContentView(this, R.layout.view_recycler);
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String title = bundle.getString(PARAM_TITLE);
        String json = bundle.getString(PARAM_FILE);
        View titleBar = findViewById(R.id.layout_title);
        titleBar.setVisibility(View.VISIBLE);
        TextView headTitle = titleBar.findViewById(R.id.head_title);
        headTitle.setText(title);
        View headBack = titleBar.findViewById(R.id.head_back);
        headBack.setVisibility(View.VISIBLE);
        headBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        try{
            AssetManager assetManager = getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(json)));
            String line;
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        String str = stringBuilder.toString();
        List<Guide> data = SerializeUtil.getInstance().deserialize(str, new TypeReference<List<Guide>>() {});
        List<GuideItem> items = new ArrayList<>();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                Guide guide = data.get(i);
                items.add(new GuideItem(guide.getTitle(), GuideItem.Companion.getTYPE_TITLE()));
                List<String> contents = guide.getContents();
                for (int j = 0; j < contents.size(); j++) {
                    items.add(new GuideItem(contents.get(j), GuideItem.Companion.getTYPE_CONTENT()));
                }
            }
        }
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBinding.recyclerView.setAdapter(new GuideAdapter(items));
    }
}
