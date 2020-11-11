package edu.csce.af027.homework3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    PictureDatabase db;
    Double latitude;
    Double longitude;
    String title;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Log.d("Camera","in camera fragment onCreate");
         latitude = getArguments().getDouble("latitude");
         longitude= getArguments().getDouble("longitude");
         title= getArguments().getString("title");

         db = Room.databaseBuilder(getContext(),
                PictureDatabase.class, "picture_database").allowMainThreadQueries().build();
        dispatchTakePictureIntent();
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();

        return fragment;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "edu.csce.af027.homework3.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

Geocoder geocoder= new Geocoder(getContext());
        List<Address> addresses = null;
        String title= null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d("address", addresses.get(0).getAddressLine(0));
            title= addresses.get(0).getAddressLine(0)+"***"+timeStamp;
        } catch (IOException e) {
            e.printStackTrace();
        }


        db.pictureDAO().insert(new Picture(title,latitude,longitude,currentPhotoPath));

        return image;
    }



}
