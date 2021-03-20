package com.huawei.java.main;

import java.util.Iterator;

public class DelVMHandler {
	public static void delVM(int vmId,int index)
	{
		for(Server server:Main.servers)
		{
			Iterator<VirtualMachine> iterator=server.getVms().iterator();
			while(iterator.hasNext())
			{
				VirtualMachine vm=iterator.next();
				if(vm.getId()==vmId)
				{
					for(Integer id:Main.deployMessages[index].getAddInfo())
					{
						if(id==vmId)
						{
							Main.deployMessages[index].addVmsTheDayDel(vmId,vm.getDeployMode(),server);
						}
					}
					server.delVM(vm);
					return ;
				}
			}
		}
	}
}
