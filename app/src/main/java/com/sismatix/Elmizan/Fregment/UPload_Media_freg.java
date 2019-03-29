package com.sismatix.Elmizan.Fregment;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Adapter.MyAdapter;
import com.sismatix.Elmizan.FileUtils;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UPload_Media_freg extends Fragment implements View.OnClickListener {

    View v;
    LinearLayout lv_choose_img, lv_upload_youtube_link, lv_upload_photos_media;
    TextView tv_choose_img, tv_youtube_link, tv_upload_media;
    // private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 6384;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;

    private View parentView;
    private ListView listView;
    private ProgressBar mProgressBar;
    private Button btnChoose, btnUpload;

    private ArrayList<Uri> arrayList;
    private ArrayList<String> youtubearrayList;
    //private ProgressBar mProgressBar;
    public static final int RequestPermissionCode = 7;

    LinearLayout lv_add_youtube_link_3, lv_add_youtube_link_2, lv_add_youtube_link_1;

    ImageView iv_add_youtube_3, iv_add_youtube_2, iv_add_youtube_1;
    EditText edt_youtube_link_3, edt_youtube_link_2, edt_youtube_link_1;
    MyAdapter mAdapter;
    String youtube_link_1, youtube_link_2, youtube_link_3;

    boolean ytb_one = true, ytb_two = true, ytb_three = true;

    public UPload_Media_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_upload__media_freg, container, false);
        AllocateMemory(v);


        lv_upload_photos_media.setOnClickListener(this);
        lv_choose_img.setOnClickListener(this);
        lv_upload_youtube_link.setOnClickListener(this);
        iv_add_youtube_1.setOnClickListener(this);
        iv_add_youtube_2.setOnClickListener(this);
        iv_add_youtube_3.setOnClickListener(this);

        arrayList = new ArrayList<>();
        youtubearrayList = new ArrayList<>();
        arrayList.clear();
        youtubearrayList.clear();


        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                removeItemFromList(position);

                return true;
            }
        });*/
        return v;
    }

    // method to remove list item
    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub

                // main code on after clicking yes
                arrayList.remove(deletePosition);


            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        int currentItem = 0;
                        while (currentItem < count) {
                            Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                            currentItem = currentItem + 1;
                            Log.d("Uri Selected", imageUri.toString());
                            try {
                                // Get the file path from the URI
                                String path = FileUtils.getPath(getActivity(), imageUri);
                                Log.d("Multiple File Selected", path);

                                arrayList.add(imageUri);
                                MyAdapter mAdapter = new MyAdapter(getActivity(), arrayList);
                                listView.setAdapter(mAdapter);

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                    } else if (data.getData() != null) {
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(getContext(), uri);
                            Log.d("Single File Selected", path);

                            arrayList.add(uri);
                            mAdapter = new MyAdapter(getActivity(), arrayList);
                            listView.setAdapter(mAdapter);
                         //   mAdapter.notifyDataSetChanged();
                          //  mAdapter.notifyDataSetInvalidated();
                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.choose_image));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    //Permission function starts from here
    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,

                }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalstoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalstoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && ReadExternalstoragePermission && WriteExternalstoragePermission) {

                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {

        int CAMERA_PermissionResult = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        int READ_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);


        return CAMERA_PermissionResult == PackageManager.PERMISSION_GRANTED &&
                READ_EXTERNAL_STORAGE_PermissionResult == PackageManager.PERMISSION_GRANTED &&
                WRITE_EXTERNAL_STORAGE_PermissionResult == PackageManager.PERMISSION_GRANTED;

    }


    private void uploadImagesToServer() {
        String user_id = Login_preference.getuser_id(getActivity());
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

        showProgress();

        // create list of file parts (photo, video, ...)
        List<MultipartBody.Part> parts = new ArrayList<>();
        List<RequestBody> youtube = new ArrayList<>();

        // create upload service client
        // ApiService service = retrofit.create(ApiService.class);

        if (arrayList != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < arrayList.size(); i++) {
                parts.add(prepareFilePart("user_media_images[]", arrayList.get(i)));
            }
        }


        if (youtubearrayList != null) {
            for (int i = 0; i < youtubearrayList.size(); i++) {

                youtube.add(createPartFromString(youtubearrayList.get(i)));
                //  parts.add(prepareFilePart("user_media_images[]", arrayList.get(i)));
            }
        }


        Log.e("parts", "" + parts);
        Log.e("youtubearrayList_272", "" + youtubearrayList);


        // create a map of data to pass along
        //RequestBody video_url= createPartFromString(youtube);
        //RequestBody video_url = RequestBody.create(MediaType.parse("text/plain"), youtube);
        Log.e("video_url", "" + youtube);
        RequestBody userid_pass = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody useridold_images = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody insertedby = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));

        // finally, execute the request
        Call<ResponseBody> call = api.uploadMultiple_files(youtube, insertedby, useridold_images, userid_pass, parts);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                hideProgress();
                Log.e("response_upload_media", "" + response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_534", "" + jsonObject);
                    String status = jsonObject.getString("status");
                    Log.e("add_article", "" + status);
                    String message = jsonObject.getString("msg");
                    JSONObject files = jsonObject.getJSONObject("files");
                    Log.e("files", "" + files);
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("media_upload_error", "err");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                hideProgress();
                //Snackbar.make(parentView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        lv_upload_youtube_link.setEnabled(false);
        lv_choose_img.setEnabled(false);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        lv_choose_img.setEnabled(true);
        lv_upload_youtube_link.setEnabled(true);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {

        // RequestBody userid_pass = RequestBody.create(MediaType.parse("text/plain"), user_id);


        return RequestBody.create(
                MediaType.parse("text/plain"), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getActivity(), fileUri);
        // create RequestBody instance from file
        RequestBody requestFile =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            requestFile = RequestBody.create(
                    MediaType.parse(Objects.requireNonNull(getActivity().getContentResolver().getType(fileUri))),
                    file
            );
        }
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void AllocateMemory(View v) {


        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        lv_add_youtube_link_3 = (LinearLayout) v.findViewById(R.id.lv_add_youtube_link_3);
        lv_add_youtube_link_2 = (LinearLayout) v.findViewById(R.id.lv_add_youtube_link_2);
        lv_add_youtube_link_1 = (LinearLayout) v.findViewById(R.id.lv_add_youtube_link_1);
        lv_choose_img = (LinearLayout) v.findViewById(R.id.lv_choose_img);
        lv_upload_youtube_link = (LinearLayout) v.findViewById(R.id.lv_upload_youtube);
        lv_upload_photos_media = (LinearLayout) v.findViewById(R.id.lv_upload_photos_media);
        tv_choose_img = (TextView) v.findViewById(R.id.tv_choose_img);
        tv_youtube_link = (TextView) v.findViewById(R.id.tv_youtube_link);
        tv_upload_media = (TextView) v.findViewById(R.id.tv_upload_media);
        listView = (ListView) v.findViewById(R.id.listView);
        iv_add_youtube_3 = (ImageView) v.findViewById(R.id.iv_add_youtube_3);
        iv_add_youtube_2 = (ImageView) v.findViewById(R.id.iv_add_youtube_2);
        iv_add_youtube_1 = (ImageView) v.findViewById(R.id.iv_add_youtube_1);
        edt_youtube_link_3 = (EditText) v.findViewById(R.id.edt_youtube_link_3);
        edt_youtube_link_2 = (EditText) v.findViewById(R.id.edt_youtube_link_2);
        edt_youtube_link_1 = (EditText) v.findViewById(R.id.edt_youtube_link_1);
    }

    @Override
    public void onClick(View view) {
        if (view == lv_upload_photos_media) {


            if (edt_youtube_link_1.getVisibility() == View.VISIBLE) {

                youtube_link_1 = edt_youtube_link_1.getText().toString();

                if (youtube_link_1.length() != 0) {
                    youtubearrayList.add(youtube_link_1);

                }
            }

            if (edt_youtube_link_2.getVisibility() == View.VISIBLE) {
                youtube_link_2 = edt_youtube_link_2.getText().toString();
                if (youtube_link_2.length() != 0) {
                    youtubearrayList.add(youtube_link_2);
                }
            }

            if (edt_youtube_link_3.getVisibility() == View.VISIBLE) {
                youtube_link_3 = edt_youtube_link_3.getText().toString();

                if (youtube_link_3.length() != 0) {
                    youtubearrayList.add(youtube_link_3);
                }
            }


            Log.e("youtube_arraylist", "" + youtubearrayList);
            uploadImagesToServer();
        } else if (view == lv_choose_img) {
            if (CheckingPermissionIsEnabledOrNot()) {
                Toast.makeText(getActivity(), "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
            } else {
                //Calling method to enable permission.
                RequestMultiplePermission();
            }
            showChooser();
        } else if (view == lv_upload_youtube_link) {

            Log.e("uplodyoutube", "vcxv");
            lv_add_youtube_link_1.setVisibility(View.VISIBLE);

        } else if (view == iv_add_youtube_1) {

            lv_add_youtube_link_2.setVisibility(View.VISIBLE);
            iv_add_youtube_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_highlight_off_black_48dp));


        } else if (view == iv_add_youtube_2) {
            iv_add_youtube_2.setImageDrawable(getResources().getDrawable(R.drawable.ic_highlight_off_black_48dp));
            lv_add_youtube_link_3.setVisibility(View.VISIBLE);
        } else if (view == iv_add_youtube_3) {
            Toast.makeText(getActivity(), "You Can Upload only 3 youtube link", Toast.LENGTH_SHORT).show();
        }
    }
}
