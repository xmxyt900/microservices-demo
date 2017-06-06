package brownout;

import java.util.ArrayList;

import model.Container;
import model.WorkerNode;
import policy.*;

public class BrownoutMain {

	public final static double OVERLOAD_THRESHOLD = 0.0;
	public static void main(String args[]) {

		CommandExecution ce = new CommandExecution();
		ce.checkOsInformation();

		// These two lines should be put together, to be refactored later
		String containersInfo = ce.getContainersCPU();
		ArrayList<Container> containerList = ce.generateContainerList(containersInfo);

		// These two lines should be put together, to be refactored later
		String workcernodeInfo = ce.getWorkerNodesCPU();
		ArrayList<WorkerNode> workerNodeList = ce.generateWorkNodeList(workcernodeInfo, containerList);

		System.out.println(String.format("%20s", "Worker Node Name") + " " + String.format("%10s", "CPU %") + " "
				+ String.format("%20s", "Con Id") + " " + String.format("%15s", "Con CPU %") + " "
				+ String.format("%20s", "Con MEM %"));

		for (WorkerNode wn : workerNodeList) {
			for (Container container : wn.getContainersList()) {
				System.out.println(
						String.format("%20s", wn.getWorkerNodeName()) + " " + String.format("%10s", wn.getCpuUtil())
								+ "    " + String.format("%20s", container.getContainerId()) + " "
								+ String.format("%15s", container.getCpuUtil()) + " "
								+ String.format("%15s", container.getMemUtil()));
			}
		}

		BrownoutController bc = new BrownoutController();
		double dimmerValue = bc.getDimmerValue(workerNodeList);
		
//		ArrayList<Container> deactivatedContainerList = (new FirstComponentPolicy(dimmerValue, workerNodeList)).getDeactivatedContainerList();
		ArrayList<Container> deactivatedContainerList = (new LowestUtilizationFirstPolicy(dimmerValue, workerNodeList)).getDeactivatedContainerList();

	
		ce.deactivateContatiners(deactivatedContainerList);

		for(Container container: deactivatedContainerList){
			String serviceId;
			serviceId = ce.getServiceByContainerName(container);
			ce.updateServices(serviceId);
		}
	}
}
