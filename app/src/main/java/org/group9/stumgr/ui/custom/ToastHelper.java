package org.group9.stumgr.ui.custom;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
   private final Context ctx;

   public ToastHelper(Context ctx) {
      this.ctx = ctx;
   }

   public void showShort(String fmt, Object... args) {
      String text = String.format(fmt, args);
      Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
   }
}
