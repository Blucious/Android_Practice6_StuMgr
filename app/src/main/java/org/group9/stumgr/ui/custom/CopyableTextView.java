package org.group9.stumgr.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.group9.stumgr.util.ContextUtils;


public class CopyableTextView extends androidx.appcompat.widget.AppCompatTextView {


   public CopyableTextView(Context context) {
      super(context);
   }

   public CopyableTextView(Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
   }

   public CopyableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   @Override
   protected void onAttachedToWindow() {
      super.onAttachedToWindow();

      initView();
   }

   private void initView() {

      setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            CharSequence text = CopyableTextView.this.getText();
            Context context = getContext();

            ContextUtils.setTextPrimaryClip(context, text);

            Toast.makeText(context, "内容已复制", Toast.LENGTH_SHORT).show();
         }
      });

   }

}
