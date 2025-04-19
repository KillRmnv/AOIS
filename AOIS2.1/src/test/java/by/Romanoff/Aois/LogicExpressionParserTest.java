package by.Romanoff.Aois;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpressionParserTest {

    @Test
    void testParseBasicExpressions() {
        LogicExpressionParser parser = new LogicExpressionParser();
        LinkedHashMap<String, Character> Statements = new LinkedHashMap<>();
        HashMap<Integer, String> result = parser.parseOnBasicExpressions("(a&b)|!c",Statements);

        assertEquals(3, result.size());
        assertTrue(result.containsValue("α&β"));
        assertTrue(result.containsValue("!γ"));
        assertTrue(result.containsValue("1|0"));
    }

    @Test
    void testParseNestedExpressions() {
        LogicExpressionParser parser = new LogicExpressionParser();
        LinkedHashMap<String, Character> Statements = new LinkedHashMap<>();
        HashMap<Integer, String> result = parser.parseOnBasicExpressions("((a|b)&c)|!d",Statements);

        assertEquals(4, result.size());
        assertTrue(result.containsValue("α|β"));
        assertTrue(result.containsValue("1&γ"));
        assertTrue(result.containsValue("!δ"));
        assertTrue(result.containsValue("2|0"));
    }

    @Test
    void testParseSimpleExpression() {
        LogicExpressionParser parser = new LogicExpressionParser();
        LinkedHashMap<String, Character> Statements = new LinkedHashMap<>();
        HashMap<Integer, String> result = parser.parseOnBasicExpressions("a&b",Statements);

        assertEquals(1, result.size());
        assertTrue(result.containsValue("α&β"));
    }

    @Test
    void testParseSingleVariable() {
        LogicExpressionParser parser = new LogicExpressionParser();
        LinkedHashMap<String, Character> Statements = new LinkedHashMap<>();
        HashMap<Integer, String> result = parser.parseOnBasicExpressions("a",Statements);

        assertEquals(0, result.size());
    }
    @Test
    void testParseExpressionWithNegation() {
        LogicExpressionParser parser = new LogicExpressionParser();
        LinkedHashMap<String, Character> Statements = new LinkedHashMap<>();
        HashMap<Integer, String> result = parser.parseOnBasicExpressions("!(a&b)",Statements);

        assertEquals(2, result.size());
        assertTrue(result.containsValue("α&β"));
        assertTrue(result.containsValue("!0"));
    }

    @Test
    void testParseComplexExpression() {
        LogicExpressionParser parser = new LogicExpressionParser();
        LinkedHashMap<String, Character> Statements = new LinkedHashMap<>();
        HashMap<Integer, String> result = parser.parseOnBasicExpressions("!(a&b)|c&(d|e)",Statements);

        assertEquals(5, result.size());
        assertTrue(result.containsValue("α&β"));
        assertTrue(result.containsValue("!0"));
        assertTrue(result.containsValue("δ|ε"));
        assertTrue(result.containsValue("γ&2"));
        assertTrue(result.containsValue("1|3"));
    }
}