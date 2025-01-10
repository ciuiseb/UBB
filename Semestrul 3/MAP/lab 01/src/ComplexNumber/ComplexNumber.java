package ComplexNumber;

public class ComplexNumber {


    private int realPart;
    private int imaginaryPart;

    public ComplexNumber(int realPart, int imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public int getRealPart() {
        return realPart;
    }

    public int getImaginaryPart() {
        return imaginaryPart;
    }

    @Override
    public String toString() {
        return "ComplexNumber.ComplexNumber{" +
                realPart + " + " + imaginaryPart + "i" +
                '}';
    }
}
