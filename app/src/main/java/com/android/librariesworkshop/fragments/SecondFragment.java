package com.android.librariesworkshop.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.librariesworkshop.BusEvent;
import com.android.librariesworkshop.R;
import com.android.librariesworkshop.application.WorkshopApplication;
import com.squareup.otto.Subscribe;

public class SecondFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private int mParam1;

    private TextView plusOneView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SecondFragment.
     */
    public static SecondFragment newInstance(int param1) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

        WorkshopApplication.bus.register(this);
    }

    @Override
    public void onDestroy() {
        WorkshopApplication.bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        plusOneView = (TextView) view.findViewById(R.id.text_box);
        return view;
    }

    public void addOne() {
        plusOneView.setText(String.valueOf(++mParam1));
    }

    @Subscribe
    public void ottoAddOne(BusEvent.FragmentSampleEvent event) {
        plusOneView.setText(String.valueOf(++mParam1));
    }
}
