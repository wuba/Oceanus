package com.bj58.oceanus.core.interceptor.execute;

import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.exception.ExecuteException;

public interface ExecutorInterceptor {
	
	void intercept(BatchItem batchItem) throws ExecuteException;

}
