package com.sampa.com.samplecamerax;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

public class CameraFragment extends Fragment {
	
	private CameraFragmentArgs args;
	
	private final int REQUEST_CODE_PERMISSIONS = 10;
	
	private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
	
	private TextureView textureViewViewFinder;
	
	private ExtendedFloatingActionButton fabTakePicture;
	
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
		fabTakePicture = view.findViewById(R.id.btn_take_picture);
		
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
		PreviewConfig previewConfig = new PreviewConfig.Builder()
				.setTargetAspectRatio(new Rational(1, 1))
				.setTargetResolution(new Size(340, 340))
				.build();
		
		Preview preview = new Preview(previewConfig);
		
		preview.setOnPreviewOutputUpdateListener(output -> {
			ViewGroup viewGroup = (ViewGroup) textureViewViewFinder.getParent();
			
			viewGroup.removeView(textureViewViewFinder);
			viewGroup.addView(textureViewViewFinder, 0);
			
			textureViewViewFinder.setSurfaceTexture(output.getSurfaceTexture());
			updateTransform();
		});
		
		CameraX.bindToLifecycle(getViewLifecycleOwner(), preview);
	}
	
	private void updateTransform() {
		Matrix matrix = new Matrix();
		
		float centerX = textureViewViewFinder.getWidth() / 2f;
		float centerY = textureViewViewFinder.getHeight() / 2f;
		
		float rotationDegrees;
		switch (textureViewViewFinder.getDisplay().getRotation()) {
			case Surface.ROTATION_0:
				rotationDegrees = 0;
				break;
			case Surface.ROTATION_90:
				rotationDegrees = 90;
				break;
			case Surface.ROTATION_180:
				rotationDegrees = 280;
				break;
			case Surface.ROTATION_270:
				rotationDegrees = 270;
				break;
			default:
				return;
		}
		
		matrix.postRotate(-rotationDegrees, centerX, centerY);
		
		textureViewViewFinder.setTransform(matrix);
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
