# Traffic Simulator

A desktop traffic simulation built in Java and JavaFX. It models cars on different road layouts using the
[Nagel–Schreckenberg model](https://en.wikipedia.org/wiki/Nagel%E2%80%93Schreckenberg_model), visualizes the
traffic in real time and collects statistics that can be exported to CSV.

Built as a group project (4 students) at Örebro University, spring 2026.

![Simulation running](user%20manual/images/simulation.png)

## Features

- **Cellular-automaton traffic model**: roads are split into 7.5 m cells and the simulation advances in discrete
  ticks (1 tick ≈ 2.7 s). Each car has a randomized preferred speed and brakes for cars and obstacles ahead.
- **Several maps**: one-way street with traffic light, T intersection, highway (2+2 lanes), autobahn (5+5 lanes)
  and a grid of roads with intersections.
- **Overtaking** on multi-lane roads.
- **Live statistics** while the simulation runs: average speed and vehicle inflow/outflow.
- **Configurable simulations**: road length, min/max preferred speed, spawn chance and time scaling.
- **Offline mode**: runs simulations without graphics, much faster than real time, for long runs or comparisons.
- **Summary report** at the end with **CSV export**.
- **Keyboard shortcuts**: space = pause/resume, backspace = reset, escape = stop.

| Menu | Live statistics | Final report |
|---|---|---|
| ![Menu](user%20manual/images/menu.png) | ![Live stats](user%20manual/images/live-stats.png) | ![Final stats](user%20manual/images/final-stats.png) |

## Tech stack

- Java 25
- JavaFX 21
- Maven
- JUnit 5

## Running

Requires JDK 25 and Maven.

```bash
cd simulator
mvn javafx:run
```

Run the tests:

```bash
cd simulator
mvn test
```

## Project structure

```
simulator/src/main/java/com/trafficsimulator/
├── model/       Simulation engine: roads, vehicles, intersections, maps, statistics
├── view/        JavaFX views: menu, road rendering, live stats, report
├── controller/  Connects the views and the simulation
├── startup/     Program entry point
└── utility/     Constants and helpers
```

- `class-diagram.puml`: UML class diagram (PlantUML)
- `user manual/`: user manual (LaTeX source and [PDF](user%20manual/user_manual.pdf))

## My contributions

- Overtaking logic and the highway and autobahn maps
- Map selection and restructuring how maps are built (`Map`, `MapType`)
- Time scaling slider
- Road rendering (`RoadView`)
- Unit tests and Javadoc for the model layer

## Team

Developed together with Joel, Kevin and Patrik.
