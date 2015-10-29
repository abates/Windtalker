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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.SocketException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Talk extends JFrame implements Receiver, ActionListener {
    private static final long serialVersionUID = 5709807099045135313L;
    private String username;
    private Codec codec;
    private Transceiver transceiver;
    private JTextField inputMessage = new JTextField("", 50);
    private JButton send = new JButton("Send");
    private LogField history = new LogField("", 50, 10);
    private JCheckBox ignoreMyself = new JCheckBox();
    private MaxScrollBufferField bufferLength = new MaxScrollBufferField(10);

    public Talk(String username, Codec codec) {
        super("Windtalker");
        if (username.length() > 64) {
            this.username = username.substring(0, 64);
        } else {
            this.username = username;
        }
        setPreferredSize(new Dimension(800, 400));
        setLocation(200, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.codec = codec;

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        mainPanel.setBorder(new EmptyBorder(0, 5, 0, 5));

        /*
         * Create the panel with the options fields
         */
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 4));
        optionsPanel.add(new JLabel("Ignore Sent Messages:"));
        optionsPanel.add(ignoreMyself);
        optionsPanel.add(new JLabel("History Buffer Length:"));
        optionsPanel.add(bufferLength);
        bufferLength.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                history.setMaxLength(bufferLength.getLength());
            }
        });
        mainPanel.add(optionsPanel, BorderLayout.NORTH);

        /*
         * This is the text area where the message history appears
         */
        mainPanel.add(history);

        /*
         * Create the panel that has the input message text field and send
         * button
         */
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.X_AXIS));
        sendPanel.add(inputMessage);
        inputMessage.addActionListener(this);

        sendPanel.add(send);
        send.addActionListener(this);
        mainPanel.add(sendPanel, BorderLayout.SOUTH);
    }

    public void run() {
        try {
            transceiver = new Transceiver(codec, this);
        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(null, "Failed to start network stack: " + ex.getMessage(), "Error",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        pack();
        setVisible(true);
    }

    @Override
    public void receive(String message, boolean self) {
        if (!(self && ignoreMyself.isSelected())) {
            history.log(message);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            /* do nothing if the input field is blank */
            if (!inputMessage.getText().matches("^\\s*$")) {
                transceiver.send(username + ": " + inputMessage.getText());
            }
        } catch (MessageTooLongException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        inputMessage.setText("");
    }

}
