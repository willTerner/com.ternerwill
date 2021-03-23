package com.huawei.java.main;

public class AddVMHandler {
	public static void handleAdd(String[] request,int index)
	{
		String type=request[1].trim();
		String id=request[2].trim();
//		int matchMode=0;//1代表前些天的匹配，2代表后面的匹配
//		if(Main.dayNumber-index>=Main.dayNumber/10)
//			matchMode=1;
//		else 
//			matchMode=2;
		for(VirtualMachineInfo vmInfo:Main.vmList)
		{
			if(vmInfo.getModelType().equals(type))
			{
				//创建虚拟机
				VirtualMachine vm=new VirtualMachine(vmInfo,Integer.valueOf(id));
				//在服务器上创建一台虚拟机,遍历服务器列表，
				for(Server info:Main.servers)
				{
					int mode=-2;
//					if(info.getType()==1)
						mode=info.addVM(vm);
//					else 
//						mode=info.addVM2(vm);
					if(mode!=-1)
					{
						vm.setDeployMode(mode);
						if(info.getId()!=-1)
						vm.setServerId(info.getId());
						vm.setServer(info);
						info.insertVM(vm);
						if(!Main.deployMessages[index].hasSameServer(info))
						Main.deployMessages[index].addServer(info);
						Main.deployMessages[index].addVms(vm);
						return ;
					}
				}
				//未在已有的服务器列表找到合适的服务器，遍历服务器类型，找到合适的服务器
//				if(matchMode==1)
//				{
					for(ServerInfo serverInfo:Main.serverList)
					{
							if(serverInfo.couldDeploy(vmInfo))
							{
								Server server=new Server(serverInfo,Main.serverPartInfoList.get(Main.serverList.indexOf(serverInfo)));
								Main.servers.add(server);
								int mode=server.addVM(vm);
								Main.deployMessages[index].addServer(server);
								Main.deployMessages[index].buyServer(serverInfo.getModleType());
								if(mode!=-1)
								{
									vm.setDeployMode(mode);
									vm.setServer(server);
									server.insertVM(vm);
									server.setCreateDay(index);
									Main.deployMessages[index].addVms(vm);
									Main.deployMessages[index].addCreateServers(server);
									return ;
								}
							}
					}
					break;
//				}
//				else
//				{
//					for(ServerInfo serverInfo:Main.serverList)
//					{
//							if(serverInfo.couldDeploy2(vmInfo))
//							{
//								Server server=new Server(serverInfo,Main.serverPartInfoList.get(Main.serverList.indexOf(serverInfo)));
//								Main.servers.add(server);
//								int mode=server.addVM2(vm);
//								Main.deployMessages[index].addServer(server);
//								Main.deployMessages[index].buyServer(serverInfo.getModleType());
//								if(mode!=-1)
//								{
//									vm.setDeployMode(mode);
//									vm.setServer(server);
//									server.insertVM(vm);
//									server.setCreateDay(index);
//									Main.deployMessages[index].addVms(vm);
//									Main.deployMessages[index].addCreateServers(server);
//									return ;
//								}
//							}
//					}
//					break;
//				}
			}
		}
	}
}
