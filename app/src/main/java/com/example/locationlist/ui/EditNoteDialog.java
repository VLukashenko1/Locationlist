package com.example.locationlist.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.locationlist.R;
import com.example.locationlist.util.PointWithDistance;
import com.example.locationlist.vm.EditNoteDialogViewModel;
import com.squareup.picasso.Picasso;

public class EditNoteDialog extends DialogFragment {
    PointWithDistance pointWithDistance;
    private EditNoteDialogViewModel editNoteDialogViewModel;
    public EditNoteDialog(PointWithDistance pointWithDistance) {
        this.pointWithDistance = pointWithDistance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        editNoteDialogViewModel = new ViewModelProvider(this).get(EditNoteDialogViewModel.class);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_note_edit, null);

        EditText editText = dialogView.findViewById(R.id.dialogNote);
        editText.setText(pointWithDistance.point.getNote());

        TextView textView = dialogView.findViewById(R.id.dialogDestinationTV);
        String fullString = "Distance to this point: ";
        int start = fullString.toCharArray().length;
        fullString = fullString + " " + pointWithDistance.distanceString;
        int end = fullString.toCharArray().length;
        SpannableString spannableString = new SpannableString(fullString);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);

        ImageView imageView = dialogView.findViewById(R.id.dialogPointIcon);
        Picasso.get().load(pointWithDistance.point.getPhotoLink()).into(imageView);

        builder.setView(dialogView)
                .setTitle("Notate editor")
                .setPositiveButton("OK", (dialog, which) -> checkInput(editText))
                .setNegativeButton("Cancel", (dialog, which) -> {
                });

        return builder.create();
    }

    void checkInput(EditText editText){
        if (editText != null && editText.getText() != null){
            pointWithDistance.point.setNote(editText.getText().toString());
            editNoteDialogViewModel.updatePointNote(pointWithDistance.point);
        }
    }
}
