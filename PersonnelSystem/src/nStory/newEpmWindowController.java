package nStory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class newEpmWindowController implements Initializable {

    private Connection conn = null;

    @FXML
    private  String firstName;
    @FXML
    private  String lastName;
    @FXML
    private int employeeID;
    @FXML
    private String managerName;
    @FXML
    private String role;

    @FXML
    private TableView<Employee> empTable;
    @FXML
    private TableColumn<Employee, Integer> ID;
    @FXML
    private TableColumn<Employee, String> lName;
    @FXML
    private TableColumn<Employee, String> fName;

    @FXML
    private Button saveEmp, cancelAdd;
    @FXML
    private ChoiceBox managers;

    @FXML
    private TextField empIDField;
    @FXML
    private TextField fNameField;
    @FXML
    private TextField lNameField;
    @FXML
    private ListView roleField;


    private List<Employee> employeeList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Populate roles in List, configured for multiple selections (Hold down CTRL + Click)
        roleField.getItems().addAll("Director", "IT", "Support", "Accounting", "Analyst", "Sales");
        roleField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //List for managers' names
        List<String> list = new ArrayList<>();

        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost;database=Employees;integratedSecurity=true;");

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Employees";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                String managerName = rs.getString("ManagerName");

                if (managerName != null && !list.contains(managerName))
                    list.add(managerName);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        //Add managers to Manager dropdown
        ObservableList obList = FXCollections.observableList(list);
        managers.setItems(obList);
    }

    //Handles the Cancel and Save operations of the New Employee Window.
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {

        Stage stage;
        Parent root;

        if (event.getSource() == cancelAdd) {
            stage = (Stage) cancelAdd.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("resources/mainWindow.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            stage = (Stage) saveEmp.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("resources/addEmployeeWindow.fxml"));
            addNewEmployee(fNameField.getText(), lNameField.getText(), Integer.parseInt(empIDField.getText()), managers.getSelectionModel().getSelectedItem().toString(),
                    roleField.getSelectionModel().getSelectedItems().toString());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    //Creates a new Employee object and adds it to the Database.
    private void addNewEmployee(String fName, String lName, int ID, String managerName, String role) throws SQLException {

        Employee newEmployee = new Employee(fName, lName, ID, managerName, role);
         managerName  = newEmployee.getManagerName();
         employeeID = newEmployee.getEmployeeID();
         firstName = newEmployee.getFirstName();
         lastName = newEmployee.getLastName();
         role = newEmployee.getRole();

         List employeeData = new ArrayList();
         Collections.addAll(employeeData, managerName, employeeID, firstName,  lastName, role);

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Employees VALUES (?, ?, ?, ?, ?)");
        for (int i=0; i < employeeData.size(); i++) {
            stmt.setString(1, managerName);
            stmt.setInt(2, employeeID);
            stmt.setString(3, lastName);
            stmt.setString(4, firstName);
            stmt.setString(5, role);
           }
           stmt.executeUpdate();
    }

}

