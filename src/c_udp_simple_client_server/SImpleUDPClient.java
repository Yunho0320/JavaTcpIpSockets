package c_udp_simple_client_server;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InterruptedIOException;

/*
Same argment setup as tcp
The only difference is that UDP is connectionless, meaning that unlike TCP, we don't need to be connected to a remote address (server) first.
We just send packets first and wait for replies

What's the diff between Socket(TCP) and DatagramSocket(UDP)
TCP needs connection (handshake) but UDP is connectionless
TCP data = streams of bytes, UDP data = individual packets (datagrams)
TCP uses Socket, ServerSocket, UDP uses DatagramSocket

 */
public class SImpleUDPClient {

    private static final int TIMEOUT = 3000;  // Resend timeout (milliseconds)
    private static final int MAXTRIES = 5;    // Retry up to 5 times

    public static void main(String[] args) throws IOException {

        if ((args.length < 2) || (args.length > 3)) {
            throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");
        }

        InetAddress serverAddress = InetAddress.getByName(args[0]); // hostname or IP of the server
        byte[] bytesToSend = args[1].getBytes();
        int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;


        DatagramSocket socket = new DatagramSocket(); //Unlike TCP Socket, it contains address and port
        socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)

        DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);
        DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

        int tries = 0;
        boolean receivedResponse = false;

        do {
            socket.send(sendPacket); // Send the echo string
            try {
                socket.receive(receivePacket);   // Attempt echo reply reception

                if (!receivePacket.getAddress().equals(serverAddress)) {
                    throw new IOException("Received packet from an unknown source");
                }

                receivedResponse = true;
            } catch (InterruptedIOException e) {
                tries += 1;
                System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
            }
        } while ((!receivedResponse) && (tries < MAXTRIES));

        if (receivedResponse) {
            System.out.println("Received: " + new String(receivePacket.getData()));
        } else {
            System.out.println("No response -- giving up.");
        }

        socket.close();
    }
}
