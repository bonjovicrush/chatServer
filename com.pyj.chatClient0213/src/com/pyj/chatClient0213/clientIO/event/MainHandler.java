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

	ExecutorService executorService; // ������ Ǯ ����
	Socket socket;
	static Stage mLoginDialog;
	// List<Client> connections = new Vector<Client>(); // ������ �����ϴ� Ŭ���̾�Ʈ���� ����

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
		if (btnStartStop.getText().equals("����")) {
			txtDisplay.clear();
			startClient();
		} else if (btnStartStop.getText().equals("����")) {
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
			loginStage.setTitle("����������");
			loginStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @brief �޽��� ����
	 * @param event
	 */
	@FXML
	void onSend(ActionEvent event) {
		send(txtInput.getText());
		txtInput.setText(null);
	}

	/**
	 * @brief ������ ������ Ŭ���̾�Ʈ���� ������ ���
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
					send("#id" + id); // ID����
					Platform.runLater(() -> {
						displayText("[���� �Ϸ�: " + socket.getRemoteSocketAddress() + "]");
						btnStartStop.setText("����");
						btnSend.setDisable(false);
					});
				} catch (Exception e) {
					System.out.println("startClient chatch");
					Platform.runLater(() -> displayText("[���� ��� �ȵ�]"));
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
				displayText("���� ����");
				btnStartStop.setText("����");
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

				System.out.println("���ú�:"+data);
				
				if (data.contains("#message")) {
					System.out.println("receiveMessage �Լ� ����");
					receiveMessage(data, readByteCount);
					//Platform.runLater(() -> displayText(data));
				} else {
					System.out.println(""+data);
					Platform.runLater(() -> displayText(data));
				}
			} catch (IOException e) {
				Platform.runLater(() -> displayText("���� ��� �ȵ�"));
				System.out.println("���ú� ĳġ");
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
					//Platform.runLater(()->displayText("[������ �Ϸ�]"));
				} catch (Exception e) {
					//Platform.runLater(() -> displayText("[���� ��� �ȵ�]"));
					System.out.println("���� ĳġ");
					stopClient();
				}
			}
		};
		thread.start();
	}

	@FXML
	void onTextFieldKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			// System.out.println("����Ű ����");
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
			loginStage.setTitle("�α��� ȭ��");
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
		System.out.println("receiveMessage�Լ�:str "+str);
		//StringTokenizer st = new StringTokenizer(str, "#data");
		StringTokenizer st = new StringTokenizer(str);
		fromID = st.nextToken("#");
		String str2 = st.nextToken();
		System.out.println("str2: "+str2);
		msg= str2.substring(4, str2.length());
		
		
		//msg = st.nextToken();
		System.out.println("receiveMessage�Լ�: fromid, msg"+fromID+" : "+msg);

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
				loginStage.setTitle("���� ����");
				loginStage.show();
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
