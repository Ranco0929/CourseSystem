package org.guge.coursebackend.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class SimilarityResolve {
    public static double resolve(String targetString, String sourceString) {

        double similarity=0;
        int size = targetString.length();

        String[] str1=targetString.split("[ ,.。、，？?;\\\"]+");
        String[] str2=sourceString.split("[ ,.。，、？?;\\\"]+");


        for (String s : str1) {
            double max = -1, tmp;
            String strmax = "";
            for (String value : str2) {
                tmp = PaperDuplication.compareStrings(s, value);
                if (tmp > max) {
                    max = tmp;
                    strmax = value;
                }
            }
            if (max != 0) {
                // System.out.println(s + " 单行最大相似度：" + max + " 相似行：" + strmax);
            }
            similarity += max * s.length() / size;
        }


        // System.out.println("整体相似度："+similarity);

        return similarity;
    }

}

class PaperDuplication{
    public static double compareStrings(String str1, String str2) {

        var pairs1 = wordLetterPairs(str1.toUpperCase());
        var pairs2 = wordLetterPairs(str2.toUpperCase());

        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (Object pair1 : pairs1) {
            for (int j = 0; j < pairs2.size(); j++) {
                Object pair2 = pairs2.get(j);
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }
        return  (2.0*intersection)/union  ;
    }
    private static String[] letterPairs(String str) {
        int numPairs = str.length()-1;
        String[] pairs = new String[numPairs];
        for (int i=0; i<numPairs; i++) {
            pairs[i] = str.substring(i,i+2);
        }
        return pairs;
    }

    private static List<String> wordLetterPairs(String str) {
        List<String> allPairs = new ArrayList<>();
        String[] words = str.split("\\s");

        for (String word : words) {
            String[] pairsInWord = letterPairs(word);
            allPairs.addAll(Arrays.asList(pairsInWord));
        }
        return allPairs;
    }

}


