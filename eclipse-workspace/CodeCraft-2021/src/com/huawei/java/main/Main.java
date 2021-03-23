package com.huawei.java.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
	static Scanner scan;
	static ArrayList<ServerInfo> serverList=null;
	static ArrayList<ServerPartInfo> serverPartInfoList=null;
	static ArrayList<VirtualMachineInfo> vmList=null;
	static ArrayList<Server> servers=new ArrayList<>();
	static Pattern pattern=Pattern.compile("\\((.+)\\)");
	static DayDeployMessage[] deployMessages;
	static int vmNumber=0;
	static int dayNumber=0;
	//从文件中读取数据
	public static void loadDataByFile()
	{
		FileInputStream input=null;
		try {
			input=new FileInputStream("training-1.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scan=new Scanner(input);
		int serverNum=0;
		if(scan.hasNext())
			serverNum=scan.nextInt();
		serverList=new ArrayList<>(serverNum);
		serverPartInfoList=new ArrayList<>(serverNum);
		int i=0;
		do
		{
			String line=null;
			if(scan.hasNextLine())
			{
				line=scan.nextLine();
				if(!"".equals(line))
				{
					Matcher match=pattern.matcher(line);
					if(match.find())
					{
						String result=match.group(1);
						String[] props=result.split(",");
						ServerInfo info=new ServerInfo(props[0],Integer.valueOf(props[1].trim()),Integer.valueOf(props[2].trim()),Integer.valueOf(props[3].trim()),Integer.valueOf(props[4].trim()));
						serverList.add(info);
						i++;
					}
				}
			}
		}while(i<serverNum);
		//对服务器按照成本从小到大排序
		Collections.sort(serverList);
		//构造相应的serverPartInfo
		for(ServerInfo info:serverList)
		{
			serverPartInfoList.add(new ServerPartInfo(info));
		}
		int vmNum=0;
		if(scan.hasNextInt())
		{
			vmNum=scan.nextInt();
		}
		vmList=new ArrayList<>(vmNum);
		i=0;
		do
		{
			String line=null;
			if(scan.hasNextLine())
			{
				line=scan.nextLine();
				if(!"".equals(line))
				{
					Matcher match=pattern.matcher(line);
					if(match.find())
					{
						String[] props=match.group(1).split(",");
						vmList.add(new VirtualMachineInfo(props[0],Integer.valueOf(props[1].trim()),Integer.valueOf(props[2].trim()),Integer.valueOf(props[3].trim())==1?true:false));
						i++;
					}
				}
			}
		}while(i<vmNum);
	}
	public static void isOverloaded()
	{
		for(Server server:servers)
		{
			int cpuA=0;
			int memoryA=0;
			int cpuB=0;
			int memoryB=0;
			for(VirtualMachine vm:server.getVms())
			{
				int mode=vm.getDeployMode();
				int cpuNumber=vm.getVmInfo().getCpuNumber();
				int memory=vm.getVmInfo().getMemory();
				if(mode==0)
				{
					cpuA+=cpuNumber/2;
					cpuB+=cpuNumber/2;
					memoryA+=memory/2;
					memoryB+=memory/2;
				}
				else if(mode==1)
				{
					cpuA+=cpuNumber;
					memoryA+=memory;
				}
				else if(mode==2)
				{
					cpuB+=cpuNumber;
					memoryB+=memory;
				}
			}
			if(cpuA>server.getPartA().getPartInfo().getCpuNumber()&&memoryA>=server.getPartA().getPartInfo().getMemory())
			{
				throw new IllegalArgumentException("Server Overloaded");
			}
			if(cpuB>server.getPartB().getPartInfo().getCpuNumber()&&memoryB>=server.getPartB().getPartInfo().getMemory())
			{
				throw new IllegalArgumentException("Server Overloaded");
			}
		}
	}
	public static void main(String[] args){
							loadDataByFile();
		 int dayNum=0; 
		 if(scan.hasNextInt()) 
			 dayNum=scan.nextInt();
		 deployMessages=new DayDeployMessage[dayNum];
		 if(dayNum==0)
		 {
			return ;
		 }
		 int i=0;
		 dayNumber=dayNum;
		 do
		 {
			 int requestNum=0; 
			 if(scan.hasNextInt())
				 requestNum=scan.nextInt();
			 int j=0;
			 if(requestNum==0)
				 break;
			 int formerServerNum=servers.size();
			 deployMessages[i]=new DayDeployMessage(i+1,formerServerNum);
			 //处理每一天的请求
			 do
			 {
				 String line=null;
		         if(scan.hasNextLine()) 
		        	 line=scan.nextLine();
		         if(!"".equals(line))
		         {
		        	 Matcher match=pattern.matcher(line);
			         if(match.find())
			         {
			        	 String[] request=match.group(1).split(","); 
				         String requestType=request[0]; 
				         if("add".equals(requestType))
				         {
				        	 AddVMHandler.handleAdd(request,i);
				        	 deployMessages[i].addInfo(Integer.valueOf(request[2].trim()));
				        	 vmNumber++;
				         }
				         else if("del".equals(requestType))
				         {
				        	 DelVMHandler.delVM(Integer.valueOf(request[1].trim()),i);
				        	 vmNumber--;
				         }
				         j++;
			         } 
		         }
			 }while(j<requestNum);
			 deployMessages[i].setDeployMessage();
			 //isOverloaded();
			 i++;
		 }while(i<dayNum);
		 printResult();
		 int k=0;
		 for(Server ser:servers)
		 {
			 if(ser.isUnfit())
				System.out.println(k);
			 k++;
		 }
//		 calcuServers();
//		 printServers();
		 calculateCost();
	}
	private static void printResult()
	{
		//FileOutputStream output=new FileOutputStream("result.txt");
		for(int k=0;k<dayNumber;k++)
		 {
			 //System.out.print(deployMessages[k].toString());
//			 try {
//				output.write(deployMessages[k].toString().getBytes("UTF-8"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		 }
	}
	private static void calcuServers()
	{
		 FileOutputStream output=null;
		 try {
			output=new FileOutputStream("bugServers.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 for(Server server:servers)
		 {
			 String price=String.valueOf(server.getServerInfo().getPrice())+"\n";
			 try {
				output.write(price.getBytes("UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}
	private static void printServers()
	{
		 int sum=0;
		 int totalPrice=0;
			for(int j=dayNumber/16;j<dayNumber;j++)
			{
				//int cost=0;
				ArrayList<Server> buyServers=deployMessages[j].getCreateServers();
//				int size=buyServers.size();
//				System.out.println("第"+(j+1)+"天购买的服务器数量"+size);
//				sum+=size;
				for(Server server:buyServers)
				{
					//System.out.println(server.getServerInfo().toString());
					//cost+=server.getServerInfo().getPrice();
					sum+=server.getServerInfo().getPrice();
					totalPrice+=server.getServerInfo().getMaintainCost()*(dayNumber-j);
				}
				//System.out.println("第"+(j+1)+"天购买的服务器价格为"+cost);
			}
			System.out.println("前面一部分购买服务器的成本为"+sum);
			System.out.println("前面一部分的维护总成本为"+totalPrice);
			System.out.println("\n");
	}
	private static void  calculateCost()
	{
		long sum=0;
		for(Server server:servers)
		{
			sum+=server.getServerInfo().getPrice();
		}
		for(int k=0;k<dayNumber;k++)
		{
			for(Server server:deployMessages[k].getCreateServers())
			{
				int mainCost=server.getServerInfo().getMaintainCost();
				sum+=mainCost*(dayNumber-k);
			}
		}
		System.out.println("总成本"+sum);
	}

}
