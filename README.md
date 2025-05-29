
# TalentView

TalentView is a JavaFX-based desktop application designed to manage and visualize talent profiles. It leverages object-oriented principles and JavaFX's capabilities to provide a user-friendly interface for talent management.

## Features

- **Profile Management**: Add, edit, and delete talent profiles.
- **Data Visualization**: View talent data through various charts and graphs.
- **Search & Filter**: Easily search and filter talent profiles based on different criteria.

## Technologies Used

- **JavaFX**: For building the GUI.
- **FXML**: For defining the UI structure.
- **CSS**: For styling the application.
- **Java 11+**: For the backend logic.

## Architecture Overview

TalentView follows the **Model-View-Controller (MVC)** design pattern:

- **Model**: Represents the data and business logic.
- **View**: The FXML files define the UI components.
- **Controller**: Java classes that handle user interactions and update the model and view accordingly.

## Key Design Patterns and Methods

### 1. Singleton Pattern

The application utilizes the Singleton pattern to ensure that certain classes, such as the main application class, have only one instance throughout the application's lifecycle.

```java
public class ApplicationManager {
    private static ApplicationManager instance;

    private ApplicationManager() {
        // private constructor
    }

    public static ApplicationManager getInstance() {
        if (instance == null) {
            instance = new ApplicationManager();
        }
        return instance;
    }
}
````

### 2. Factory Method Pattern

To create different types of talent profiles, a Factory Method pattern is employed, allowing for the creation of objects without specifying the exact class of object that will be created.

```java
public abstract class TalentProfile {
    public abstract void displayProfile();
}

public class DeveloperProfile extends TalentProfile {
    @Override
    public void displayProfile() {
        // display developer profile
    }
}

public class DesignerProfile extends TalentProfile {
    @Override
    public void displayProfile() {
        // display designer profile
    }
}

public class TalentProfileFactory {
    public TalentProfile createProfile(String type) {
        if (type.equals("Developer")) {
            return new DeveloperProfile();
        } else if (type.equals("Designer")) {
            return new DesignerProfile();
        }
        return null;
    }
}
```

### 3. Observer Pattern

The Observer pattern is used to update the UI components when the underlying data changes, ensuring that the view reflects the latest model state.

```java
public class TalentModel {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // other methods to modify talent data
}
```

### 4. MVC Controllers

Controllers handle user input and update the model and view accordingly. For instance, the `ProfileController` manages the interactions related to talent profiles.

```java
public class ProfileController {
    private TalentModel model;
    private TalentView view;

    public ProfileController(TalentModel model, TalentView view) {
        this.model = model;
        this.view = view;
    }

    public void addProfile(String name, String type) {
        TalentProfile profile = new TalentProfileFactory().createProfile(type);
        model.addProfile(profile);
        view.updateView();
    }

    // other methods to handle user actions
}
```

## Running the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/Shalafi01/javafx-TalentView.git
   ```

2. Navigate to the project directory:

   ```bash
   cd javafx-TalentView
   ```

3. Compile and run the application:

   ```bash
   javac -d bin src/*.java
   java -cp bin Main
   ```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
