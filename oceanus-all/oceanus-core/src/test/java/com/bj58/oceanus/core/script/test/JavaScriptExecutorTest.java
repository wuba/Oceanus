package com.bj58.oceanus.core.script.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import com.bj58.oceanus.core.script.CompiledJavaScriptExecutor;
import com.bj58.oceanus.core.script.ScriptExecutor;
import com.bj58.oceanus.core.shard.Script;
import com.bj58.oceanus.core.shard.ScriptFunction;
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JavaScriptExecutorTest { 
	
	@Test
	public void testScript() throws ScriptException, IOException {
		String script="function count(){counter=counter+1;return counter;}; count();";
		ScriptExecutor<Double> scriptExecutor=new CompiledJavaScriptExecutor(script);
		Map map=new HashMap();
		map.put("counter", 0);
		Double result=scriptExecutor.execute(null,map);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, (double)result,0);
	}
	@Test
	public void testMod() throws ScriptException, IOException {
		String script="mod($USER_ID);";
		ScriptExecutor<Double> scriptExecutor=new CompiledJavaScriptExecutor(script);
		Map map=new HashMap();
		map.put("counter", 0);
		map.put("$USER_ID",1111);
		map.put("$NODE_SIZE",5);
		
		Double result=scriptExecutor.execute(null,map);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, (double)result,0);
		System.out.println("result="+result);
	}
	@Test
	public void testScriptFunction(){
		Script script=new Script();
		script.setContent("return mod($age)");
		ScriptFunction function=new ScriptFunction(script);
		int size=256;
		Map parameters=new HashMap();
		parameters.put("age", 257);
		int result=function.execute(size, parameters);
		Assert.assertEquals(1, result);
	}
	@Test
	public void testScriptFunction2(){
		Script script=new Script();
		script.setContent("return mod($age)");
		ScriptFunction function=new ScriptFunction(script);
		int size=256;
		Map parameters=new HashMap();
		parameters.put("age", 1);
		int result=function.execute(size, parameters);
		Assert.assertEquals(1, result);
	}
	@Test
	public void testScriptFunction3(){
		Script script=new Script();
		script.setExecute("mod($age)");
		ScriptFunction function=new ScriptFunction(script);
		int size=256;
		Map parameters=new HashMap();
		parameters.put("age", 257);
		int result=function.execute(size, parameters);
		Assert.assertEquals(1, result);
	}
}
