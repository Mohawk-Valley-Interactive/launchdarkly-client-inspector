package com.launchdarkly.android.clientinspector.viewmodel;

import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.launchdarkly.android.clientinspector.R;
import com.launchdarkly.android.clientinspector.model.LdClientModel;

public class LdClientViewModel extends ViewModel {

    @BindingAdapter("isOnline")
    public static void isOnline(Button button, Boolean b) {
        String buttonText = b ? "Exit Inspector" : "Connect";
        button.setText(buttonText);
    }

    @BindingAdapter("isOnline")
    public static void isOnline(TextView textView, Boolean b) {
        textView.setTextColor(b ? Color.GREEN : Color.RED);
        textView.setText(b ? R.string.client_connection_status_online : R.string.client_connection_status_offline);
    }

    public LiveData<String> clientConnectionStatusMessage;
    public LiveData<Boolean> isOnline;
    public MutableLiveData<String> mobileKey;

    public LdClientViewModel() {
        ldClientModel = LdClientModel.getInstance();

        isOnline = ldClientModel.isOnline;
        mobileKey = ldClientModel.mobileKey;
    }

    public void onConnectionToggleClicked() {
        if (isOnline.getValue()) {
            ldClientModel.disconnectLdClientConnection();
        } else {
            ldClientModel.initializeLdClientConnection();
        }
    }

    protected LdClientModel ldClientModel;
}
