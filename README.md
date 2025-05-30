# Seasonal Worker Management System

This project implements a system for managing and searching seasonal workers, built with JavaFX.

## Table of Contents

* [Features](#features)
* [System Architecture](#system-architecture)
* [Development Process](#development-process)
* [Input Validation](#input-validation)
* [Testing](#testing)
* [Getting Started](#getting-started)

## Features

The application provides the following key functionalities:

* **Worker Search**: Perform complex searches for seasonal workers based on user-defined parameters such as name, surname, spoken languages, availability periods, roles performed, place of residence, and driving license/car availability. Users can choose between AND or OR search logic.

* **Worker Details View**: Access detailed information for each worker, including personal data, past work experiences, and emergency contacts.

* **Data Modification**: Modify existing worker records, including personal details, work experiences, and emergency contacts.

* **New Worker Registration**: Add new workers to the system, including their personal data and at least one emergency contact.

* **Work/Contact Management**: Add or delete work experiences and emergency contacts for selected workers.

## Java and JavaFX Concepts and Features
- String manipulation
- Input validation with regular expressions (`Pattern`/`Matcher`)
- JSON file parsing/import
- File I/O 
- Enum types for predefined value sets
- Date parsing and validation
- Alert dialogs for user notifications
- Static UI via FXML
- Dynamic UI elements (runtime component creation)
- Responsive layout (auto-resizing and adapting to window shape)
- Event handling with lambda expressions 
- JavaFX collections 
- Duplicate-elimination logic (`Set`, `SortedSet`, `TreeSet` usage)
- Stream API (filtering, mapping, collecting)
- Inheritance (class hierarchies for shared behavior)
- Use of interfaces
- Custom exceptions for validation errors
- Exception handling (try/catch/finally)
- Advanced search functionality (complex filtering criteria)
- Agnostic data import layer (separation of I/O from business logic)
- Data Access Object (DAO) pattern
- Model-View-Controller (MVC) architectural pattern

## System Architecture

The system is designed following the **Model-View-Controller (MVC)** architectural pattern, separating the application into three distinct packages:

* **Model**: Manages all data and business logic. It was developed first to ensure robust data handling before implementing the graphical interface.

* **View**: Handles the graphical representation of the Model, implemented using JavaFX FXML files for static elements and dynamically generated content for data-dependent displays.

* **Controller**: Defines the system's behavior based on user input, including handling user actions and dynamic UI updates.

In addition to MVC, the **Data Access Object (DAO)** pattern is implemented within the Model package. This pattern abstracts data access operations, separating them from higher-level functionalities and hiding database complexity behind the `databaseDAO` interface.

The `databaseDAOimpl` class handles data import from an external JSON file (used for testing purposes).

## Development Process

An agile and incremental development process was used, with an iterative four-phase cycle for each new feature:

1. **Design**: Producing documentation, flowcharts, and criticality lists for new functionalities.

2. **Implementation**: Defining functions according to design specifications.

3. **Testing and Validation**: Verifying robustness and functionality, including edge cases.

4. **Refactoring**: Improving code readability for future modifications.

This incremental approach allowed for function-by-function testing and a runnable program at the end of each cycle.

## Input Validation

Comprehensive input validation is implemented to ensure data integrity and user experience. Checks include:

* **Mandatory Fields**: Ensuring required fields are not left empty.

* **Email and Phone Number Validity**: Verifying correct formats using the `checkPattern()` function in the `persona` class.

* **Generic Dates**: Validating dates are in `dd/MM/yyyy` format.

* **Birth Dates**: Ensuring birth dates are not before 01/01/1900 or after the current date.

* **Availability Periods**: Validating date formats, start/end dates, and preventing future end dates beyond 01/01/2100. Duplicate periods are ignored.

* **Predefined Value Sets**: Validating "automunito" (car owner) and "patente" (driving license) fields against predefined values (e.g., "Si", "No" for car owner; specific license categories for driving license).

* **Duplicate Values**: Removing duplicate entries for spoken languages and availability zones.

* **Valid Remuneration**: Checking if the remuneration field for a job is a valid number.

* **Work Period**: Ensuring past work experiences fall within the last 5 years and do not have future end dates.

Errors are notified to the user.

## Testing

The following validation activities were performed:

* Verification of consistency between class diagrams and specified requirements.

* Verification between class diagrams and the developed software.

* **Developer Testing**: Functionality and robustness testing by the developer, including abnormal situations like the absence of a pre-existing database.

* **Peer-Review Testing**: Executable software was sent to colleagues for verification, leading to the identification and resolution of further issues, primarily related to input validation.

## Getting Started

To run the project, you will need JavaFX. Further instructions on setting up and running the application can be found in the project documentation.
