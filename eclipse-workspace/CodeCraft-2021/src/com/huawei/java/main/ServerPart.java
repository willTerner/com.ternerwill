package com.huawei.java.main;

public class ServerPart {
	private ServerPartInfo partInfo;
	private int remainCpuNumber;
	private int remainMemory;
	private int usedCpuNumber=0;
	private int usedMemory=0;
	public ServerPart(ServerPartInfo info)
	{
		this.partInfo=info;
		remainCpuNumber=info.getCpuNumber();
		remainMemory=info.getMemory();
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
}
