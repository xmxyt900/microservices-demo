import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Comando {
    public static void main(String args[]) {

        String s = null;

        try {

            String so = System.getProperty("os.name");
            String comando;
            if (so.equals("Linux"))
                //comando = "ifconfig";
               // comando = "ansible DockerCluster1 -m shell -a 'free -m'";
                comando = "sh getWorkerNodesCPU.sh"; 
            else
                comando = "ipconfig";

            Process p = Runtime.getRuntime().exec(comando);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(
                    p.getErrorStream()));

            System.out.println("Command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            System.out
                    .println("Command:\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.exit(0);
        } catch (IOException e) {
            System.out.println("Excepcion: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
