package com.huawei.java.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	//从文件中读取数据
	public static void loadDataByFile() throws FileNotFoundException
	{
		FileInputStream input=new FileInputStream("training-1.txt");
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
					try {
						loadDataByFile();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 int dayNum=0; 
		 if(scan.hasNextInt()) 
			 dayNum=scan.nextInt();
		 deployMessages=new DayDeployMessage[dayNum];
		 if(dayNum==0)
		 {
			return ;
		 }
		 int i=0;
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
			 i++;
		 }while(i<dayNum);
		 for(int k=0;k<dayNum;k++)
		 {
			 System.out.print(deployMessages[k].toString());
		 }
	}

}
