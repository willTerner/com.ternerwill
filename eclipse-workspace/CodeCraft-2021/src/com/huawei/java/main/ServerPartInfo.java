package com.huawei.java.main;

public class ServerPartInfo {
	public String modelType;
	public int cpuNumber;
	public int memory;
	public ServerPartInfo(ServerInfo info)
	{
		this.modelType=info.getModleType();
		this.cpuNumber=info.getCpuNumber()/2;
		this.memory=info.getMemory()/2;
	}
	@Override
	public String toString()
	{
		return "[modelType="+modelType+", cpuNumber="+cpuNumber+", memory="+memory+"]";
	}
	//getter
	public String getModelType()
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
	//getter
}
