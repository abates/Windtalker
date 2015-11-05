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

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * @author Andrew Bates
 * 
 *         LogField is a Swing widget that provides a scrolling pane for log
 *         messages. The pain will automatically add new messages to the bottom
 *         of the pane as well as automatically scroll to the bottom as new
 *         messages are added.
 */
public class LogField extends JScrollPane {
    private static final long serialVersionUID = 6678508751403261470L;
    private JTextArea textArea;
    private int maxLength;

    /**
     * Initializes a new log field setting the initial text as well as the width
     * in columns and the length in lines
     * 
     * @param text
     *            default text to display
     * @param width
     *            of the field in text columns
     * @param maxLength
     *            length of the field in text lines
     */
    public LogField(String text, int columns, int maxLength) {
        textArea = new JTextArea(text, 0, columns);
        textArea.setLineWrap(false);
        textArea.setEditable(false);
        this.maxLength = maxLength;
        setViewportView(textArea);
    }

    /**
     * Append a message to the field
     * 
     * @param message
     *            the message to append to the log field
     */
    public void log(String message) {
        if (!message.endsWith("\n")) {
            message += "\n";
        }
        textArea.append(message);
        truncate();
        textArea.setRows(textArea.getLineCount());
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Set the maximum length of the scroll back buffer
     * 
     * @param maxLength
     *            the length (in lines) of the scroll buffer
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        truncate();
    }

    private void truncate() {
        if (textArea.getLineCount() <= maxLength) {
            return;
        }
        try {
            textArea.replaceRange(null, 0, textArea.getLineStartOffset(textArea.getLineCount() - maxLength));
        } catch (BadLocationException ex) {
            /*
             * We should *NEVER* get here, so we're going to raise a runtime
             * exception if we do
             */
            throw new RuntimeException(ex);
        }
    }
}
