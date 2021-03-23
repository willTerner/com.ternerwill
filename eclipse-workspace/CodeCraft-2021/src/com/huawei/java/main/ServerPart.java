package com.huawei.java.main;

import java.util.ArrayList;

public class ServerPart {
	private ServerPartInfo partInfo;
	private int remainCpuNumber;
	private int remainMemory;
	private int usedCpuNumber=0;
	private int usedMemory=0;
	private ArrayList<VirtualMachine> vms=new ArrayList<>();
	public ServerPart(ServerPartInfo info)
	{
		this.partInfo=info;
		remainCpuNumber=info.getCpuNumber();
		remainMemory=info.getMemory();
	}
	public void addVms(VirtualMachine vm)
	{
		vms.add(vm);
	}
	public ArrayList<VirtualMachine> getVms()
	{
		return vms;
	}
	public boolean isUnfit()
	{
		if(isWasteCpu()||isWasteMemory())
		{
			if(vms.size()<2)
				return true;
		}
//		else if((remainCpuNumber<=20&&remainMemory>=100)||(remainMemory<=20&&remainCpuNumber>=100))
//			return true;
		return false;
	}
	private boolean isWasteMemory()
	{
		if(remainCpuNumber<=2&&remainMemory>=40)
			return true;
		else if(remainCpuNumber<=20&&remainMemory>=100)
			return true;
		return false;
	}
	private boolean isWasteCpu()
	{
		if(remainMemory<=2&&remainCpuNumber>=40)
			return true;
		else if(remainMemory<=20&&remainCpuNumber>=100)
			return true;
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
