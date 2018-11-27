package examples.DisplayContainers;


import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SystemInfoCollector {

    public SystemInfoCollector() { }

    public String getCpuInfo() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        
        return String.format(
                "%s\n%s",
                "% de uso del CPU por la JVM actual: " + osBean.getProcessCpuLoad(),
                "% de uso del CPU en total: " + osBean.getSystemCpuLoad()
                );
    } 

    public String getIp() {
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            return String.format("IP: %s", ip);
        } catch (UnknownHostException | SocketException e) { 
            return "No se pudo obtener la IP";
        }
    }

    public String getHostname() {
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            return String.format("Hostname: %s", hostname);
        } catch (UnknownHostException e) {
            return "No se pudo obtener el hostname";
        }
    }

    public String getCurrentTime() {
        long time = System.currentTimeMillis();
        return String.format(
                "La hora es %d",
                time);
    }

    public String toString() {
        return String.format(
                "STATUS: %s\n%s\n%s\n%s",
                this.getCurrentTime(),
                this.getHostname(),
                this.getIp(),
                this.getCpuInfo());
    }
}
