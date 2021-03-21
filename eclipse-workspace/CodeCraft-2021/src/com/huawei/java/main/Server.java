package com.huawei.java.main;

import java.util.ArrayList;

public class Server {
	private ServerInfo serverInfo;
	private int id=-1;//服务器id
	private ServerPart partA;
	private ServerPart partB;
	private int usedCpu=0;//使用了多少cpu
	private int usedMemory=0;//使用了多少内存
	private int remainCpu;
	private int remainMemory;
	public ArrayList<VirtualMachine> vms=new ArrayList<>();
	public Server(ServerInfo info,ServerPartInfo partInfo)
	{
		this.serverInfo=info;
		this.partA=new ServerPart(partInfo);
		this.partB=new ServerPart(partInfo);
		remainCpu=info.getCpuNumber();
		remainMemory=info.getMemory();
	}
	//getttor
	public ArrayList<VirtualMachine> getVms()
	{
		return vms;
	}
	public ServerPart getPartA()
	{
		return partA;
	}
	public ServerPart getPartB()
	{
		return partB;
	}
	public ServerInfo getServerInfo()
	{
		return serverInfo;
	}
	public int getId()
	{
		return id;
	}
	public int getUsedCpu()
	{
		return usedCpu;
	}
	public int getUsedMemory()
	{
		return usedMemory;
	}
	public int getRemainCpu()
	{
		return remainCpu;
	}
	public int getRemainMemory()
	{
		return remainMemory;
	}
	//getter
	//setter
	public void setId(int id)
	{
		this.id=id;
	}
	//setter
	//如果虚拟机达到了服务器的三分之一，并且cpu/内存相差大，那么不把虚拟机放到此服务器上
	int addVM(VirtualMachine vm)
	{
		int mode=-1;//-1代表不能添加，0代表双节点部署，1代表单节点部署到a，2代表单节点到B
		int cpu=vm.getVmInfo().getCpuNumber();
		int memo=vm.getVmInfo().getMemory();
		//首先判断能够部署
		boolean isDoublePart=vm.getVmInfo().getIsDoublePart();
		if(isDoublePart)
		{
			//两个节点都能提供相应的资源
			if(partA.fitDeploy(cpu/2, memo/2)&&partB.fitDeploy(cpu/2, memo/2))
			{
				mode=0;
			}
		}
		else
		{
			if(partA.fitDeploy(cpu,memo))
			{
				mode=1;
				
			}
			else if(partB.fitDeploy(cpu,memo))
			{
				mode=2;
			}
		}
		if(mode==-1)
			return mode;
		else
		{
			usedCpu+=vm.getVmInfo().getCpuNumber();
			usedMemory+=vm.getVmInfo().getMemory();
			remainCpu-=vm.getVmInfo().getCpuNumber();
			remainMemory-=vm.getVmInfo().getMemory();
			if(mode==0)
			{
				//双节点部署
				int cpuNumbera=partA.getRemainCpuNumber();
				int memorya=partA.getRemainMemory();
				int cpuNumberb=partB.getRemainCpuNumber();
				int memoryb=partB.getRemainMemory();
				partA.setRemainCpuNumber(cpuNumbera-cpu/2);
				partA.setRemainMemory(memorya-memo/2);
				partB.setRemainMemory(memoryb-memo/2);
				partB.setRemainCpuNumber(cpuNumberb-cpu/2);
				partA.setRatio();
				partB.setRatio();
				return mode;
			}
			if(mode==1)
			{
				//部署到节点A
				int cpuNumber=partA.getRemainCpuNumber();
				int memory=partA.getRemainMemory();
				cpuNumber-=cpu;
				memory-=memo;
				partA.setRemainCpuNumber(cpuNumber);
				partA.setRemainMemory(memory);
				partA.setRatio();
				return mode;
			}
			if(mode==2)
			{
				int cpuNumber=partB.getRemainCpuNumber();
				int memory=partB.getRemainMemory();
				cpuNumber-=cpu;
				memory-=memo;
				partB.setRemainCpuNumber(cpuNumber);
				partB.setRemainMemory(memory);
				partB.setRatio();
				return mode;
			}
		}
		return mode;
	}
	public void insertVM(VirtualMachine vm)
	{
		vms.add(vm);
	}
	public boolean delVM(VirtualMachine vm)
	{
		if(vms.remove(vm))
		{
			//改变资源
			int cpuNumber=vm.getVmInfo().getCpuNumber();
			int memory=vm.getVmInfo().getMemory();
			usedCpu-=cpuNumber;
			usedMemory-=memory;
			remainCpu+=cpuNumber;
			remainMemory+=memory;
			if(vm.getVmInfo().getIsDoublePart())
			{
				//改变A节点的资源
				partA.setRemainCpuNumber(partA.getRemainCpuNumber()+cpuNumber/2);
				partA.setRemainMemory(partA.getRemainMemory()+memory/2);
				partA.setRatio();
				//改变B节点的资源
				partB.setRemainCpuNumber(partB.getRemainCpuNumber()+cpuNumber/2);
				partB.setRemainMemory(partB.getRemainMemory()+memory/2);
				partB.setRatio();
			}
			else
			{
				if(vm.getDeployMode()==1)
				{
					//改变A节点的资源
					partA.setRemainCpuNumber(partA.getRemainCpuNumber()+cpuNumber);
					partA.setRemainMemory(partA.getRemainMemory()+memory);
					partA.setRatio();
				}
				if(vm.getDeployMode()==2)
				{
					//改变节点B的资源
					partB.setRemainCpuNumber(partB.getRemainCpuNumber()+cpuNumber);
					partB.setRemainMemory(partB.getRemainMemory()+memory);
					partB.setRatio();
				}
			}
			return true;
		}
		return false;
	}
}
