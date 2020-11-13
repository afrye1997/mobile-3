package edu.csce.af027.homework3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.List;

public class ShowPictureFragment extends Fragment {
    ImageView myImageView;
    Picture picture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("in image frag","on create view");

        View root = inflater.inflate(R.layout.showimageview, container, false);
        myImageView = root.findViewById(R.id.ivMyImageView);

        Bundle bundle = getArguments();
         picture= (Picture) bundle.getSerializable("pic");





        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
           //setPic(picture);
    }

    public void onResume() {
        super.onResume();
//        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        Log.i("IsRefresh", "Yes");
        try {
            setPic(picture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ShowPictureFragment newInstance() {
        ShowPictureFragment fragment = new ShowPictureFragment();
        Log.d("in image frag","new instance");
        return fragment;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private void setPic(Picture picture) throws IOException {
        ExifInterface exif = new ExifInterface(picture.getPicturePath());
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);

        Matrix matrix = new Matrix();
        if (rotation != 0) {matrix.preRotate(rotationInDegrees);}



        // Get the dimensions of the View
        int targetW = myImageView.getWidth();
        int targetH = myImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(picture.getPicturePath(), bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        Log.d("math h", String.valueOf(photoH));
        Log.d("math w", String.valueOf(photoW));

        Log.d("tar h", String.valueOf(targetH));
        Log.d("tar w", String.valueOf(targetW));


        // Determine how much to scale down the image
    //  int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
       int scaleFactor = Math.max(1, Math.min(photoW/1080, photoH/1776));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

//Geocoder geocoder= new Geocoder(getContext());
//        List<Address> addresses = null;
//        String title= null;
//        try {
//            addresses = geocoder.getFromLocation(picture.latitude, picture.longitude, 1);
//            Log.d("address", addresses.get(0).getAddressLine(0));
//            title= addresses.get(0).getAddressLine(0)+"***";
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Bitmap bitmap = BitmapFactory.decodeFile(picture.getPicturePath(), bmOptions);
        Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, photoW, photoH, matrix, true);

//        myImageView.setImageBitmap(bitmap);
        myImageView.setImageBitmap(adjustedBitmap);
    }

}
