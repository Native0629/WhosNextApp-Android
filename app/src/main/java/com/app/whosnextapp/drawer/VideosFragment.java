package com.app.whosnextapp.drawer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.app.whosnextapp.R;
import com.app.whosnextapp.loginregistration.VideoTrimmerActivity;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.knowledge4.videotrimmer.utils.FileUtils;

import static android.app.Activity.RESULT_OK;

public class VideosFragment extends Fragment {

    @BindView(R.id.fab_upload)
    FloatingActionButton fab_upload;

    Globals globals;
    private HomePageActivity homePageActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homePageActivity = (HomePageActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, v);
        globals = (Globals) getActivity().getApplicationContext();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_page_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.header_bell);
        View actionView = MenuItemCompat.getActionView(menuItem);
        // tv_noitfication_count = actionView.findViewById(R.id.tv_noitfication_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.header_bell).setVisible(true);
        menu.findItem(R.id.header_refresh).setVisible(false);
        menu.findItem(R.id.header_search).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.header_bell) {
            startActivity(new Intent(homePageActivity, NotificationActivity.class));
        }
        return false;
    }

    @OnClick(R.id.fab_upload)
    public void uploadMedia() {
        videoTypeSelectionDialog();
    }

    private void videoTypeSelectionDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.group_video), getResources().getString(R.string.text_feed_post)};
        globals.show_dialog(getContext(), getString(R.string.select_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        getPermissionForGroupVideo();
                        break;
                    case 1:
                        getPermissionForVideo();
                        break;
                }
            }
        });
    }

    private void getPermissionForGroupVideo() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                groupVideoSelectionDialog();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Globals.showToast(homePageActivity, getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(homePageActivity)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_camera_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void groupVideoSelectionDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_capture_video), getResources().getString(R.string.text_pick_video)};
        globals.show_dialog(getContext(), getString(R.string.select_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraForGroupVideo();
                        break;
                    case 1:
                        openGalleryForGroupVideo();
                        break;
                }
            }
        });
    }

    private void openCameraForGroupVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.WN_BIO_VIDEO_DURATION);
        startActivityForResult(intent, Constants.WN_REQUEST_CAMERA_GROUP);
    }

    private void openGalleryForGroupVideo() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize(Constants.WN_MEDIA_TYPE_VIDEO);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), Constants.WN_REQUEST_VIDEO_TRIMMER_GROUP);
    }

    private void getPermissionForVideo() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                videoSelectionDialog();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Globals.showToast(homePageActivity, getString(R.string.permission_denied) + deniedPermissions.toString());
            }
        };
        TedPermission.with(homePageActivity)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.request_camera_permission))
                .setDeniedMessage(getString(R.string.on_denied_permission))
                .setGotoSettingButtonText(getString(R.string.setting))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void videoSelectionDialog() {
        CharSequence[] options = new CharSequence[]{getResources().getString(R.string.text_capture_video), getResources().getString(R.string.text_pick_video)};
        globals.show_dialog(getContext(), getString(R.string.select_video), "", options, true, new Globals.OptionDialogClickHanderListener() {
            @Override
            public void OnItemClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraForVideo();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize(Constants.WN_MEDIA_TYPE_VIDEO);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), Constants.WN_REQUEST_VIDEO_TRIMMER);
    }

    private void openCameraForVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.WN_VIDEOS_DURATION);
        startActivityForResult(intent, Constants.WN_REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.WN_REQUEST_CAMERA) {
                if (data != null) {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri != null) {
                        startTrimActivity(selectedMediaUri);
                    } else {
                        Globals.showToast(homePageActivity, getString(R.string.toast_cannot_retrieve_selected_video));
                    }
                }
            }

            if (requestCode == Constants.WN_REQUEST_VIDEO_TRIMMER) {
                assert data != null;
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startTrimActivity(selectedUri);
                } else {
                    Globals.showToast(homePageActivity, getString(R.string.toast_cannot_retrieve_selected_video));
                }
            }

            if (requestCode == Constants.WN_REQUEST_CAMERA_GROUP) {
                if (data != null) {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri != null) {
                        startGroupVideoTrimActivity(selectedMediaUri);
                    } else {
                        Globals.showToast(homePageActivity, getString(R.string.toast_cannot_retrieve_selected_video));
                    }
                }
            }

            if (requestCode == Constants.WN_REQUEST_VIDEO_TRIMMER_GROUP) {
                if (data != null) {
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startGroupVideoTrimActivity(selectedUri);
                    } else {
                        Globals.showToast(homePageActivity, getString(R.string.toast_cannot_retrieve_selected_video));
                    }
                }
            }

        }
    }

    private void startGroupVideoTrimActivity(Uri selectedMediaUri) {
        startActivityForResult(new Intent(homePageActivity, VideoTrimmerActivity.class)
                .putExtra(Constants.WN_EXTRA_VIDEO_PATH, FileUtils.getPath(homePageActivity, selectedMediaUri))
                .putExtra(Constants.WN_REQUEST_CODE, Constants.WN_VIDEO_TRIM_GROUP)
                .putExtra(Constants.WN_VIDEO_DURATION, Constants.WN_BIO_VIDEO_DURATION), Constants.WN_VIDEO_TRIM);
        homePageActivity.overridePendingTransition(0, 0);
    }

    private void startTrimActivity(@NonNull Uri uri) {
        startActivityForResult(new Intent(homePageActivity, VideoTrimmerActivity.class)
                .putExtra(Constants.WN_EXTRA_VIDEO_PATH, FileUtils.getPath(homePageActivity, uri))
                .putExtra(Constants.WN_REQUEST_CODE, Constants.WN_VIDEO_TRIM)
                .putExtra(Constants.WN_VIDEO_DURATION, Constants.WN_VIDEOS_DURATION), Constants.WN_VIDEO_TRIM);
        homePageActivity.overridePendingTransition(0, 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homePageActivity = null;
    }
}
