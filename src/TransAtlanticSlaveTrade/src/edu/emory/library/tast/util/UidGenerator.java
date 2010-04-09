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
package edu.emory.library.tast.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class UidGenerator
{
	
	private static Random rnd = new Random(System.currentTimeMillis());
	
	private MessageDigest getMD5Generator()
	{
		try
		{
			return MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private synchronized void generateRandomBytes(byte[] bytes)
	{
		rnd.nextBytes(bytes);
	}
	
	public String generate()
	{
		
		byte[] randomBytes = new byte[16];
		generateRandomBytes(randomBytes);
		
		MessageDigest md5 = getMD5Generator();
		md5.update(randomBytes);
		byte[] idAsBytes = md5.digest();
		
        StringBuffer idAsString = new StringBuffer();
        for (int j = 0; j < idAsBytes.length; j++)
        {
            int b = idAsBytes[j] & 0xFF;
            if (b < 0x10) idAsString.append('0');
            idAsString.append(Integer.toHexString(b));
        }
		
		return idAsString.toString().toUpperCase();
		
	}
	
	public static void main(String[] args)
	{
		UidGenerator gen = new UidGenerator();
		System.out.println(gen.generate());
		System.out.println(gen.generate());
		System.out.println(gen.generate());
		System.out.println(gen.generate());
		System.out.println(gen.generate());
		System.out.println(gen.generate());
		System.out.println(gen.generate());
	}

}