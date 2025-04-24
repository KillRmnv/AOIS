package by.Romanoff.Aois;

public class Main {
    public static void main(String[] args) {
        Subtractor subtractor = new Subtractor();
        subtractor.createTable();
        TetradConverter converter = new TetradConverter();
        var result = converter.adding();
        converter.print(result);
        converter.minimizeTetrads(result);
    }
}