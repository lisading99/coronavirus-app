package com.example.coronavirusandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 */
public class AlertDialogFragment extends DialogFragment {

    public AlertDialogFragment() {
        super();
        // Required empty public constructor
    }

public static AlertDialogFragment newInstance(String title, String message) {
    AlertDialogFragment frag = new AlertDialogFragment();
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("message", message);
    frag.setArguments(args);
    return frag;
}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                return;
                            }
                        }
                )
                .create();
    }
}
