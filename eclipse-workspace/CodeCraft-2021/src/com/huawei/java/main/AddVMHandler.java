package com.huawei.java.main;

public class AddVMHandler {
	public static void handleAdd(String[] request,int index)
	{
		String type=request[1].trim();
		String id=request[2].trim();
		for(VirtualMachineInfo vmInfo:Main.vmList)
		{
			if(vmInfo.getModelType().equals(type))
			{
				//创建虚拟机
				VirtualMachine vm=new VirtualMachine(vmInfo,Integer.valueOf(id));
				//在服务器上创建一台虚拟机,遍历服务器列表，
				for(Server info:Main.servers)
				{
					int mode=info.addVM(vm);
					if(mode!=-1)
					{
						vm.setDeployMode(mode);
						if(info.getId()!=-1)
						vm.setServerId(info.getId());
						info.insertVM(vm);
						if(!Main.deployMessages[index].hasSameServer(info))
						Main.deployMessages[index].addServer(info);
						return ;
					}
				}
				//未在已有的服务器列表找到合适的服务器，遍历服务器类型，找到合适的服务器
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
							server.insertVM(vm);
						}
						break;
					}
				}
				break;
			}
		}
	}
}
