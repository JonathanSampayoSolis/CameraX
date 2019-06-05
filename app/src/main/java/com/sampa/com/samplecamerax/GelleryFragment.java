package com.sampa.com.samplecamerax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GelleryFragment extends Fragment {
	
	public static GelleryFragment newInstance() {
		
		Bundle args = new Bundle();
		
		GelleryFragment fragment = new GelleryFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_gallery, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
	
	}
}
