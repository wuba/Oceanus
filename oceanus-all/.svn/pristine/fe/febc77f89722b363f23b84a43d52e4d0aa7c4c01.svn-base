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

/**
 * A SET statement for a non-standard configuration variable.
 */

public class SetConfigurationNode extends StatementNode
{
    private String variable, value;

    /**
     * Initializer for SetTransactionIsolationNode
     *
     * @param variable name of the variable
     * @param value the new value
     */
    public void init(Object variable,
                     Object value) {
        this.variable = (String)variable;
        this.value = (String)value;
    }

    /**
     * Fill this node with a deep copy of the given node.
     */
    public void copyFrom(QueryTreeNode node) throws StandardException {
        super.copyFrom(node);

        SetConfigurationNode other = (SetConfigurationNode)node;
        this.variable = other.variable;
        this.value = other.value;
    }

    /**
     * Convert this object to a String.  See comments in QueryTreeNode.java
     * for how this should be done for tree printing.
     *
     * @return This object as a String
     */

    public String toString() {
        return "variable: " + variable + "\n" +
               "value: " + value + "\n" +
            super.toString();
    }

    public String getVariable() {
        return variable;
    }

    public String getValue() {
        return value;
    }

    public String statementToString() {
        return "SET " + variable;
    }

}
