/**
 * Copyright 2015 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cokus.compareViewpager.sample.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.StackPageTransformer;
import com.cokus.compareViewpager.sample.R;
import com.cokus.compareViewpager.sample.fragment.ColorFragment;
import com.bartoszlipinski.flippablestackview.utilities.ValueInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartosz Lipinski
 * 12.12.14
 */
public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_FRAGMENTS = 15;

    private FlippableStackView mFlippableStack;

    private Button left;
    private Button right;
    private Button close;

    private ColorFragmentAdapter mPageAdapter;

    private List<Fragment> mViewPagerFragments;

    public static int mCurrentPos =14;
    public static  final long CLOSE_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        createViewPagerFragments();
        mPageAdapter = new ColorFragmentAdapter(getSupportFragmentManager(), mViewPagerFragments);

        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        mFlippableStack = (FlippableStackView) findViewById(R.id.flippable_stack_view);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        close = (Button) findViewById(R.id.close);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ColorFragment)mPageAdapter.getFragments().get(mCurrentPos)).switchImageLeft();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ColorFragment)mPageAdapter.getFragments().get(mCurrentPos)).switchImageRight();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ColorFragment)mPageAdapter.getFragments().get(mCurrentPos)).closeAnim();
                mCurrentPos=--mCurrentPos>0?mCurrentPos:0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFlippableStack.setCurrentItem(mCurrentPos);
                    }
                },CLOSE_TIME);
            }
        });


        mFlippableStack.initStack(4, portrait ?
                StackPageTransformer.Orientation.VERTICAL :
                StackPageTransformer.Orientation.HORIZONTAL);
        mFlippableStack.setAdapter(mPageAdapter);
        mFlippableStack.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPos = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createViewPagerFragments() {
        mViewPagerFragments = new ArrayList<>();

        int startColor = getResources().getColor(R.color.emerald);
        int startR = Color.red(startColor);
        int startG = Color.green(startColor);
        int startB = Color.blue(startColor);

        int endColor = getResources().getColor(R.color.wisteria);
        int endR = Color.red(endColor);
        int endG = Color.green(endColor);
        int endB = Color.blue(endColor);

        ValueInterpolator interpolatorR = new ValueInterpolator(0, NUMBER_OF_FRAGMENTS - 1, endR, startR);
        ValueInterpolator interpolatorG = new ValueInterpolator(0, NUMBER_OF_FRAGMENTS - 1, endG, startG);
        ValueInterpolator interpolatorB = new ValueInterpolator(0, NUMBER_OF_FRAGMENTS - 1, endB, startB);

        for (int i = 0; i < NUMBER_OF_FRAGMENTS; ++i) {
            mViewPagerFragments.add(ColorFragment.newInstance(Color.argb(255, (int) interpolatorR.map(i), (int) interpolatorG.map(i), (int) interpolatorB.map(i)),i));
        }
    }

    private class ColorFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public List<Fragment> getFragments() {
            return fragments;
        }

        public ColorFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}
