/*
 * LICENSE
 *
 * Copyright 2015 Andrew Bates Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.andrewbates.windtalker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author Andrew Bates
 * 
 *         The Transciever class is what actually sends and receives messages on
 *         the network.
 */
public class Transceiver implements Runnable {
    private DatagramSocket socket;
    private InetAddress localAddress;
    private InetAddress broadcast;
    private Codec codec;
    private Receiver receiver;
    private int maxMessageSize = 1024;

    /**
     * Initialize a new transceiver
     * 
     * @param codec
     *            Codec to use for encoding and decoding messages
     * @param receiver
     *            The receiver that will display received messages
     * @throws SocketException
     *             thrown if any problem occurs while attempting to start the
     *             network connection
     */
    public Transceiver(Codec codec, Receiver receiver) throws SocketException {
        setupNetwork();
        this.codec = codec;
        this.receiver = receiver;
        new Thread(this).start();
    }

    /**
     * Find the local broadcast address and start listening on that interface
     * 
     * @throws SocketException
     *             thrown if anything goes wrong when trying to listen on the
     *             local interface
     */
    private void setupNetwork() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");

        try {
            this.socket = new DatagramSocket(5001, InetAddress.getByName("0.0.0.0"));
        } catch (UnknownHostException ex) {
            throw new RuntimeException("Failed to resolve address 0.0.0.0: " + ex.getMessage());
        }

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback())
                continue; // Don't want to broadcast to the loopback interface
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null)
                    continue;

                // Use the address
                this.broadcast = broadcast;
                this.localAddress = interfaceAddress.getAddress();
                return;
            }
        }
        throw new RuntimeException("Could not find an available broadcast address");
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        byte[] payload = new byte[maxMessageSize];
        DatagramPacket p = new DatagramPacket(payload, payload.length);
        while (true) {
            try {
                socket.receive(p);
                String message = codec.decode(new String(payload, 0, p.getLength()));
                receiver.receive(message, p.getAddress().equals(localAddress));
                System.out.println("RX: " + new String(payload, 0, p.getLength()));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    /**
     * Send a message to the network
     * 
     * @param message
     *            The message to send
     * @throws MessageTooLongException
     *             thrown if the message exceeds the maximum length we can
     *             actually send on the network
     */
    public void send(String message) throws MessageTooLongException {
        if (message.length() > maxMessageSize) {
            throw new MessageTooLongException(maxMessageSize);
        }
        byte[] payload = codec.encode(message).getBytes();
        DatagramPacket packet = new DatagramPacket(payload, payload.length, broadcast, socket.getLocalPort());
        System.out.println("TX: " + new String(payload));
        try {
            socket.send(packet);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
