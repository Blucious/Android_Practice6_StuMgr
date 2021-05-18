package org.group9.stumgr.databinding;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;


public class DataBindingAdapters {

   @SuppressLint("SetTextI18n")
   @BindingAdapter("android:text")
   public static void setInteger(TextView view, Integer val) {
      if (val == null) {
         view.setText("");
      } else {
         view.setText(Integer.toString(val));
      }
   }

   @SuppressLint("SetTextI18n")
   @BindingAdapter("android:text")
   public static void setInteger(EditText view, Integer val) {
      if (val == null) {
         view.setText("");
      } else {
         view.setText(Integer.toString(val));
      }
   }

   @InverseBindingAdapter(attribute = "android:text")
   public static Integer getInteger(EditText view) {
      Editable editable = view.getText();
      if (editable.length() == 0) {
         return null;
      }
      return Integer.parseInt(editable.toString());
   }


   /*
    ---------------------------------------------------
    References:
      Set drawable resource ID in android:src for ImageView using data binding in Android
      https://stackoverflow.com/questions/35809290/set-drawable-resource-id-in-androidsrc-for-imageview-using-data-binding-in-andr/36941125#36941125
    */
   @BindingAdapter("android:src")
   public static void setImageUri(ImageView view, String imageUri) {
      if (imageUri == null) {
         view.setImageURI(null);
      } else {
         view.setImageURI(Uri.parse(imageUri));
      }
   }

   @BindingAdapter("android:src")
   public static void setImageUri(ImageView view, Uri imageUri) {
      view.setImageURI(imageUri);
   }

   @BindingAdapter("android:src")
   public static void setImageDrawable(ImageView view, Drawable drawable) {
      view.setImageDrawable(drawable);
   }

   @BindingAdapter("android:src")
   public static void setImageDrawable(ImageView view, Bitmap bitmap) {
      view.setImageBitmap(bitmap);
   }

   @BindingAdapter("android:src")
   public static void setImageResource(ImageView imageView, int resource) {
      imageView.setImageResource(resource);
   }
}
