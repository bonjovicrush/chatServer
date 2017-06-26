package com.pyj.chatClient0213.clientIO.event;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class MainHandler implements Initializable {
	@FXML
	private Button btnStartStop;

	@FXML
	private TextArea txtDisplay;

	@FXML
	private TextField txtInput;

	@FXML
	private Button btnSend;
	public static String id;

	static String fromID;
	static String msg;

	static MainHandler mainHandler;
	LoginController lc;

	ExecutorService executorService; // 쓰레드 풀 제어
	Socket socket;
	static Stage mLoginDialog;
	// List<Client> connections = new Vector<Client>(); // 서버에 접속하는 클라이언트들을 관리

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.mainHandler = MainHandler.this;

	}

	public MainHandler getMainHandler() {
		return mainHandler;
	}

	public static Stage getLoginDlg() {
		return mLoginDialog;
	}

	public void setMainHandler(MainHandler mainHandler) {
		this.mainHandler = mainHandler;
	}

	@FXML
	void onConnect(ActionEvent event) {
		if (btnStartStop.getText().equals("시작")) {
			txtDisplay.clear();
			startClient();
		} else if (btnStartStop.getText().equals("종료")) {
			//send("#disConnect"+id);
			stopClient();
		}
	}

	@FXML
	void onPersonalMsg(ActionEvent event) {
		Parent pm;
		try {
			pm = FXMLLoader
					.load(getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/PersonalMessage.fxml"));
			Scene scene = new Scene(pm);
			Stage loginStage = new Stage();
			//loginStage.initOwner(owner);
			
			loginStage.setScene(scene);
			loginStage.setTitle("쪽지보내기");
			loginStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @brief 메시지 전송
	 * @param event
	 */
	@FXML
	void onSend(ActionEvent event) {
		send(txtInput.getText());
		txtInput.setText(null);
	}

	/**
	 * @brief 서버에 접속한 클라이언트들의 정보를 출력
	 * @param text
	 */
	void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}

	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress("localhost", 5001));
					send("#id" + id); // ID전송
					Platform.runLater(() -> {
						displayText("[연결 완료: " + socket.getRemoteSocketAddress() + "]");
						btnStartStop.setText("종료");
						btnSend.setDisable(false);
					});
				} catch (Exception e) {
					System.out.println("startClient chatch");
					Platform.runLater(() -> displayText("[서버 통신 안됨]"));
					if (!socket.isClosed()) {
						stopClient();
					}
					return;
				}
				receive();
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

	public void stopClient() {
		try {
			Platform.runLater(() -> {
				displayText("연결 끊음");
				btnStartStop.setText("시작");
				btnSend.setDisable(true);
			});

			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (Exception e) {
			System.out.println("stopClient chatch");
			e.printStackTrace();
		}
	}

	void receive() {
		while (true) {
			try {
				byte[] byteArr = new byte[100];
				InputStream inputStream = socket.getInputStream();
				int readByteCount = inputStream.read(byteArr);

				if (readByteCount == -1) {
					throw new IOException();
				}

				String data = new String(byteArr, 0, readByteCount, "UTF-8");

				System.out.println("리시브:"+data);
				
				if (data.contains("#message")) {
					System.out.println("receiveMessage 함수 실행");
					receiveMessage(data, readByteCount);
					//Platform.runLater(() -> displayText(data));
				} else {
					System.out.println(""+data);
					Platform.runLater(() -> displayText(data));
				}
			} catch (IOException e) {
				Platform.runLater(() -> displayText("서버 통신 안됨"));
				System.out.println("리시브 캐치");
				stopClient();
				break;
			}

		}
	}

	public void send(String data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				byte[] byteArr;
				try {
					byteArr = data.getBytes("UTF-8");
					OutputStream outputStream = socket.getOutputStream();
					outputStream.write(byteArr);
					outputStream.flush();
					//Platform.runLater(()->displayText("[보내기 완료]"));
				} catch (Exception e) {
					//Platform.runLater(() -> displayText("[서버 통신 안됨]"));
					System.out.println("센드 캐치");
					stopClient();
				}
			}
		};
		thread.start();
	}

	@FXML
	void onTextFieldKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			// System.out.println("엔터키 눌림");
			onSend(null);
		}
	}

	public boolean loginShow() {
		try {
			FXMLLoader loaderLogin = new FXMLLoader(
					(getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/Login.fxml")));
			Parent login = loaderLogin.load();
			Scene sceneLogin = new Scene(login);
			Stage loginStage = new Stage();
			// loginStage.initOwner(owner);
			loginStage.setScene(sceneLogin);
			loginStage.setTitle("로그인 화면");
			loginStage.showAndWait();

			if (lc.isCheck() == false) {
				return false;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	void receiveMessage(String data, int readByteCount) {
		String str = data.substring("#message#from".length(), data.length());
		System.out.println("receiveMessage함수:str "+str);
		//StringTokenizer st = new StringTokenizer(str, "#data");
		StringTokenizer st = new StringTokenizer(str);
		fromID = st.nextToken("#");
		String str2 = st.nextToken();
		System.out.println("str2: "+str2);
		msg= str2.substring(4, str2.length());
		
		
		//msg = st.nextToken();
		System.out.println("receiveMessage함수: fromid, msg"+fromID+" : "+msg);

		receiveMessageShow();
	}

	void receiveMessageShow() {
		Parent pm;
		try {
			pm = FXMLLoader.load(
					getClass().getResource("/com/pyj/chatClient0213/clientIO/layout/PersonalMessageReceive.fxml"));
			Scene scene = new Scene(pm);
			
			Platform.runLater(() -> {
				Stage loginStage = new Stage();
				// loginStage.initOwner(owner);
				loginStage.setScene(scene);
				loginStage.setTitle("받은 쪽지");
				loginStage.show();
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
