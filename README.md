# BLOOP Interpreter

A working interpreter for **BLOOP** (Beginner-Level Object-Oriented Program),
a small scripting language — built entirely in pure Java.

> "We didn't just learn how programming languages work. We built one."

---

## What is BLOOP?

BLOOP is a simple scripting language that reads like plain English:
```bloop
put 10 into x
put 3 into y
put x + y * 2 into result
print result

if result > 16 then:
    print "big number"

repeat 3 times:
    print "hello"
```

Output:
```
16
hello
hello
hello
```

---

## How the Interpreter Works

The interpreter is a three-stage pipeline:
```
Source Code (.bloop file)
        ↓
  [ Tokenizer ]   → breaks text into tokens
        ↓
  [   Parser  ]   → builds a tree of instructions
        ↓
  [ Evaluator ]   → executes the tree, prints output
```

---

## 📁 Project Structure
```
src/
├── lexer/          → Tokenizer: reads source code, produces tokens
├── ast/            → Expression nodes: the tree structure
├── runtime/        → Instructions and Environment (variable store)
├── parser/         → Parser: tokens → instruction list
├── interpreter/    → Connects all three stages
└── Main.java       → Entry point

samples/            → .bloop programs to test the interpreter
docs/               → Project design notes
```

---

## 👥 Team

| Member | Role | Owns |
|--------|------|------|
| Manish | Lead, Parser, Interpreter | `parser/`, `interpreter/`, `Main.java` |
| Khushi | Lexer | `lexer/` |
| Divyanshi | AST & Runtime | `ast/`, `runtime/` |

---

## ✅ Sample Programs

Four test programs are in the `samples/` folder covering:
- Arithmetic and operator precedence
- String output
- Conditional (if/then)
- Repeat loop

---

## 📚 Key Concepts Used

- Enum, Interface, Immutability (Token, AST nodes)
- Recursive Descent Parsing (operator precedence)
- Composite Pattern (Expression tree)
- HashMap-based Environment (variable store)
- Java Lambdas and Optional (Advanced OOP)

---

*Sitare University — Advanced OOP in Java — Group Project*