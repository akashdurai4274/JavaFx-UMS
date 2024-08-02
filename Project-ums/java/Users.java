package com.example;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.swing.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class Users {
    Connection con = null;
    PreparedStatement pst;
    Statement st;
    ResultSet rs;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8";
            String uname = "root";
            String pass = "root@123";
            con = DriverManager.getConnection(url, uname, pass);
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    public ObservableList<usermodel> getlist() {
        ObservableList<usermodel> userList = FXCollections.observableArrayList();
        String sql = "SELECT ID, NAME, AGE, CITY from users";
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            usermodel user;
            while (rs.next()) {
                user = new usermodel(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("AGE"), rs.getString("CITY"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void loadData() {
        ObservableList<usermodel> list = getlist();
        colID.setCellValueFactory(new PropertyValueFactory<usermodel, Integer>("id"));
        colNAME.setCellValueFactory(new PropertyValueFactory<usermodel, String>("name"));
        colAGE.setCellValueFactory(new PropertyValueFactory<usermodel, Integer>("age"));
        colCITY.setCellValueFactory(new PropertyValueFactory<usermodel, String>("city"));
        table.setItems(list);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCLEAR;

    @FXML
    private Button btnDELETE;

    @FXML
    private Button btnSAVE;

    @FXML
    private Button btnUPDATE;

    @FXML
    private TableColumn<usermodel, Integer> colAGE;

    @FXML
    private TableColumn<usermodel, String> colCITY;

    @FXML
    private TableColumn<usermodel, Integer> colID;

    @FXML
    private TableColumn<usermodel, String> colNAME;

    @FXML
    private Label lblAGE;

    @FXML
    private Label lblCITY;

    @FXML
    private Label lblID;

    @FXML
    private Label lblNAME;

    @FXML
    private TableView<usermodel> table;

    @FXML
    private TextField txtAGE;

    @FXML
    private TextField txtCITY;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtNAME;

    @FXML
    void btnClearClicked(ActionEvent event) {
        txtID.setText("");
        txtNAME.setText("");
        txtAGE.setText("");
        txtCITY.setText("");
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        int result = JOptionPane.showConfirmDialog(null, "Sure? You want to Delete the Data?", "Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        String id = txtID.getText();
        if (result == JOptionPane.YES_OPTION) {
            try {
                String qry = "Delete from users where ID=?";
                pst = con.prepareStatement(qry);
                pst.setString(1, id);
                pst.executeUpdate();
                btnClearClicked(event);
                JOptionPane.showMessageDialog(null, "Data Deleted Sucessfully🎉");
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {
        String name = txtNAME.getText();
        String age = txtAGE.getText();
        String city = txtCITY.getText();
        if (name.isEmpty() || age.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter All Feilds");
        }
        if (txtID.getText().isEmpty()) {
            try {
                String qry = "Insert into users (NAME, AGE, CITY) values (?,?,?)";
                pst = con.prepareStatement(qry);
                pst.setString(1, name);
                pst.setString(2, age);
                pst.setString(3, city);
                pst.executeUpdate();
                btnClearClicked(event);
                loadData();
                JOptionPane.showMessageDialog(null, "Data insert Sucessfully🎉");
            } catch (SQLException e) {

            }
        }
    }

    @FXML
    void btnUpdateClicked(ActionEvent event) {
        String id = txtID.getText();
        String name = txtNAME.getText();
        String age = txtAGE.getText();
        String city = txtCITY.getText();

        if (!txtID.getText().isEmpty()) {
            try {
                String qry = "update users set NAME=?, AGE=?, CITY=? where ID=?";
                pst = con.prepareStatement(qry);
                pst.setString(1, name);
                pst.setString(2, age);
                pst.setString(3, city);
                pst.setString(4, id);
                pst.executeUpdate();
                btnClearClicked(event);
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void tableClicked(MouseEvent event) {
        usermodel user = table.getSelectionModel().getSelectedItem();
        txtID.setText(String.valueOf(user.getId()));
        txtNAME.setText(user.getName());
        txtAGE.setText(String.valueOf(user.getAge()));
        txtCITY.setText(user.getCity());
    }

    @FXML
    void initialize() {
        connect();
        loadData();
    }

}
