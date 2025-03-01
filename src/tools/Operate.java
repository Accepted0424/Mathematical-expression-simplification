package tools;

import expr.Mono;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Operate {
    public static ArrayList<Mono> mul(ArrayList<Mono> left, ArrayList<Mono> right) {
        //先合并同类相
        ArrayList<Mono> mergedLeft = merge(left);
        ArrayList<Mono> mergedRight = merge(right);

        ArrayList<Mono> monos = new ArrayList<>();
        for (Mono l: mergedLeft) {
            for (Mono r: mergedRight) {
                monos.add(simpleMul(l,r));
            }
        }
        return monos;
    }

    public static ArrayList<Mono> add(ArrayList<Mono> left, ArrayList<Mono> right) {
        ArrayList<Mono> addedMonos = new ArrayList<>();
        addedMonos.addAll(left);
        addedMonos.addAll(right);
        return merge(addedMonos);
    }

    private static Mono simpleMul(Mono left, Mono right) {
        BigInteger leftCoe = left.getCoe();
        BigInteger rightCoe = right.getCoe();
        int leftPow = left.getVarsPow();
        int rightPow = right.getVarsPow();
        return new Mono(leftCoe.multiply(rightCoe), leftPow + rightPow);
    }

    private static Mono simpleAdd(Mono left, Mono right) {
        if (left.getVarsPow() == right.getVarsPow()) {
            return new Mono(left.getCoe().add(right.getCoe()), left.getVarsPow());
        } else {
            return null;
        }
    }

    //合并同类项
    public static ArrayList<Mono> merge(ArrayList<Mono> monos) {
        ArrayList<Mono> mergedMonos = new ArrayList<>();
        Map<Integer, List<Mono>> map = monos.stream()
            .collect(Collectors.groupingBy(Mono::getVarsPow));
        map.forEach((power, monoList) -> {
            // 对每一组的 Mono 按幂次进行合并
            BigInteger totalCoefficient = monoList.stream()
                .map(Mono::getCoe)
                .reduce(BigInteger.ZERO, BigInteger::add);
            mergedMonos.add(new Mono(totalCoefficient, power)); // 添加合并后的结果
        });
        return mergedMonos;
    }

    public static String mergeSymbol(String s) {
        String merged = "";
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

}
