package com.launchdarkly.android.clientinspector.viewmodel;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @BindingAdapter("exampleButtonVisibility")
    public static void setExampleButtonVisibility(Button button, Boolean b) {
        button.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    public LiveData<Boolean> isClientOnline;
    public LiveData<Boolean> isExampleButtonVisible;
    public LiveData<Color> headingColor;
    public MutableLiveData<String> exampleButtonText;
    public MutableLiveData<String> exampleDescriptionText;
    public MutableLiveData<String> mobileKey;

    public LdClientViewModel() {
        ldClientModel = LdClientModel.getInstance();

        isClientOnline = ldClientModel.isOnline;
        mobileKey = ldClientModel.mobileKey;

        exampleButtonText = ldClientModel.exampleButtonText;
        exampleDescriptionText = ldClientModel.exampleDescriptionText;
        isExampleButtonVisible = ldClientModel.exampleShowButton;
    }

    public void onConnectionToggleClicked() {
        if (isClientOnline.getValue()) {
            ldClientModel.disconnectLdClientConnection();
        } else {
            ldClientModel.initializeLdClientConnection();
        }
    }

    public void onExampleButtonClicked(View view) {
        Resources res = view.getResources();
        String message = res.getString(R.string.client_example_button_toast_text );
        Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
    }

    protected LdClientModel ldClientModel;
}
