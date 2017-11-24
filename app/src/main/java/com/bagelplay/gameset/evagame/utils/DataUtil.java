package com.bagelplay.gameset.evagame.utils;

import android.content.Context;
import android.util.Log;

import com.bagelplay.gameset.R;
import com.bagelplay.gameset.evagame.doman.Food;
import com.bagelplay.gameset.utils.LogUtils;
import com.bagelplay.gameset.utils.RandNum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by liubo on 2017/10/31.
 */

public class DataUtil {

    public static List<Food> getFruitData(Context context) {
        Random random = new Random();
        String[] fruits_list_cn = context.getResources().getStringArray(R.array.fruits_list_cn);
        String[] fruits_list_en = context.getResources().getStringArray(R.array.fruits_list_en);

        List<Food> list = new ArrayList<>();
        list.add(new Food(fruits_list_cn[0], fruits_list_en[0], R.raw.cf_apple, R.raw.ef_apple, R.drawable.f_apple));
        list.add(new Food(fruits_list_cn[1], fruits_list_en[1], R.raw.cf_banana, R.raw.ef_banana, R.drawable.f_banana));
        list.add(new Food(fruits_list_cn[2], fruits_list_en[2], R.raw.cf_cantaloupe, R.raw.ef_cantaloupe, R.drawable.f_cantaloupe));
        list.add(new Food(fruits_list_cn[3], fruits_list_en[3], R.raw.cf_cherry, R.raw.ef_cherry, R.drawable.f_cherry));
        list.add(new Food(fruits_list_cn[4], fruits_list_en[4], R.raw.cf_coconut, R.raw.ef_coconut, R.drawable.f_coconuts));
        list.add(new Food(fruits_list_cn[5], fruits_list_en[5], R.raw.cf_dates, R.raw.ef_dates, R.drawable.f_dates));
        list.add(new Food(fruits_list_cn[6], fruits_list_en[6], R.raw.cf_dragonfruit, R.raw.ef_dragonfruit, R.drawable.f_dragonfruit));
        list.add(new Food(fruits_list_cn[7], fruits_list_en[7], R.raw.cf_grapes, R.raw.ef_grapes, R.drawable.f_grapes));
        list.add(new Food(fruits_list_cn[8], fruits_list_en[8], R.raw.cf_honeydew, R.raw.ef_honeydew, R.drawable.f_honeydew));
        list.add(new Food(fruits_list_cn[9], fruits_list_en[9], R.raw.cf_kiwi, R.raw.ef_kiwi, R.drawable.f_kiwi));
        list.add(new Food(fruits_list_cn[10], fruits_list_en[10], R.raw.cf_lemon, R.raw.ef_lemon, R.drawable.f_lemon));
        list.add(new Food(fruits_list_cn[11], fruits_list_en[11], R.raw.cf_mango, R.raw.ef_mango, R.drawable.f_mango));
        list.add(new Food(fruits_list_cn[12], fruits_list_en[12], R.raw.cf_orange, R.raw.ef_orange, R.drawable.f_orange));
        list.add(new Food(fruits_list_cn[13], fruits_list_en[13], R.raw.cf_peach, R.raw.ef_peach, R.drawable.f_peach));
        list.add(new Food(fruits_list_cn[14], fruits_list_en[14], R.raw.cf_pear, R.raw.ef_pear, R.drawable.f_pear));
        list.add(new Food(fruits_list_cn[15], fruits_list_en[15], R.raw.cf_persimmons, R.raw.ef_persimmons, R.drawable.f_persimmons));
        list.add(new Food(fruits_list_cn[16], fruits_list_en[16], R.raw.cf_pineapple, R.raw.ef_pineapple, R.drawable.f_pineapple));
        list.add(new Food(fruits_list_cn[17], fruits_list_en[17], R.raw.cf_strawberry, R.raw.ef_strawberry, R.drawable.f_strawberry));
        list.add(new Food(fruits_list_cn[18], fruits_list_en[18], R.raw.cf_watermelon, R.raw.ef_watermelon, R.drawable.f_watermelon));

//        Collections.shuffle(list, random);
        List<Food> tempList = new ArrayList<>();
        int size = list.size();
        a:for (;;){
            int randNum = RandNum.randNum2(size);
            Food tempFood = list.get(randNum);
            if (!tempList.contains(tempFood)) {
                tempList.add(tempFood);
            }
            if (tempList.size()==4) {
                break a;
            }
        }

        return tempList;
    }

    public static List<Food> getVegetableData(Context context) {
        Random random = new Random();
        String[] vegetables_list_cn = context.getResources().getStringArray(R.array.vegetables_list_cn);
        String[] vegetable_list_en = context.getResources().getStringArray(R.array.vegetables_list_en);

        List<Food> list = new ArrayList<>();
        list.add(new Food(vegetables_list_cn[0], vegetable_list_en[0], R.raw.cv_broccoli, R.raw.ev_broccoli, R.drawable.v_broccoli));
        list.add(new Food(vegetables_list_cn[1], vegetable_list_en[1], R.raw.cv_carrot, R.raw.ev_carrot, R.drawable.v_carrot));
        list.add(new Food(vegetables_list_cn[2], vegetable_list_en[2], R.raw.cv_celery, R.raw.ev_celery, R.drawable.v_celery));
        list.add(new Food(vegetables_list_cn[3], vegetable_list_en[3], R.raw.cv_corn, R.raw.ev_corn, R.drawable.v_corn));
        list.add(new Food(vegetables_list_cn[4], vegetable_list_en[4], R.raw.cv_cucumber, R.raw.ev_cucumber, R.drawable.v_cucumber));
        list.add(new Food(vegetables_list_cn[5], vegetable_list_en[5], R.raw.cv_eggplant, R.raw.ev_eggplant, R.drawable.v_eggplant));
        list.add(new Food(vegetables_list_cn[6], vegetable_list_en[6], R.raw.cv_garlic, R.raw.ev_garlic, R.drawable.v_garlic));
        list.add(new Food(vegetables_list_cn[7], vegetable_list_en[7], R.raw.cv_greenpeas, R.raw.ev_greenpeas, R.drawable.v_greenpeas));
        list.add(new Food(vegetables_list_cn[8], vegetable_list_en[8], R.raw.cv_lettuce, R.raw.ev_lettuce, R.drawable.v_lettuce));
        list.add(new Food(vegetables_list_cn[9], vegetable_list_en[9], R.raw.cv_onion, R.raw.ev_onion, R.drawable.v_onion));
        list.add(new Food(vegetables_list_cn[10], vegetable_list_en[10], R.raw.cv_pepper, R.raw.ev_pepper, R.drawable.v_pepper));
        list.add(new Food(vegetables_list_cn[11], vegetable_list_en[11], R.raw.cv_potato, R.raw.ev_potato, R.drawable.v_potato));
        list.add(new Food(vegetables_list_cn[12], vegetable_list_en[12], R.raw.cv_tomato, R.raw.ev_tomato, R.drawable.v_tomato));

        Collections.shuffle(list, random);
        List<Food> tempList = new ArrayList<>();
        int size = list.size();
        a:for (;;){
            int randNum = RandNum.randNum2(size);
            Food tempFood = list.get(randNum);
            if (!tempList.contains(tempFood)) {
                tempList.add(tempFood);
            }
            if (tempList.size()==4) {
                break a;
            }
        }

        return tempList;
    }
}
