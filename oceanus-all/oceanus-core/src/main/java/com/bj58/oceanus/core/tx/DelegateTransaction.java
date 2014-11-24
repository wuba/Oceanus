/*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.oceanus.core.tx;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedHashSet;
import java.util.Set;

import com.bj58.oceanus.core.tx.DelegateSavepoint.SavepointDesc;

/**
 * Transaction
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DelegateTransaction implements Transaction {
	Set<Transaction> transactions = new LinkedHashSet<Transaction>();
    boolean commited;
	public void addTransaction(Transaction tx){
		transactions.add(tx);
	}
	@Override
	public void commit() throws SQLException {
		
		for (Transaction tx : transactions) {
			tx.commit();
		}
		commited=true;
	}

	@Override
	public void rollback() throws SQLException {
		SQLException e = null;
		for (Transaction tx : transactions) {
			try {
				tx.rollback();
			} catch (SQLException ex) {
				e = ex;
				// TODO log
			}
		}
		if (e != null) {
			throw e;
		}

	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		if (savepoint instanceof DelegateSavepoint) {
			DelegateSavepoint delegate = (DelegateSavepoint) savepoint;
			Set<SavepointDesc> savePoints = delegate.savePoints;
			if (savePoints != null) {
				SQLException e = null;
				for (SavepointDesc savepointDesc : savePoints) {
					try {
						savepointDesc.rollback();
					} catch (SQLException ex) {
						e = ex;
						// TODO log
					}
				}
				if (e != null) {
					throw e;
				}
			}

		} else {
			SQLException e = null;
			for (Transaction tx : transactions) {
				try {
					tx.rollback(savepoint);
				} catch (SQLException ex) {
					e = ex;
					// TODO log
				}
			}
			if (e != null) {
				throw e;
			}
		}

	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		if (savepoint instanceof DelegateSavepoint) {
			DelegateSavepoint delegate = (DelegateSavepoint) savepoint;
			Set<SavepointDesc> savePoints = delegate.savePoints;
			if (savePoints != null) {
				SQLException e = null;
				for (SavepointDesc savepointDesc : savePoints) {
					try {
						savepointDesc.releaseSavepoint();
					} catch (SQLException ex) {
						e = ex;
						// TODO log
					}
				}
				if (e != null) {
					throw e;
				}
			}

		} else {
			SQLException e = null;
			for (Transaction tx : transactions) {
				try {
					tx.releaseSavepoint(savepoint);
				} catch (SQLException ex) {
					e = ex;
					// TODO log
				}
			}
			if (e != null) {
				throw e;
			}
		}

	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		DelegateSavepoint delegate = new DelegateSavepoint(name);
		for (Transaction tx : transactions) {
			SavepointWrapper savepoint = (SavepointWrapper) tx
					.setSavepoint(name);
			delegate.addSavepoint(savepoint);
		}
		return delegate;
	}
	@Override
	public boolean isCommited() { 
		return commited;
	}

}
