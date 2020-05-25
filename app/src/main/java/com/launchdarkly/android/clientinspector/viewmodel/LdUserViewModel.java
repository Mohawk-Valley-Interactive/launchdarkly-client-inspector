package com.launchdarkly.android.clientinspector.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.launchdarkly.android.clientinspector.model.LdClientModel;

public class LdUserViewModel extends ViewModel {

    public LiveData<Boolean> isConnected;

    public MutableLiveData<String> userKey;
    public MutableLiveData<Boolean> userIsAnonymous;
    public MutableLiveData<Boolean> userEmailIsPrivate;
    public MutableLiveData<String> userEmailVal;
    public MutableLiveData<Boolean> userFirstNameIsPrivate;
    public MutableLiveData<String> userFirstNameVal;
    public MutableLiveData<Boolean> userLastNameIsPrivate;
    public MutableLiveData<String> userLastNameVal;
    public MutableLiveData<Boolean> userNameIsPrivate;
    public MutableLiveData<String> userNameVal;
    public MutableLiveData<Boolean> userCountryIsPrivate;
    public MutableLiveData<String> userCountryVal;
    public MutableLiveData<Boolean> userCustom1IsPrivate;
    public MutableLiveData<String> userCustom1Name;
    public MutableLiveData<String> userCustom1Val;
    public MutableLiveData<Boolean> userCustom2IsPrivate;
    public MutableLiveData<String> userCustom2Name;
    public MutableLiveData<String> userCustom2Val;
    public MutableLiveData<Boolean> userCustom3IsPrivate;
    public MutableLiveData<String> userCustom3Name;
    public MutableLiveData<String> userCustom3Val;

    public LdUserViewModel() {
        ldClientModel = LdClientModel.getInstance();

        isConnected = ldClientModel.isOnline;

        userKey = ldClientModel.userKey;
        userIsAnonymous = ldClientModel.userIsAnonymous;
        userEmailIsPrivate = ldClientModel.userEmailIsPrivate;
        userEmailVal = ldClientModel.userEmailVal;
        userFirstNameIsPrivate = ldClientModel.userFirstNameIsPrivate;
        userFirstNameVal = ldClientModel.userFirstNameVal;
        userLastNameIsPrivate = ldClientModel.userLastNameIsPrivate;
        userLastNameVal = ldClientModel.userLastNameVal;
        userNameIsPrivate = ldClientModel.userFirstNameIsPrivate;
        userNameVal = ldClientModel.userNameVal;
        userCountryIsPrivate = ldClientModel.userCountryIsPrivate;
        userCountryVal = ldClientModel.userCountryVal;
        userCustom1IsPrivate = ldClientModel.userCustom1IsPrivate;
        userCustom1Name = ldClientModel.userCustom1Name;
        userCustom1Val = ldClientModel.userCustom1Val;
        userCustom2IsPrivate = ldClientModel.userCustom2IsPrivate;
        userCustom2Name = ldClientModel.userCustom2Name;
        userCustom2Val = ldClientModel.userCustom2Val;
        userCustom3IsPrivate = ldClientModel.userCustom3IsPrivate;
        userCustom3Name = ldClientModel.userCustom3Name;
        userCustom3Val = ldClientModel.userCustom3Val;
    }

    public void updateUser() {
        ldClientModel.updateUser();
    }

    protected LdClientModel ldClientModel;
}
