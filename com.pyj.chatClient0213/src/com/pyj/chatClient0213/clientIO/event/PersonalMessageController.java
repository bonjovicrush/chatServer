package com.pyj.chatClient0213.clientIO.event;

import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonalMessageController implements Initializable {
	@FXML
	private TextField textFieldReceiver;

	@FXML
	private TextArea textAreaMessage;

	@FXML
	private Button btnSend;

	@FXML
	private Button btnCancle;
	
	MainHandler mh = new MainHandler();

	@FXML
	void onSend(ActionEvent event) {
		//mh.send("#message#to" + textFieldReceiver.getText() + "#msg" + textAreaMessage.getText());
		mh.getMainHandler().send("#message#to" + textFieldReceiver.getText() + "#msg" + textAreaMessage.getText());

		System.out.println(
				"paranoid: 쪽지보내기 :" + "#message#to" + textFieldReceiver.getText() + "#msg" + textAreaMessage.getText());
		close();
	

	}

	@FXML
	void onCancle(ActionEvent event) {
		close();
	}

	void close() {
		Stage personalMessageStage = (Stage) btnSend.getScene().getWindow();
		personalMessageStage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
}
