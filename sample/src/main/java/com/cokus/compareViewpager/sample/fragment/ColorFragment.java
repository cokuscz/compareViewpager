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

package com.cokus.compareViewpager.sample.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.cokus.compareViewpager.sample.R;
import com.cokus.compareViewpager.sample.activity.MainActivity;
import com.cokus.compareViewpager.sample.view.CurtainView;
import com.cokus.compareViewpager.sample.view.ICurtainViewBase;


/**
 * Created by Bartosz Lipinski
 * 28.01.15
 */
public class ColorFragment extends Fragment {

    private static final String EXTRA_COLOR = "com.bartoszlipinski.flippablestackview.fragment.ColorFragment.EXTRA_COLOR";

    FrameLayout mMainLayout;
    CurtainView mCurtainViewLeft;
    CurtainView mCurtainViewRight;
    int position;
    ImageView mImageViewLeft;
    ImageView mImageViewRight;

    public void setPosition(int position) {
        this.position = position;
    }

    public static ColorFragment newInstance(int backgroundColor,int position) {
        ColorFragment fragment = new ColorFragment();
        fragment.setPosition(position);
        Bundle bdl = new Bundle();
        bdl.putInt(EXTRA_COLOR, backgroundColor);
        fragment.setArguments(bdl);
        return fragment;
    }

    public void switchImageLeft(){
        if(mCurtainViewRight!=null&&mCurtainViewRight.getCurtainStatus()== ICurtainViewBase.CurtainStatus.OPENED){
            mCurtainViewRight.toggleStatus();
        }
        if(mCurtainViewLeft!=null){
            mCurtainViewLeft.bringToFront();
            mCurtainViewLeft.toggleStatus();
        }
    }

    public void switchImageRight(){
        if(mImageViewLeft!=null&&mCurtainViewLeft.getCurtainStatus()== ICurtainViewBase.CurtainStatus.OPENED){
            mCurtainViewLeft.toggleStatus();
        }
        if(mCurtainViewRight!=null){
            mCurtainViewRight.bringToFront();
            mCurtainViewRight.toggleStatus();
        }
    }

    public void closeAnim(){
        closeLeft();
        closeRight();

    }

    public void closeRight(){
        float curTranslationX = mImageViewRight.getTranslationX();
        // 获得当前按钮的位置
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImageViewRight, "translationX"
                , 0, 0
                ,curTranslationX,mCurtainViewRight.getFixedValue());
        // 表示的是:
        // 动画作用对象是mButton
        // 动画作用的对象的属性是X轴平移（在Y轴上平移同理，采用属性"translationY"
        // 动画效果是:从当前位置平移到 x=1500 再平移到初始位置
        animator.setDuration(MainActivity.CLOSE_TIME);
        animator.start();
    }

    public void closeLeft(){
        float curTranslationX =  mImageViewLeft.getTranslationX();
        // 获得当前按钮的位置
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImageViewLeft, "translationX"
                ,0,0, -curTranslationX
                ,-mCurtainViewRight.getFixedValue());
        // 表示的是:
        // 动画作用对象是mButton
        // 动画作用的对象的属性是X轴平移（在Y轴上平移同理，采用属性"translationY"
        // 动画效果是:从当前位置平移到 x=1500 再平移到初始位置
        animator.setDuration(MainActivity.CLOSE_TIME);
        animator.start();
    }


    public void initTouchEvent(){
        mCurtainViewLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(position == MainActivity.mCurrentPos
                        && mCurtainViewRight.getCurtainStatus()!= ICurtainViewBase.CurtainStatus.OPENED) {
                    mCurtainViewLeft.bringToFront();
                    return false;
                }
                else
                return  true;
            }
        });


        mCurtainViewRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(position == MainActivity.mCurrentPos && mCurtainViewLeft.getCurtainStatus()!= ICurtainViewBase.CurtainStatus.OPENED) {
                    mCurtainViewRight.bringToFront();
                    return false;
                }
                else
                    return  true;
            }
        });


        mCurtainViewLeft.setOnPullingListener(new ICurtainViewBase.OnPullingListener() {
            @Override
            public void onPulling(int rawStart, int diff, int x,ICurtainViewBase.CurtainGravity cGravity, ICurtainViewBase.CurtainStatus cStatus) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(x,
                        ViewGroup.LayoutParams.MATCH_PARENT);//两个400分别为添加图片的大小
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                mImageViewLeft.setLayoutParams(params);
            }
        });

        mCurtainViewLeft.setAutoScrollingListener(new ICurtainViewBase.AutoScrollingListener() {
            @Override
            public void onScrolling(int currValue, int currVelocity, int startValue, int finalValue) {
                int x = currValue;
                if(currValue<=0){
                    if(finalValue==0){
                        x= 2*mCurtainViewLeft.getFixedValue()+currValue;
                    }else
                    x = -finalValue + (-finalValue+currValue);
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(x,
                        ViewGroup.LayoutParams.MATCH_PARENT);//两个400分别为添加图片的大小
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                mImageViewLeft.setLayoutParams(params);
            }

            @Override
            public void onScrollFinished() {

            }
        });

        mCurtainViewRight.setOnPullingListener(new ICurtainViewBase.OnPullingListener() {
            @Override
            public void onPulling(int rawStart, int diff, int x,ICurtainViewBase.CurtainGravity cGravity, ICurtainViewBase.CurtainStatus cStatus) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(x,
                        ViewGroup.LayoutParams.MATCH_PARENT);//两个400分别为添加图片的大小
//                params.gravity=Gravity.CENTER;
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

                mImageViewRight.setLayoutParams(params);
            }
        });

        mCurtainViewRight.setAutoScrollingListener(new ICurtainViewBase.AutoScrollingListener() {
            @Override
            public void onScrolling(int currValue, int currVelocity, int startValue, int finalValue) {
                int x = currValue;

                    if(finalValue==0) {
                        if(mCurtainViewRight.getCurtainStatus() == ICurtainViewBase.CurtainStatus.OPENED)
                            x=2*mCurtainViewRight.getFixedValue() - currValue;
                        else
                        x = mCurtainViewRight.getFixedValue() - currValue;
                    }else{
                        if(mCurtainViewRight.getCurtainStatus() == ICurtainViewBase.CurtainStatus.OPENED)
                            x= 2*mCurtainViewRight.getFixedValue()- currValue;
                        else{
                            x = currValue;
                        }
                    }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(x,
                        ViewGroup.LayoutParams.MATCH_PARENT);//两个400分别为添加图片的大小
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

                mImageViewRight.setLayoutParams(params);
            }

            @Override
            public void onScrollFinished() {

            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dummy, container, false);
        Bundle bdl = getArguments();

        mMainLayout = (FrameLayout) v.findViewById(R.id.main_layout);
        mImageViewLeft = (ImageView) v.findViewById(R.id.img_left);
        mImageViewRight = (ImageView) v.findViewById(R.id.img_right);

//        LayerDrawable bgDrawable = (LayerDrawable) mMainLayout.getBackground();
//        GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.background_shape);
//        shape.setColor(bdl.getInt(EXTRA_COLOR));

        mCurtainViewRight = (CurtainView) v.findViewById(R.id.curtainview_right);
        setLayoutGravity(Gravity.RIGHT,mCurtainViewRight);
        ICurtainViewBase.CurtainGravity cGravity;
        cGravity = ICurtainViewBase.CurtainGravity.RIGHT;
        mCurtainViewRight.setCurtainGravityAndFixedValue(cGravity, mCurtainViewRight.getWidth()/2);

        mCurtainViewLeft = (CurtainView) v.findViewById(R.id.curtainview_left);
        setLayoutGravity(Gravity.LEFT,mCurtainViewLeft);
        ICurtainViewBase.CurtainGravity cGravityLeft;
        cGravityLeft = ICurtainViewBase.CurtainGravity.LEFT;
        mCurtainViewLeft.setCurtainGravityAndFixedValue(cGravityLeft, mCurtainViewLeft.getWidth()/2);

       initTouchEvent();

        return v;
    }

    private void setLayoutGravity(int layoutGravity,CurtainView curtainView) {
        /**
         * CurtainView can not just used in FrameLayout,but also in LinearLayout
         * or RelativeLayout whose LayoutParams is subclass of
         * MarginLayoutParams. When CurtainGravity is set to be RIGHT or BOTTOM
         * ,be sure that the CurtainView is actually RIGHT or BOTTOM to it's
         * parent. eg:when you use CurtainView under RelativeLayout and the
         * CurtainGravity is BOTTOM, you should set the attribute:
         * android:layout_alignParentBottom="true" In this case, CurtainView's
         * parent is FrameLayout,we do like this when we change the
         * CurtainGravity.
         */
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) curtainView.getLayoutParams();
        layoutParams.gravity = layoutGravity;
        curtainView.setLayoutParams(layoutParams);
    }


}
