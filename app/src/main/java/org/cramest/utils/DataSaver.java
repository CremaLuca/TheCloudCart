package org.cramest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataSaver {

    private static DataSaver instance;

    public static DataSaver getInstance(){
        if(instance == null){
            instance = new DataSaver();
        }
        return instance;
    }

    public void saveDataString(Context c,String tag,String value){
        // Create object of SharedPreferences.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putString(tag, value);
        //commits your edits
        editor.commit();
        //System.out.println("Salvataggio riuscito di "+value+" ? " + editor.commit());
    }
    public void saveDataInt(Context c,String tag,int value){
        // Create object of SharedPreferences.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putInt(tag, value);
        //commits your edits
        editor.commit();
    }

    public String getDataString(Context c,String tag){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getString(tag, null);
    }
    public int getDataInt(Context c,String tag){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getInt(tag, 0);
    }

    public void clearData(Context c){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }
}
