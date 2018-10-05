import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

public class NtpProbe {
    InetAddress address;
    DatagramSocket socket;

    double destinationTimestamp;
    double rtt;
    double localClockOffset;
    NtpMessage msg;

    public void setRTT() {
        // Immediately record the incoming timestamp
        this.rtt = (this.destinationTimestamp-this.msg.originateTimestamp) -
            (this.msg.transmitTimestamp-this.msg.receiveTimestamp);
    }

    public void setLocalClockOffset(){
        this.localClockOffset = ((this.msg.receiveTimestamp - this.msg.originateTimestamp) +
                (this.msg.transmitTimestamp - this.destinationTimestamp)) / 2;
    }


    /**
     * getMessage:
     * 
     * returns the result of an NTP message sent to serverName
     **/
    public void sendMessage() throws IOException{

        // Set addres to send
        byte[] buf = new NtpMessage().toByteArray();

        DatagramPacket packet =
            new DatagramPacket(buf, buf.length, this.address, 123);

        // Set the transmit timestamp *just* before sending the packet
        // ToDo: Does this actually improve performance or not?
        // I don't know cuz, you tell me. P.
        NtpMessage.encodeTimestamp(packet.getData(), 40,
                (System.currentTimeMillis()/1000.0) + 2208988800.0);

        this.socket.send(packet); 

        // Get response
        packet = new DatagramPacket(buf, buf.length);
        this.socket.receive(packet);

        // Process response
        this.msg = new NtpMessage(packet.getData());

        this.destinationTimestamp =
            (System.currentTimeMillis()/1000.0) + 2208988800.0;

        this.setRTT();
        this.setLocalClockOffset();

    }

    public NtpProbe(String serverName, DatagramSocket socket) throws UnknownHostException {
        this.address = InetAddress.getByName(serverName);
        this.socket = socket;
    }
}
