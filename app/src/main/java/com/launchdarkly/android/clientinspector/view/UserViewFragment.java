package com.launchdarkly.android.clientinspector.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.launchdarkly.android.clientinspector.R;
import com.launchdarkly.android.clientinspector.databinding.FragmentUserBinding;
import com.launchdarkly.android.clientinspector.viewmodel.LdUserViewModel;

public class UserViewFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModelProvider = new ViewModelProvider(getActivity());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        ldUserViewModel = viewModelProvider.get(LdUserViewModel.class);
        FragmentUserBinding fragmentUserBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);

        fragmentUserBinding.setLifecycleOwner(this.getViewLifecycleOwner());
        fragmentUserBinding.setUserViewModel(ldUserViewModel);

        return fragmentUserBinding.getRoot();
    }

    protected LdUserViewModel ldUserViewModel;
    protected ViewModelProvider viewModelProvider;
}
