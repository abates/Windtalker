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
 *         Exception that indicates an input message is too long to be encoded
 */
public class MessageTooLongException extends Exception {
    private static final long serialVersionUID = -6578936811273134717L;

    /**
     * Initialize the exception to indicate the maximum size of a message
     * 
     * @param maxSize
     *            the maximum number of characters acceptable in a message
     */
    public MessageTooLongException(int maxSize) {
        super("Messages cannot be more than " + maxSize + " characters");
    }
}
