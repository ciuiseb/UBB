package ComplexExpression;

import ComplexNumber.ComplexNumber;

import java.util.LinkedList;

public class ComplexMultiplication extends ComplexExpression{
    public ComplexMultiplication(LinkedList<ComplexNumber> nr) {
        super(nr);
    }

    @Override
    public ComplexNumber execute(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.getRealPart() * b.getRealPart() - b.getImaginaryPart() * a.getImaginaryPart(),
                a.getRealPart() * b.getImaginaryPart() + a.getImaginaryPart() * b.getRealPart());
    }
}
