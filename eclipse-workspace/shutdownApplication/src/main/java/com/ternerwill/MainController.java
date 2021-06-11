package com.ternerwill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class MainController implements Initializable {

	@FXML
	private AnchorPane scene;
	@FXML
	private ColorPicker theme;
	
	@FXML
	private TextField hour1,minute1,second1,hour2,minute2,second2;
	@FXML
	private Button submit1,submit2;
	
	@FXML
	private Label title;
	@FXML
	private Button cancelShutdown;
	@FXML
	private Label cancleTime;
	private Properties props=new Properties();
	private String propsFile=null;
	
	public void changeTheme(ActionEvent e) {
		Color color=theme.getValue();
		scene.setBackground(new Background(new BackgroundFill(color,null,null)));
	}
	
	//取消关机
	public void cacelShut(ActionEvent e) {
		Runtime run=Runtime.getRuntime();
		try {
			props.load(new FileInputStream(propsFile));
			LocalDateTime scheTime=LocalDateTime.of(Integer.valueOf(props.getProperty("year")), Integer.valueOf(props.getProperty("month")), Integer.valueOf(props.getProperty("day")), Integer.valueOf(props.getProperty("hour")), Integer.valueOf(props.getProperty("minute")), Integer.valueOf(props.getProperty("second")));
			if(scheTime.compareTo(LocalDateTime.now())<0) {
				createErrorAlert("没有已计划的关机");
				return ;
			}
			run.exec("shutdown -a");
			showNoShutdownTime();
			createInfoAlert("取消关机成功");
			OutputStream output=new FileOutputStream(propsFile);
			props.setProperty("year", "-1");
			props.setProperty("month","-1");
			props.setProperty("day", "-1");
			props.setProperty("hour", "-1");
			props.setProperty("minute", "-1");
			props.setProperty("second","-1");
			props.store(output, null);
			output.close();
		} 
		catch(DateTimeException e1) {
			createErrorAlert("没有已计划的关机");
			e1.printStackTrace();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//通过方式一关机
	public void submitShutdown1(ActionEvent e) {
		int hour,minute,second=0;
		try {
			hour=Integer.valueOf(hour1.getText());
			minute=Integer.valueOf(minute1.getText());
			second=Integer.valueOf(second1.getText());
		}catch(NumberFormatException e1) {
			createErrorAlert("输入格式错误，请检查后重新输入");
			recoverInput1();
			return ;
		}
		
		LocalTime timeSche=LocalTime.of(hour, minute, second);
		LocalTime timeNow=LocalTime.now();
		Duration dura=Duration.ofSeconds(timeNow.getHour()*3600+timeNow.getMinute()*60+timeNow.getSecond());
		LocalTime shutdownTime=timeSche.minus(dura);
		if(shutdownTime.getHour()==0&&shutdownTime.getMinute()==0&&shutdownTime.getSecond()==0) {
			createErrorAlert("您输入的关机时间是当前时间，可直接关机");
			recoverInput1();
			return;
		}
		shutdown(shutdownTime);
		LocalDate nowDate=LocalDate.now();
		if(timeSche.compareTo(timeNow)<0) {
			nowDate=nowDate.plusDays(1);
		}
		showShutdownTime(LocalDateTime.of(nowDate, timeSche));
		recoverInput1();
	}
	
	//复原输入框
	private void recoverInput1() {
		hour1.setText("0");
		minute1.setText("0");
		second1.setText("0");
	}
	
	private void recoverInput2() {
		hour2.setText("0");
		minute2.setText("10");
		second2.setText("0");
	}
	private void createErrorAlert(String message) {
		Alert alert=new Alert(AlertType.ERROR,message);
		alert.show();
	}
	
	private void createInfoAlert(String message) {
		Alert alert=new Alert(AlertType.INFORMATION,message);
		alert.show();
	}
	
	
	//通过方式二来关机
	public void submitShutdown2(ActionEvent e) {
		int hour,minute,second=0;
		try {
			hour=Integer.valueOf(hour2.getText());
			minute=Integer.valueOf(minute2.getText());
			second=Integer.valueOf(second2.getText());
			if(second>=60||minute>=60||hour>=24) {
				throw new IllegalArgumentException("时或分或秒超过范围，请重新输入");
			}
			if(hour<0||minute<0||second<0) {
				throw new NumberFormatException();
			}
		}catch(NumberFormatException e1) {
			createErrorAlert("输入格式有问题，请检查后重新输入");
			recoverInput2();
			return;
		}catch(IllegalArgumentException e2) {
			createErrorAlert(e2.getMessage());
			recoverInput2();
			return ;
		}
		if(hour==0&&minute==0&&second==0) {
			createErrorAlert("您输入的关机时间是当前时间，可直接关机");
			recoverInput2();
			return;
		}
		shutdown(LocalTime.of(hour, minute,second));
		LocalDateTime now=LocalDateTime.now();
		now=now.plusSeconds(second).plusMinutes(minute).plusHours(hour);
		showShutdownTime(now);
		recoverInput2();
	}
	
	/*@param shutdownTime 距离关机的时长
	*
	*/
	private void shutdown(LocalTime shutdownTime) {
		LocalDateTime now=LocalDateTime.now();
		LocalDateTime scheTime=now.plusSeconds(shutdownTime.getSecond()).plusMinutes(shutdownTime.getMinute()).plusHours(shutdownTime.getHour());
		try {
			Runtime run=Runtime.getRuntime();
			run.exec("shutdown -s -t "+(shutdownTime.getHour()*3600+shutdownTime.getMinute()*60+shutdownTime.getSecond()));
			props.setProperty("hour", String.valueOf(scheTime.getHour()));
			props.setProperty("minute", String.valueOf(scheTime.getMinute()));
			props.setProperty("second", String.valueOf(scheTime.getSecond()));
			props.setProperty("year", String.valueOf(scheTime.getYear()));
			props.setProperty("month", String.valueOf(scheTime.getMonthValue()));
			props.setProperty("day", String.valueOf(scheTime.getDayOfMonth()));
			OutputStream output=new FileOutputStream(propsFile);
			props.store(output, null);
			output.close();
			createInfoAlert("设置关机时间成功!");
		}catch(IOException e1) {
			createErrorAlert("执行系统命令异常，设置关机时间未成功");
			e1.printStackTrace();
		}catch(Exception e3) {
			createErrorAlert("未知错误导致设置关机时间失败");
			e3.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Map<String,String> map=System.getenv();
		String userName=map.get("USERNAME");
		propsFile="C:/users/"+userName+"/AppData/"+"Local/shutdownApplication/task.properties";
		File file=new File(propsFile);
		if(!file.exists()) {
			try {
				File dir=new File("C:/users/"+userName+"/AppData/"+"Local/shutdownApplication");
				if(!dir.exists()||(dir.exists()&&!dir.isDirectory())) {
					dir.mkdirs();
				}
				file.createNewFile();
				FileOutputStream output=new FileOutputStream(propsFile);
				props.setProperty("year", "-1");
				props.setProperty("month", "-1");
				props.setProperty("day", "-1");
				props.setProperty("hour", "-1");
				props.setProperty("minute","-1");
				props.setProperty("second", "-1");
				props.store(output, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			props.load(new FileInputStream(propsFile));
			LocalDateTime scheTime=LocalDateTime.of(Integer.valueOf(props.getProperty("year")), Integer.valueOf(props.getProperty("month")), Integer.valueOf(props.getProperty("day")),Integer.valueOf(props.getProperty("hour")),Integer.valueOf(props.getProperty("minute")), Integer.valueOf(props.getProperty("second")));
			LocalDateTime now=LocalDateTime.now();
			if(scheTime.compareTo(now)<0) {
				showNoShutdownTime();
				return;
			}
			showShutdownTime(scheTime);
		}
		catch(DateTimeException e) {
			showNoShutdownTime();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void showShutdownTime(LocalDateTime scheTime) {
		if(scheTime.toLocalDate().compareTo(LocalDate.now())>0) {
			cancleTime.setText("计算机将于明天"+String.format("%02d",scheTime.getHour())+":"+String.format("%02d",scheTime.getMinute())+":"+String.format("%02d",scheTime.getSecond())+"关闭");
		}
		else {
			cancleTime.setText("计算机将于今天"+String.format("%02d",scheTime.getHour())+":"+String.format("%02d",scheTime.getMinute())+":"+String.format("%02d",scheTime.getSecond())+"关闭");
		}
	}
	private void showNoShutdownTime() {
		cancleTime.setText("无计划关机时间");
	}
    
}
