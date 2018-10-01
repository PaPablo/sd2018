import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;

public class Client
{

    public static void main(String[] args) throws IOException
    {
        String serverName;

        // Process command-line args
        if(args.length==1)
        {
            serverName = args[0];
        }
        else
        {
            printUsage();
            return;
        }

        // Create socket
        DatagramSocket socket = new DatagramSocket();

        // Get message from server using created socket
        // This is done to avoid creating a new socket every time
        NtpProbe probe = new NtpProbe(serverName, socket);

        for (int i = 0; i < 8; i++) {
            probe.sendMessage();

            System.out.println(String.format("\n\nPrueba %d", i+1));
            // Corrected, according to RFC2030 errata

            // Display response
            System.out.println("Round-trip delay: " +
                    new DecimalFormat("0.00").format(probe.rtt*1000) + " ms");

            System.out.println("Local clock offset: " +
                    new DecimalFormat("0.0000").format(probe.localClockOffset*1000) + " ms");
        }

        socket.close();
    }



    /**
     * Prints usage
     */
    static void printUsage()
    {
        System.out.println(
                "NtpClient - an NTP client for Java.\n" +
                "\n" +
                "This program connects to an NTP server and prints the response to the console.\n" +
                "\n" +
                "\n" +
                "Usage: java NtpClient server\n" +
                "\n" +
                "\n" +
                "This program is copyright (c) Adam Buckley 2004 and distributed under the terms\n" +
                "of the GNU General Public License.  This program is distributed in the hope\n" +
                "that it will be useful, but WITHOUT ANY WARRANTY; without even the implied\n" +
                "warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\n" +
                "General Public License available at http://www.gnu.org/licenses/gpl.html for\n" +
                "more details.");

    }
}
