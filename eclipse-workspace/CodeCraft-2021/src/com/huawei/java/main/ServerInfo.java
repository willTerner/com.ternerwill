package com.huawei.java.main;

import java.util.Objects;

class ServerInfo implements Comparable<ServerInfo>{
	private String modelType;//服务器型号
	private int cpuNumber;//cpu核数
	private int memory;//内存大小
	private int price;//硬件成本
	private int maintainCost;//维护成本
	//初始化服务器
	public ServerInfo(String modelType,int cpuNumber,int memory,int price,int maintainCost)
	{
		this.modelType=modelType;
		this.cpuNumber=cpuNumber;
		this.price=price;
		this.maintainCost=maintainCost;
		this.memory=memory;
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
	//getter
	private boolean isWasteCpu(int remainCpuNumber,int remainMemory)
	{
		if(remainCpuNumber<=2&&remainMemory>=40)
			return true;
		return false;
	}
	private boolean isWasteMemory(int remainCpuNumber,int remainMemory)
	{
		if(remainMemory<=2&&remainCpuNumber>=40)
			return true;
		return false;
	}
	public boolean couldDeploy(VirtualMachineInfo vmInfo)
	{
		if(vmInfo.getIsDoublePart())
		{
			if(cpuNumber>=vmInfo.getCpuNumber()&&memory>=vmInfo.getMemory())
			{
				int laterCpu=this.cpuNumber/2-vmInfo.getCpuNumber()/2;
				int laterMemory=this.memory/2-vmInfo.getMemory()/2;
				if(isWasteCpu(laterCpu,laterMemory)||isWasteMemory(laterCpu,laterMemory))
					return false;
				else return true;
			}
			return false;
		}
		else
		{
			if(cpuNumber/2>=vmInfo.getCpuNumber()&&memory/2>=vmInfo.getMemory())
			{
				int laterCpu=this.cpuNumber/2-vmInfo.getCpuNumber();
				int laterMemory=this.memory/2-vmInfo.getMemory();
				if(isWasteCpu(laterCpu,laterMemory)||isWasteMemory(laterCpu,laterMemory))
					return false;
				else return true;
			}
			return false;
		}
	}
	//针对后面几天采用原来的匹配方法
	public boolean couldDeploy2(VirtualMachineInfo vmInfo)
	{
		if(vmInfo.getIsDoublePart())
		{
			if(cpuNumber>=vmInfo.getCpuNumber()&&memory>=vmInfo.getMemory())
				return true;
			else return false;
		}
		else
		{
			if(cpuNumber/2>=vmInfo.getCpuNumber()&&memory/2>=vmInfo.getMemory())
			{
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
