package org.group9.stumgr.ui.custom;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;

/**
 * 在AppCompatActivity的基础上增加一些导航功能
 * 1.显示标题旁边的返回按钮
 */
public class NavigableAppCompatActivity extends BaseAppCompatActivity {

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      initView();
   }

   private void initView() {
      // 显示标题栏上加上返回按钮
      Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> {
         actionBar.setHomeButtonEnabled(true);
         actionBar.setDisplayHomeAsUpEnabled(true);
      });

   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int id = item.getItemId();

      // 处理返回按钮选中事件
      if (id == android.R.id.home) {
         finish();
      } else {
         return super.onOptionsItemSelected(item);
      }
      return true;
   }

}
