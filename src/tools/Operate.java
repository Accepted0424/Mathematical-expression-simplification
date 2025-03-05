package tools;

import expr.*;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Operate {
    public static ArrayList<AtomicElement> mul(ArrayList<AtomicElement> left, ArrayList<AtomicElement> right) {
        //先合并同类相
        ArrayList<AtomicElement> mergedLeft = merge(left);
        ArrayList<AtomicElement> mergedRight = merge(right);

        ArrayList<AtomicElement> monos = new ArrayList<>();
        for (AtomicElement l: mergedLeft) {
            for (AtomicElement r: mergedRight) {
                monos.add(simpleMul(l,r));
            }
        }
        return monos;
    }

    public static ArrayList<AtomicElement> add(ArrayList<AtomicElement> left, ArrayList<AtomicElement> right) {
        ArrayList<AtomicElement> addedMonos = new ArrayList<>();
        addedMonos.addAll(left);
        addedMonos.addAll(right);
        return merge(addedMonos);
    }

    private static AtomicElement simpleMul(AtomicElement left, AtomicElement right) {
        BigInteger leftCoe = left.getCoe();
        BigInteger rightCoe = right.getCoe();
        int leftPow = left.getVarsPow();
        int rightPow = right.getVarsPow();
        ArrayList<SinCosFactor> triFactors = left.getTriFactors();
        triFactors.addAll(right.getTriFactors());
        return new AtomicElement(leftCoe.multiply(rightCoe), leftPow + rightPow, triFactors);
    }

    private static AtomicElement simpleAdd(AtomicElement left, AtomicElement right) {
        if (left.getVarsPow() == right.getVarsPow() && left.getTriFactorsStr().equals(right.getTriFactorsStr())) {
            return new AtomicElement(left.getCoe().add(right.getCoe()), left.getVarsPow(), left.getTriFactors());
        } else if (left.getVarsPow() == right.getVarsPow()) {
            return new AtomicElement(left.getCoe().add(right.getCoe()), left.getVarsPow(), null);
        }
        else {
            return null;
        }
    }

    //合并同类项
    public static ArrayList<AtomicElement> merge(ArrayList<AtomicElement> atoms) {
        HashMap<String, AtomicElement> map = new HashMap<>();
        for (AtomicElement atom: atoms) {
            String key = atom.getVarsPow() + "_" + atom.getTriFactorsStr();
            if (map.containsKey(key)) {
                AtomicElement merged = simpleAdd(map.get(key), atom);
                if (merged != null) {
                    map.put(key, merged);
                } else {
                    map.put(key, atom);
                }
            } else {
                map.put(key, atom);
            }
        }
        return new ArrayList<>(map.values());
    }

    public static String mergeSymbol(String s) {
        String merged = "";
        // merge两边确保不会连续出现三个+或-
        merged = s.replaceAll("\\+\\+", "+");
        merged = merged.replaceAll("--", "+");
        merged = merged.replaceAll("\\+-", "-");
        merged = merged.replaceAll("-\\+", "-");
        merged = merged.replaceAll("\\+\\+", "+");
        merged = merged.replaceAll("--", "+");
        merged = merged.replaceAll("\\+-", "-");
        merged = merged.replaceAll("-\\+", "-");

        return merged;
    }

    /**
     * @param s input string
     * @return 最外层括号内的字符串和括号外的字符串（均不包括最外层括号）
     */
    public static Map.Entry<String, String> getStrInOutermostBracket(String s) {
        int inBracket = 0;
        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                if (inBracket == 0) {
                    start = i;
                }
                inBracket++;
            } else if (ch == ')') {
                inBracket--;
                if (inBracket == 0) {
                    String innerStr = s.substring(start + 1, i);
                    String outerStr = s.substring(0, start) + s.substring(i + 1);
                    return new AbstractMap.SimpleEntry<>(innerStr, outerStr);
                }
            }
        }
        return null;
    }

    public static boolean hashSameElement(ArrayList<Object> left, ArrayList<Object> right) {
        if (left.size() != right.size()) {
            return false;
        }
        if (left.getClass() != right.getClass()) {
            return false;
        }

        return true;
    }
}
