package ui;

import javafx.fxml.FXML;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;

import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;

import javafx.scene.image.ImageView;
import rmi.RemoteHelper;

import javafx.scene.control.Menu;

public class MainFrameController {
	@FXML
	private Menu openMenu;
	@FXML
	private TextArea codeArea;
	@FXML
	private TextArea inputArea;
	@FXML
	private TextArea outputArea;
	@FXML
	private TitledPane loggedInPane;
	@FXML
	private ImageView userDisplayPicture;
	@FXML
	private Label userName;
	
	/**
	 * 当前代码是否已保存
	 */
	private boolean isSaved = true;
	
	public void init(){
		codeArea.textProperty().addListener(cl -> {
			isSaved = false;
		});
	}

	// Event Listener on MenuItem.onAction
	@FXML
	public void clickSaveMenuItem(ActionEvent event) {
		if(!isSaved){
			LocalDateTime localDateTime = LocalDateTime.now();
			String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "-");
			if(time.contains(".")){
				time = time.substring(0, time.charAt('.'));
			}
			try {
				RemoteHelper.getInstance().getIOService().writeFile(codeArea.getText(), userName.getText(), localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE) + '-' + time);
				// TODO 刷新历史版本菜单
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			isSaved = true;
		}
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void clickExitMenuItem(ActionEvent event) {
		if(isSaved){
			exit();
		}
		else{
			Alert alert = new Alert(AlertType.CONFIRMATION, "要保存当前代码吗？", 
					new ButtonType("是", ButtonData.YES),
					new ButtonType("否", ButtonData.NO),
					new ButtonType("取消", ButtonData.CANCEL_CLOSE));
			alert.setTitle("确认");
			alert.setHeaderText("当前代码尚未保存");
			alert.showAndWait().ifPresent(response -> {
				if(response != ButtonType.CANCEL){
					if(response == ButtonType.YES){
						clickSaveMenuItem(null);
					}
					exit();
				}
			});
		}
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void clickExecuteMenuItem(ActionEvent event) {
		try {
			outputArea.setText(RemoteHelper.getInstance().getExecuteService().execute(codeArea.getText(), inputArea.getText()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	// Event Listener on MenuItem.onAction
	@FXML
	public void clickAboutMenuItem(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("关于");
		alert.setHeaderText(ui.Main.Name);
		alert.setContentText("开发者：胡本霖");
		alert.show();
	}
	// Event Listener on Button.onAction
	@FXML
	public void clickLogOutButton(ActionEvent event) {
		try {
			RemoteHelper.getInstance().getUserService().logout(userName.getText());
			ui.Main.primaryStage.setScene(ui.Main.logInScene);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setUserData(String userId){
		userName.setText(userId);
		// TODO 加载头像功能
	}
	
	private void exit(){
		try {
			RemoteHelper.getInstance().getUserService().logout(userName.getText());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		ui.Main.exit();
	}
}
