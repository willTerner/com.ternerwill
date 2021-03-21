package com.huawei.java.main;

public class ServerPartInfo {
	public String modelType;
	public int cpuNumber;
	public int memory;
	public double ratio=-1;
	public ServerPartInfo(ServerInfo info)
	{
		this.modelType=info.getModleType();
		this.cpuNumber=info.getCpuNumber()/2;
		this.memory=info.getMemory()/2;
		this.ratio=this.cpuNumber*1.0/this.memory;
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
	public double getRatio()
	{
		return ratio;
	}
	//getter
}
