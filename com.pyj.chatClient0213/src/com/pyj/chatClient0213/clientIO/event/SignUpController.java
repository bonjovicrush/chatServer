package com.pyj.chatClient0213.clientIO.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.pyj.chatClient0213.clientIO.dao.DBManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController implements Initializable {
	
	final static int ERROR_CODE_DUPLICATE_ID=1062;

	@FXML
	private TextField textFieldID;

	@FXML
	private PasswordField passwordField1;

	@FXML
	private PasswordField passwordField2;

	@FXML
	private Button btnSignUp;

	@FXML
	private Label labelText;

	@FXML
	private Button btnCancle;
	DBManager dbm = new DBManager();
	
	public Label getLabelText() {
		return labelText;
	}

	public void setLabelText(Label labelText) {
		this.labelText = labelText;
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		btnCancle.setOnAction((event)->{
			Stage signUpStage =(Stage) btnCancle.getScene().getWindow();
			signUpStage.close();
		});
	}
	
	@FXML
	void onSignUp(ActionEvent event) {
		if(textFieldID.getText().equals("")){
			labelText.setText("ID를 입력하세요.");
		}
		else if(passwordField1.getText().equals("")||passwordField2.getText().equals("")){
			labelText.setText("패스워드를 입력하세요.");
		}
		else if(passwordField1.getText().equals(passwordField2.getText())==false){
			labelText.setText("패스워드가 일치하지 않습니다.");
		}
		else{
			int addUser = dbm.addUser(textFieldID.getText(), passwordField1.getText());
			if(addUser==1){
			Stage signUpStage =(Stage) btnCancle.getScene().getWindow();
			signUpStage.close();
			}
			else if(addUser == ERROR_CODE_DUPLICATE_ID){
				labelText.setText("동일한 ID가 존재합니다.");
			}
		}
	}

}
