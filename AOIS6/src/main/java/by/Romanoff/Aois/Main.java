package by.Romanoff.Aois;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        HashTable ht = new HashTable(20);

        // Пример вставки данных
        ht.insert("Пифагор", "Математика");
        ht.insert("Ньютон", "Физика");
        ht.insert("Дарвин", "Биология");
        ht.insert("Ломоносов", "Наука");
        ht.insert("Толстой", "Литература");
        ht.insert("Менделеев", "Химия");
        ht.insert("Королев", "Космонавтика");
        ht.insert("Шекспир", "Литература");
        ht.insert("Эйнштейн", "Физика");
        ht.insert("Циолковский", "Космонавтика");

        ht.printTable();

        System.out.println("Поиск Пифагор: " + ht.search("Пифагор"));
        System.out.println("Поиск Дарвин: " + ht.search("Дарвин"));

        ht.update("Ньютон", "Классическая механика");
        ht.delete("Менделеев");

        ht.printTable();

        System.out.println("Коэффициент заполнения: " + ht.loadFactor());
    }
}