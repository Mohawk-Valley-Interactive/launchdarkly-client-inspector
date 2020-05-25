package com.launchdarkly.android.clientinspector.viewmodel;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.launchdarkly.android.clientinspector.model.LdClientModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LdFlagsViewModel extends ViewModel {
    public LiveData<Map<String, ?>> flagList;
    public LiveData<String> origin;
    public LiveData<String> lastUpdated;

    @BindingAdapter("flags_origin")
    public static void onFlagOrigin(TextView textView, String originKey) {
        textView.setText("Flags as fetched from: " + originKey);
    }

    @BindingAdapter("flags_changed")
    public static void onFlagsChanged(TableLayout table, Map<String, ?> flags) {
        table.removeAllViews();

        if (flags != null) {
            ArrayList<String> keys = new ArrayList<>(flags.keySet());
            Collections.sort(keys);

            for (String key : keys) {
                TableRow row = new TableRow(table.getContext());
                TextView text1 = new TextView(table.getContext());
                TextView text2 = new TextView(table.getContext());

                text1.setText(key + ": ");
                text2.setText(flags.get(key).toString());

                row.addView(text1);
                row.addView(text2);
                table.addView(row);
            }
        }
    }

    public LdFlagsViewModel() {
        ldClientModel = LdClientModel.getInstance();
        flagList = ldClientModel.flagList;
        lastUpdated = ldClientModel.lastUpdated;
        origin = ldClientModel.lastConnectedOrigin;
    }

    protected LdClientModel ldClientModel;
}
