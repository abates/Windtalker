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

/**
 * @author Andrew Bates
 * 
 *         The Codec interface defines the methods an implementor must create
 *         for the Windtalker to use before sending messages over the air.
 *         Messages can be encoded and decoded. As long as both the sender and
 *         receiver have implemented the encoding and decoding the same way, the
 *         messages should be understandable by both parties.
 * 
 */
public interface Codec {
    /**
     * The decode method will take a message that has been previously encoded
     * and convert it back into the original message
     * 
     * @param message
     *            encoded string
     * @return the decoded message
     */
    public String decode(String message);

    /**
     * The encode method will take a clear text message and encode it using the
     * secret algorithm. Once encoded the message can be translated and no one
     * in the middle should be able to understand the meaning of the message
     * 
     * @param message
     *            string to be encoded for sending
     * @return encoded message
     */
    public String encode(String message);
}
