package com.example.royalroad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountSettingFragment extends Fragment
{
    Button BackBTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_account_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        BackBTN = view.findViewById(R.id.BackBTN);
        BackBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HomeActivity CurrentHome = (HomeActivity) getActivity();

                int ID = CurrentHome.HomePager.getCurrentItem();

                CurrentHome.vpAdapter.ReplaceFragment(ID, new BaseSettingsFragment());
                CurrentHome.vpAdapter.notifyItemChanged(ID);

                CurrentHome.HomePager.setAdapter(CurrentHome.vpAdapter);
                CurrentHome.HomePager.setCurrentItem(ID);
            }
        });
    }
}