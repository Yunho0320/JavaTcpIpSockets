package b_tcp_simple_client_server;

import java.net.*;   // for Socket, ServerSocket, and InetAddress
import java.io.*;    // for IOException and Input/OutputStream

/*
It's an echo server
Only one argument, port number, 7
 */
public class Server {

    private static final int BUFSIZE = 32;   // Size of receive buffer

    public static void main(String[] args) throws IOException {

        if (args.length != 1)   // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Port>");

        int servPort = Integer.parseInt(args[0]);

        // Create a server socket to accept client connection requests
        // ServerSocket listens for incoming connections
        ServerSocket servSock = new ServerSocket(servPort);

        int recvMsgSize;   // Size of received message
        byte[] receiveBuf = new byte[BUFSIZE]; // Receive buffer

        while (true) { // Run forever, accepting and servicing connections
            /*This is very important
            The client created a socket object and sent a connection request to the port.
            This connection request is detected by ServerSocket object.
            Then once connection is approved/accepted, server gets the matching Socket, and now client and server is connected
             */
            Socket clntSock = servSock.accept();

            SocketAddress clientAddress = clntSock.getRemoteSocketAddress(); //This gives us the IP address and port number of the client that is connected to the server
            System.out.println("Handling client at " + clientAddress);

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            // Receive until client closes connection, indicated by -1 return
            while ((recvMsgSize = in.read(receiveBuf)) != -1) {
                out.write(receiveBuf, 0, recvMsgSize);
            }

            clntSock.close();   // Close the socket. We are done with this client!
        }
    }
}
