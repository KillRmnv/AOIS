package by.Romanoff.Aois;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    private HashTable hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable(10);
    }

    @Test
    void testInsertAndSearch() {
        hashTable.insert("key1", "value1");
        assertEquals("value1", hashTable.search("key1"));
    }

    @Test
    void testInsertDuplicateKey() {
        hashTable.insert("key1", "value1");
        hashTable.insert("key1", "value2"); // Попытка вставить тот же ключ
        assertEquals("value1", hashTable.search("key1")); // Должно остаться старое значение
    }

    @Test
    void testInsertAfterDelete() {
        hashTable.insert("key1", "value1");
        assertTrue(hashTable.delete("key1"));
        hashTable.insert("key1", "value2"); // Должна быть успешная вставка после удаления
        assertEquals("value2", hashTable.search("key1"));
    }

    @Test
    void testUpdateExistingEntry() {
        hashTable.insert("key1", "value1");
        assertTrue(hashTable.update("key1", "newValue"));
        assertEquals("newValue", hashTable.search("key1"));
    }

    @Test
    void testUpdateNonExistingEntry() {
        assertFalse(hashTable.update("nonexistent", "value"));
    }

    @Test
    void testDeleteExistingEntry() {
        hashTable.insert("key1", "value1");
        assertTrue(hashTable.delete("key1"));
        assertNull(hashTable.search("key1"));
    }

    @Test
    void testDeleteNonExistingEntry() {
        assertFalse(hashTable.delete("nonexistent"));
    }

    @Test
    void testLoadFactor() {
        assertEquals(0.0, hashTable.loadFactor());
        hashTable.insert("key1", "value1");
        hashTable.insert("key2", "value2");
        assertEquals(0.2, hashTable.loadFactor()); // 2 записи / размер 10
    }

    @Test
    void testCollisionHandling() {
        // Создаем коллизию вручную
        // Например, ключи с похожими начальными символами для заданной хеш-функции
        hashTable.insert("a", "valueA");
        hashTable.insert("aA", "valueAA");
        // Поскольку у них, вероятно, будет одинаковый хеш-код (проверить под дебагом)
        assertEquals("valueA", hashTable.search("a"));
        assertEquals("valueAA", hashTable.search("aA"));
    }

    @Test
    void testInsertIntegerKey() {
        hashTable.insert(123, "integerValue");
        assertEquals("integerValue", hashTable.search(123));
    }
    @Test
    void printTable() {
        hashTable.insert("key1", "value1");
        hashTable.insert("2key", "value2");
        hashTable.insert("true", "value3");
        hashTable.insert("key4", "value4");
        hashTable.printTable();
    }
}
