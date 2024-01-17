package com.reflect.reflect;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {


    public static String KEY_TOKEN="token";
    private SharedPreferences sharedPreferences;
    public SharedPref(Context context)
    {
        this.sharedPreferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }

    public void saveString(String key,String value)
    {
        this.sharedPreferences.edit().putString(key, value).apply();
    }
    public String getString(String key)
    {
        return this.sharedPreferences.getString(key,"");
    }
}
