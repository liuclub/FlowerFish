package com.bagelplay.gameset.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangtianjie on 2017/8/31.
 */

public class RandNum {



    //从0到num-1
    public static int randNum(int num) {
        Random r = new Random();
        return Math.abs(r.nextInt() % num);
    }

    //一组长度为numsize,的随机数组
    public static int[] getRandNumNumArray(int numsize){

        //得到一个从1-numsize的初始数组

        List<Integer> numArray =new ArrayList<>();

        for(int i=0;i<numsize;i++){

            numArray.add(i);
        }


        //随机后数组
        int [] resultArray=new int[numsize];

        for(int j=numsize-1;j>=0;j--){



            int temp=randNum(j+1);

            resultArray[j]=numArray.get(temp);
            numArray.remove(temp);



        }


        return resultArray;

    }
}
