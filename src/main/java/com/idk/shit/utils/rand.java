package com.idk.shit.utils;
import java.util.Random;
public class rand {
    public int rand(int[] values, int[] weights){
         int totalWeight = 0;
         for (int weight : weights) {
             totalWeight += weight;
         }
 
         Random rand = new Random();
         int randomValue = rand.nextInt(totalWeight);
 
         int cumulativeWeight = 0;
         for (int i = 0; i < values.length; i++) {
             cumulativeWeight += weights[i];
             if (randomValue < cumulativeWeight) {
                 return values[i];
             }
         }
         return values[values.length - 1];
 
     }
     public float rand_x(float left, float right){
        Random ran = new Random();
        return (left+ran.nextFloat()*(right-left));
     }

 }