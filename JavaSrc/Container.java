/**
 * The Container class stores the data of each container, including container
 * id, container cpu utilization and memory utilization.
 * @author minxianx
 *
 */
public class Container {

	String containerId;
	double cpuUtil;
	double memUtil;
	Container(String p_containerId, double p_cpuUtil, double p_memUtil){
		this.containerId = p_containerId;
		this.cpuUtil = p_cpuUtil;
		this.memUtil = p_memUtil;
	}
	
	/**
	 * @return the containerId
	 */
	public String getContainerId() {
		return containerId;
	}
	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
	/**
	 * @return the cpuUtil
	 */
	public double getCpuUtil() {
		return cpuUtil;
	}
	/**
	 * @param cpuUtil the cpuUtil to set
	 */
	public void setCpuUtil(double cpuUtil) {
		this.cpuUtil = cpuUtil;
	}
	/**
	 * @return the memUtil
	 */
	public double getMemUtil() {
		return memUtil;
	}
	/**
	 * @param memUtil the memUtil to set
	 */
	public void setMemUtil(double memUtil) {
		this.memUtil = memUtil;
	}
}
