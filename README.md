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

* **Worker Search**: Perform complex searches for seasonal workers based on user-defined parameters such as name, surname, spoken languages, availability periods, roles performed, place of residence, and driving license/car availability[cite: 5, 38]. Users can choose between AND or OR search logic[cite: 39].
* **Worker Details View**: Access detailed information for each worker, including personal data, past work experiences, and emergency contacts[cite: 10, 11].
* **Data Modification**: Modify existing worker records, including personal details, work experiences, and emergency contacts[cite: 12, 17].
* **New Worker Registration**: Add new workers to the system, including their personal data and at least one emergency contact[cite: 29, 30].
* **Work/Contact Management**: Add or delete work experiences and emergency contacts for selected workers[cite: 18, 24, 25, 27].

## System Architecture

The system is designed following the **Model-View-Controller (MVC)** architectural pattern, separating the application into three distinct packages:

* **Model**: Manages all data and business logic. It was developed first to ensure robust data handling before implementing the graphical interface[cite: 57, 58, 63].
* **View**: Handles the graphical representation of the Model, implemented using JavaFX FXML files for static elements and dynamically generated content for data-dependent displays[cite: 66, 67].
* **Controller**: Defines the system's behavior based on user input, including handling user actions and dynamic UI updates[cite: 68, 69, 70].

In addition to MVC, the **Data Access Object (DAO)** pattern is implemented within the Model package[cite: 61]. This pattern abstracts data access operations, separating them from higher-level functionalities and hiding database complexity behind the `databaseDAO` interface[cite: 62].

The `databaseDAOimpl` class handles data import from an external JSON file (used for testing purposes)[cite: 79, 80, 81].

## Development Process

An agile and incremental development process was used, with an iterative four-phase cycle for each new feature:

1.  **Design**: Producing documentation, flowcharts, and criticality lists for new functionalities[cite: 49].
2.  **Implementation**: Defining functions according to design specifications[cite: 51].
3.  **Testing and Validation**: Verifying robustness and functionality, including edge cases[cite: 52, 53].
4.  **Refactoring**: Improving code readability for future modifications[cite: 54].

This incremental approach allowed for function-by-function testing and a runnable program at the end of each cycle[cite: 55].

## Input Validation

Comprehensive input validation is implemented to ensure data integrity and user experience. Checks include:

* **Mandatory Fields**: Ensuring required fields are not left empty[cite: 113].
* **Email and Phone Number Validity**: Verifying correct formats using the `checkPattern()` function in the `persona` class[cite: 114, 115].
* **Generic Dates**: Validating dates are in `dd/MM/yyyy` format[cite: 116].
* **Birth Dates**: Ensuring birth dates are not before 01/01/1900 or after the current date[cite: 118].
* **Availability Periods**: Validating date formats, start/end dates, and preventing future end dates beyond 01/01/2100. Duplicate periods are ignored[cite: 119].
* **Predefined Value Sets**: Validating "automunito" (car owner) and "patente" (driving license) fields against predefined values (e.g., "Si", "No" for car owner; specific license categories for driving license)[cite: 120, 121].
* **Duplicate Values**: Removing duplicate entries for spoken languages and availability zones[cite: 122].
* **Valid Remuneration**: Checking if the remuneration field for a job is a valid number[cite: 123].
* **Work Period**: Ensuring past work experiences fall within the last 5 years and do not have future end dates[cite: 123].

Errors are notified to the user[cite: 15, 22, 34, 42].

## Testing

The following validation activities were performed:

* Verification of consistency between class diagrams and specified requirements[cite: 125].
* Verification between class diagrams and the developed software[cite: 126].
* **Developer Testing**: Functionality and robustness testing by the developer, including abnormal situations like the absence of a pre-existing database[cite: 128, 129, 130].
* **Peer-Review Testing**: Executable software was sent to colleagues for verification, leading to the identification and resolution of further issues, primarily related to input validation[cite: 133].

## Getting Started

To run the project, you will need JavaFX. Further instructions on setting up and running the application can be found in the project documentation.