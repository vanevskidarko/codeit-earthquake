# Codeit Earthquake Application

This is a web application that fetches, stores, and displays recent earthquake data from the USGS (United States Geological Survey) public API.

## Features

*   Fetches the latest earthquake data from the USGS API.
*   Parses the GeoJSON response and extracts relevant fields.
*   Filters earthquakes with a magnitude greater than 2.0.
*   Stores the filtered data in a PostgreSQL database.
*   Provides a REST API to expose the stored data.
*   A simple frontend to view, filter, and paginate the earthquake data.
*   Allows deleting earthquake records.

## Technologies Used

*   **Backend:** Java 21, Spring Boot
*   **Database:** PostgreSQL
*   **Frontend:** HTML, Bootstrap, jQuery
*   **Build Tool:** Maven

---

## 1. Project Setup Instructions

### Prerequisites

*   Java 21
*   Apache Maven
*   PostgreSQL

### Installation

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd codeit-earthquake
    ```

2.  **Install dependencies:**
    ```bash
    mvn clean install
    ```

---

## 2. Database Configuration Steps

1.  **Install PostgreSQL:**
    Make sure you have a running instance of PostgreSQL.

2.  **Create the database:**
    Connect to PostgreSQL and create a new database. You can name it `earthquake_db` or choose your own name.
    ```sql
    CREATE DATABASE earthquake_db;
    ```

3.  **Configure Environment Variables:**
    Create a file named `.env` in the root directory of the project. Add your database connection details to this file:

    ```
    DB_URL=jdbc:postgresql://localhost:5432/earthquake_db
    DB_USERNAME=your_postgres_username
    DB_PASSWORD=your_postgres_password
    ```
    Replace `earthquake_db`, `your_postgres_username`, and `your_postgres_password` with your actual database name, username, and password.

---

## 3. How to Run Backend and Frontend

The frontend is a single HTML file that is served by the Spring Boot backend, so you only need to run the backend application.

### Running the Application

You can run the application in one of the following ways:

*   **Using Maven:**
    ```bash
    mvn spring-boot:run
    ```
*   **From your IDE:**
    Run the `main` method in the `CodeitEarthquakeApplication` class.

Once the application is running, you can access the frontend by opening your web browser and navigating to:

[http://localhost:8080](http://localhost:8080)

---

## 4. Assumptions Made

*   It is assumed that the user has Java 21, Maven, and PostgreSQL installed and configured on their system.
*   The PostgreSQL server is assumed to be running on `localhost` at port `5432`.
*   The requirement to "filter earthquakes after a defined time" has been implemented on the frontend as a time picker. This filters for earthquakes that occurred on the current day after the selected time.
*   The application fetches data from the `all_hour.geojson` feed, which means the data is always from the last hour. The filtering logic in the `fetchAndSaveEarthquakes` service method reflects this.

## 5. Optional Improvements Implemented

*   **Environment Variables for Configuration:** Instead of hardcoding database credentials in `application.properties`, a `.env` file is used to store sensitive information. This is a security best practice.
*   **Dynamic Frontend:** A user-friendly frontend with dynamic filtering, pagination, and the ability to delete records has been implemented to provide a better user experience.
*   **Comprehensive Integration Tests:** The service layer is covered by integration tests that mock the external API, ensuring the business logic is tested reliably.
