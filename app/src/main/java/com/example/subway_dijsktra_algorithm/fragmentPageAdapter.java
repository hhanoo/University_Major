package com.example.subway_dijsktra_algorithm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class fragmentPageAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> items = new ArrayList<Fragment>();
    private FragmentManager mFM;

    public fragmentPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mFM = fm;
    }

    //프래그먼트를 보여주는 처리를 구현한 곳
    @NonNull
    @Override
    public Fragment getItem(int position) { //불러올 fragment
        switch (position) {
            case 0:
                return fragment_shortest_time.newInstance();
            case 1:
                return fragment_least_transfer.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    //상단의 탭 레이아웃 인디케이터 쪽에 텍스트를 선언해주는 곳
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "최단 시간";
            case 1:
                return "최소 환승";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void addItem(Fragment item) {
        items.add(item);
    }

    public FragmentManager refresh() {
        return mFM;
    }
}
