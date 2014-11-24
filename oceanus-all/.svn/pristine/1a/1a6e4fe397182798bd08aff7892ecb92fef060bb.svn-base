package com.bj58.oceanus.exchange.ordered;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ComparisonChain;

public class OrderByTest {
    @Test
	public void testOrderByColumns(){
		List<Person> list=new ArrayList<Person>();
		Person person=new Person();
		list.add(person);
		person.name="luolishu";
		person.age=119;
		person.tall=1;
		person=new Person();
		person.name="luolishu";
		person.age=21;
		person.tall=1111;
		list.add(person);
		person=new Person();
		person.name="luolishu3";
		person.age=19;
		person.tall=10;
		list.add(person);
		person=new Person();
		person.name="luolishu2";
		person.age=21;
		person.tall=1;
		list.add(person);
		person=new Person();
		person.name="luolishu5";
		person.age=31;
		person.tall=1;
		list.add(person);
		for(Person p:list){
			System.out.println(p);
		}
		Collections.sort(list);
		System.out.println("=====================================");
		for(Person p:list){
			System.out.println(p);
		}
	}
    @Test
	public void testNullComparable(){
        int result=ComparisonChain.start().compare("hello", null).result();
        System.out.println(result);
    }
	
	
	class Person implements Comparable<Person>{
		String name;
		int age;
		int tall;
		Date date;
		@Override
		public int compareTo(Person o) {
			int result=ComparisonChain.start().compare(name, o.name).compare(o.age, age).compare(tall, o.tall).result();
			return result;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + ", tall=" + tall
					+ ", date=" + date + "]";
		}
		 
		
		
	}
}
