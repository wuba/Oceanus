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
import com.bj58.sql.parser.JoinNode.JoinType;

import java.util.Properties;

public class AlterAddIndexNode extends TableElementNode
{
    ExistenceCheck existenceCheck;
    boolean unique;
    IndexColumnList indexColumnList;
    JoinType joinType ;
    Properties properties;
    StorageFormatNode storageFormat;
    
    @Override
    public void init(Object cond,
                     Object unique,
                     Object indexName,
                     Object indexColumnList,
                     Object joinType,
                     Object properties,
                     Object storage)
    {
        super.init(indexName, ElementType.AT_ADD_INDEX);
        
        this.existenceCheck = (ExistenceCheck)cond;
        this.unique = ((Boolean)unique).booleanValue();
        this.indexColumnList = (IndexColumnList) indexColumnList;
        this.joinType = (JoinType) joinType;
        this.properties = (Properties) properties;
        this.storageFormat = (StorageFormatNode) storage;
    }
    
    public String getIndexName()
    {
        return name;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void copyFrom(QueryTreeNode node) throws StandardException 
    {
        super.copyFrom(node);

        AlterAddIndexNode other = (AlterAddIndexNode)node;
        this.existenceCheck = other.existenceCheck;
        this.unique = other.unique;
        this.indexColumnList = other.indexColumnList;
        this.joinType = other.joinType;
        this.properties = other.properties;
        this.storageFormat = (StorageFormatNode)getNodeFactory().copyNode(other.storageFormat,
                                                                          getParserContext());
    }

    @Override
    public String toString()
    {
        return super.toString()
                + "\nexistenceCheck: " + existenceCheck
                + "\nunique: "+ unique
                + "\nindexColumnList: " + indexColumnList
                + "\njoinType: " + joinType
                + "\nproperties: " + properties;
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

    public String statementToString()
    {
        return "ALTER TABLE ADD INDEX";
    }

    public ExistenceCheck getExistenceCheck()
    {
        return existenceCheck;
    }
    
    public boolean isUnique()
    {
        return unique;
    }
    
    public IndexColumnList getIndexColunmList()
    {
        return indexColumnList;
    }
    
    public JoinType getJoinType()
    {
        return joinType;
    }
    
    public Properties getProperties()
    {
        return properties;
    }
    
    public StorageFormatNode getStorageFormat()
    {
        return storageFormat;
    }
}
