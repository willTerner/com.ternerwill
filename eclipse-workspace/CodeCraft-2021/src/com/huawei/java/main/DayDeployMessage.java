package com.huawei.java.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DayDeployMessage {
	private int index;
	private int formerSeverNum;
	private LinkedHashMap<String,Integer> buyMessage=new LinkedHashMap<>();
	private ArrayList<Server> dayServers=new ArrayList<>();
	private String deployMessage="";
	private ArrayList<Integer> addInfo=new ArrayList<Integer>();//一天中的add请求
	private LinkedHashMap<Integer,VirtualMachineTheDayDel> vmsTheDayDel=new LinkedHashMap<>();
	//一天中添加的虚拟机
	private ArrayList<VirtualMachine> addvms=new ArrayList<>();
	private ArrayList<Server> delServers=new ArrayList<>();
	private ArrayList<Server> createServers=new ArrayList<>();
	private String switchMessage="";
	public void addCreateServers(Server server)
	{
		createServers.add(server);
	}
	//迁移服务器
	public int switchVirtualMachine()
	{
		int canSwitchNumber=(int) Math.floor(5*Main.vmNumber/1000);
		if(canSwitchNumber<=0)
			return 0;
		if((Main.dayNumber-index)>Main.dayNumber/2)
			return 0;
		int i=0;
		for(Server server:createServers)
		{
			Iterator<VirtualMachine> iterator=server.getVms().iterator();
			while(iterator.hasNext())
			{
				
				VirtualMachine vm=iterator.next();
				for(Server delserver:delServers)
				{
					if(server!=delserver)
					{
							int mode=delserver.addVM(vm);
							if(mode!=-1)
							{
									vm.setDeployMode(mode);
									vm.setServerId(delserver.getId());
									vm.setServer(delserver);
									delserver.insertVM(vm);
									iterator.remove();
									server.delVM(vm);
									if(mode==0)
									switchMessage+="("+vm.getId()+", "+vm.getServerId()+")\n";
									else if(mode==1)
									switchMessage+="("+vm.getId()+", "+vm.getServerId()+", A)\n";
									else if(mode==2)
										switchMessage+="("+vm.getId()+", "+vm.getServerId()+", B)\n";
									i++;
									if(i==canSwitchNumber)
										return i;
									break;
							}
					}
				}
			}
		}
		return i;
	}
	public String getSwitchMessage()
	{
		return switchMessage;
	}
	public ArrayList<Server> getCreateServers()
	{
		return createServers;
	}
	public ArrayList<Server> getAddServers()
	{
		return dayServers;
	}
	public DayDeployMessage(int index,int formerSeverNum)
	{
		this.index=index;
		this.formerSeverNum=formerSeverNum;
	}
	public void addDelServers(Server server)
	{
		int isExist=0;
		for(Server server1:delServers)
		{
			if(server1==server)
			{
				isExist=1;
				break;
			}
		}
		if(isExist==0)
			delServers.add(server);
	}
	public void addVms(VirtualMachine vm)
	{
		addvms.add(vm);
	}
	public void addVmsTheDayDel(Integer id,int mode,Server server)
	{
		vmsTheDayDel.put(id,new VirtualMachineTheDayDel(mode,server));
	}
	public ArrayList<Integer> getAddInfo()
	{
		return addInfo;
	}
	public void setDeployMessage()
	{
		allocateId();
		//确定一天的输出信息
		for(int i=0;i<addInfo.size();i++)
		{
			//如果该虚拟机是当天删除的虚拟机，要单独拿出来讨论
			for(Integer vid:vmsTheDayDel.keySet())
			{
				if(addInfo.get(i)==vid)
				{
					VirtualMachineTheDayDel del=vmsTheDayDel.get(vid);
					int mode=del.mode;
					if(mode==0)
						deployMessage+="("+del.server.getId()+")\n";
					else if(mode==1)
						deployMessage+="("+del.server.getId()+", A)\n";
					else if(mode==2)
						deployMessage+="("+del.server.getId()+", B)\n";
				}
			}
			//遍历一天中用到的服务器，找到合适的虚拟机
			for(Server server:dayServers)
			{
				for(VirtualMachine vm:server.getVms())
				{
					if(addInfo.get(i)==vm.getId())
					{
						int mode=vm.getDeployMode();
						if(mode==0)
							deployMessage+="("+server.getId()+")\n";
						else if(mode==1)
							deployMessage+="("+server.getId()+", A)\n";
						else if(mode==2)
							deployMessage+="("+server.getId()+", B)\n";
					}
				}
			}
		}
	}
	public boolean hasSameServer(Server server)
	{
		for(Server server1:dayServers)
		{
			if(server1==server)
				return true;
		}
		return false;
	}
	public void addServer(Server server)
	{
		dayServers.add(server);
	}
	public void addInfo(int vmId)
	{
		addInfo.add(vmId);
	}
	public void buyServer(String modelType)
	{
		if(buyMessage.keySet().contains(modelType))
		{
			buyMessage.replace(modelType, buyMessage.get(modelType)+1);
		}
		else
		{
			buyMessage.put(modelType, 1);
		}
	}
	public void allocateId()
	{
		int index=formerSeverNum;
		for(String serverModelType:buyMessage.keySet())
		{
			for(int i=0;i<buyMessage.get(serverModelType);i++)
			{
				for(Server server:dayServers)
				{
					if(server.getId()==-1&&server.getServerInfo().getModleType().equals(serverModelType))
					{
						server.setId(index);
						for(VirtualMachine vm:server.getVms())
						{
							vm.setServerId(server.getId());
						}
						break;
					}
				}
				index++;
			}
		}
	}
	//输出每一天的结果
	@Override
	public String toString()
	{
		if(buyMessage.size()==0)
			return "(purchase, "+0+")\n"+"(migration, 0)\n"+deployMessage;
		else
		{
			String result="(purchase, "+buyMessage.size()+")\n";
			for(String modelType:buyMessage.keySet())
			{
				result+="("+modelType+", "+buyMessage.get(modelType)+")\n";
			}
			result+="(migration, 0)\n";
			result+=deployMessage;
			return result;
		}
	}
	
}
