package com.sismatix.Elmizan.Fregment;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.google.gson.JsonObject;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Image_adapter;
import com.sismatix.Elmizan.Adapter.MyAdapter;
import com.sismatix.Elmizan.FileUtils;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    LinearLayout lv_choose_img, lv_upload_youtube_link, lv_upload_photos_media,lv_upload_click;
    TextView tv_choose_img, tv_youtube_link, tv_upload_media,tv_multiplefile;
    // private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 6384;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 124;

    private View parentView;
    private ListView listView;
    private ProgressBar mProgressBar;
    private Button btnChoose, btnUpload;

    private ArrayList<Uri> arrayList;
    private ArrayList<String> youtubearrayList;
    private ArrayList<String> old_media_List;
    //private ProgressBar mProgressBar;
    public static final int RequestPermissionCode = 7;

    LinearLayout lv_add_youtube_link_3, lv_add_youtube_link_2, lv_add_youtube_link_1,lv_upload_parent;
    ImageView iv_add_youtube_3, iv_add_youtube_2, iv_add_youtube_1;
    EditText edt_youtube_link_3, edt_youtube_link_2, edt_youtube_link_1;
    MyAdapter mAdapter;
    String youtube_link_1, user_id,youtube_link_2, youtube_link_3,old_image_video_pass,oldurl_pass_imag,old_video;
    String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
    boolean ytb_one = true, ytb_two = true, ytb_three = true;
    boolean validation_ok=true;
    RecyclerView recycler_image;
    Image_adapter image_adapter;
    public UPload_Media_freg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_upload__media_freg, container, false);
        AllocateMemory(v);
        setupUI(lv_upload_parent);
        lang_arbi();

        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setTypeface(Navigation_activity.typeface);

        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.multiple_file));


        Bundle bundle = this.getArguments();

        if (bundle != null) {

            old_image_video_pass = bundle.getString("old_image_video_pass");
            user_id = bundle.getString("user_id");

            Log.e("old_image_up_115", "" + old_image_video_pass);

        }else {
            Log.e("old_image_up_00", "" + bundle);

        }

        lv_upload_photos_media.setOnClickListener(this);
        lv_choose_img.setOnClickListener(this);
        lv_upload_youtube_link.setOnClickListener(this);
        tv_upload_media.setOnClickListener(this);
       // iv_add_youtube_1.setOnClickListener(this);
       // iv_add_youtube_2.setOnClickListener(this);
       // iv_add_youtube_3.setOnClickListener(this);

        arrayList = new ArrayList<>();
        youtubearrayList = new ArrayList<>();
        old_media_List = new ArrayList<>();
        arrayList.clear();
        youtubearrayList.clear();
        old_media_List.clear();



        if(bundle!=null)
        {
            Call_GET_old_Media_API();
        }


       /* if (expression.matches(youtube_link_1)&&expression.matches(youtube_link_2)&&expression.matches(youtube_link_3)){
            Toast.makeText(getContext(), "Valid Url", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Must be Youtube Url", Toast.LENGTH_SHORT).show();
        }*/

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
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    private void Call_GET_old_Media_API() {


        try {
            JSONObject obj = new JSONObject(old_image_video_pass);
            Log.e("obj_160",""+obj);

            JSONObject media_obj=obj.getJSONObject("arr_user_media_content");
            Log.e("media_obj",""+media_obj);

            ///get images....
            JSONArray jsonArray_image = media_obj.getJSONArray("image");
            Log.e("json_image_168", "" + jsonArray_image);

            if (jsonArray_image.equals("[]") == true) {
                Log.e("jsonarray_blanck", "" + jsonArray_image);

            } else {
                for (int i = 0; i < jsonArray_image.length(); i++) {
                    try {
                        String image = jsonArray_image.getString(i);

                        if (image.equalsIgnoreCase("") == true || image.equalsIgnoreCase("null") == true
                                || image == null) {
                            Log.e("image_null", "" + image);
                        } else {
                            oldurl_pass_imag = jsonArray_image.getString(i);
                            Log.e("oldurl_pass_183", "" + oldurl_pass_imag);
                            old_media_List.add(oldurl_pass_imag);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", "" + e);
                    } finally {
                    }

                }
            }



            ///get video....
            JSONArray jsonArray_video = media_obj.getJSONArray("video");
            Log.e("json_video_168", "" + jsonArray_video);

            if (jsonArray_video.equals("[]") == true) {
                Log.e("jsonarray_blanck", "" + jsonArray_video);

            } else {
                for (int j = 0; j < jsonArray_video.length(); j++) {
                    try {
                        String video = jsonArray_video.getString(j);

                        if (video.equalsIgnoreCase("") == true || video.equalsIgnoreCase("null") == true
                                || video == null) {
                            Log.e("image_null", "" + video);
                        } else {
                            old_video = jsonArray_video.getString(j);
                            Log.e("old_video_183", "" + old_video);
                            youtubearrayList.add(old_video);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", "" + e);
                    } finally {
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                            Log.e("Uri Selected", imageUri.toString());
                            try {

                                // Get the file path from the URI
                                String path = FileUtils.getPath(getActivity(), imageUri);
                                Log.e("Multiple_FileSelected",""+ path);

                                long length = saveBitmapToFile(new File(path)).length();
                                length = length / (1024 * 1024);
                                Log.e("length_320",""+length);


                                if (length <= 5) {
                                    if (!TextUtils.isEmpty(path)) {
                                        arrayList.add(imageUri);
                                        Image_adapter mAdapter = new Image_adapter(getActivity(), arrayList);
                                        LinearLayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
                                        recycler_image.setLayoutManager(layoutManager);
                                        recycler_image.setAdapter(mAdapter);

                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Please upload image smaller then 5 MB.", Toast.LENGTH_SHORT).show();

                                }

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
                            Log.d("Single_File_Selected", path);


                            long length = saveBitmapToFile(new File(path)).length();
                            length = length / (1024 * 1024);
                            Log.e("length_320",""+length);


                            if (length <= 5) {
                                if (!TextUtils.isEmpty(path)) {
                                    arrayList.add(uri);/*
                                    mAdapter = new MyAdapter(getActivity(), arrayList);

                                    listView.setAdapter(mAdapter);
*/

                                    Image_adapter mAdapter = new Image_adapter(getActivity(), arrayList);
                                    LinearLayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
                                    recycler_image.setLayoutManager(layoutManager);
                                    recycler_image.setAdapter(mAdapter);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please upload image smaller then 5 MB.", Toast.LENGTH_SHORT).show();

                            }




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

    public File saveBitmapToFile(File file) {
        try {

// BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
// factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

// The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream,
                    null, o2);
            inputStream.close();

            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
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

                       // Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                      //  Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();

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
    public boolean isvalid_youtube_url(String youtube) {
        String ePattern = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(youtube);
        Log.e("youtube_link",""+m.matches());
        return m.matches();
    }

    private void uploadImagesToServer() {
        String user_id = Login_preference.getuser_id(getActivity());
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

        showProgress();
        lv_upload_click.setVisibility(View.GONE);

        // create list of file parts (photo, video, ...)
        List<MultipartBody.Part> parts = new ArrayList<>();
        List<RequestBody> youtube = new ArrayList<>();
        List<RequestBody> old_media = new ArrayList<>();

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


        if (old_media_List != null) {
            for (int i = 0; i < old_media_List.size(); i++) {

                old_media.add(createPartFromString(old_media_List.get(i)));
                //  parts.add(prepareFilePart("user_media_images[]", arrayList.get(i)));
            }
        }



        Log.e("parts", "" + parts);
        Log.e("youtubearrayList_272", "" + youtubearrayList);
        Log.e("old_media_415", "" + old_media);


        // create a map of data to pass along
        //RequestBody video_url= createPartFromString(youtube);
        //RequestBody video_url = RequestBody.create(MediaType.parse("text/plain"), youtube);
        Log.e("video_url", "" + youtube);
        RequestBody userid_pass = RequestBody.create(MediaType.parse("text/plain"), user_id);
        //RequestBody useridold_images = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody insertedby = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));

        // finally, execute the request
        Call<ResponseBody> call = api.uploadMultiple_files(youtube, insertedby, old_media, userid_pass, parts);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                hideProgress();
                Log.e("response_upload_media", "" + response);

                lv_upload_click.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_534", "" + jsonObject);
                    String status = jsonObject.getString("status");
                    Log.e("add_article", "" + status);
                   /* JSONObject files = jsonObject.getJSONObject("files");
                    Log.e("files", "" + files);
                   */ if (status.equalsIgnoreCase("success")) {
                        String message = jsonObject.getString("msg");

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        Fragment myFragment = new Premimum_Lawyer_freg();

                        Bundle b=new Bundle();
                        b.putString("user_id",Login_preference.getuser_id(getActivity()));
                        Log.e("user_id",""+Login_preference.getuser_id(getActivity()));
                        myFragment.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();



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
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Login_freg.hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

         //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
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
        lv_upload_click = (LinearLayout) v.findViewById(R.id.lv_upload_click);
        lv_upload_parent = (LinearLayout) v.findViewById(R.id.lv_upload_parent);
        lv_add_youtube_link_3 = (LinearLayout) v.findViewById(R.id.lv_add_youtube_link_3);
        lv_add_youtube_link_2 = (LinearLayout) v.findViewById(R.id.lv_add_youtube_link_2);
        lv_add_youtube_link_1 = (LinearLayout) v.findViewById(R.id.lv_add_youtube_link_1);
        lv_choose_img = (LinearLayout) v.findViewById(R.id.lv_choose_img);
        lv_upload_youtube_link = (LinearLayout) v.findViewById(R.id.lv_upload_youtube);
        lv_upload_photos_media = (LinearLayout) v.findViewById(R.id.lv_upload_photos_media);
        tv_choose_img = (TextView) v.findViewById(R.id.tv_choose_img);
        tv_youtube_link = (TextView) v.findViewById(R.id.tv_youtube_link);
        tv_upload_media = (TextView) v.findViewById(R.id.tv_upload_media);
        tv_multiplefile = (TextView) v.findViewById(R.id.tv_multiplefile);
        listView = (ListView) v.findViewById(R.id.listView);
        iv_add_youtube_3 = (ImageView) v.findViewById(R.id.iv_add_youtube_3);
        iv_add_youtube_2 = (ImageView) v.findViewById(R.id.iv_add_youtube_2);
        iv_add_youtube_1 = (ImageView) v.findViewById(R.id.iv_add_youtube_1);
        edt_youtube_link_3 = (EditText) v.findViewById(R.id.edt_youtube_link_3);
        edt_youtube_link_2 = (EditText) v.findViewById(R.id.edt_youtube_link_2);
        edt_youtube_link_1 = (EditText) v.findViewById(R.id.edt_youtube_link_1);

        recycler_image=(RecyclerView)v.findViewById(R.id.recycler_image);



        tv_multiplefile.setTypeface(Navigation_activity.typeface);
        tv_upload_media.setTypeface(Navigation_activity.typeface);
        tv_choose_img.setTypeface(Navigation_activity.typeface);
        tv_youtube_link.setTypeface(Navigation_activity.typeface);
        edt_youtube_link_3.setTypeface(Navigation_activity.typeface);
        edt_youtube_link_2.setTypeface(Navigation_activity.typeface);
        edt_youtube_link_1.setTypeface(Navigation_activity.typeface);

    }

    @Override
    public void onClick(View view) {
        if (view == tv_upload_media || view==lv_upload_photos_media ) {

            if (edt_youtube_link_1.getVisibility() == View.VISIBLE) {
                youtube_link_1 = edt_youtube_link_1.getText().toString();
                if (youtube_link_1.length() != 0) {
                    if(isvalid_youtube_url(youtube_link_1)==true) {
                        Log.e("youtube_link_1_496",""+youtube_link_1);
                        youtubearrayList.add(youtube_link_1);
                    }else {
                        Toast.makeText(getActivity(), "Enter Valid Youtube Url", Toast.LENGTH_SHORT).show();
                        validation_ok=false;
                    }
                }else{
                    //validation_ok=false;
                }
            }else{
                validation_ok=false;
            }

            if (edt_youtube_link_2.getVisibility() == View.VISIBLE) {
                youtube_link_2 = edt_youtube_link_2.getText().toString();
                if (youtube_link_2.length() != 0) {

                    if(isvalid_youtube_url(youtube_link_2)==true) {
                        Log.e("youtube_link_1_497", "" + youtube_link_2);

                        youtubearrayList.add(youtube_link_2);
                    }else {
                        Toast.makeText(getActivity(), "Enter Valid Youtube Url", Toast.LENGTH_SHORT).show();
                        validation_ok=false;
                        Log.e("youtube_link_1_626", "" + youtube_link_2);

                    }
                }else{
                   // validation_ok=false;
                    Log.e("youtube_link_1_627", "" + youtube_link_2);

                }
            }else{
                validation_ok=false;
                Log.e("youtube_link_1_628", "" + youtube_link_2);

            }

            if (edt_youtube_link_3.getVisibility() == View.VISIBLE) {
                youtube_link_3 = edt_youtube_link_3.getText().toString();

                if (youtube_link_3.length() != 0) {

                    if(isvalid_youtube_url(youtube_link_2)==true) {
                        Log.e("youtube_link_1_496", "" + youtube_link_1);

                        youtubearrayList.add(youtube_link_3);
                    }else {
                        Toast.makeText(getActivity(), "Enter Valid Youtube Url", Toast.LENGTH_SHORT).show();
                        validation_ok=false;

                    }
                }else{
                    //validation_ok=false;
                    Log.e("youtube_link_1_629", "" + youtube_link_2);
                }
            }else{
                validation_ok=false;
                Log.e("youtube_link_1_630", "" + youtube_link_2);


            }
            Log.e("youtube_arraylist", "" + youtubearrayList);


            if(validation_ok==true) {
                Log.e("546_validation_ok == ", "" + validation_ok);
                uploadImagesToServer();
            }else{
                Log.e("549_validation_ok == ", "" + validation_ok);
            }


        } else if (view == lv_choose_img) {
           /* if (CheckingPermissionIsEnabledOrNot()) {
                Toast.makeText(getActivity(), "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
            } else {
                //Calling method to enable permission.
                RequestMultiplePermission();
            }
           */ if(CheckingPermissionIsEnabledOrNot())
            {
                showChooser();
            }else {
                RequestMultiplePermission();
            }

        } else if (view == lv_upload_youtube_link) {

            Log.e("uplodyoutube", "vcxv");
            lv_add_youtube_link_1.setVisibility(View.VISIBLE);
            lv_add_youtube_link_2.setVisibility(View.VISIBLE);
            lv_add_youtube_link_3.setVisibility(View.VISIBLE);
            lv_upload_youtube_link.setEnabled(false);

        }/* else if (view == iv_add_youtube_1) {
            ytb_one = false;
            iv_add_youtube_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ytb_one = true;
                    lv_add_youtube_link_1.setVisibility(View.GONE);
                }
            });
            lv_add_youtube_link_2.setVisibility(View.VISIBLE);
            iv_add_youtube_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_highlight_off_black_48dp));

        } else if (view == iv_add_youtube_2) {
            ytb_one = false;
            iv_add_youtube_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ytb_one = true;
                    lv_add_youtube_link_2.setVisibility(View.GONE);
                }
            });
            iv_add_youtube_2.setImageDrawable(getResources().getDrawable(R.drawable.ic_highlight_off_black_48dp));
            lv_add_youtube_link_3.setVisibility(View.VISIBLE);
        } else if (view == iv_add_youtube_3) {
            ytb_one = false;
            iv_add_youtube_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ytb_one = true;
                    lv_add_youtube_link_3.setVisibility(View.GONE);
                }
            });
            Toast.makeText(getActivity(), "You Can Upload only 3 youtube link", Toast.LENGTH_SHORT).show();
        }*/
    }

    /*@Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {


                    Fragment myFragment = new Premimum_Lawyer_freg();
                    Bundle b=new Bundle();
                    b.putString("user_id",user_id);
                    Log.e("user_id_748",""+user_id);
                    myFragment.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();


///                    loadFragment(new Premimum_Lawyer_freg());


                    return true;
                }
                return false;
            }
        });
    }*/

    public void loadFragment(Fragment fragment) {
        Log.e("clickone", "");
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in,
                0, 0, R.anim.fade_out);
        transaction.replace(R.id.main_fram_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
