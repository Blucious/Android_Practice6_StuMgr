package org.group9.stumgr.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ContextUtils {

   public static void setTextPrimaryClip(Context context, CharSequence text) {
      ClipboardManager clipboardManager = (ClipboardManager)
         context.getSystemService(Context.CLIPBOARD_SERVICE);

      ClipData clipData = ClipData.newPlainText("text", text);

      clipboardManager.setPrimaryClip(clipData);
   }
}
