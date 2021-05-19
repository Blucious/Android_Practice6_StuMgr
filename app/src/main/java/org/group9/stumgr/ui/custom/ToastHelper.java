package org.group9.stumgr.ui.custom;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
   private final Context ctx;

   public ToastHelper(Context ctx) {
      this.ctx = ctx;
   }

   public void showShort(String fmt, Object... args) {
      show(Toast.LENGTH_SHORT, fmt, args);
   }

   public void showLong(String fmt, Object... args) {
      show(Toast.LENGTH_LONG, fmt, args);
   }

   public void show(int duration, String fmt, Object... args) {
      String text = String.format(fmt, args);
      Toast.makeText(ctx, text, duration).show();
   }
}
