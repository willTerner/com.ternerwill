package com.huawei.java.main;

public class ServerPart {
	private ServerPartInfo partInfo;
	private int remainCpuNumber;
	private int remainMemory;
	private int usedCpuNumber=0;
	private int usedMemory=0;
	private double ratio=-1;
	public ServerPart(ServerPartInfo info)
	{
		this.partInfo=info;
		remainCpuNumber=info.getCpuNumber();
		remainMemory=info.getMemory();
		this.ratio=this.partInfo.getRatio();
	}
	public boolean fitDeploy(int cpuNumber,int memory)
	{
		if(remainCpuNumber>=cpuNumber&&remainMemory>=memory)
		{
			if((remainCpuNumber>=partInfo.getCpuNumber()*0.75||remainMemory>=partInfo.getMemory()*0.75)&&Math.abs(this.ratio-cpuNumber*1.0/memory)>=1)
				return false;
			else return true;
		}
		return false;
	}
	//getter
	public ServerPartInfo getPartInfo()
	{
		return partInfo;
	}
	public int getRemainCpuNumber()
	{
		return remainCpuNumber;
	}
	public int getRemainMemory()
	{
		return remainMemory;
	}
	public int getUsedCpuNumber()
	{
		return usedCpuNumber;
	}
	public int getUsedMemory()
	{
		return usedMemory;
	}
	public double getRatio()
	{
		return ratio;
	}
	//getter
	
	//setter
	public void setRemainCpuNumber(int cpuNumber)
	{
		this.usedCpuNumber+=this.remainCpuNumber-cpuNumber;
		this.remainCpuNumber=cpuNumber;
	}
	public void setRemainMemory(int memory)
	{
		this.usedMemory+=this.remainMemory-memory;
		this.remainMemory=memory;
	}
	public void setRatio()
	{
		this.ratio=this.remainCpuNumber*1.0/this.remainMemory;
	}
}
