import java.util.ArrayList;

/**
 * The WorkerNode class stores the data of each Worker Node, including
 * worker node id and containers on the node.
 * @author minxianx
 *
 */
public class WorkerNode {

	String workerNodeName;
	ArrayList<Container> containersList;
	public WorkerNode(String p_workerNodename, ArrayList<Container> p_containersList){
	
		this.workerNodeName = p_workerNodename;
		this.containersList = p_containersList;
		
	}
	/**
	 * @return the workerNodeName
	 */
	public String getWorkerNodeName() {
		return workerNodeName;
	}
	/**
	 * @param workerNodeName the workerNodeName to set
	 */
	public void setWorkerNodeName(String workerNodeName) {
		this.workerNodeName = workerNodeName;
	}
	/**
	 * @return the containersList
	 */
	public ArrayList<Container> getContainersList() {
		return containersList;
	}
	/**
	 * @param containersList the containersList to set
	 */
	public void setContainersList(ArrayList<Container> containersList) {
		this.containersList = containersList;
	}
}
