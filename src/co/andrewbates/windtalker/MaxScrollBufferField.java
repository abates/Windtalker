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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MaxScrollBufferField extends JTextField implements FocusListener {
    private static final long serialVersionUID = -6302957643317105375L;
    int length;

    public MaxScrollBufferField(int defaultLength) {
        if (defaultLength < 1) {
            throw new IllegalArgumentException("default length must be a positive number");
        }
        setText("" + defaultLength);
        addFocusListener(this);
        length = defaultLength;
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent event) {
        try {
            length = Integer.parseInt(getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            requestFocus();
        }
    }

    public int getLength() {
        return length;
    }
}
