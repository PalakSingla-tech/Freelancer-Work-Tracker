# Freelancer Work Tracker

A desktop-based application developed using **Core Java**, **JDBC**, and **JavaFX** to help freelancers manage their projects, clients, and work sessions efficiently. The application provides two dashboards — **User Dashboard** and **Admin Dashboard** — with distinct access levels and features.

---

## 🧰 Technologies Used

- **Java (Core Java)**
- **JavaFX** for GUI
- **FXML** for UI layout
- **MySQL** as the database
- **JDBC** for database interaction

---

## 📦 Features

### 👤 User Dashboard
Authenticated users can:

- **Project Management**:
  - Add new projects
  - Edit existing projects
  - Delete projects
  - View all projects

- **Client Management**:
  - Add new client information
  - Delete client entries
  - View all clients

- **Work Session Tracking**:
  - Record new work sessions
  - View stored sessions

- **Reports**:
  - View **weekly reports** of work sessions
  - View **monthly reports** showing total time worked

---

### 🔐 Admin Dashboard
Admins must enter a **secret key** before accessing the dashboard.

After successful authentication, the admin can:

- View all registered **users**
- View all **projects** created by users
- View all recorded **work sessions**
- Delete any user account from the system


