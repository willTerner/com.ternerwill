package com.huawei.java.main;

public class VirtualMachine {
	private VirtualMachineInfo vmInfo;
	private int id;
	private int serverId=-1;
	private int deployMode;//部署模式，0为双节点部署，1为单节点部署到a节点，2为单节点部署到b节点
	public VirtualMachine(VirtualMachineInfo vmInfo,int id)
	{
		this.vmInfo=vmInfo;
		this.id=id;
	}
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof VirtualMachine)
		{
			VirtualMachine vm=(VirtualMachine)o;
			return vm.id==this.id;
		}
		return false;
	}
	//getter
	public VirtualMachineInfo getVmInfo()
	{
		return vmInfo;
	}
	public int getId()
	{
		return id;
	}
	public int getDeployMode()
	{
		return deployMode;
	}
	//getter
	
	//setter
	public void setServerId(int serverId)
	{
		this.serverId=serverId;
	}
	public void setDeployMode(int mode)
	{
		this.deployMode=mode;
	}
	//setter
}
