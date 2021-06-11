package com.ternerwill;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root=FXMLLoader.load(Main.class.getResource("Main.fxml"));
		Scene scene=new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/图标.png")));
		primaryStage.setResizable(false);
		System.out.println(scene.getWidth());
		primaryStage.show();
	}
}
