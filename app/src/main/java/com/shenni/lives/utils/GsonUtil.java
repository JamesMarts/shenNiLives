package com.shenni.lives.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class GsonUtil {


    // 将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = null;
        try {
            result = gson.fromJson(jsonData, type);
        } catch (Exception e) {
        }
        return result;
    }

    //将Json数组解析成相应的映射对象列表
    public static <T> List<T> parseJsonArrayWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
//        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
//        List<T> result = gson.fromJson(jsonData, listType);
//        return result;

        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(jsonData).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, type));
        }
        return mList;
    }


//
//	// 将Json数据解析成相应的映射对象
//	public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
//		Gson gson = new Gson();
//		T result = gson.fromJson(jsonData, type);
//		return result;
//	}
//
//	public static <T> T parseJsonWithGson(JSONObject jsonData, Class<T> type) {
//		Gson gson = new Gson();
//		String json = jsonData.toString();
//		T result = gson.fromJson(json, type);
//		return result;
//	}
//
//	public static <T> T parseJsonWithGson(Object jsonData, Class<T> type) {
//		Gson gson = new Gson();
//		T result = gson.fromJson(gson.toJson(jsonData), type);
//		return result;
//	}
//
//	// 将Json数组解析成相应的映射对象列表
//	public static <T> List<T> parseJsonArrayWithGson(String jsonData,
//                                                     Class<T> type) {
//		Gson gson = new Gson();
//		// Type listType = new TypeToken<ArrayList<T>>(){}.getType();
//		// List<T> result = gson.fromJson(jsonData, listType);
//		// return result;
//
//		ArrayList<T> mList = new ArrayList<T>();
//		JsonArray array = new JsonParser().parse(jsonData).getAsJsonArray();
//		for (final JsonElement elem : array) {
//			mList.add(gson.fromJson(elem, type));
//		}
//		return mList;
//	}
//
//	public static <T> List<T> parseJsonArrayWithGson(Object jsonData,
//                                                     Class<T> type) {
//		Gson gson = new Gson();
//		// Type listType = new TypeToken<ArrayList<T>>(){}.getType();
//		// List<T> result = gson.fromJson(jsonData, listType);
//		// return result;
//
//		ArrayList<T> mList = new ArrayList<T>();
//		JsonArray array = new JsonParser().parse(gson.toJson(jsonData))
//				.getAsJsonArray();
//		for (final JsonElement elem : array) {
//			mList.add(gson.fromJson(elem, type));
//		}
//		return mList;
//	}

}
