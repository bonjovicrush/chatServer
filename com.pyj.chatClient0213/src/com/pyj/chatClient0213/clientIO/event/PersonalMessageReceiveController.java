package com.pyj.chatClient0213.clientIO.event;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonalMessageReceiveController implements Initializable {

    @FXML
    private TextArea textAreaResponse;

    @FXML
    private TextField textFieldSender;

    @FXML
    private TextArea textAreaMessage;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnConfirm;
    MainHandler mh;

    @FXML
    void onSend(ActionEvent event) {
	    	
    	closeDialog();
    }

    @FXML
    void onConfirm(ActionEvent event) {
    	closeDialog();
    }
    
    void closeDialog(){
    	Stage stage = (Stage) btnSend.getScene().getWindow();
    	stage.close();
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		textFieldSender.setText(MainHandler.fromID);
		textAreaMessage.setText(MainHandler.msg);
	}
}
