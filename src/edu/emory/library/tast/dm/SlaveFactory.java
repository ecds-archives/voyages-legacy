package edu.emory.library.tast.dm;

public class SlaveFactory extends AbstractDescriptiveObjectFactory
{

	public AbstractDescriptiveObject newInstance()
	{
		return new Slave();
	}

}
