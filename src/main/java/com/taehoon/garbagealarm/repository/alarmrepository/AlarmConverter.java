package com.taehoon.garbagealarm.repository.alarmrepository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.room.TypeConverter;

public class AlarmConverter {

    @TypeConverter
    public static ArrayList<String> stringToDayList(String data) {

        Type listType = new TypeToken<ArrayList<String>>() {}.getType();

        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String dayListToString(ArrayList<String> someObjects) {

        Gson gson = new Gson();
        String json = gson.toJson(someObjects);

        return json;
    }

}
