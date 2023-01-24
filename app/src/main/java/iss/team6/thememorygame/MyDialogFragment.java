package iss.team6.thememorygame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import java.util.Map;

import kotlinx.coroutines.MainCoroutineDispatcher;

public class MyDialogFragment extends DialogFragment {
    private static MediaPlayerTool mp = null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        Button button = (Button) view.findViewById(R.id.dialog_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mp != null) {
                        mp.stop();
                        mp = null;
                    }
                    mp = new MediaPlayerTool(getActivity(), R.raw.m2043);
                } catch (Exception e) {}
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                Map<Integer, String> hashSet = ((MainActivity)getActivity()).getSaveImgMap();
                String[] imgPathsArr = new String[hashSet.size()];
                int index = 0;
                for(String path: hashSet.values())
                {
                    imgPathsArr[index++] = path;
                }
                intent.putExtra("ImgPaths", imgPathsArr);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}
