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
	private int createDay=-1;//添加的天数减一
	public ArrayList<VirtualMachine> vms=new ArrayList<>();
	public Server(ServerInfo info,ServerPartInfo partInfo)
	{
		this.serverInfo=info;
		this.partA=new ServerPart(partInfo);
		this.partB=new ServerPart(partInfo);
		remainCpu=info.getCpuNumber();
		remainMemory=info.getMemory();
	}
	public boolean isUnfit()
	{
		if(partA.isUnfit()||partB.isUnfit())
		{
			return true;
		}
		return false;
	}
	//getttor
	public int getType()
	{
		if(Main.dayNumber-createDay>=Main.dayNumber/10)
			return 1;//优化模式部署
		else return 2;//以前部署模式
	}
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
	public void setCreateDay(int day)
	{
		this.createDay=day;
	}
	//setter
	private boolean isWasteCpu(int remainCpuNumber,int remainMemory)
	{
		if(remainCpuNumber<=2&&remainMemory>=40)
			return true;
		return false;
	}
	private boolean isWasteMemory(int remainCpuNumber,int remainMemory)
	{
		if(remainMemory<=2&&remainCpuNumber>=40)
			return true;
		return false;
	}
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
			if(partA.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()/2&&partA.getRemainMemory()>=vm.getVmInfo().getMemory()/2&&partB.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()/2&&partB.getRemainMemory()>=vm.getVmInfo().getMemory()/2)
			{
				int laterCpuNumberA=partA.getRemainCpuNumber()-vm.getVmInfo().getCpuNumber()/2;
				int laterMemoryA=partA.getRemainMemory()-vm.getVmInfo().getMemory()/2;
				int laterCpuNumberB=partB.getRemainCpuNumber()-vm.getVmInfo().getCpuNumber()/2;
				int laterMemoryB=partB.getRemainMemory()-vm.getVmInfo().getMemory()/2;
				if(isWasteCpu(laterCpuNumberA,laterMemoryA)||isWasteMemory(laterCpuNumberA,laterMemoryA)||isWasteCpu(laterCpuNumberB,laterMemoryB)||isWasteMemory(laterCpuNumberB,laterMemoryB))
					mode=-1;
				else mode=0;
			}
		}
		else
		{
			if(partA.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()&&partA.getRemainMemory()>=vm.getVmInfo().getMemory())
			{
				int laterCpuNumberA=partA.getRemainCpuNumber()-vm.getVmInfo().getCpuNumber();
				int laterMemoryA=partA.getRemainMemory()-vm.getVmInfo().getMemory();
				if(isWasteCpu(laterCpuNumberA,laterMemoryA)||isWasteMemory(laterCpuNumberA,laterMemoryA))
					mode=-1;
				else mode=1;
				
			}
			else if(partB.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()&&partB.getRemainMemory()>=vm.getVmInfo().getMemory())
			{
				int laterCpuNumberB=partB.getRemainCpuNumber()-vm.getVmInfo().getCpuNumber();
				int laterMemoryB=partB.getRemainMemory()-vm.getVmInfo().getMemory();
				if(isWasteCpu(laterCpuNumberB,laterMemoryB)||isWasteMemory(laterCpuNumberB,laterMemoryB))
					mode=-1;
				else mode=2;
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
				partA.addVms(vm);
				partB.addVms(vm);
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
				partA.addVms(vm);
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
				partB.addVms(vm);
				return mode;
			}
		}
		return mode;
	}
	int addVM2(VirtualMachine vm)
	{
		int mode=-1;//-1代表不能添加，0代表双节点部署，1代表单节点部署到a，2代表单节点到B
		int cpu=vm.getVmInfo().getCpuNumber();
		int memo=vm.getVmInfo().getMemory();
		//首先判断能够部署
		boolean isDoublePart=vm.getVmInfo().getIsDoublePart();
		if(isDoublePart)
		{
			//两个节点都能提供相应的资源
			if(partA.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()/2&&partA.getRemainMemory()>=vm.getVmInfo().getMemory()/2&&partB.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()/2&&partB.getRemainMemory()>=vm.getVmInfo().getMemory()/2)
			{
				mode=0;
			}
		}
		else
		{
			if(partA.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()&&partA.getRemainMemory()>=vm.getVmInfo().getMemory())
			{
				mode=1;
				
			}
			else if(partB.getRemainCpuNumber()>=vm.getVmInfo().getCpuNumber()&&partB.getRemainMemory()>=vm.getVmInfo().getMemory())
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
				partA.addVms(vm);
				partB.addVms(vm);
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
				partA.addVms(vm);
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
				partB.addVms(vm);
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
				//改变B节点的资源
				partB.setRemainCpuNumber(partB.getRemainCpuNumber()+cpuNumber/2);
				partB.setRemainMemory(partB.getRemainMemory()+memory/2);
				partA.getVms().remove(vm);
				partB.getVms().remove(vm);
			}
			else
			{
				if(vm.getDeployMode()==1)
				{
					//改变A节点的资源
					partA.setRemainCpuNumber(partA.getRemainCpuNumber()+cpuNumber);
					partA.setRemainMemory(partA.getRemainMemory()+memory);
					partA.getVms().remove(vm);
				}
				if(vm.getDeployMode()==2)
				{
					//改变节点B的资源
					partB.setRemainCpuNumber(partB.getRemainCpuNumber()+cpuNumber);
					partB.setRemainMemory(partB.getRemainMemory()+memory);
					partB.getVms().remove(vm);
				}
			}
			return true;
	}
}
