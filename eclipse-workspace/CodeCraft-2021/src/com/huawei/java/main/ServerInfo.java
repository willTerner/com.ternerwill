package com.huawei.java.main;

import java.util.Objects;

class ServerInfo implements Comparable<ServerInfo>{
	private String modelType;//服务器型号
	private int cpuNumber;//cpu核数
	private int memory;//内存大小
	private int price;//硬件成本
	private int maintainCost;//维护成本
	private double ratio=-1;
	//初始化服务器
	public ServerInfo(String modelType,int cpuNumber,int memory,int price,int maintainCost)
	{
		this.modelType=modelType;
		this.cpuNumber=cpuNumber;
		this.price=price;
		this.maintainCost=maintainCost;
		this.memory=memory;
		this.ratio=this.cpuNumber*1.0/this.memory;
	}
	//getter
	public String getModleType()
	{
		return modelType;
	}
	public int getCpuNumber()
	{
		return cpuNumber;
	}
	public int getMemory()
	{
		return memory;
	}
	public int getPrice()
	{
		return price;
	}
	public int getMaintainCost()
	{
		return maintainCost;
	}
	public double getRatio()
	{
		return ratio;
	}
	//getter
	public boolean couldDeploy(VirtualMachineInfo vmInfo)
	{
		if(vmInfo.getIsDoublePart())
		{
			if(cpuNumber>=vmInfo.getCpuNumber()&&memory>=vmInfo.getMemory())
			{
				if((vmInfo.getCpuNumber()>=cpuNumber/2||vmInfo.getMemory()>=memory/2)&&Math.abs(ratio-vmInfo.getRatio())>=1)
				return false;
				else 
					return true;
			}
			return false;
		}
		else
		{
			if(cpuNumber/2>=vmInfo.getCpuNumber()&&memory/2>=vmInfo.getMemory())
			{
				if((vmInfo.getCpuNumber()>=cpuNumber/4||vmInfo.getMemory()>=memory/4)&&Math.abs(ratio-vmInfo.getRatio())>=1)
					return false;
				else 
				return true;
			}
			else return false;
		}
	}
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ServerInfo)
		{
			ServerInfo s=(ServerInfo)o;
			return Objects.equals(this.modelType,s.modelType);
		}
		return false;
	}
	@Override
	public int compareTo(ServerInfo server)
	{
		if(server==null)
		{
			System.out.println("parameter is null!");
			return -1;
		}
		else
		{
			//首先按照成本排序
			int price2=server.price;
			int maintain=server.maintainCost;
			if(this.equals(server))
				return 0;
			else
			{
				if(price<price2)
					return -1;
				else if(price>price2)
					return 1;
				else if(maintainCost<maintain)
					return -1;
				else 
					return 1;
			}
		}
	}
	@Override
	public String toString()
	{
		return "["+modelType+","+cpuNumber+","+memory+","+price+","+maintainCost+"]";
	}
}
