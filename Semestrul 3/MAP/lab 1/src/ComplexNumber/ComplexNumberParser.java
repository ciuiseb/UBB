package ComplexNumber;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexNumberParser {
    public static LinkedList<ComplexNumber> parseExpression(String expression) {
        LinkedList<ComplexNumber> numbers = new LinkedList<>();

        Pattern numberPattern = Pattern.compile("([+-]?\\d*)?([+-]?\\d*)i");
        Matcher matcher = numberPattern.matcher(expression);

        while (matcher.find()) {

            if(matcher.group(2).isEmpty() && matcher.group(1).equals("-")){
                numbers.add(new ComplexNumber(0, -1));
            } else if(matcher.group(2).isEmpty() && matcher.group(1).isEmpty()){
                numbers.add(new ComplexNumber(0, 1));
            }else {
                int realPart = matcher.group(1) != null ? Integer.parseInt(matcher.group(1)) : 0;
                int imagPart = matcher.group(2).equals("+") ? 1 : matcher.group(2).equals("-") ? -1 : Integer.parseInt(matcher.group(2));

                numbers.add(new ComplexNumber(realPart, imagPart));
            }
        }

        return numbers;
    }

    public static Operation getOperation(String expression) {
        Pattern operationPattern = Pattern.compile(" [+\\-*/] ");
        Matcher matcher = operationPattern.matcher(expression);

        if (matcher.find()) {
            switch (matcher.group()) {
                case " + " -> {
                    return Operation.ADDITION;
                }
                case " - " -> {
                    return Operation.SUBTRACTION;
                }
                case " * " -> {
                    return Operation.MULTIPLICATION;
                }
                case " / " -> {
                    return Operation.DIVISION;
                }
            }
        }

        return null;
    }
}
