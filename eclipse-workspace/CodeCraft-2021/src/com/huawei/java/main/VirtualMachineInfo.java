package com.huawei.java.main;

public class VirtualMachineInfo {
	private String modelType;
	private int cpuNumber;
	private int memory;
	private boolean isDoublePart;
	private double ratio=-1;
	public VirtualMachineInfo(String modelType,int cpuNumber,int memory,boolean isDoublePart)
	{
		this.cpuNumber=cpuNumber;
		this.modelType=modelType;
		this.memory=memory;
		this.isDoublePart=isDoublePart;
		this.ratio=this.cpuNumber*1.0/this.memory;
	}
	//getter
	public double getRatio()
	{
		return ratio;
	}
	public String getModelType()
	{
		return modelType;
	}
	public int getMemory()
	{
		return memory;
	}
	public int getCpuNumber()
	{
		return cpuNumber;
	}
	public boolean getIsDoublePart()
	{
		return isDoublePart;
	}
	//getter
	@Override
	public String toString()
	{
		return "[modelType="+modelType+" ,cpuNumber="+cpuNumber+", memory="+memory+", idDoublePart="+isDoublePart+"]";
	}
}
