package nStory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    private Connection conn = null;

    @FXML
    private TableView<Employee> empTable;
    @FXML
    private TableColumn<Employee, Integer> ID;
    @FXML
    private TableColumn<Employee, String> lName;
    @FXML
    private TableColumn<Employee, String> fName;
    @FXML
    private Button newEmp;
    @FXML
    private ChoiceBox managers;

    List<String> list = new ArrayList<>();

    private List<Employee> employeeList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //set cell properties for Employee table
        ID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        //Connect to database, retrieve all records from Employee table,
        try{
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost;database=Employees;integratedSecurity=true;");

            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM Employees";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

                String managerName  = rs.getString("ManagerName");
                int employeeID = rs.getInt("EmployeeID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("lastName");
                String employeeRole = rs.getString("EmployeeRole");

                employeeList.add(new Employee(firstName, lastName, employeeID, managerName, employeeRole));

                //Populate choicebox
                if (managerName != null && !list.contains(managerName))
                list.add(managerName);
            }

        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

        ObservableList obList = FXCollections.observableList(list);
        managers.setItems(obList);
    }

    //Queries database to retrieve employees based on selected manager
    public void getEmployees(ActionEvent actionEvent) throws SQLException {
        empTable.getItems().clear();
        employeeList.clear();

        PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM Employees WHERE ManagerName IN (?)");
        pStatement.setString(1, (String) managers.getValue());
        ResultSet filteredRS = pStatement.executeQuery();

        while (filteredRS.next()) {

            EmployeeListUpdater(filteredRS);
        }
    }

    //Populates the employee table with records retrieved from database
    private void EmployeeListUpdater(ResultSet resultSet) throws SQLException {

        String managerName  = resultSet.getString("ManagerName");
        int employeeID = resultSet.getInt("EmployeeID");
        String firstName = resultSet.getString("FirstName");
        String lastName = resultSet.getString("LastName");
        String employeeRole = resultSet.getString("EmployeeRole");

        employeeList.add(new Employee(firstName, lastName, employeeID, managerName, employeeRole));

        ObservableList empList = FXCollections.observableList(employeeList);
        empTable.getItems().setAll(empList);
    }

    //Displays either the Add New Employee window
    @FXML
     private void handleButtonAction (ActionEvent event) throws Exception {

        Stage stage;
        Parent root;

        if(event.getSource()==newEmp){
            stage = (Stage) newEmp.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("resources/addEmployeeWindow.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}




