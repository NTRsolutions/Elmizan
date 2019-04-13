package com.sismatix.Elmizan.Fregment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.MyAdapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Part;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Article_Freg extends Fragment implements View.OnClickListener {

    View v;
    TextView tv_title, tv_upload_img, tv_youtube_link, tv_detail_article, tv_add_article;
    EditText edt_title, edt_youtube_link, edt_article_detail;
    RadioButton radio_upload_youtube_link, radio_upload_image, radioButton;
    CircleImageView iv_upload_image;
    ImageView iv_camera;
    RadioGroup radioGroup;
    LinearLayout lv_add_article, lv_upload_youtube_link, lv_upload_image;


    Bitmap bitmap = null;
    String path, filename, URL_MYACCOUNT, URL, encodedImage;
    Bundle bundle;
    public static final int RequestPermissionCode = 7;
    String article_id, oldurl_pass;
    LinearLayout lv_add_article_click,lv_add_article_parent;
    boolean Choose_img_clicked=false;
    ProgressBar progressBar_add_article;

    public Add_Article_Freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_article__freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setTypeface(Navigation_activity.typeface);

        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.add_Page_article));
        Allocate_Memory(v);
        lang_arbi();

        setupUI(lv_add_article_parent);
        oldurl_pass="test";
        bundle = this.getArguments();

        if (bundle != null) {
            article_id = bundle.getString("article_id");
            Log.e("article_id_115", "" + article_id);
        } else {
            Log.e("article_id_115", "" + article_id);
        }

        radio_upload_image.setOnClickListener(this);
        radio_upload_youtube_link.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        lv_add_article.setOnClickListener(this);




        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            if (article_id == "" || article_id == null || article_id == "null" || article_id.equalsIgnoreCase(null)
                    || article_id.equalsIgnoreCase("null")) {
                Log.e("article_id_pass_512", "" + article_id);
            } else {

                Log.e("article_id_pass", "" + article_id);
                CALL_ARTICLE_DETAIL_API();
            }

        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    ///get article detail
    private void CALL_ARTICLE_DETAIL_API() {
        progressBar_add_article.setVisibility(View.VISIBLE);
        lv_add_article_click.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        String userid = Login_preference.getuser_id(getActivity());

        Call<ResponseBody> article_detail;

        article_detail = api.get_article_detail(article_id, userid);

        article_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_article_detail", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {

                    progressBar_add_article.setVisibility(View.GONE);
                    lv_add_article_click.setVisibility(View.VISIBLE);
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("status_data_obj", "" + data_obj);

                        String date = data_obj.getString("article_created_at_format_day") + " " +
                                data_obj.getString("article_created_at_format_month") + " " +
                                data_obj.getString("article_created_at_format_year");

                        JSONObject article_media_raw = data_obj.getJSONObject("article_media_raw");
                        Log.e("article_media_raw", "" + article_media_raw);

                        JSONArray jsonArray_image = article_media_raw.getJSONArray("image");
                        Log.e("jsonArray_image", "" + jsonArray_image);

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
                                        oldurl_pass = jsonArray_image.getString(0);
                                        Log.e("oldurl_pass", "" + oldurl_pass);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                }

                            }
                        }

                        //////////////////
                        JSONArray jsonArray_video_old = article_media_raw.getJSONArray("video");
                        Log.e("jsonArray_video_old", "" + jsonArray_video_old);

                        if (jsonArray_video_old.equals("[]") == true) {
                            Log.e("jsonarray_blanck", "" + jsonArray_video_old);

                        } else {
                            for (int i = 0; i < jsonArray_video_old.length(); i++) {
                                try {
                                    String video = jsonArray_video_old.getString(i);

                                    if (video.equalsIgnoreCase("") == true || video.equalsIgnoreCase("null") == true
                                            || video == null) {
                                        Log.e("image_null", "" + video);
                                    } else {
                                        oldurl_pass = jsonArray_video_old.getString(0);
                                        Log.e("oldvideo_pass", "" + oldurl_pass);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                }

                            }
                        }


                        Navigation_activity.Check_String_NULL_Value(edt_title, data_obj.getString("article_title"));
                        // Navigation_activity.Check_String_NULL_Value(e, date);
                        Navigation_activity.Check_String_NULL_Value(edt_article_detail, String.valueOf(Html.fromHtml(data_obj.getString("article_description"))));

                        JSONObject image_obj = data_obj.getJSONObject("article_media_urls");
                        Log.e("img_obj", "" + image_obj);
                        JSONArray jsonArray = image_obj.getJSONArray("image");
                        Log.e("jsonArray_news", "" + jsonArray);

                        if (jsonArray.equals("[]") == true) {
                            Log.e("jsonarray_blanck", "" + jsonArray);

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    String image = jsonArray.getString(i);

                                    if (image.equalsIgnoreCase("") == true || image.equalsIgnoreCase("null") == true
                                            || image == null) {
                                        Log.e("image_null", "" + image);
                                    } else {
                                        radio_upload_image.setChecked(true);
                                        lv_upload_image.setVisibility(View.VISIBLE);
                                        Glide.with(getActivity()).load(image).into(iv_upload_image);
                                        Log.e("image_news", "" + image);

                                        }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                }

                            }
                        }

                        //get video array
                        JSONArray jsonArray_video = image_obj.getJSONArray("video");
                        Log.e("jsonArray_video", "" + jsonArray_video);


                        if (jsonArray_video.equals("[]") == true) {
                            Log.e("jsonarray_blanck", "" + jsonArray_video);

                        } else {
                            for (int j = 0; j < jsonArray_video.length(); j++) {
                                try {
                                    String video = jsonArray_video.getString(j);
                                    if (video.equalsIgnoreCase("") == true || video.equalsIgnoreCase("null") == true
                                            || video == null) {
                                        Log.e("video", "" + video);
                                    } else {
                                        radio_upload_youtube_link.setChecked(true);
                                        lv_upload_youtube_link.setVisibility(View.VISIBLE);
                                        Navigation_activity.Check_String_NULL_Value(edt_youtube_link, video);

                                        //Glide.with(getActivity()).load(image).into(iv_upload_image);
                                        Log.e("video", "" + video);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                }
                            }
                        }
                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == radio_upload_image) {
            lv_upload_image.setVisibility(View.VISIBLE);
            lv_upload_youtube_link.setVisibility(View.GONE);

        } else if (view == radio_upload_youtube_link) {
            lv_upload_image.setVisibility(View.GONE);
            lv_upload_youtube_link.setVisibility(View.VISIBLE);

        } else if (view == iv_camera) {
            if (CheckingPermissionIsEnabledOrNot()) {
                Toast.makeText(getActivity(), "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
            }
            else {
                //Calling method to enable permission.
                RequestMultiplePermission();
            }
            selectImage();

        } else if (view == lv_add_article) {
            Check_Validation();
        }

    }


    public boolean isvalid_youtube_url(String youtube) {
        String ePattern = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(youtube);
        Log.e("youtube_link",""+m.matches());
        return m.matches();
    }

    public void Check_Validation() {
        final String article_title = edt_title.getText().toString();
        final String article_detail = edt_article_detail.getText().toString();
        final String youtube_link = edt_youtube_link.getText().toString();

        if (edt_title.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Article name", Toast.LENGTH_SHORT).show();
        } else if (edt_article_detail.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Article Detail", Toast.LENGTH_SHORT).show();
        } else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Please select one value", Toast.LENGTH_SHORT).show();

        } else if(radio_upload_youtube_link.isChecked()==true)
        {
                if(edt_youtube_link.getText().length()==0)
                {
                    Toast.makeText(getContext(), "Please enter your Youtube Link", Toast.LENGTH_SHORT).show();
                }else if(isvalid_youtube_url(edt_youtube_link.getText().toString())==false){
                    Toast.makeText(getContext(), "Enter valid Youtube Link", Toast.LENGTH_SHORT).show();
                }else {
                    CALL_ADD_ARTICLE_API(article_title, article_detail, youtube_link, path);
                }
        }else if(radio_upload_image.isChecked()==true)
        {
            if(iv_upload_image.getDrawable() == null) {
                Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            }else if(path == "" || path == null || path == "null" || path.equalsIgnoreCase(null)
                    || path.equalsIgnoreCase("null")) {

                if (oldurl_pass.equalsIgnoreCase("test")==true)
                {
                    Log.e("old_image",""+oldurl_pass);
                    Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
                 //   Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
                }else{
                    CALL_ADD_ARTICLE_API(article_title, article_detail, youtube_link, path);
                }
                //Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            }
            else
             {

                CALL_ADD_ARTICLE_API(article_title, article_detail, youtube_link, path);

            }
        }else
        {
            CALL_ADD_ARTICLE_API(article_title, article_detail, youtube_link, path);
        }
    }

    private void CALL_ADD_ARTICLE_API(String article_title, String article_detail, String youtube_link, String path) {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("article_title", "" + article_title);
        Log.e("youtube_link_627", "" + youtube_link);
        String userid = Login_preference.getuser_id(getActivity());
        Log.e("user_id", "" + userid);
        Call<ResponseBody> add_article = null;

        String article_id_pass, old_imag_pass;

        RequestBody article_title1 = RequestBody.create(MediaType.parse("text/plain"), article_title);
        RequestBody article_detail1 = RequestBody.create(MediaType.parse("text/plain"), article_detail);
        RequestBody youtube_link1 = RequestBody.create(MediaType.parse("text/plain"), youtube_link);
        RequestBody userid1 = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), ApiClient.user_status);

        if (article_id == "" || article_id == null || article_id == "null" || article_id.equalsIgnoreCase(null)
                || article_id.equalsIgnoreCase("null")) {
            article_id_pass = "insert";
            old_imag_pass = "";
            RequestBody article_id1 = RequestBody.create(MediaType.parse("text/plain"), article_id_pass);
            RequestBody old_article = RequestBody.create(MediaType.parse("text/plain"), old_imag_pass);

            if (radio_upload_image.isChecked() == true) {
                File file = new File(path);
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("article_images[]", file.getName(), fileReqBody);
                Log.e("insert_image", "" + part);
                add_article = api.add_article_image
                        (userid1, article_id1, old_article,
                                article_title1, article_detail1, status, part);
            } else {
                Log.e("insert_youtube", "" + youtube_link1);
                add_article = api.add_article_youtube_link
                        (userid1, youtube_link1, article_id1, old_article,
                                article_title1, article_detail1, status);
            }
        } else {
            article_id_pass = article_id;
            old_imag_pass = oldurl_pass;
            Log.e("article_id_pass_edit", "" + article_id_pass);
            Log.e("old_imag_pass_645_edit", "" + old_imag_pass);

            RequestBody article_id1 = RequestBody.create(MediaType.parse("text/plain"), article_id_pass);
            RequestBody old_article = RequestBody.create(MediaType.parse("text/plain"), old_imag_pass);

            if (radio_upload_image.isChecked() == true) {

                Log.e("Edit_image", "imahe");
                if (path == "" || path == null || path == "null" || path.equalsIgnoreCase(null)
                        || path.equalsIgnoreCase("null")) {
                    Log.e("EDit_old_image", "" + path);
                    RequestBody blank = RequestBody.create(MediaType.parse("text/plain"), "");
                    add_article = api.add_article_image_blank
                            (userid1, article_id1, old_article,
                                    article_title1, article_detail1, status, blank);
                } else {
                    File file = new File(path);
                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("article_images[]", file.getName(), fileReqBody);

                    Log.e("Edit_new_image", "" + part);
                    add_article = api.add_article_image
                            (userid1, article_id1, old_article,
                                    article_title1, article_detail1, status, part);
                }
            } else {
                RequestBody old_article_pass = RequestBody.create(MediaType.parse("text/plain"), "");

                Log.e("edit_youtube_link", "" + youtube_link1);
                Log.e("old_article_pass", "" + old_article_pass);
                add_article = api.add_article_youtube_link
                        (userid1, youtube_link1, article_id1, old_article_pass,
                                article_title1, article_detail1, status);
            }
        }


        add_article.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_534", "" + jsonObject);
                    String status = jsonObject.getString("status");
                    Log.e("add_article", "" + status);
                    String message = jsonObject.getString("msg");

                    if (status.equalsIgnoreCase("success")) {
                        // // Log.e("msg_article", "" + message);
                        Bundle b = new Bundle();
                        b.putString("user_id", Login_preference.getuser_id(getActivity()));
                        Fragment myFragment = new Article_freg();
                        myFragment.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        String media_upload_error = jsonObject.getString("media_upload_error");
                        Toast.makeText(getActivity(), "" + media_upload_error, Toast.LENGTH_SHORT).show();

                        Log.e("add_article_status_else", "" + status);
                        Log.e("media_upload_error", "" + media_upload_error);
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                //onCaptureImageResult(data);
                Log.e("DATATATATAT===========", "==" + data.getExtras().get("data"));
                bitmap = (Bitmap) data.getExtras().get("data");
                // encodedImage = imgBitMapToString(bitmap);
                Log.e("camera_imagess", "" + encodedImage);
                Log.e("bitmap_559", "" + bitmap);
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                circularBitmapDrawable.setCircular(true);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getActivity(), bitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                 String  path_pass = String.valueOf(finalFile);

                Log.e("length_567",""+saveBitmapToFile(new File(path_pass)).length());

                long length = saveBitmapToFile(new File(path_pass)).length();
                length = length / (1024 * 1024);
                Log.e("length_570",""+length);

                if (length <= 5) {
                        path = String.valueOf(finalFile);

                        Log.e("length_580",""+path);
                        filename = path.substring(path.lastIndexOf("/") + 1);
                        iv_upload_image.setImageBitmap(bitmap);


                } else {
                    Toast.makeText(getActivity(), "Please upload image smaller then 5 MB.", Toast.LENGTH_SHORT).show();

                }


            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(imagePath));
                Log.e("path of image from gallery..*************...", imagePath);
                File imagefile = new File(imagePath);

                String   path_pass = String.valueOf(imagefile);


                long length = saveBitmapToFile(new File(path_pass)).length();
                length = length / (1024 * 1024);
                Log.e("length_610",""+length);


                if (length <= 5) {

                        path = String.valueOf(imagefile);
                        Log.e("pathhhhhhh_profilepic", "" + path);
                        filename = path.substring(path.lastIndexOf("/") + 1);
                        Log.e("pat_gallery_filenm", "" + filename);


                    BitmapDrawable d = new BitmapDrawable(getResources(), imagefile.getAbsolutePath());
                    iv_upload_image.setImageDrawable(d);
                } else {
                    Toast.makeText(getActivity(), "Please upload image smaller then 5 MB.", Toast.LENGTH_SHORT).show();

                }




              /*  RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), imagefile.getAbsolutePath());
                circularBitmapDrawable.setCircular(true);
*/
            }
        }
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("PathURLLLLLLLLLLLL", "" + Uri.parse(path));
        return Uri.parse(path);
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
    private void selectImage() {
        final CharSequence[] items = {getActivity().getResources().getString(R.string.take_photo)
                ,getActivity().getResources().getString(R.string.choose_gallery)
                ,getActivity().getResources().getString(R.string.Remove_photo),
                getActivity().getResources().getString(R.string.Cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle( getActivity().getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals(getActivity().getResources().getString(R.string.take_photo))) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (items[item].equals(getActivity().getResources().getString(R.string.choose_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (items[item].equals(getActivity().getResources().getString(R.string.Remove_photo))) {

                    bitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.my_profile);
                    Log.e("bitmap_355", "" + bitmap);
                    iv_upload_image.setImageBitmap(bitmap);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    path = String.valueOf(finalFile);
                    Log.e("remove_pic_path",""+path);

                    filename = path.substring(path.lastIndexOf("/") + 1);


                } else if (items[item].equals( getActivity().getResources().getString(R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
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
    private void Allocate_Memory(View v) {

        progressBar_add_article = (ProgressBar) v.findViewById(R.id.progressBar_add_article);
        radio_upload_youtube_link = (RadioButton) v.findViewById(R.id.radio_upload_youtube_link);
        radio_upload_image = (RadioButton) v.findViewById(R.id.radio_upload_image);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        tv_add_article = (TextView) v.findViewById(R.id.tv_add_article);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_upload_img = (TextView) v.findViewById(R.id.tv_upload_img);
        tv_youtube_link = (TextView) v.findViewById(R.id.tv_youtube_link);
        tv_detail_article = (TextView) v.findViewById(R.id.tv_detail_article);

        edt_title = (EditText) v.findViewById(R.id.edt_title);
        edt_youtube_link = (EditText) v.findViewById(R.id.edt_youtube_link);
        edt_article_detail = (EditText) v.findViewById(R.id.edt_article_detail);

        iv_upload_image = (CircleImageView) v.findViewById(R.id.iv_upload_image);
        iv_camera = (ImageView) v.findViewById(R.id.iv_camera);

        lv_add_article_parent = (LinearLayout) v.findViewById(R.id.lv_add_article_parent);
        lv_add_article_click = (LinearLayout) v.findViewById(R.id.lv_add_article_click);
        lv_add_article = (LinearLayout) v.findViewById(R.id.lv_add_article);
        lv_upload_youtube_link = (LinearLayout) v.findViewById(R.id.lv_upload_youtube_link);
        lv_upload_image = (LinearLayout) v.findViewById(R.id.lv_upload_image);

        tv_add_article.setTypeface(Navigation_activity.typeface);
        tv_title.setTypeface(Navigation_activity.typeface);
        tv_upload_img.setTypeface(Navigation_activity.typeface);
        tv_youtube_link.setTypeface(Navigation_activity.typeface);
        tv_detail_article.setTypeface(Navigation_activity.typeface);
        edt_title.setTypeface(Navigation_activity.typeface);
        edt_youtube_link.setTypeface(Navigation_activity.typeface);
        edt_article_detail.setTypeface(Navigation_activity.typeface);
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



}
