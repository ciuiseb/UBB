package ComplexExpression;

import ComplexNumber.ComplexNumber;

import java.util.LinkedList;

public class ComplexDivision extends ComplexExpression{
    public ComplexDivision(LinkedList<ComplexNumber> nr) {
        super(nr);
    }

    @Override
    public ComplexNumber execute(ComplexNumber a, ComplexNumber b) {
        var denominator = b.getRealPart() * b.getRealPart() + b.getImaginaryPart() * b.getImaginaryPart();
        return new ComplexNumber((a.getRealPart() * b.getRealPart() + a.getImaginaryPart() * b.getImaginaryPart()) / denominator,
                (a.getImaginaryPart() * b.getRealPart() - a.getRealPart() * b.getImaginaryPart()) / denominator);
    }
}
