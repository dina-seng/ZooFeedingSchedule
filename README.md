# 🦁 Zoo Management System (ZMS)
### *Enterprise Java Desktop Application for Wildlife Logistics*

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Swing-blue?style=for-the-badge)

## 📌 Project Overview
The **Zoo Management System** is a robust management tool built to handle the complex daily operations of a modern zoo. This project focuses on managing specialized staff roles, diverse animal habitats (Forest, Ocean, Savannah), and real-time food inventory tracking.

This was developed as a core project during my Software Engineering track at **CADT**, emphasizing **Object-Oriented Programming (OOP)** and the **Data Access Object (DAO)** design pattern.

---

## ✨ Key Features
* **Role-Based Access Control (RBAC):** Distinct functionalities for `Manager` and `Keeper` roles using Inheritance.
* **Automated Feeding Schedules:** Logic-driven scheduling based on animal type and habitat requirements.
* **Habitat Management:** Scalable architecture to add new habitats (Ocean, Forest, etc.) without breaking existing logic.
* **Data Persistence:** Full integration with **MySQL** via JDBC for reliable data storage.
* **Custom Exception Layer:** Professional error handling (e.g., `InvalidNameException`, `ZooException`) to ensure application stability.

---

## 🏗️ Technical Architecture
The application follows a structured **N-Tier Architecture** to ensure low coupling and high cohesion:

1.  **Presentation Layer:** Java Swing GUI for user interaction.
2.  **Service Layer (`Zoo.java`):** Centralized business logic and state management.
3.  **Data Access Layer (DAO):** Dedicated classes for SQL operations, separating database logic from the UI.
4.  **Model Layer:** OOP-based entities utilizing Abstraction and Encapsulation.

---

## 🛠️ Installation & Setup

### Prerequisites
* **JDK 17** or higher
* **MySQL Server** 8.0+
* **VS Code** (with Java Extension Pack)

### Steps to Run
1.  **Clone the Repository:**
    ``` bash
    git clone https://github.com/Sor-Channorakpitou/ZooFeedingSchedule.git
    ```
2.  **Database Configuration:**
    * Create a database named `zoo_db`.
    * Run the provided SQL script (usually found in `/database/db_setup.sql`).
    * Update your credentials in `MySqlDatabaseConnection.java`.
3.  **Build and Launch:**
    * Open the project in VS Code.
    * Run `ZooLoginGUI.java` to start the application.

---

## 🎓 OOP Principles Applied
* **Inheritance & Abstraction:** Used an `Abstract Habitat` class to define core behaviors for specific environments like `Forest` or `Ocean`.
* **Polymorphism:** Managed a diverse list of Staff and Habitats through parent-class references, allowing for easy system extension.
* **Encapsulation:** Protected sensitive data (like salaries and passwords) using private fields and validated accessors.
* **Separation of Concerns:** Kept SQL logic strictly inside DAO classes, making the UI "Database Agnostic."

---

## 👤 Author
**Seng Dina - Sor Channorakpitou**
* **Role:** Software Engineering Student
* **Institution:** Cambodia Academy of Digital Technology (CADT) 
* **DINA's contact:** 
* **LinkedIn:** https://www.linkedin.com/public-profile/settings?trk=d_flagship3_profile_self_view_public_profile
* **Portfolio:** [https://portfolio-dina-seng-9ego6ko1b-dinasengs-projects.vercel.app/](https://portfolio-dina-seng-bm2a.vercel.app/)
* **PITOU's contact:** 
* **LinkedIn:** www.linkedin.com/in/sor-channorakpitou-03496a385
