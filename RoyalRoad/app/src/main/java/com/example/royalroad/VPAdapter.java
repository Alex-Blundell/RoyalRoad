package com.example.royalroad;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
public class VPAdapter extends FragmentStateAdapter
{
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public VPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        return fragmentList.get(position);
    }

    public void AddFragment(Fragment fragment)
    {
        fragmentList.add(fragment);
    }

    public void ReplaceFragment(int Position, Fragment fragment)
    {
        fragmentList.remove(Position);
        fragmentList.add(Position, fragment);
    }

    @Override
    public int getItemCount()
    {
        return fragmentList.size();
    }

    public Fragment GetFragment(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
