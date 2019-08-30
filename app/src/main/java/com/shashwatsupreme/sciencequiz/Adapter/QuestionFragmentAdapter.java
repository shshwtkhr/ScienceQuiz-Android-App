package com.shashwatsupreme.sciencequiz.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shashwatsupreme.sciencequiz.QuestionFragment;

import java.util.List;

public class QuestionFragmentAdapter extends FragmentPagerAdapter
{
    Context context;
    List<QuestionFragment> fragmentList;

    public QuestionFragmentAdapter(FragmentManager fm, Context context, List<QuestionFragment> fragmentList)
    {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i)
    {
        return fragmentList.get(i);
    }

    @Override
    public int getCount()
    {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return new StringBuilder("Question  ").append(position+1).toString();
    }
}
