package ubb;

public class Main {
    public static void main(String[] args) {
        var al = new AnalizatorLexical("tabela_coduri.txt");
        al.getFIPandTS("program");
    }
}