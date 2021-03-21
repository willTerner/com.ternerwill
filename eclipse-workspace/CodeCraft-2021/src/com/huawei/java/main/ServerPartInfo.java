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
	private boolean issameTypeRatio(int cpuNumber,int memory)
	{
		double vmRatio=cpuNumber*1.0/memory;
		if((ratio>=1&&vmRatio>=1)||(ratio<=1&&vmRatio<=1))
				return true;
		else return false;
	}
	@Override
	public String toString()
	{
		return "[modelType="+modelType+", cpuNumber="+cpuNumber+", memory="+memory+"]";
	}
	public boolean fitDeploy(int cpuNumber,int memory)
	{
		if(this.cpuNumber>=cpuNumber&&this.memory>=memory&&issameTypeRatio(cpuNumber,memory))
			return true;
		else return false;
	}
	public boolean fitDeploy2(int cpuNumber,int memory)
	{
		if(this.cpuNumber>=cpuNumber&&this.memory>=memory)
			return true;
		return false;
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
