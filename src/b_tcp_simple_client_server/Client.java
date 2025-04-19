package b_tcp_simple_client_server;

import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
This code connects a TCP server, send a word, waits to receive the same word back.
It's an echo client
My arguments are: 127.0.0.1 hello 7
 */
public class Client {

    public static void main(String[] args) throws IOException {

        if ((args.length < 2) || (args.length > 3))
            throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");

        String server = args[0];       // Server name or IP address
        // Convert argument String to bytes using the default character encoding
        byte[] data = args[1].getBytes();
        int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;  // 7 is a default echo port

        // Create socket that is connected to server on specified port
        Socket socket = new Socket(server, servPort);
        System.out.println("Connected to server...sending echo string");

        InputStream in = socket.getInputStream(); // used to read data sent back from the server
        OutputStream out = socket.getOutputStream(); //used to send data to the server

        out.write(data); // Send the encoded string to the server

        // Receive the same string back from the server
        int totalBytesRcvd = 0; // Total bytes received so far
        int bytesRcvd;          // Bytes received in last read
        // While loop is used because data might arrive in chunks, not all at once
        while (totalBytesRcvd < data.length) {
            if ((bytesRcvd = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd)) == -1)
                throw new SocketException("Connection closed prematurely");
            totalBytesRcvd += bytesRcvd;
        } // data array is full

        System.out.println("Received: " + new String(data));

        socket.close(); // Close the socket and its streams
    }
}
