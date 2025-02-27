import expr.Expr;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String s = Pattern.compile("[ \\t]").matcher(input).replaceAll("");
        s = Pattern.compile("-").matcher(s).replaceAll("+-");

        Expr expr = new Expr(s);

        System.out.println(expr);

    }
}