# OGP Alchemy

A Java implementation of an alchemical RPG domain model, developed as the exam project for *Object-Oriented Programming 2025–2026* at KU Leuven.

---

## Overview

OGP Alchemy models the core business logic of a medieval RPG centred around alchemical ingredients, containers, devices, a laboratory, and recipes. The project focuses exclusively on object-oriented design — there is no graphical interface or user interaction layer.

The codebase demonstrates applied OOP principles including encapsulation strategies (defensive, nominal, and total programming), bidirectional associations, domain-driven modelling, and layered abstraction.

---

## Project Structure

```
src/
├── AlchemicIngredient.java     # Ingredient instances with quantity & temperature
├── IngredientType.java         # Immutable substance properties (name, standard temperature, standard state)
├── IngredientContainer.java    # Physical containers for transporting and storing ingredients
├── devices/
│   ├── CoolingBox.java         # Cools a single ingredient to a configured target temperature
│   ├── Oven.java               # Heats a single ingredient (±5 unit precision)
│   ├── Kettle.java             # Combines multiple ingredients into a new compound
│   └── Transmogrifier.java     # Converts ingredient state between Liquid and Powder
├── Laboratory.java             # Houses devices and manages ingredient inventory
├── Recipe.java                 # Ordered sequence of alchemical operations
└── RecipeBook.java             # Ordered collection of recipes

test/
├── CoolingBoxTest.java
├── OvenTest.java
├── KettleTest.java
└── TransmogrifierTest.java
```

---

## Domain Model

### Ingredients

Each `AlchemicIngredient` is characterised by a name, type, state (Liquid or Powder), quantity, and temperature. Names support three forms: simple, full (with affixes such as `Heated` or `Cooled`), and special (mixture alias). Temperatures are represented as `[coldness, hotness]` pairs bounded within `[0, 10000]`. Quantities are immutable — any operation that changes an amount produces a new ingredient instance.

### Units of Measurement

| Liquid | Factor | Powder | Factor |
|---|---|---|---|
| drop | — | pinch | — |
| spoon | 8 drops | spoon | 6 pinches |
| vial | 5 spoons | sachet | 7 spoons |
| bottle | 3 vials | box | 6 sachets |
| jug | 7 bottles | sack | 3 boxes |
| barrel | 12 jugs | chest | 10 sacks |
| storeroom | 5 barrels | storeroom | 5 chests |

### Devices

All devices share a consistent interface: `add(IngredientContainer)`, `execute()`, and `result()`.

| Device | Behaviour |
|---|---|
| **CoolingBox** | Reduces an ingredient's temperature to a configured target; never heats |
| **Oven** | Raises an ingredient's temperature to a configured hotness, with ±5 unit tolerance |
| **Kettle** | Merges an unlimited number of ingredients; derives the resulting name, state, quantity, and temperature by defined domain rules |
| **Transmogrifier** | Converts state between Liquid and Powder, rounding down on fractional loss |

### Laboratory

The laboratory serves as the central inventory manager. It normalises ingredient temperatures on storage, merges newly added stock with existing inventory of the same type (via the Kettle), and supports retrieval by name and quantity or by full available stock. Each laboratory holds at most one device of each type, and the device–laboratory relationship is bidirectional.

### Recipes and Recipe Books

A `Recipe` is composed of three parallel lists: operations, ingredient names, and quantities. Supported operations are `add`, `heat` (+10 temperature units), `cool` (−10 temperature units), and `mix`. The laboratory's `execute(Recipe, int)` method runs a recipe against a quantity multiplier, handling partial execution gracefully by returning intermediate results and unused ingredients to storage. A `RecipeBook` is an ordered collection of recipes; removing a recipe from one book has no effect on others.

---

## Design Decisions

| Concern | Strategy | Rationale |
|---|---|---|
| Name validation | Defensive | Guards against invalid state at construction; designed to accommodate new allowed characters with minimal change |
| IngredientType | Total | Accepts any input and maps it to a valid internal representation |
| Temperature | Total | Upper bound is configurable to accommodate future specification changes |
| Quantity | Nominal | Caller is responsible for supplying valid values; documented in contracts |
| Laboratory storage | Defensive | Protects inventory integrity against invalid or conflicting additions |

---

## UML Design

A UML class diagram is included in the repository root (`uml_design.pdf`). It covers all implemented classes — excluding exception and test classes — and documents class names, fields, and public method signatures.

---

## Getting Started

**Prerequisites**

- Java 11 or higher
- JUnit 5

**Setup**

1. Clone the repository.
2. Open the project in IntelliJ IDEA (or any Java IDE) via **File → Open**.
3. Ensure JUnit 5 is on the classpath.
4. Run the test suite via **Run → Run All Tests**, or execute individual test classes.

---

## Testing

Tests target the four device classes and the methods they invoke, using JUnit 5. The test scope follows the project specification — exhaustive coverage of every method is out of scope, but all core device behaviours are exercised.

---

## Authors

Loïck Sansen · Casper Vermeeren

*KU Leuven — Faculty of Physics & Computer Science, 2025–2026*
