/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.dm;


public class MethodAppendix
{
	
	private String group;
	private double ave_em;
	private int number_em;
	private double sd_em;
	private double ave_disem;
	private int number_disem;
	private double sd_disem;	
	
	public MethodAppendix(){
	}
	
	public MethodAppendix(String name,double ave1,int num1,double sd1,double ave2, int num2, double sd2)
	{
		this.group=name;
		this.ave_em=ave1;
		this.number_em=num1;
		this.sd_em=sd1;
		this.ave_disem=ave2;
		this.number_disem=num2;
		this.sd_disem=sd2;	
	}
	
	
	public String getGroup()
	{
		return group;
	}
	
	public void setGroup(String name)
	{
		this.group=name;
	}

	public double getAve_em()
	{
		return ave_em;
	}

	public void setAve_em(double data)
	{
		this.ave_em = data;
	}

	public int getNumber_em()
	{
		return number_em;
	}

	public void setNumber_em(int data)
	{
		this.number_em = data;
	}

	public double getSd_em()
	{
		return sd_em;
	}

	public void setSd_em(double data)
	{
		this.sd_em = data;
	}
	
	public double getAve_disem()
	{
		return ave_disem;
	}

	public void setAve_disem(double data)
	{
		this.ave_disem = data;
	}
	
	public int getNumber_disem()
	{
		return number_disem;
	}

	public void setNumber_disem(int data)
	{
		this.number_disem = data;
	}
	public double getSd_disem()
	{
		return sd_disem;
	}

	public void setSd_disem(double data)
	{
		this.sd_disem = data;
	}

}
