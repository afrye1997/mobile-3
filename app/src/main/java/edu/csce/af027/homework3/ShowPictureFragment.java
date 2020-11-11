package edu.csce.af027.homework3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
//        root.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
//
//        }




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
       setPic(picture);
    }

    public static ShowPictureFragment newInstance() {
        ShowPictureFragment fragment = new ShowPictureFragment();
        Log.d("in image frag","new instance");
        return fragment;
    }


    private void setPic(Picture picture) {
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


        Bitmap bitmap = BitmapFactory.decodeFile(picture.getPicturePath(), bmOptions);
        myImageView.setImageBitmap(bitmap);
    }

}
