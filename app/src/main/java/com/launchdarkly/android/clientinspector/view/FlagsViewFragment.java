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
import com.launchdarkly.android.clientinspector.databinding.FragmentFlagsBinding;
import com.launchdarkly.android.clientinspector.viewmodel.LdFlagsViewModel;

public class FlagsViewFragment extends Fragment {

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

        ldFlagsViewModel = viewModelProvider.get(LdFlagsViewModel.class);
        FragmentFlagsBinding fragmentFlagsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_flags, container, false);

        fragmentFlagsBinding.setLifecycleOwner(this.getViewLifecycleOwner());
        fragmentFlagsBinding.setFlagsViewModel(ldFlagsViewModel);

        return fragmentFlagsBinding.getRoot();
    }

    protected LdFlagsViewModel ldFlagsViewModel;
    protected ViewModelProvider viewModelProvider;
}
