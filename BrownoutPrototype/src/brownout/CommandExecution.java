package brownout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.Container;
import model.WorkerNode;

/**
 * This class is used for executing shell commands on worker nodes from the
 * master node.
 * 
 * @author minxianx
 *
 */
class CommandExecution {

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
		System.out.println("======Output Worker Nodes Utilization======");
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
		System.out.println("======Output Containers Utilization======");
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
	 * Execute the shell commands and output the simplified results (not put same info as
	 * previous).
	 * 
	 * @param command
	 */
	String executeCommandWithLessInfo(String command) {

		String input = null;
		String error = null;
		String tempInput = null;
		StringBuffer sb = new StringBuffer("");

		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			while ((input = stdInput.readLine()) != null) {
				if(!(input.equals(tempInput))){
				System.out.println(input);
				}
				
				// Append the line to string
				// "\n" is required to added, otherwise all the outputs will be
				// in a single line
				sb.append(input + "\n");
				tempInput = input;
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
	 * Generate the worker node information with split operations.
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

	/**
	 * Put all the containers on the same host into a single container list
	 * @param p_hostName
	 * @param p_containerList
	 * @return
	 */
	ArrayList<Container> getContainerListByHostName(String p_hostName, ArrayList<Container> p_containerList) {
		ArrayList<Container> containerListOnSameHost = new ArrayList<Container>();
		for (Container container : p_containerList) {
			if (container.getHostName().equals(p_hostName)) {
				containerListOnSameHost.add(container);
			}
		}

		return containerListOnSameHost;
	}

	/**
	 * Stop (deactivate) the containers in the deactivated container list
	 * @param p_deactivatedContainerList
	 * @return
	 */
	String deactivateContatiners(ArrayList<Container> p_deactivatedContainerList) {
		String commandResults = null;
		String command;
		System.out.println("======Stop Containers======");
		String args_hostName;
		String arges_containerId;

		for (Container container : p_deactivatedContainerList) {
			args_hostName = container.getHostName();
			arges_containerId = container.getContainerId();
			// This shell file requires two parameters
			command = "sh stopContainer.sh " + args_hostName + " " + arges_containerId;
			commandResults = executeCommand(command);

		}
		return commandResults;
	}

	/**
	 * Get service id of container id, its implementation is based on 
	 * docker inspect
	 * @param container
	 * @return
	 */
	String getServiceByContainerName(Container container){
		String args_hostName = container.getHostName();;
		String arges_containerId = container.getContainerId();
		System.out.println("======Output Service of Stopped containers======");
		String command = "sh getServiceIdByContianerID.sh " + args_hostName + " " + arges_containerId;
		String commandResults = executeCommand(command);
		
		//Example: "com.docker.swarm.service.id": "jyyuzs6p6u2e5c362jtp9m5at", 
		int beginIndex = commandResults.indexOf(':') + 3;   //Begin with 'j'
		int endIndex = commandResults.length() - 3;   //end with 't'
		String serviceId = commandResults.substring(beginIndex, endIndex); 
	    System.out.println(serviceId);
				
		return serviceId;
	}
	
	/**
	 * Restart the stopped containers (new container instances)
	 * @param serviceId
	 */
	void updateServices(String serviceId){
		System.out.println("======Update Services====");
		String command = "sh  updateService.sh " + serviceId;
		executeCommandWithLessInfo(command);
	}
	
}
