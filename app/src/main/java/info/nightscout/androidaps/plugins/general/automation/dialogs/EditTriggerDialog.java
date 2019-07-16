package info.nightscout.androidaps.plugins.general.automation.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.R;
import info.nightscout.androidaps.plugins.general.automation.events.EventAutomationUpdateTrigger;
import info.nightscout.androidaps.plugins.general.automation.triggers.Trigger;

public class EditTriggerDialog extends DialogFragment {

    public interface OnClickListener {
        void onClick(Trigger newTriggerObject);
    }

    @BindView(R.id.layoutTrigger)
    LinearLayout mLayoutTrigger;

    private Trigger mTrigger;
    private Unbinder mUnbinder;

    public static EditTriggerDialog newInstance(Trigger trigger) {
        Bundle args = new Bundle();
        args.putString("trigger", trigger.toJSON());
        EditTriggerDialog fragment = new EditTriggerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.automation_dialog_edit_trigger, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        // load data from bundle
        Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
        if (bundle != null) {
            String triggerData = bundle.getString("trigger");
            if (triggerData != null) mTrigger = Trigger.instantiate(triggerData);
        }

        // display root trigger
        if (mTrigger != null) {
            mTrigger.generateDialog(mLayoutTrigger, getFragmentManager());
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.ok)
    public void onButtonOk(View unused) {
        dismiss();
        MainApp.bus().post(new EventAutomationUpdateTrigger(mTrigger));
    }

    @OnClick(R.id.cancel)
    public void onButtonCancel(View unused) {
        dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("trigger", mTrigger.toJSON());
    }
}
