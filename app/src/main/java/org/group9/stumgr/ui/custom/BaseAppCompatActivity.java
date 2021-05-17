package org.group9.stumgr.ui.custom;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseAppCompatActivity extends AppCompatActivity {

   private ToastHelper toastHelper;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      toastHelper = new ToastHelper(this);
   }

   public ToastHelper getToastHelper() {
      return toastHelper;
   }
}
