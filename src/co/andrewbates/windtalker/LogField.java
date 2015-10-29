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

public class LogField extends JScrollPane {
    private static final long serialVersionUID = 6678508751403261470L;
    private JTextArea textArea;
    private int maxLength;

    public LogField(String text, int columns, int maxLength) {
        textArea = new JTextArea(text, 0, columns);
        textArea.setLineWrap(false);
        textArea.setEditable(false);
        this.maxLength = maxLength;
        setViewportView(textArea);
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        truncate();
    }

    public void log(String message) {
        if (!message.endsWith("\n")) {
            message += "\n";
        }
        textArea.append(message);
        truncate();
        textArea.setRows(textArea.getLineCount());
        textArea.setCaretPosition(textArea.getDocument().getLength());
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
