# Parking Lot Reservation System

This is a backend project for a smart parking lot system built with Java and Spring Boot. It provides a REST API to manage parking floors, slots, and handle reservations from customers.

---

## Features

* Create and manage multi-level parking floors.
* Add and configure individual parking slots for different vehicle types.
* Reserve a parking slot for a specific date and time range.
* Automatically checks for booking conflicts to prevent double-booking.
* Calculates parking fees based on vehicle type and duration (partial hours are rounded up).
* List all available slots for a given time range.
* Fetch details of a specific reservation by its ID.
* Cancel an existing reservation.

---

## Tech Stack

* **Framework:** Spring Boot 3
* **Language:** Java 17
* **Database:** H2 In-Memory Database
* **Data Access:** Spring Data JPA / Hibernate
* **Build Tool:** Maven

---

## API Endpoints

Here are the main endpoints available in this API:

#### Floors & Slots
* `POST /api/floors` - Create a new parking floor.
* `POST /api/slots` - Create a new parking slot on a specific floor.

#### Reservations
* `POST /api/reserve` - Create a new reservation for a slot.
* `GET /api/reservations/{id}` - Get details for a single reservation.
* `DELETE /api/reservations/{id}` - Cancel a reservation.

#### Availability
* `GET /api/availability` - Get a list of all available slots for a given time range.
    * Example: `/api/availability?startTime=2025-09-24T18:00:00&endTime=2025-09-24T20:00:00`

---

## How to Run Locally

### Prerequisites
* JDK 17 or later
* Apache Maven

### Setup
1.  **Clone the repository:**
    ```bash
    git clone <your-github-repo-link>
    ```

2.  **Navigate to the project directory:**
    ```bash
    cd parking-lot-backend
    ```

3.  **Run the application:**
    * On Windows (Command Prompt/PowerShell):
        ```bash
        .\mvnw.cmd spring-boot:run
        ```
    * On Mac/Linux (or Git Bash on Windows):
        ```bash
        ./mvnw spring-boot:run
        ```

The application will start on `http://localhost:8080`.

---

## Accessing the H2 Database Console

Once the application is running, you can access a simple web console to view the in-memory database.

* **URL:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:parkinglotdb`
* **User Name:** `saideep`
* **Password:** `password`

To view the data in the sql statement box type one of the following commands and click Run.
* To see all the floors you have created:

         SELECT * FROM FLOORS;

* To see all the slots:

        SELECT * FROM SLOTS;

* To see all the reservations:

        SELECT * FROM RESERVATIONS;