package com.huawei.java.main;

public class VirtualMachineInfo {
	private String modelType;
	private int cpuNumber;
	private int memory;
	private boolean isDoublePart;
	private double ratio=-1;
	private int type=0;//cpu/内存
	public VirtualMachineInfo(String modelType,int cpuNumber,int memory,boolean isDoublePart)
	{
		this.cpuNumber=cpuNumber;
		this.modelType=modelType;
		this.memory=memory;
		this.isDoublePart=isDoublePart;
		this.ratio=this.cpuNumber*1.0/this.memory;
		if(memory-cpuNumber>=100)
			type=1;//内存多
	}
	//getter
	public int getType()
	{
		return type;
	}
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
