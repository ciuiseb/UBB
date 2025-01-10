package ComplexExpression;

import ComplexNumber.ComplexNumber;

import java.util.LinkedList;

public class ComplexSubstraction extends ComplexExpression {
    public ComplexSubstraction(LinkedList<ComplexNumber> nr) {
        super(nr);
    }

    @Override
    public ComplexNumber execute(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.getRealPart() - b.getRealPart(),
                a.getImaginaryPart() - b.getImaginaryPart());
    }
}
