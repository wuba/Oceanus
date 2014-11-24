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

public class IndexConstraintDefinitionNode extends ConstraintDefinitionNode implements IndexDefinition
{
    private String indexName;
    private IndexColumnList indexColumnList;
    private JoinType joinType;
    private StorageFormatNode storageFormat;
    
    @Override
    public void init(Object tableName,
                     Object indexColumnList,
                     Object indexName,
                     Object joinType,
                     Object storage)
    {
        super.init(tableName,
                   ConstraintType.INDEX,
                   null, // column list? don't need. Use indexColumnList instead
                   null, // properties - none
                   null, // constrainText  - none
                   null, // conditionCheck  - none
                   StatementType.UNKNOWN, // behaviour? 
                   ConstraintType.INDEX);
        
        this.indexName = (String) indexName;
        this.indexColumnList = (IndexColumnList) indexColumnList;
        this.joinType = (JoinType) joinType;
        this.storageFormat = (StorageFormatNode) storage;
    }
    
    public String getIndexName()
    {
        return indexName;
    }
    
    public IndexColumnList getIndexColumnList()
    {
        return indexColumnList;
    }

    public JoinType getJoinType()
    {
        return joinType;
    }
    
    public StorageFormatNode getStorageFormat()
    {
        return storageFormat;
    }
    
    // This is used for the non-unique "INDEX" defintions only
    public boolean getUniqueness() 
    {
        return false;
    }
    
    public TableName getObjectName()
    {
        return constraintName;
    }
    
    @Override
    public void copyFrom(QueryTreeNode node) throws StandardException
    {
        super.copyFrom(node);
        
        IndexConstraintDefinitionNode other = (IndexConstraintDefinitionNode) node;
        this.indexName = other.indexName;
        this.indexColumnList = other.indexColumnList;
        this.joinType = other.joinType;
        this.storageFormat = (StorageFormatNode)getNodeFactory().copyNode(other.storageFormat,
                                                                          getParserContext());
    }
    
    @Override
    public String toString()
    {
        return super.toString()
                + "\nindexName: " + indexName
                + "\njoinType: " + joinType
                ;
    }

    @Override
    public void printSubNodes(int depth) {
        super.printSubNodes(depth);
        if (indexColumnList != null) {
            printLabel(depth, "indexColumnList: ");
            indexColumnList.treePrint(depth + 1);
        }
        if (storageFormat != null) {
            printLabel(depth, "storageFormat: ");
            storageFormat.treePrint(depth + 1);
        }
    }
    
}
