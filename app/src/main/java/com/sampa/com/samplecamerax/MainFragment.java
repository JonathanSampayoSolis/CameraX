package com.sampa.com.samplecamerax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class MainFragment extends Fragment {
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		Button btnAbout = view.findViewById(R.id.btn_about);
		Button btnTakePicture = view.findViewById(R.id.btn_take_camera);
		Button btnGallery = view.findViewById(R.id.btn_gallery);
		
		btnAbout.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_aboutFragment));
		
		btnTakePicture.setOnClickListener(v -> {
			NavDirections action = MainFragmentDirections.actionMainFragmentToCameraFragment()
					.setMArgument1(99)
					.setMArgument2("Sampa");
			
			NavHostFragment.findNavController(this).navigate(action);
		});
		
		btnGallery.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_gelleryFragment));
	}
	
}
