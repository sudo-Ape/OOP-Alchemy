# ⚗️ OGP Alchemy

> A Java implementation of an alchemical RPG system — the exam project for *Object-Oriented Programming 2025–2026* at KU Leuven.

---

## 📖 Overview

OGP Alchemy models the core logic of a medieval RPG focused entirely on alchemical ingredients, containers, devices, a laboratory, and recipes. No graphical or user-interaction elements are included — this project is purely about the underlying object-oriented design.

---

## 🏗️ Project Structure

```
src/
├── AlchemicIngredient.java     # Ingredient instances with quantity & temperature
├── IngredientType.java         # Fixed substance properties (name, std. temperature, std. state)
├── IngredientContainer.java    # Physical containers for storing ingredients
├── devices/
│   ├── CoolingBox.java         # Cools a single ingredient to a set temperature
│   ├── Oven.java               # Heats a single ingredient (±5 unit precision)
│   ├── Kettle.java             # Mixes multiple ingredients into a new compound
│   └── Transmogrifier.java     # Converts ingredient state (Liquid ↔ Powder)
├── Laboratory.java             # Houses devices and stores ingredients
├── Recipe.java                 # Ordered list of alchemical operations
└── RecipeBook.java             # Ordered collection of recipes
```

---

## ✨ Features

### Ingredients
- Each ingredient has a **name**, **type**, **state** (Liquid / Powder), **quantity**, and **temperature**.
- Names support simple, full (with affixes like `Heated` / `Cooled`), and special (mixture alias) forms.
- Temperatures are represented as `[coldness, hotness]` pairs in the range `[0, 10000]`.
- Quantities are **immutable** — any operation that changes the quantity produces a new ingredient.

### Units of Measurement

| Liquid | Powder |
|--------|--------|
| drop | pinch |
| spoon (8 drops) | spoon (6 pinches) |
| vial (5 spoons) | sachet (7 spoons) |
| bottle (3 vials) | box (6 sachets) |
| jug (7 bottles) | sack (3 boxes) |
| barrel (12 jugs) | chest (10 sacks) |
| storeroom (5 barrels) | storeroom (5 chests) |

### Devices
All devices expose `add(IngredientContainer)`, `execute()`, and `result()`.

| Device | Behaviour |
|--------|-----------|
| **CoolingBox** | Cools one ingredient to a configured temperature; never heats |
| **Oven** | Heats one ingredient to a configured hotness (±5 unit deviation) |
| **Kettle** | Mixes unlimited ingredients; computes name, state, quantity, and temperature by defined rules |
| **Transmogrifier** | Converts state (Liquid ↔ Powder), rounding down on loss |

### Laboratory
- Stores ingredients by type; automatically normalises temperature on storage.
- Merges newly added ingredients with existing stock of the same name (via the Kettle).
- Retrieves by name + quantity, or returns the full available stock.
- Houses at most **one device of each type**; device–lab link is **bidirectional**.

### Recipes & Recipe Books
- A recipe is three parallel lists: **operations**, **ingredient names**, and **quantities**.
- Operations: `add`, `heat` (+10 units), `cool` (−10 units), `mix`.
- The lab's `execute(Recipe, int)` method runs a recipe with a quantity multiplier.
- Partial execution is graceful: intermediate results and unused ingredients are returned to storage.
- A `RecipeBook` is an ordered collection of recipes; removing from one book does not affect others.

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- JUnit 5 (for running tests)
- Intellij IDE (recommended) or any Java IDE

### Build & Run
```bash
# Clone the repository
git clone https://github.com/<your-team>/<repo-name>.git
cd <repo-name>

# Compile
javac -d out src/**/*.java

# Run tests (JUnit)
# Use your IDE's test runner, or configure a build tool such as Maven/Gradle
```

---

## 🧪 Testing

Tests focus on the **devices** and the methods they invoke, using the JUnit framework. Full exhaustive testing of every method is out of scope per the project spec.

```
test/
├── CoolingBoxTest.java
├── OvenTest.java
├── KettleTest.java
└── TransmogrifierTest.java
```

---

## 📐 UML Design

A UML diagram (PDF) is included in the repository root. It covers all developed classes (excluding exception and test classes), with names, fields, and public methods.

```
uml_design.pdf
```

---

## 📋 Implementation Notes

- **Name** — implemented *defensively*; designed to easily accommodate new allowed characters.
- **IngredientType** — implemented *totally*.
- **Temperature** — implemented *totally*; upper bound is configurable for future changes.
- **Quantity** — implemented *nominally*.
- **Laboratory ingredient storage** — implemented *defensively*.

---

## ⚠️ Constraints

- Generative AI was **not** used in writing code or documentation, per course policy.
- Submission deadline: **Friday 15 May 2026, 23:59** via Toledo (official KU Leuven student platform).
- Project counts for **5 / 20 points**; the remaining 15 are based on the oral exam.

---

## 👥 Authors

- <!-- Loïck Sansen -->
- <!-- Casper Vermeeren -->
