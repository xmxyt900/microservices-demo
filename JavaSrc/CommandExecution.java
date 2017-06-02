import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class is used for executing shell commands on worker nodes from the
 * master node.
 * 
 * @author minxianx
 *
 */
class CommandExecution {

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
                           String.format("%20s", "Con CPU %") + " " + 
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

	public CommandExecution() {

	}

	/**
	 * Get the CPU utilization of each worker node
	 * 
	 * @return
	 */
	String getWorkerNodesCPU() {
		String commandResults;
		String command = "sh getWorkerNodesCPU.sh";
		System.out.println("Output Worker Nodes Utilization:");
		commandResults = executeCommand(command);
		return commandResults;

	}

	/**
	 * Get Container utilization of all running containers
	 * 
	 * @return
	 */
	String getContainersCPU() {
		String commandResults;
		String command = "sh getContainersUtil.sh";
		System.out.println("Output Containers Utilization:");
		commandResults = executeCommand(command);
		return commandResults;
	}

	/**
	 * Execute the shell commands and output the results (normal outputs and
	 * errors).
	 * 
	 * @param command
	 */
	String executeCommand(String command) {

		String input = null;
		String error = null;
		StringBuffer sb = new StringBuffer("");

		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			while ((input = stdInput.readLine()) != null) {
				System.out.println(input);
				// Append the line to string
				// "\n" is required to added, otherwise all the outputs will be
				// in a single line
				sb.append(input + "\n");
			}

			while ((error = stdError.readLine()) != null) {
				System.out.println(error);
			}

		} catch (IOException e) {
			System.out.println("Excepcion: ");
			e.printStackTrace();
			System.exit(-1);
		}

		return sb.toString();
	}

	/**
	 * Check the operation system, the commands are only supported to run under
	 * Linux OS now.
	 */
	void checkOsInformation() {
		String os = System.getProperty("os.name");

		if (os.equals("Linux"))
			// comando = "ifconfig";
			// comando = "ansible DockerCluster1 -m shell -a 'free -m'";
			System.out.println("Operation System is situable");
		else {
			System.out.println("Your OS is not Linux, these commands are only supported by Linux OS!");
			// comando = "ipconfig";
			System.exit(0);
		}
	}

	/**
	 * Generate the containers with split operations from string
	 * 
	 * @param containersInfo
	 * @return
	 */
	ArrayList<Container> generateContainerList(String containersInfo) {
		ArrayList<Container> cl = new ArrayList<Container>();
		String[] stringLines = containersInfo.split("\\r?\\n");
		String hostName = null;
		String[] containerLine;

		String containerId;
		double cpuUtil;
		double memUtil;

		for (String singleStringLine : stringLines) {
			// Example: DockerCluster1 | SUCCESS | rc=0 >>
			if (singleStringLine.contains("SUCCESS")) {
				containerLine = singleStringLine.split(" \\| ");
				hostName = containerLine[0];
			}
			// Example: CONTAINER CPU % MEM USAGE / LIMIT MEM % NET I/O BLOCK
			// I/O PIDS
			// Example: 04474571a642 0.00% 55.21 MiB / 3.36 GiB 1.60% 4.22 kB /
			// 3.34 kB 0 B / 0 B 21
			if (singleStringLine.contains("%") && !singleStringLine.contains("CPU")) {
				containerLine = singleStringLine.split("\\s+ ");
				containerId = containerLine[0];
				cpuUtil = Double.parseDouble(containerLine[1].substring(0, containerLine[1].length() - 1));

				memUtil = Double.parseDouble(containerLine[3].substring(0, containerLine[1].length() - 1));

				Container container = new Container(hostName, containerId, cpuUtil, memUtil);
				cl.add(container);
			}
		}
		return cl;

	}

	/**
	 * To be finished
	 * 
	 * @param workcernodeInfo
	 * @return
	 */
	ArrayList<WorkerNode> generateWorkNodeList(String workcernodeInfo, ArrayList<Container> p_containerList) {
		ArrayList<WorkerNode> wnl = new ArrayList<WorkerNode>();

		String[] stringLines = workcernodeInfo.split("\\r?\\n");
		String hostName = null;
		String[] containerLine;
		double cpuUtil;
		ArrayList<Container> containerListOnSameHost = new ArrayList<Container>();

		for (String singleStringLine : stringLines) {
			// Example: DockerCluster1 | SUCCESS | rc=0 >>
			if (singleStringLine.contains("SUCCESS")) {
				containerLine = singleStringLine.split(" \\| ");
				hostName = containerLine[0];
			}
			// Example: 1.0%
			if (singleStringLine.contains("%")) {
				cpuUtil = Double.parseDouble(singleStringLine.substring(0, singleStringLine.length() - 1));
				containerListOnSameHost = getContainerListByHostName(hostName, p_containerList);
				WorkerNode worknode = new WorkerNode(hostName, cpuUtil, containerListOnSameHost);
				wnl.add(worknode);
			}
		}
		return wnl;

	}

	ArrayList<Container> getContainerListByHostName(String p_hostName, ArrayList<Container> p_containerList) {
		ArrayList<Container> containerListOnSameHost = new ArrayList<Container>();
		for (Container container : p_containerList) {
			if (container.getHostName().equals(p_hostName)) {
				containerListOnSameHost.add(container);
			}
		}

		return containerListOnSameHost;
	}

}
