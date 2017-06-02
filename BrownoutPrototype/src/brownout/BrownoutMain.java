package brownout;


import java.util.ArrayList;

public class BrownoutMain {

	public static void main(String args[]) {

    	CommandExecution ce = new CommandExecution();
    	ce.checkOsInformation();
    	

        
        //These two lines should be put together, to be refactored later
        String containersInfo = ce.getContainersCPU();                
        ArrayList<Container> containerList = ce.generateContainerList(containersInfo);
        
    	//These two lines should be put together, to be refactored later
        String workcernodeInfo = ce.getWorkerNodesCPU();
        ArrayList<WorkerNode> workerNodeList = ce.generateWorkNodeList(workcernodeInfo, containerList);

        
//        for(int i = 0; i < containerList.size(); i++){
//            System.out.println(containerList.get(i).getHostName() + " " +
//                           containerList.get(i).getContainerId() + " " +
//                                       containerList.get(i).getCpuUtil() + " " +
//                           containerList.get(i).getMemUtil());
//    }
//
//        for(int i = 0; i < workerNodeList.size(); i++){
//        	System.out.println(workerNodeList.get(i).getWorkerNodeName() + " " + 
//        			workerNodeList.get(i).getCpuUtil());
//
//        }
        
        System.out.println(String.format("%20s", "Worker Node Name") + " " +
                           String.format("%10s", "CPU %") + " " + 
		                   String.format("%20s", "Con Id") + " " +
                           String.format("%15s", "Con CPU %") + " " + 
		                   String.format("%20s", "Con MEM %"));
        
        for(WorkerNode wn : workerNodeList){
        	for(Container container: wn.getContainersList()){
        		System.out.println(String.format("%20s", wn.getWorkerNodeName()) + " " +
        	                       String.format("%10s", wn.getCpuUtil()) + "    " + 
        				           String.format("%20s",container.getContainerId()) + " " +
        	                       String.format("%15s", container.getCpuUtil()) + " " + 
        				           String.format("%15s",container.getMemUtil()));
        	}
        }
      
    }
}
