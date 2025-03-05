import expr.Expr;
import tools.Operate;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String s = Pattern.compile("[ \\t]").matcher(input).replaceAll("");
        s = Operate.mergeSymbol(s);

        Expr expr = new Expr(s);
        System.out.println(expr);
    }
}

// test case
// 3*sin(x+1) + 2 * sin(1+x)                    Accepted
// 3*sin(1+(x+1)^2) + 2*sin((1+x)^2+1)          Accepted
// 3*sin(1+(x+1)^2)*sin(x+1) + 2*sin((1+x)^2+1) Accepted
// sin(1)*x*sin(2) + sin(2-1)*sin(1+1)*x        Accepted
// sin(1)*x*sin(2) + sin(2-1)*sin(1+0)*x        Accepted