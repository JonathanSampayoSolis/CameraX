package com.sampa.com.samplecamerax;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class CameraFragment extends Fragment {
	
	private static final String TAG = CameraFragment.class.getSimpleName().toUpperCase();
	
	private final int REQUEST_CODE_PERMISSIONS = 10;
	
	private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
	
	private TextureView textureViewViewFinder;
	
	private ExtendedFloatingActionButton fabTakePicture;
	
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
				.build();
		
		Preview preview = new Preview(previewConfig);
		
		preview.setOnPreviewOutputUpdateListener(output -> {
			ViewGroup viewGroup = (ViewGroup) textureViewViewFinder.getParent();
			
			viewGroup.removeView(textureViewViewFinder);
			viewGroup.addView(textureViewViewFinder, 0);
			
			textureViewViewFinder.setSurfaceTexture(output.getSurfaceTexture());
			updateTransform();
		});
		
		ImageCaptureConfig captureConfig = new ImageCaptureConfig.Builder()
				.setTargetAspectRatio(new Rational(1, 1))
				.setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
				.build();
		
		ImageCapture capture = new ImageCapture(captureConfig);
		
		fabTakePicture.setOnClickListener(v -> {
			File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
					"/".concat(getString(R.string.app_name)));
			
			if (!folder.exists())
				//noinspection ResultOfMethodCallIgnored
				folder.mkdir();
			
			File file = new File(folder, UUID.randomUUID().toString().concat(".jpg"));
			
			capture.takePicture(file, new ImageCapture.OnImageSavedListener() {
				@Override
				public void onImageSaved(@NonNull File file) {
					Toast.makeText(getContext(), "Picture saved successfully!", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
					Toast.makeText(getContext(), "Picture didn't save, check logs!", Toast.LENGTH_SHORT).show();
					
					Log.e(TAG, "UseCaseError: " + useCaseError.toString() +
							"\n message: " + message);
					
					if (cause != null)
						cause.printStackTrace();
					
				}
			});
		});
		
		CameraX.bindToLifecycle(getViewLifecycleOwner(), preview, capture);
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
