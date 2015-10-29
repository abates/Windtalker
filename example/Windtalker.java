
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

import javax.swing.JOptionPane;

import co.andrewbates.windtalker.Codec;
import co.andrewbates.windtalker.Talk;

class ExampleCodec implements Codec {
    private char min = 31;
    private int max = 127;

    private String rot47(String message) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            int c = message.charAt(i) + 48;
            if (c > max) {
                c = min + (c - max);
            }
            s.append((char) c);
        }
        return s.toString();
    }

    @Override
    public String encode(String message) {
        return rot47(message);
    }

    @Override
    public String decode(String message) {
        return rot47(message);
    }

}

public class Windtalker {
    public static void main(String[] argv) {
        Codec codec = new ExampleCodec();
        String username = JOptionPane.showInputDialog("Input a username");
        Talk talk = new Talk(username, codec);

        talk.run();
    }
}
