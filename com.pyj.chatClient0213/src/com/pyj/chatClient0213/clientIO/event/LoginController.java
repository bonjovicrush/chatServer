package com.pyj.chatClient0213.clientIO.event;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.pyj.chatClient0213.clientIO.dao.DBManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {

	@FXML
	private TextField txtID;
	@FXML
	private Button btnLogin;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Button btnCancle;
	@FXML
    private Label loginState;
	@FXML
	private Button btnSignUp;
	
	private static boolean check;
	
	public static boolean isCheck() {
		return check;
	}
	
	DBManager dbm = new DBManager();

	private Map<String, String> loginInfo = new HashMap<String, String>();

	@FXML
	void onLogin(ActionEvent event) {
		
		if(txtID.getText().equals("")){
			loginState.setText("ID를 입력하세요.");
		}
		else if(txtPassword.getText().equals("")){
			loginState.setText("패스워드를 입력하세요.");
		}
		else if (dbm.login(txtID.getText(), txtPassword.getText())) {
			try {
				System.out.println("로그인 성공");
				Stage primaryStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/Client.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				
				Stage loginStage = (Stage) txtID.getScene().getWindow();
				loginStage.close();
				
				primaryStage.show();
				
				MainHandler mainHandler = null;
				mainHandler.id = txtID.getText();
			} catch(Exception e){
				e.printStackTrace();
			}
		} else{
			loginState.setText("로그인 정보가 올바르지 않습니다.");
		}
	}

	@FXML
	void onCancle(ActionEvent event) {
		Stage nowStage = (Stage) txtID.getScene().getWindow();
		nowStage.close();
		Platform.exit();
	}
	
    @FXML
    void onSignUp(ActionEvent event) {
    	Stage primaryStage = (Stage) txtID.getScene().getWindow();
    	//Stage dialog = new Stage(StageStyle.UTILITY);
    	Stage dialog = new Stage();
    	dialog.initModality(Modality.WINDOW_MODAL);
    	dialog.initOwner(primaryStage);
    	dialog.setTitle("회원 등록");
    	try {
			Parent  parent = FXMLLoader.load(getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/SignUp.fxml"));
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dbm.connectDB();
	}
	
	
}
