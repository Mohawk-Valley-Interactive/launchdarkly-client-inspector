package com.launchdarkly.android.clientinspector.model;


import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.launchdarkly.android.ConnectionInformation;
import com.launchdarkly.android.LDAllFlagsListener;
import com.launchdarkly.android.LDClient;
import com.launchdarkly.android.LDConfig;
import com.launchdarkly.android.LDFailure;
import com.launchdarkly.android.LDStatusListener;
import com.launchdarkly.android.LDUser;
import com.launchdarkly.android.clientinspector.MainActivity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class LdClientModel {

    // Static
    public static LdClientModel getInstance() {
        if (instance == null) {
            instance = new LdClientModel();
        }

        return instance;
    }

    public static void setDefaultMobileKey(String mobileKey) {
        defaultMobileKey = mobileKey;
    }

    public static void registerApplication(Application a) {
        application = a;
    }

    public static void registerActivity(MainActivity a) {
        activity = a;
    }

    private static Activity activity = null;
    private static LdClientModel instance = null;
    private static Application application = null;
    private static String defaultMobileKey = "";

    // Non-static
    protected LDClient ldClient = null;

    public MutableLiveData<Map<String, ?>> flagList = new MutableLiveData<>();
    public MutableLiveData<String> lastUpdated = new MutableLiveData<>("NOT UPDATED YET");
    public MutableLiveData<Integer> secondsTimeout = new MutableLiveData<>(5);
    public MutableLiveData<Boolean> isOnline = new MutableLiveData<>(false);
    public MutableLiveData<String> lastConnectedOrigin = new MutableLiveData<>("CONNECTION NEVER INITIATED");
    public MutableLiveData<String> mobileKey = new MutableLiveData<>("");
    public MutableLiveData<String> statusMessage = new MutableLiveData<>("...");
    public MutableLiveData<String> userKey = new MutableLiveData<>("default-user-key");
    public MutableLiveData<Boolean> userIsAnonymous = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> userEmailIsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userEmailVal = new MutableLiveData<>("");
    public MutableLiveData<Boolean> userFirstNameIsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userFirstNameVal = new MutableLiveData<>("");
    public MutableLiveData<Boolean> userLastNameIsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userLastNameVal = new MutableLiveData<>("");
    public MutableLiveData<Boolean> userNameIsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userNameVal = new MutableLiveData<>("");
    public MutableLiveData<Boolean> userCountryIsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userCountryVal = new MutableLiveData<>("");
    public MutableLiveData<Boolean> userCustom1IsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userCustom1Name = new MutableLiveData<>("");
    public MutableLiveData<String> userCustom1Val = new MutableLiveData<>("");
    public MutableLiveData<Boolean> userCustom2IsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userCustom2Name = new MutableLiveData<>("");
    public MutableLiveData<String> userCustom2Val = new MutableLiveData<>("");
    public MutableLiveData<Boolean> userCustom3IsPrivate = new MutableLiveData<>(false);
    public MutableLiveData<String> userCustom3Name = new MutableLiveData<>("");
    public MutableLiveData<String> userCustom3Val = new MutableLiveData<>("");

    private LdClientModel() {
        mobileKey.postValue(defaultMobileKey);
    }

    public void updateUser() {
        LDUser user = getUser();
        ldClient.identify(user);
    }

    public void disconnectLdClientConnection() {
        if (ldClient != null && ldClient.isInitialized()) {
            try {
                ldClient.close();
                lastConnectedOrigin.postValue(lastConnectedOrigin.getValue() + " (DISCONNECTED)");
                isOnline.postValue(false);
                ldClient = null;

                activity.finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());

            } catch (IOException e) {
                Log.e("LaunchDarklyClientManager", "Failed to close the client with the following error: " + e.getMessage());
            }
        }
    }

    public void initializeLdClientConnection() {
        if (application == null) {
            Log.e("LaunchDarklyClientManager", "Application not registered. Please remember to register application before calling init.");
            return;
        }

        if (ldClient != null && ldClient.isInitialized()) {
            Log.w("LaunchDarklyClientManager", "Initialize called on initialized client; closing existing connection before re-initializing.");
            try {
                ldClient.close();
                isOnline.postValue(false);
            } catch (IOException e) {
                Log.e("LaunchDarklyClientManager", "Failed to close ldClient during initialization (" + e.getLocalizedMessage() + "). Aborting initialization.");
                e.printStackTrace();
            }
        }

        ldClient = LDClient.init(application, getConfig(), getUser(), secondsTimeout.getValue());
        ldClient.registerStatusListener(new LDStatusListener() {
            @Override
            public void onConnectionModeChanged(ConnectionInformation connectionInformation) {
                statusMessage.postValue("Connection changed: " + connectionInformation.getConnectionMode().name());
            }

            @Override
            public void onInternalFailure(LDFailure ldFailure) {
                statusMessage.postValue("Connection failed: " + ldFailure.getMessage());
                isOnline.postValue(false);
            }
        });

        if (ldClient.isInitialized()) {
            ldClient.registerAllFlagsListener(new LDAllFlagsListener() {
                @Override
                public void onChange(List<String> flagKey) {
                    flagList.postValue(ldClient.allFlags());
                    lastUpdated.postValue(new Timestamp(System.currentTimeMillis()).toString());
                }
            });
            isOnline.postValue(true);
            flagList.postValue(ldClient.allFlags());
            lastConnectedOrigin.postValue(mobileKey.getValue());
            lastUpdated.postValue(new Timestamp(System.currentTimeMillis()).toString());
            statusMessage.postValue("Client initialized.");
        } else {
            isOnline.postValue(false);
            statusMessage.postValue("Initialization failed.");
        }
    }

    private LDConfig getConfig() {
        Log.i("LaunchDarklyClientManager", "Connecting to: " + mobileKey.getValue());
        return new LDConfig.Builder().setMobileKey(mobileKey.getValue()).build();
    }

    private LDUser getUser() {
        LDUser.Builder ldUserBuilder = new LDUser.Builder(userKey.getValue());

        ldUserBuilder.anonymous(userIsAnonymous.getValue());

        String value = userEmailVal.getValue();
        if (!value.isEmpty()) {
            if (userEmailIsPrivate.getValue()) {
                ldUserBuilder.privateEmail(value);
            } else {
                ldUserBuilder.email(value);
            }
        }

        value = userFirstNameVal.getValue();
        if (!value.isEmpty()) {
            if (userFirstNameIsPrivate.getValue()) {
                ldUserBuilder.privateFirstName(value);
            } else {
                ldUserBuilder.firstName(value);
            }
        }

        value = userLastNameVal.getValue();
        if (!value.isEmpty()) {
            if (userLastNameIsPrivate.getValue()) {
                ldUserBuilder.privateLastName(value);
            } else {
                ldUserBuilder.lastName(value);
            }
        }

        value = userNameVal.getValue();
        if (!value.isEmpty()) {
            if (userNameIsPrivate.getValue()) {
                ldUserBuilder.privateName(value);
            } else {
                ldUserBuilder.name(value);
            }
        }

        value = userCountryVal.getValue();
        if (!value.isEmpty()) {
            if (userCountryIsPrivate.getValue()) {
                ldUserBuilder.privateCountry(value);
            } else {
                ldUserBuilder.country(value);
            }
        }

        String name = userCustom1Name.getValue();
        value = userCustom1Val.getValue();
        boolean isPrivate = userCustom1IsPrivate.getValue();
        if (!name.isEmpty() && !value.isEmpty()) {
            if (isBoolean(value)) {
                boolean asBoolean = Boolean.parseBoolean(value);
                if (isPrivate) {
                    ldUserBuilder.privateCustom(name, asBoolean);
                } else {
                    ldUserBuilder.custom(name, asBoolean);
                }
            } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                if (value.contains(".") || value.contains("-")) {
                    double asDouble = Double.parseDouble(value);
                    if (isPrivate) {
                        ldUserBuilder.privateCustom(name, asDouble);
                    } else {
                        ldUserBuilder.custom(name, asDouble);
                    }
                } else {
                    long asLong = Long.parseLong(value);
                    if (isPrivate) {
                        ldUserBuilder.privateCustom(name, asLong);
                    } else {
                        ldUserBuilder.custom(name, asLong);
                    }
                }
            } else {
                if (isPrivate) {
                    ldUserBuilder.privateCustom(name, value);
                } else {
                    ldUserBuilder.custom(name, value);
                }
            }
        }

        name = userCustom2Name.getValue();
        value = userCustom2Val.getValue();
        isPrivate = userCustom2IsPrivate.getValue();
        if (!name.isEmpty() && !value.isEmpty()) {
            if (isBoolean(value)) {
                boolean asBoolean = Boolean.parseBoolean(value);
                if (isPrivate) {
                    ldUserBuilder.privateCustom(name, asBoolean);
                } else {
                    ldUserBuilder.custom(name, asBoolean);
                }
            } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                if (value.contains(".") || value.contains("-")) {
                    double asDouble = Double.parseDouble(value);
                    if (isPrivate) {
                        ldUserBuilder.privateCustom(name, asDouble);
                    } else {
                        ldUserBuilder.custom(name, asDouble);
                    }
                } else {
                    long asLong = Long.parseLong(value);
                    if (isPrivate) {
                        ldUserBuilder.privateCustom(name, asLong);
                    } else {
                        ldUserBuilder.custom(name, asLong);
                    }
                }
            } else {
                if (isPrivate) {
                    ldUserBuilder.privateCustom(name, value);
                } else {
                    ldUserBuilder.custom(name, value);
                }
            }
        }

        name = userCustom3Name.getValue();
        value = userCustom3Val.getValue();
        isPrivate = userCustom3IsPrivate.getValue();
        if (!name.isEmpty() && !value.isEmpty()) {
            if (isBoolean(value)) {
                boolean asBoolean = Boolean.parseBoolean(value);
                if (isPrivate) {
                    ldUserBuilder.privateCustom(name, asBoolean);
                } else {
                    ldUserBuilder.custom(name, asBoolean);
                }
            } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                if (value.contains(".") || value.contains("-")) {
                    double asDouble = Double.parseDouble(value);
                    if (isPrivate) {
                        ldUserBuilder.privateCustom(name, asDouble);
                    } else {
                        ldUserBuilder.custom(name, asDouble);
                    }
                } else {
                    long asLong = Long.parseLong(value);
                    if (isPrivate) {
                        ldUserBuilder.privateCustom(name, asLong);
                    } else {
                        ldUserBuilder.custom(name, asLong);
                    }
                }
            } else {
                if (isPrivate) {
                    ldUserBuilder.privateCustom(name, value);
                } else {
                    ldUserBuilder.custom(name, value);
                }
            }
        }

        return ldUserBuilder.build();
    }

    private boolean isBoolean(String value) {
        String asLower = value.toLowerCase();
        if (asLower.contains("t") || asLower.contains("f") || asLower.equals("true") || asLower.equals("false")) {
            return true;
        }
        return false;
    }

}
