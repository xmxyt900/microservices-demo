import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * This class is used for executing shell commands on worker nodes from the 
 * master node.
 * @author minxianx
 *
 */
class CommandExecution {
	
    public static void main(String args[]) {

    	CommandExecution ce = new CommandExecution();
    	ce.checkOsInformation();
        ce.getWorkerNodesCPU();
        ce.getContainersCPU();
      
    }
    
    public CommandExecution(){
    	
    }
    
    /**
     * Get the CPU utilization of each worker node
     * @return
     */
    String getWorkerNodesCPU(){
    	String command = "sh getWorkerNodesCPU.sh";
    	System.out.println("Output Worker Nodes Utilization");
    	executeCommand(command);
    	return "sh getWorkerNodesCPU.sh";
    	
    }
    
    /**
     * Get Container utilization of all running containers
     * @return
     */
    String getContainersCPU(){
    	String command = "sh getContainersUtil.sh";
    	System.out.println("Output Containers Utilization");
    	executeCommand(command);
    	return "sh getContainersUtil.sh";
    }
    
    /**
     * Execute the shell commands and output the results (normal outputs and errors).   
     * @param command
     */
    void executeCommand(String command){

    	String input = null;
    	String error = null;
    	
    	try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));

            while ((input = stdInput.readLine()) != null) {
            	System.out.println(input);
            }

            while ((error = stdError.readLine()) != null) {
                System.out.println(error);
            }

			
		} catch (IOException e) {
            System.out.println("Excepcion: ");
            e.printStackTrace();
            System.exit(-1);
		}
    }
    
    /**
     * Check the operation system, the commands are only supported to run under Linux
     * OS now.
     */
    void checkOsInformation(){
    	String os = System.getProperty("os.name");
        
        if (os.equals("Linux"))
            //comando = "ifconfig";
           // comando = "ansible DockerCluster1 -m shell -a 'free -m'";
            System.out.println("Operation System is situable"); 
        else{
        	System.out.println("Your OS is not Linux, these commands are only supported by Linux OS!");
           // comando = "ipconfig";
        	System.exit(0);
        }
    }
    
}
