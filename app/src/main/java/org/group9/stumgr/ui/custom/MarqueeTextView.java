package org.group9.stumgr.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * Reference: https://www.cnblogs.com/xch-yang/p/5996842.html
 */
public class MarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {

   public MarqueeTextView(Context context) {
      super(context);
   }

   public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
   }

   public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   @Override
   public boolean isFocused() {
      return true;
   }
}
