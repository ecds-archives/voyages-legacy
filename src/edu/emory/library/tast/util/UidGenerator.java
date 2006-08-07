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