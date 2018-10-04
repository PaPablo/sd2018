import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Time;

public class Client
{

    public static void main(String[] args) throws IOException
    {
        String[] servers = {
            "pool.ntp.org",
            "asia.pool.ntp.org",
            "europe.pool.ntp.org",
            "north-america.pool.ntp.org",
            "oceania.pool.ntp.org",
            "south-america.pool.ntp.org",
        };


        // Create socket
        DatagramSocket socket = new DatagramSocket();

        System.out.println(
                String.format("| %26s | %23s | %23s | %10s |\n| %26s | %23s | %23s | %10s |\n| %26s | %23s | %23s | %10s |",
                    "-",
                    "-",
                    "-",
                    "-",
                    "Servidor",
                    "Hora Local",
                    "Hora Servidor",
                    "Offset",
                    "-",
                    "-",
                    "-",
                    "-"
                    ));
        for (String server : servers) {
            probe(socket, server);
            System.out.println(
                    String.format("| %26s | %23s | %23s | %10s |",
                        "-",
                        "-",
                        "-",
                        "-"
                        ));
        }

        socket.close();
    }

    public static void probe(DatagramSocket socket, String serverName) throws IOException{
        // Get message from server using created socket
        // This is done to avoid creating a new socket every time
        NtpProbe probe = new NtpProbe(serverName, socket);

        // Date formater
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");


        for (int i = 0; i < 8; i++) {
            probe.sendMessage();

            // Corrected, according to RFC2030 errata

            // Display response
            //System.out.println("Round-trip delay: " +
            //new DecimalFormat("0.00").format(probe.rtt*1000) + " ms");

            long millis = System.currentTimeMillis();
            long realTime = (long) (millis + probe.localClockOffset);

            String outString = String.format(
                    "| %26s | %s | %s | %10s |",
                    serverName,
                    format.format(new Date(millis)),
                    format.format(new Date(realTime)),
                    String.format("%s ms", new DecimalFormat("0.000").format(probe.localClockOffset))
                    );
            System.out.println(outString);

        }

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
