# Ticket Booking System — Distributed Java Application (RMI · Sockets · Swing)

![Java](https://img.shields.io/badge/Java-8%2B-orange?logo=openjdk&logoColor=white)
![RMI](https://img.shields.io/badge/Middleware-Java%20RMI-blue)
![Sockets](https://img.shields.io/badge/Networking-TCP%20Sockets-green)
![Swing](https://img.shields.io/badge/GUI-Swing-blueviolet)

A distributed event-ticketing application built in Java, split across three independently runnable components: a **Swing desktop client**, a **Java RMI server** handling users, events and bookings, and a **secondary socket-based server** for event data. Users can browse events, book and cancel tickets, and receive real-time discount notifications via RMI callbacks; admins can manage events and users.

## Architecture

```
┌────────────┐        Java RMI          ┌────────────┐       TCP Sockets       ┌────────────┐
│   Client   │ ◄──────────────────────► │  Server1   │ ◄──────────────────────►│  Server2   │
│  (Swing)   │   rmi://host:9999/       │ (RMI host, │    ObjectInput/Output    │  (socket   │
│            │   BookingService         │  port 9999)│    Streams, port 5000    │  service)  │
└────────────┘                          └────────────┘                          └────────────┘
```

- **Client** — Swing GUI (login, registration, event browsing, booking, admin panels). Communicates exclusively with **Server1** over RMI and implements a remote callback interface to receive push notifications (e.g. discount alerts).
- **Server1** — The main RMI server. Exposes `ClientRMIInterface` (login, registration, bookings, admin operations) and persists users, events and bookings to disk via Java serialization (`users.ser`, `events.ser`, `bookings.ser`). Includes a `SocketClientHelper` intended to delegate event-related operations to **Server2** over plain TCP sockets.
- **Server2** — A standalone socket server (custom `Request`/`Response` protocol over `ObjectInputStream`/`ObjectOutputStream`) dedicated to managing event data independently of Server1.

> **Note:** `Server2` and the RMI↔socket bridge (`SocketClientHelper`) are present in the codebase as a secondary data service, but `Server1`'s current RMI methods (`getEvents`, `addEvent`, `deactivateShow`, etc.) operate on Server1's own local `DataStore` rather than calling through to Server2. Wiring `SocketClientHelper` into `ClientRMIImpl` is a natural next step (see [Possible Improvements](#possible-improvements)). Also double-check the socket port constants (`SocketClientHelper` targets `9999`, while `Server2Main` listens on `5000`) before relying on that integration.

## Features

**User**
- Register / log in
- Browse available events and their shows (date, time, price, seat availability)
- Book tickets with a simulated payment
- View and cancel personal bookings
- Receive real-time discount notifications pushed from the server (RMI callback)

**Admin**
- Add new events and shows
- Deactivate a show
- List all registered users
- Delete a user

## Tech Stack

| Component      | Technology                                   |
|-----------------|-----------------------------------------------|
| Language        | Java (JDK 8+)                                 |
| Client ↔ Server1| Java RMI (`java.rmi`)                         |
| Server1 ↔ Server2| Raw TCP Sockets + Java Object Serialization  |
| GUI             | Java Swing                                    |
| Persistence     | Java Serialization (`.ser` files)             |
| Build tooling   | Ant / NetBeans project structure (`nbproject`, `build.xml`) |

## Project Structure

```
.
├── Client/                          # Swing desktop client
│   └── src/
│       ├── com/example/client/
│       │   ├── communication/       # Client.java, RMI + callback interfaces
│       │   └── model/               # User, Event, Show, Booking, Payment
│       └── project/                 # Swing frames (Login, Register, Admin*, User*)
├── Server1/                         # Main RMI server
│   └── src/com/example/server1/
│       ├── Server1Main.java         # Starts RMI registry on port 9999
│       ├── ClientRMIImpl.java       # RMI service implementation
│       ├── DataStore.java           # Singleton, persists users/events/bookings
│       ├── CallbackManager.java     # Manages client callback notifications
│       ├── LocalDataHelper.java     # In-memory helper utilities
│       └── SocketClientHelper.java  # Socket client bridge to Server2
├── Server2/                         # Secondary socket-based event service
│   └── src/com/example/server2/
│       ├── Server2Main.java         # Starts socket server on port 5000
│       ├── ClientHandler.java       # Per-connection request handler (threaded)
│       ├── LogicManager.java        # GET_EVENTS / ADD_EVENT / DEACTIVATE_SHOW logic
│       ├── DataStore.java           # Singleton, persists events.ser
│       ├── Request.java             # Serializable request object
│       └── Response.java            # Serializable response object
└── *.ser                            # Runtime-generated data files (users, events, bookings)
```

## Getting Started

### Prerequisites

- JDK 8 or newer
- (Optional) NetBeans or IntelliJ IDEA, since the project ships with `nbproject`/`.iml` files; plain `javac` also works.

### Running the components

Each module has its own `src` folder and can be compiled independently. From each module's root:

```bash
# Server1 (RMI server) — run first
cd Server1
javac -d build/classes -cp "../Client/build/classes" $(find src -name "*.java")
java -cp "build/classes:../Client/build/classes" com.example.server1.Server1Main

# Server2 (socket server) — run alongside Server1
cd Server2
javac -d build/classes $(find src -name "*.java")
java -cp "build/classes:../Client/build/classes" com.example.server2.Server2Main

# Client — run last, one instance per user
cd Client
javac -d build/classes -cp "build/classes" $(find src -name "*.java")
java -cp "build/classes" project.LoginFrame
```

> On Windows, replace `:` with `;` in classpaths and use a shell that supports `find`, or compile via your IDE instead (open each folder as a NetBeans project — the `nbproject` metadata is already included).

The client connects to `rmi://localhost:9999/BookingService` by default — update this in the client source if the server runs on a different host.

## Data Persistence

All data is persisted using plain Java serialization to `.ser` files (`users.ser`, `events.ser`, `bookings.ser`) rather than a database. These files are generated at runtime and should generally be excluded from version control (see `.gitignore`).

## Possible Improvements

- Fully wire `SocketClientHelper` into `ClientRMIImpl` so Server1 actually delegates event operations to Server2, and align the socket port used by both sides.
- Replace `.ser` file persistence with a real database (e.g. SQLite/PostgreSQL via JDBC).
- Hash user passwords instead of storing them in plaintext.
- Add input validation and richer error handling on both client and server sides.
- Add automated tests for booking/cancellation logic (seat availability, concurrency).

## Authors

- Stavros Moschis
- Zacharias Kokkinakis

## License

This project was developed for academic purposes.
