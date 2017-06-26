package com.pyj.chatClient0213.clientIO;

import javax.swing.text.html.parser.Entity;

import com.pyj.chatClient0213.clientIO.dao.DBManager;
import com.pyj.chatClient0213.clientIO.event.LoginController;
import com.pyj.chatClient0213.clientIO.event.MainHandler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.stage.Stage;

public class Main extends Application {
	MainHandler mc;
	LoginController lc;

	@Override
	public void start(Stage primaryStage) {
		try {

			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/Client.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			mc = loader.getController();

			if (mc.loginShow() == false) {
				return;
			}

			primaryStage.setTitle("IIO방식 채팅 클라이언트v1.0");
			primaryStage.setScene(scene);
			primaryStage.show();
			// loginStage.showAndWait();

			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// scene.getStylesheets().add(getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/application.css").toExternalForm());
			// FXMLLoader loader = new
			// FXMLLoader((getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/Login.fxml")));
			// Parent login = loader.load();
			// Scene sceneLogin = new Scene(login);
			// primaryStage.setTitle("IIO방식 채팅 클라이언트v1.0");
			// primaryStage.setScene(sceneLogin);
			// primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		mc.stopClient();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
