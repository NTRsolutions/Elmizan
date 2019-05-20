package com.sismatix.Elmizan.Fregment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Country_model;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register_freg extends Fragment implements View.OnClickListener {
    EditText editTextname_reg,editTextEmail_reg,editTextphone_reg;
    TextInputEditText editTextpassword_reg;
    Button btn_register;
    CheckBox checkbox;
    String  checked_value_pass,countryid;
    Spinner spinner_country;
    RelativeLayout lv_register_parent;
    TextView sp_text,tv_arabic_image;
    TextInputLayout layout_mobilenuber,etPasswordLayout,layout_password,layout_email;
    LinearLayout lv_upload_image_regi;
    ImageView iv_regi_camera;
    CircleImageView iv_regi_lawyer_profile;


    Bitmap bitmap = null;
    String path, filename, encodedImage, user_id;
    Bundle bundle;
    public static final int RequestPermissionCode = 7;



    public static ArrayList<String> country_name_code = new ArrayList<String>();
    public static ArrayList<String> country_name = new ArrayList<String>();
    private List<Country_model> country_model = new ArrayList<Country_model>();
    public Register_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_register_freg, container, false);

        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.Create_an_account));
        Navigation_activity. tv_nav_title.setTypeface(Navigation_activity.typeface);
        lang_arbi();
        AllocateMemory(v);
        country_model.clear();
        country_name.clear();
        country_name_code.clear();

        setupUI(lv_register_parent);
        Login_freg.hideSoftKeyboard(getActivity());
        btn_register.setOnClickListener(this);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("checked",""+b);
                if(b==true)
                {
                    checked_value_pass="1";
                    lv_upload_image_regi.setVisibility(View.VISIBLE);

                }else {
                    checked_value_pass="0";
                    lv_upload_image_regi.setVisibility(View.GONE);

                }


              /*  if (checkbox.isChecked())
                {
                }else {
                }*/
            }
        });

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_COUNTRY_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                int selected_item_position = spinner_country.getSelectedItemPosition();
                countryid = country_name_code.get(selected_item_position);
                // MyAddress_Preference.setCountryId(getActivity(), String.valueOf(selected_item_position));
                Log.e("countryid", "" + countryid);
                String selected_country = String.valueOf(spinner_country.getSelectedItem());

             //    Toast.makeText(getActivity(), selected_item_position + " " + selected_country + " => " + countryid, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {

            }

        });

        IMAGE_CLICKLISTNER();

        return v;
    }




    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean  onTouch(View v, MotionEvent event) {
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

    private void CALL_COUNTRY_API() {
        //  progressBar_home.setVisibility(View.VISIBLE);
        country_model.clear();
        country_name.clear();
        country_name_code.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> country_list = api.get_country_list(ApiClient.user_status);

        country_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_country", "" + response.body().toString());
                //   progressBar_home.setVisibility(View.GONE);
                country_name.add("اختر");
                country_name_code.add("0");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_country",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                        JSONArray data_array=jsonObject.getJSONArray("data");

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name",""+news_object.getString("country_id"));
                                country_model.add(new Country_model(news_object.getString("country_id"),
                                        news_object.getString("country_name"),
                                        news_object.getString("country_status"),
                                        news_object.getString("country_image_url")
                                ));

                                country_name.add(  news_object.getString("country_name"));
                                country_name_code.add(news_object.getString("country_id"));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                            }

                        }
                       spinner_country.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, country_name));

                        }else if (status.equalsIgnoreCase("error")){
                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AllocateMemory(View v) {
        editTextname_reg=(EditText)v.findViewById(R.id.editTextname_reg);
        editTextEmail_reg=(EditText)v.findViewById(R.id.editTextEmail_reg);
        editTextphone_reg=(EditText)v.findViewById(R.id.editTextphone_reg);
        editTextpassword_reg=(TextInputEditText)v.findViewById(R.id.editTextpassword_reg);
        btn_register=(Button)v.findViewById(R.id.btn_register);
        checkbox=(CheckBox) v.findViewById(R.id.checkbox);
        spinner_country=(Spinner) v.findViewById(R.id.spinner_country);
        lv_register_parent=(RelativeLayout) v.findViewById(R.id.lv_register_parent);
        sp_text=(TextView) v.findViewById(R.id.sp_text);
        tv_arabic_image=(TextView) v.findViewById(R.id.tv_arabic_image);
        iv_regi_camera=(ImageView) v.findViewById(R.id.iv_regi_camera);
        iv_regi_lawyer_profile=(CircleImageView) v.findViewById(R.id.iv_regi_lawyer_profile);


        lv_upload_image_regi=(LinearLayout) v.findViewById(R.id.lv_upload_image_regi);
        layout_mobilenuber=(TextInputLayout) v.findViewById(R.id.layout_mobilenuber);
        etPasswordLayout=(TextInputLayout) v.findViewById(R.id.etPasswordLayout);
        layout_password=(TextInputLayout) v.findViewById(R.id.layout_password);
        layout_email=(TextInputLayout) v.findViewById(R.id.layout_email);


        checkbox.setTypeface(Navigation_activity.typeface);



        editTextname_reg.setTypeface(Navigation_activity.typeface);
        editTextEmail_reg.setTypeface(Navigation_activity.typeface);
        editTextpassword_reg.setTypeface(Navigation_activity.typeface);
        editTextphone_reg.setTypeface(Navigation_activity.typeface);

        sp_text.setTypeface(Navigation_activity.typeface);
        tv_arabic_image.setTypeface(Navigation_activity.typeface);
        checkbox.setTypeface(Navigation_activity.typeface);
        btn_register.setTypeface(Navigation_activity.typeface);

        layout_mobilenuber.setTypeface(Navigation_activity.typeface);
        etPasswordLayout.setTypeface(Navigation_activity.typeface);
        layout_password.setTypeface(Navigation_activity.typeface);
        layout_email.setTypeface(Navigation_activity.typeface);

    }

    @Override
    public void onClick(View view) {
        final String regi_name = editTextname_reg.getText().toString();
        final String regi_emailid = editTextEmail_reg.getText().toString();
        final String regi_phone = editTextphone_reg.getText().toString();
        final String regi_password = editTextpassword_reg.getText().toString();

        if(editTextname_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
        }else if (editTextEmail_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Email id", Toast.LENGTH_SHORT).show();
        }else if (isValidEmailAddress(editTextEmail_reg.getText().toString()) == false) {
            Toast.makeText(getContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show();
        } else if (editTextpassword_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
        } else if (editTextpassword_reg.getText().toString().length() <= 5) {
            Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
        }else if (editTextphone_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Phone number", Toast.LENGTH_SHORT).show();
        }else if (regi_phone.length() < 8 || regi_phone.length() > 13) {//et_shippingphonenumber.getText().length() == 0
            Toast.makeText(getContext(), "Please enter valid Mobile no.", Toast.LENGTH_SHORT).show();
        }else if (spinner_country.getSelectedItem().toString().trim().equals("Select") == true) {
            Toast.makeText(getActivity(), "Please Select Country", Toast.LENGTH_SHORT).show();
        }else if(checkbox.isChecked()==true)
        {
            if(path == "" || path == null || path == "null" || path.equalsIgnoreCase(null)
                    || path.equalsIgnoreCase("null"))
            {
                Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();

            }else {
                Register_Api(regi_name,regi_emailid,regi_phone,regi_password,countryid);
            }


        }
        else {
            Register_Api(regi_name,regi_emailid,regi_phone,regi_password,countryid);
        }
    }

    private void Register_Api(String regi_name, String regi_emailid, String regi_phone, String regi_password,String user_country_id) {

        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        String agree_terms="1";
        Call<ResponseBody> register = null;

        if(checkbox.isChecked()==false)
        {
            Log.e("country_value_312", "0");

            register = apii.get_register(regi_name, regi_emailid,regi_phone,
                    regi_password ,regi_password,agree_terms,checked_value_pass,user_country_id);

        }else {

            Log.e("country_value_318", "1");
            Log.e("path_358", "1"+path);

            RequestBody regi_name_pass = RequestBody.create(MediaType.parse("text/plain"), regi_name);
            RequestBody regi_emailid_pass = RequestBody.create(MediaType.parse("text/plain"), regi_emailid);
            RequestBody regi_phone_pass = RequestBody.create(MediaType.parse("text/plain"), regi_phone);
            RequestBody regi_password_pass = RequestBody.create(MediaType.parse("text/plain"), regi_password);
            RequestBody agree_terms_pass = RequestBody.create(MediaType.parse("text/plain"), agree_terms);
            RequestBody checked_value_passss = RequestBody.create(MediaType.parse("text/plain"), checked_value_pass);
            RequestBody user_country_id_pass = RequestBody.create(MediaType.parse("text/plain"), user_country_id);

            File file = new File(path);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("user_membership_card", file.getName(), fileReqBody);
            Log.e("new_part_624", "" + part);

            register = apii.CALL_User_registration_image
                    (regi_name_pass,
                            regi_emailid_pass, regi_phone_pass, regi_password_pass,regi_password_pass,
                            agree_terms_pass, checked_value_passss,
                            user_country_id_pass,part);


        }
        Log.e("checked_value_pass", "" + checked_value_pass);
        Log.e("country_value_pass", "" + countryid);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String meassg = jsonObject.getString("msg");
                    Log.e("message", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                        //JSONObject object=new JSONObject(jsonObject.getString("data"));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Login_freg nextFrag = new Login_freg();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fram_layout, nextFrag, "login")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        },3000);


                        //getActivity().finish();
                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
             //   Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }




    ///image upload

    private void IMAGE_CLICKLISTNER() {
        iv_regi_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckingPermissionIsEnabledOrNot()) {
                    //  Toast.makeText(getActivity(), "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
                } else {
                    //Calling method to enable permission.
                    RequestMultiplePermission();
                }
                selectImage();
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
                Log.e("camera_imagess", "" + bitmap);

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                circularBitmapDrawable.setCircular(true);
                iv_regi_lawyer_profile.setImageBitmap(bitmap);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getActivity(), bitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                path = String.valueOf(finalFile);
                filename = path.substring(path.lastIndexOf("/") + 1);
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
                path = String.valueOf(imagefile);
                Log.e("pathhhhhhh_profilepic", "" + path);
                filename = path.substring(path.lastIndexOf("/") + 1);
                Log.e("pat_gallery_filenm", "" + filename);

                BitmapDrawable d = new BitmapDrawable(getResources(), imagefile.getAbsolutePath());
                iv_regi_lawyer_profile.setImageDrawable(d);

              /*  RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), imagefile.getAbsolutePath());
                circularBitmapDrawable.setCircular(true);
*/
            }
        }
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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Remove Photo",
                "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (items[item].equals("Remove Photo")) {

                    bitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.my_profile);
                    Log.e("bitmap_355", "" + bitmap);
                    iv_regi_lawyer_profile.setImageBitmap(bitmap);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    path = String.valueOf(finalFile);
                    Log.e("remove_pic_path", "" + path);

                    filename = path.substring(path.lastIndexOf("/") + 1);


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
