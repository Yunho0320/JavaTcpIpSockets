package c_udp_simple_client_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SimpleUDPServer {

    private static final int ECHOMAX = 255; // Maximum size of echo datagram

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter(s): <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        DatagramSocket socket = new DatagramSocket(servPort);
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

        while (true) { // Run forever, receiving and echoing datagrams
            socket.receive(packet); // Receive packet from client
            System.out.println("Handling client at " + packet.getAddress().getHostAddress()
                    + " on port " + packet.getPort());
            socket.send(packet); // Send the same packet back to client
            packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
            // Why reset length? Because when a pakcet is received, Java automatically sets the packets' length to the number of bytes actually received
        }
    }
}
