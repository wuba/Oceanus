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

/* The original from which this derives bore the following: */

/*

   Derby - Class org.apache.derby.impl.sql.compile.CreateSequenceNode

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package com.bj58.sql.parser;

import com.bj58.sql.StandardException;
import com.bj58.sql.types.DataTypeDescriptor;
import com.bj58.sql.types.TypeId;

/**
 * A CreateSequenceNode is the root of a QueryTree that
 * represents a CREATE SEQUENCE statement.
 *
 * <p>
 * Note that this node represents *only* what was in the statement and
 * all unspecified properties will be null. A standard conforming
 * implementation would use {@code minValue}, {@code 1} and {@code false}
 * for missing {@code startValue}, {@code incrementBy} and {@code isCycle},
 * respectively.
 * </p>
 */
public class CreateSequenceNode extends DDLStatementNode
{
    private TableName sequenceName;
    private DataTypeDescriptor dataType;
    private Long startWith;
    private Long incrementBy;
    private Long maxValue;
    private Long minValue;
    private Boolean isCycle;
    private StorageFormatNode storageFormat;

    /**
     * Initializer for a CreateSequenceNode
     *
     * @param sequenceName The name of the new sequence
     * @param dataType Exact numeric type of the new sequence
     * @param startWith Starting value
     * @param incrementBy Increment amount
     * @param maxValue Largest value returned by the sequence generator
     * @param minValue Smallest value returned by the sequence generator
     * @param isCycle True if the generator should wrap around, false otherwise
     *
     * @throws StandardException on error
     */
    public void init (Object sequenceName,
                      Object dataType,
                      Object startWith,
                      Object incrementBy,
                      Object maxValue,
                      Object minValue,
                      Object isCycle,
                      Object storageFormat) 
            throws StandardException {

        this.sequenceName = (TableName)sequenceName;
        initAndCheck(this.sequenceName);
        this.dataType = (DataTypeDescriptor)dataType;
        this.startWith = (Long)startWith;
        this.incrementBy = (Long)incrementBy;
        this.maxValue = (Long)maxValue;
        this.minValue = (Long)minValue;
        this.isCycle = (Boolean)isCycle;
        this.storageFormat = (StorageFormatNode)storageFormat;
    }

    /**
     * Fill this node with a deep copy of the given node.
     */
    public void copyFrom(QueryTreeNode node) throws StandardException {
        super.copyFrom(node);

        CreateSequenceNode other = (CreateSequenceNode)node;
        this.sequenceName = (TableName)getNodeFactory().copyNode(other.sequenceName,
                                                                 getParserContext());
        this.dataType = other.dataType;
        this.startWith = other.startWith;
        this.incrementBy = other.incrementBy;
        this.maxValue = other.maxValue;
        this.minValue = other.minValue;
        this.isCycle = other.isCycle;
        this.storageFormat = (StorageFormatNode)getNodeFactory().copyNode(other.storageFormat,
                                                                          getParserContext());
    }

    /**
     * Convert this object to a String.  See comments in QueryTreeNode.java
     * for how this should be done for tree printing.
     *
     * @return This object as a String
     */
    public String toString() {
        return super.toString() +
            "sequenceName: " + sequenceName + "\n" +
            "dataType: " + dataType + "\n" +
            "startWith: " + startWith + "\n" +
            "incrementBy: " + incrementBy + "\n" +
            "maxValue: " + maxValue + "\n" +
            "minValue: " + minValue + "\n" +
            "isCycle: " + isCycle + "\n";
    }

    public void printSubNodes(int depth) {
        super.printSubNodes(depth);

        if (storageFormat != null) {
            printLabel(depth, "storageFormat: ");
            storageFormat.treePrint(depth + 1);
        }
    }

    void acceptChildren(Visitor v) throws StandardException {
        super.acceptChildren(v);

        if (storageFormat != null) {
            storageFormat = (StorageFormatNode)storageFormat.accept(v);
        }
    }

    public String statementToString() {
        return "CREATE SEQUENCE";
    }

    public final TableName getSequenceName() {
        return sequenceName;
    }

    public final DataTypeDescriptor getDataType() {
        return dataType;
    }

    public final Long getStartWith() {
        return startWith;
    }

    public final Long getMaxValue() {
        return maxValue;
    }

    public final Long getMinValue() {
        return minValue;
    }

    public final Long getIncrementBy() {
        return incrementBy;
    }

    public final Boolean isCycle() {
        return isCycle;
    }

    public StorageFormatNode getStorageFormat() {
        return storageFormat;
    }
}
