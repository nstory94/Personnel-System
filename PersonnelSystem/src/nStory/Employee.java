package nStory;

/*
Employee class to represent individual employees
 */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class Employee {

    @FXML
    private final SimpleStringProperty firstName;
    @FXML
    private final SimpleStringProperty lastName;
    @FXML
    private SimpleIntegerProperty employeeID;
    @FXML
    private SimpleStringProperty managerName;
    @FXML
    private SimpleStringProperty role;


    //Constructor
    public Employee(String fName, String lName, int employeeID, String managerName, String role) {
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.employeeID = new SimpleIntegerProperty(employeeID);
        this.managerName = new SimpleStringProperty(managerName);
        this.role = new SimpleStringProperty(role);
    }

    //Getters & setters

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lName) {
        lastName.set(lName);
    }

    public int getEmployeeID() {
        return employeeID.get();
    }

    public void setEmployeeID(int ID) {
        employeeID.set(ID);
    }

    public String getRole() {
        return role.get();
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getManagerName() {
        return managerName.get();
    }

    public SimpleStringProperty managerNameProperty() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName.set(managerName);
    }
}
