/**
 * Copyright 2011-2013 FoundationDB, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bj58.sql.parser;

import com.bj58.sql.StandardException;

import java.util.Map;
import java.util.TreeMap;

/**
 * This node represents a description of how a table or index is stored (if at all).
 */
public class StorageFormatNode extends QueryTreeNode
{
    private String format;
    private Map<String,String> options = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
        
    /**
     * Initialize a StorageFormatNode
     *
     * @param format (a String)
     */
    public void init(Object format) throws StandardException {
        this.format = (String)format;
    }

    public String getFormat() {
        return format;
    }

    public Map<String,String> getOptions() {
        return options;
    }

    public void addOption(String key, String value) throws StandardException {
        if (options.containsKey(key))
            throw new StandardException("Option " + key + " specified more than once.");
        options.put(key, value);
    }

    /**
     * Fill this node with a deep copy of the given node.
     */
    public void copyFrom(QueryTreeNode node) throws StandardException {
        super.copyFrom(node);

        StorageFormatNode other = (StorageFormatNode)node;
        this.format = other.format;
        options.putAll(other.options);
    }

    /**
     * Convert this object to a String.  See comments in QueryTreeNode.java
     * for how this should be done for tree printing.
     *
     * @return This object as a String
     */

    public String toString() {
        StringBuilder str = new StringBuilder("format: ");
        str.append(format).append("\n");
        for (Map.Entry<String,String> entry : options.entrySet()) {
            str.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        str.append(super.toString());
        return str.toString();
    }

}
