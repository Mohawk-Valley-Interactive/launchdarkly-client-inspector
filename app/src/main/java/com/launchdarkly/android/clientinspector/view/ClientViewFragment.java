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
import com.launchdarkly.android.clientinspector.databinding.FragmentClientBinding;
import com.launchdarkly.android.clientinspector.viewmodel.LdClientViewModel;

public class ClientViewFragment extends Fragment {

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

        ldClientViewModel = viewModelProvider.get(LdClientViewModel.class);
        FragmentClientBinding fragmentClientBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_client, container, false);

        fragmentClientBinding.setLifecycleOwner(this.getViewLifecycleOwner());
        fragmentClientBinding.setClientViewModel(ldClientViewModel);

        return fragmentClientBinding.getRoot();
    }

    protected LdClientViewModel ldClientViewModel;
    protected ViewModelProvider viewModelProvider;
}
