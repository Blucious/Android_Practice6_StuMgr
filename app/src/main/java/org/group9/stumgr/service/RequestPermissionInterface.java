package org.group9.stumgr.service;

import android.app.Activity;
import android.content.Context;

//授权接口
public interface RequestPermissionInterface {
   boolean RequestPermission(Context context, Activity stuactivity);
}
