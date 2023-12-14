package com.example.android_project;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.List;

public class Schedulelist extends Fragment {

    private static final String KEY = "shows";

    public static Schedulelist newInstance(List<Show> shows) {
        Schedulelist fragment = new Schedulelist();

        Bundle args = new Bundle();
        Log.d("CalendarActivity", "Passed shows: ");
        args.putSerializable(KEY, (Serializable) shows);
        fragment.setArguments(args);

        Bundle arg = fragment.getArguments();
        if (arg != null) {
            List<Show> passedShows = (List<Show>) arg.getSerializable(Schedulelist.KEY);
            // 여기서 passedShows를 검사하여 올바르게 전달되었는지 확인할 수 있습니다.
            Log.d("CalendarActivity", "Passed shows: " + passedShows);
        }
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list, container, false);

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        List<Show> shows = (List<Show>) getArguments().getSerializable(KEY);
        for (Show show : shows) {
            CardView cardView = new CardView(getContext());
            TextView title = new TextView(getContext());
            title.setText(show.title);
            cardView.addView(title);
            linearLayout.addView(cardView);
        }

        return view;
    }
}
