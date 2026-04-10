# AOIS Repository

This repository contains laboratory works for the subject "Аппаратное обеспечение Интеллектуальных систем" (Hardware Support of Intelligent Systems). The course focuses on low-level implementation of computer systems.

## Repository Structure

The repository consists of 7 independent Maven projects (AOIS1 through AOIS7), each representing a separate laboratory work.

## Common Instructions

All projects use Java 21 and Maven as the build system.

### Building a Project
```bash
cd AOIS<N>
mvn clean compile
```

### Running Tests
```bash
cd AOIS<N>
mvn test
```

### Running a Specific Test
```bash
cd AOIS<N>
mvn test -Dtest=<TestClassName>
```

### Running the Main Application
```bash
cd AOIS<N>
mvn compile exec:java -Dexec.mainClass=<main-class>
```

## Project Descriptions

### AOIS1 - Laboratory Work 1: Number Representation
**Focus**: Implementation of floating-point and fixed-point numbers, direct/inverse/complement codes, arithmetic operations

This laboratory work implements:
- Binary numbers with fixed and floating point representation
- Conversion between decimal and binary formats
- Arithmetic operations (addition, subtraction) on binary numbers
- Different binary code representations (direct, inverse, complement)

**Key Classes**:
- `App.java` - Main application entry point
- `BinConverter.java` - Conversion to binary format
- `DecimalConverter.java` - Conversion from binary to decimal
- `BinFixPointNum.java` - Fixed-point binary number representation
- `BinFloatPointNum.java` - Floating-point binary number representation
- `BinNumber.java` - Abstract base class for binary numbers
- `IOHandler.java`, `ConsoleIOHandler.java`, `UI.java` - Input/output handling

### AOIS2.1 - Laboratory Work 2.1: Logical Expression Analysis
**Focus**: Building truth tables and obtaining perfect conjunctive/disjunctive normal forms (SKNF/SDNF)

This laboratory work implements:
- Parsing of logical expressions
- Generation of truth tables
- Conversion to perfect conjunctive normal form (SKNF)
- Conversion to perfect disjunctive normal form (SDNF)

**Key Classes**:
- `Main.java` - Application entry point
- `LogicExpressionAnalyzer.java` - Main analysis logic
- `LogicExpressionParser.java` - Expression parsing
- `NormalFormCreator.java` - SKNF/SDNF conversion
- `TruthTable.java` - Truth table generation
- `UI.java` - User interface

### AOIS3 - Laboratory Work 3: Logical Function Minimization
**Focus**: Implementation of three methods for minimizing logical expressions

This laboratory work implements three different methods for minimizing logical functions:

1. **Calculation Method** (`AOIS3/src/main/java/by/romanoff/aois/CalculationMethod.java`)
   - Implements an algorithmic approach to minimize logical expressions
   - Works with SKNF and SDNF forms
   - Uses consensus theorem and absorption laws for minimization

2. **Tabular Method** (`AOIS3/src/main/java/by/romanoff/aois/TableMethod.java`)
   - Implements the tabular method (Quine-McCluskey algorithm)
   - Creates implication charts
   - Finds prime implicants and minimal cover

3. **Karnaugh Map Method** (`AOIS3/src/main/java/by/romanoff/aois/KarnosMapMethod.java`)
   - Implements minimization using Karnaugh maps
   - Supports up to 4 variables in basic implementation
   - Handles variable ordering and grouping
   - Includes graph-based optimization techniques

**Key Supporting Classes**:
- `Main.java` - Application entry point
- `GluerOfLogicExpression.java` - Combines similar expressions
- `Graph.java`, `Node.java` - Graph representations for Karnaugh maps
- `Output.java` - Output formatting utilities

### AOIS4 - Laboratory Work 4: Tetrad Converter
**Focus**: Operations with tetrads (4-bit groups) and logical circuit synthesis

This laboratory work implements:
- Conversion between different tetrad representations
- Logical synthesis principles
- Basic arithmetic operations on 4-bit values

**Key Classes**:
- `Main.java` - Application entry point
- `TetradConverter.java` - Tetrad conversion operations
- `Substractor.java` - Subtraction implementation for tetrads

### AOIS5 - Laboratory Work 5: Digital Vending Machine
**Focus**: Synthesis of digital automata

This laboratory work implements:
- Finite state machine modeling
- Digital automat principles
- Control logic for a vending machine system

**Key Classes**:
- `Main.java` - Application entry point
- `DigitalVendingMachine.java` - Core automaton implementation
- State management and transition logic

### AOIS6 - Laboratory Work 6: Hash Table Modeling
**Focus**: Implementation and analysis of hash table data structures

This laboratory work implements:
- Hash table data structure with collision resolution
- Various hash functions
- Collision handling methods (chaining, open addressing)
- Performance analysis of different approaches

**Key Classes**:
- `Main.java` - Application entry point
- `HashTable.java` - Core hash table implementation
  - Supports multiple collision resolution strategies
  - Generic key-value storage
  - Hash function customization
  - Load factor monitoring and rehashing

### AOIS7 - Laboratory Work 7: Associative Processor and Memory
**Focus**: Modeling of associative processor and memory systems

This laboratory work implements:
- Associative memory principles
- Content-addressable storage
- Parallel search operations
- Associative processor architecture

**Key Classes**:
- `Main.java` - Application entry point
- `LogicFunction.java` - Representation of logical functions
- `Matrix.java` - Matrix operations for associative processing
- `GLSearchOperations.java` - Global search operations in logical functions

## Technology Stack

- **Language**: Java 21
- **Build System**: Maven
- **Testing Framework**: JUnit 5
- **Dependencies**:
  - Lombok (for reducing boilerplate code)
  - Mockito (for mocking in tests)
  - ByteBuddy (runtime code generation)

## Importing Projects

Each project can be imported as an existing Maven project in any Java IDE:
- IntelliJ IDEA: File → New → Project from Existing Sources → select pom.xml
- Eclipse: File → Import → Maven → Existing Maven Projects
- NetBeans: File → Open Project → select project directory

## Running the Projects

Each laboratory work is self-contained and can be run independently:
1. Navigate to the project directory: `cd AOIS<N>`
2. Compile the project: `mvn compile`
3. Run tests: `mvn test`
4. Execute the main class: `mvn compile exec:java -Dexec.mainClass=<appropriate-main-class>`

Refer to each project's source code to identify the correct main class for execution.