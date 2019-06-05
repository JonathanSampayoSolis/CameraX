package com.sampa.com.samplecamerax;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class CameraFragment extends Fragment {
	
	private CameraFragmentArgs args;
	
	private final int REQUEST_CODE_PERMISSIONS = 10;
	
	private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
	
	private TextureView textureViewViewFinder;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getArguments() != null)
			args = CameraFragmentArgs.fromBundle(getArguments());
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_camera, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		textureViewViewFinder = view.findViewById(R.id.view_finder);
		
		if (allPermissionsGranted()) {
			textureViewViewFinder.post(this::startCamera);
		} else {
			ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
		}
		
		textureViewViewFinder.addOnLayoutChangeListener((view1, i, i1, i2, i3, i4, i5, i6, i7) -> updateTransform());
		
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_CODE_PERMISSIONS) {
			if (allPermissionsGranted()) {
				textureViewViewFinder.post(this::startCamera);
			}
		}
	}
	
	// region:: PRIVATE METHODS
	
	private void startCamera() {
	
	}
	
	private void updateTransform() {
	
	}
	
	private boolean allPermissionsGranted() {
		for (String permission : REQUIRED_PERMISSIONS) {
			if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), permission)
					!= PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		
		return true;
	}
	
	// endregion
	
}
