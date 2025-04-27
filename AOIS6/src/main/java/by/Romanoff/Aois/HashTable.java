package by.Romanoff.Aois;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HashTable {

    private static class Entry {
        Object id; 
        Object data; // Pi
        boolean collisionFlag; // C
        boolean occupiedFlag;  // U
        boolean terminalFlag;  // T
        boolean linkFlag;      // L
        boolean deletedFlag;   // D
        Entry next; // Po

        Entry(Object id, Object data) {
            this.id = id;
            this.data = data;
            this.collisionFlag = false;
            this.occupiedFlag = true;
            this.terminalFlag = true;
            this.linkFlag = true;
            this.deletedFlag = false;
            this.next = null;
        }

        @Override
        public String toString() {
            return String.format("ID: %s, Data: %s, C: %s, U: %s, T: %s, L: %s, D: %s, Next: %s",
                    id.toString(), data.toString(),
                    collisionFlag ? "Да" : "Нет",
                    occupiedFlag ? "Да" : "Нет",
                    terminalFlag ? "Да" : "Нет",
                    linkFlag ? "Да" : "Нет",
                    deletedFlag ? "Да" : "Нет",
                    next != null ? next.id : "null");
        }
    }

    private final List<Entry>[] table;
    private final int size;

    public HashTable(int size) {
        this.size = size;
        this.table = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(Object key) {
        if(key.getClass() == String.class) {
            int hashCode=0;
            for(int i=0;i<((String) key).length()&&i<3;i++){
                hashCode+=(((String) key).charAt(i)*69);
            }
            return Math.abs(hashCode)%size;
        }
        if(key.getClass() == Integer.class) {
            int hashCode=0;
            for(int i=0;i<((Integer) key).intValue();i++) {
                hashCode+=(((Integer) key)*69);
            }
            return Math.abs(hashCode)%size;
        }
        return Math.abs(Objects.hashCode(key)) % size;
    }

    public void insert(Object id, Object data) {
        int index = hash(id);
        List<Entry> bucket = table[index];

        for (Entry entry : bucket) {
            if (entry.id.equals(id) ) {
                if(!entry.deletedFlag) {
                    System.out.println("Ошибка: ключ уже существует!");
                    return;
                }else{
                    entry.data = data;
                    entry.id=id;
                    entry.deletedFlag=false;
                    entry.occupiedFlag=true;
                    System.out.println("Успешно вставлено: " + id);
                    return;
                }
            }
        }
        Entry newEntry = new Entry(id, data);

        if (!bucket.isEmpty()) {
            // При наличии коллизии
            newEntry.collisionFlag = true;
            if (bucket.getLast() != null) {
                bucket.getLast().terminalFlag = false;
                bucket.getLast().next=newEntry;
            }
        }

        bucket.add(newEntry);
        System.out.println("Успешно добавлено: " + id);
    }

    public Object search(Object id) {
        int index = hash(id);
        for (Entry entry : table[index]) {
            if (entry.id.equals(id) && !entry.deletedFlag) {
                return entry.data.toString();
            }
        }
        return null;
    }

    public boolean update(Object id, Object newData) {
        int index = hash(id);
        for (Entry entry : table[index]) {
            if (entry.id.equals(id) && !entry.deletedFlag) {
                entry.data = newData;
                return true;
            }
        }
        return false;
    }

    public boolean delete(Object id) {
        int index = hash(id);
        for (Entry entry : table[index]) {
            if (entry.id.equals(id) && !entry.deletedFlag) {
                entry.deletedFlag = true;
                entry.occupiedFlag = false;

                System.out.println("Удалено: " + id);
                return true;
            }
        }
        System.out.println("Ключ не найден: " + id);
        return false;
    }

    public void printTable() {
        System.out.println("------ Хеш-таблица ------");
        for (int i = 0; i < size; i++) {
            System.out.printf("[%d]: ", i);
            for (Entry entry : table[i]) {
                System.out.print(" -> {" + entry.toString() + "} ");
            }
            System.out.println();
        }
    }

    public double loadFactor() {
        int count = 0;
        for (List<Entry> bucket : table) {
            for (Entry entry : bucket) {
                if (!entry.deletedFlag) {
                    count++;
                }
            }
        }
        return (double) count / size;
    }
}