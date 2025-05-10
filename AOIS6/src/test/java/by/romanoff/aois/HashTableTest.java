package by.romanoff.aois;

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
        hashTable.insert("key1", "value2");
        assertEquals("value1", hashTable.search("key1"));
    }

    @Test
    void testInsertAfterDelete() {
        hashTable.insert("key1", "value1");
        assertTrue(hashTable.delete("key1"));
        hashTable.insert("key1", "value2");
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
        assertEquals(0.2, hashTable.loadFactor());
    }

    @Test
    void testCollisionHandling() {
        hashTable.insert("a", "valueA");
        hashTable.insert("aA", "valueAA");
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
        hashTable.insert("Футбол", "Премьер-лига,Ла Лига,Серия А,Бундеслига,Лига 1");
        hashTable.insert("Премьер-лига", "Arsenal, Aston Villa, Bournemouth, Brentford, Brighton & Hove Albion, Chelsea, Crystal Palace, Everton, Fulham, " +
                "Liverpool, Luton Town, Manchester City, Manchester United, Newcastle United, Nottingham Forest, Sheffield United, Tottenham Hotspur, " +
                "West Ham United, Wolverhampton Wanderers, Burnley.");
        hashTable.insert("Arsenal", "David Raya, William Saliba, Ben White, Gabriel, Kieran Tierney, Thomas Partey, Declan Rice, Martin Ødegaard, Bukayo Saka, Gabriel Martinelli, Kai Havertz");
        hashTable.insert("David Raya", "Goalkeeper");
        hashTable.insert("William Saliba", "Defender");
        hashTable.insert("Ben White", "Defender");
        hashTable.insert("Gabriel", "Defender");
        hashTable.insert("Kieran Tierney", "Defender");
        hashTable.insert("Thomas Partey", "Midfielder");
        hashTable.insert("Declan Rice", "Midfielder");
        hashTable.insert("Martin Ødegaard", "Midfielder");
        hashTable.insert("Bukayo Saka", "Forward");
        hashTable.insert("Gabriel Martinelli", "Forward");
        hashTable.insert("Kai Havertz", "Forward");

        hashTable.insert("Aston Villa", "Emiliano Martínez, Matty Cash, Ezri Konsa, Pau Torres, Lucas Digne, Amadou Onana, Youri Tielemans, John McGinn, Jacob Ramsey, Morgan Rogers, Ollie Watkins");
        hashTable.insert("Emiliano Martínez", "Goalkeeper");
        hashTable.insert("Matty Cash", "Defender");
        hashTable.insert("Ezri Konsa", "Defender");
        hashTable.insert("Pau Torres", "Defender");
        hashTable.insert("Lucas Digne", "Defender");
        hashTable.insert("Amadou Onana", "Midfielder");
        hashTable.insert("Youri Tielemans", "Midfielder");
        hashTable.insert("John McGinn", "Midfielder");
        hashTable.insert("Jacob Ramsey", "Midfielder");
        hashTable.insert("Morgan Rogers", "Forward");
        hashTable.insert("Ollie Watkins", "Forward");

        hashTable.printTable();
    }
}
