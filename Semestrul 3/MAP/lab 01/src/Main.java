import java.util.Scanner;

import ComplexNumber.ComplexNumberParser;
import Factory.Factory;
import Factory.ExpressionFactory;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Factory factory = new ExpressionFactory();
        var expr = factory.createExpression(
                ComplexNumberParser.getOperation(input),
                ComplexNumberParser.parseExpression(input)
        );

        var result = expr.executeExpression();
        System.out.println(result);
    }
}