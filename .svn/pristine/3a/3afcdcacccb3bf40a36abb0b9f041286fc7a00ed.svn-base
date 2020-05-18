package com.app.whosnextapp.pictures;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.PostRequest;
import com.app.whosnextapp.apis.ResponseListener;
import com.app.whosnextapp.drawer.HomePageActivity;
import com.app.whosnextapp.model.AllPicturesModel;
import com.app.whosnextapp.notification.NotificationActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class TabPicturesFragment extends Fragment implements PicturesAdapter.OnItemClick {

    @BindView(R.id.rv_pictures)
    RecyclerView rv_Pictures;
    @BindView(R.id.tv_no_feed_found)
    AppCompatTextView tv_no_feed_found;

    PicturesAdapter picturesAdapter;
    Globals globals;
    boolean isLoaderRequire = true;
    AllPicturesModel allPicturesModel;
    private ArrayList<AllPicturesModel.PostList> ImageList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_pictures, container, false);
        ButterKnife.bind(this, v);
        if (getActivity() != null)
            globals = (Globals) getActivity().getApplicationContext();
        init();
        return v;
    }

    @OnClick(R.id.fab)
    public void onUploadClick() {
        getPermissionForPost();
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
        menu.findItem(R.id.header_refresh).setVisible(true);
        menu.findItem(R.id.header_search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.header_refresh:
                getAllImages();
                break;
            case R.id.header_bell:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
        }
        return false;
    }


    private void init() {
        globals = (Globals) getActivity().getApplicationContext();
        getAllImages();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoaderRequire = false;
        getAllImages();

    }

    private void getPermissionForPost() {
        if (getActivity() != null) {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    showDialog();
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Globals.showToast(getContext(), getString(R.string.permission_denied) + deniedPermissions.toString());
                }
            };
            TedPermission.with(getActivity())
                    .setPermissionListener(permissionlistener)
                    .setRationaleMessage(getString(R.string.request_camera_permission))
                    .setDeniedMessage(getString(R.string.on_denied_permission))
                    .setGotoSettingButtonText(getString(R.string.setting))
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();
        }
    }

    private void showDialog() {
        if (getActivity() != null) {
            CharSequence[] options = new CharSequence[]{getResources().getString(R.string.take_from_camera), getResources().getString(R.string.select_from_library)};
            globals.show_dialog(getContext(), getString(R.string.text_browse_image), "", options, true, new Globals.OptionDialogClickHanderListener() {
                @Override
                public void OnItemClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            openCamera();
                            break;
                        case 1:
                            openGallery();
                            break;
                    }
                }
            });
        }
    }

    private void openCamera() {
        EasyImage.openCameraForImage(TabPicturesFragment.this, 0);
    }

    private void openGallery() {
        EasyImage.openGallery(TabPicturesFragment.this, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (!imageFiles.isEmpty()) {
                    startActivity(new Intent(getContext(), ShareToPictureActivity.class)
                            .putExtra(Constants.WN_PICTURE_PATH, imageFiles.get(0).getAbsolutePath()));
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getContext());
                    if (photoFile != null) {
                        photoFile.delete();
                    }
                }
            }
        });
    }

    public void setPostAdapter(ArrayList<AllPicturesModel.PostList> posts) {
        if (picturesAdapter == null) {
            picturesAdapter = new PicturesAdapter(getActivity(), this);
        }
        picturesAdapter.doRefresh(posts);
        if (rv_Pictures.getAdapter() == null) {
            rv_Pictures.setHasFixedSize(true);
            rv_Pictures.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rv_Pictures.setAdapter(picturesAdapter);
        }
    }

    public void getAllImages() {
        new PostRequest(getActivity(), null, getString(R.string.get_all_pictures), isLoaderRequire, isLoaderRequire, new ResponseListener() {
            @Override
            public void onSucceedToPostCall(String response) {
                allPicturesModel = new Gson().fromJson(response, AllPicturesModel.class);
                if (allPicturesModel != null && allPicturesModel.getPostList() != null && !allPicturesModel.getPostList().isEmpty()) {
                    ImageList = allPicturesModel.getPostList();
                    setPostAdapter(ImageList);
                }
                if (ImageList.size() == 0) {
                    tv_no_feed_found.setVisibility(View.VISIBLE);
                } else {
                    tv_no_feed_found.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailedToPostCall(int statusCode, String msg) {
                Globals.showToast(getActivity(), msg);
            }
        }).execute(globals.getUserDetails().getStatus().getUserId());
    }

    @Override
    public void selectImage(int i) {
        Intent intent = new Intent(getContext(), SelectImageActivity.class);
        intent.putExtra(Constants.WN_POST_ID, String.valueOf(picturesAdapter.ImageList().get(i).getPostId()));
        startActivity(intent);
    }
}
