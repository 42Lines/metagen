package net.ftlines.metagen;

public class SomeBean
{
	@net.ftlines.metagen.annot.Property
	private Integer value;

	public Integer getValue()
	{
		return value;
	}

	public void setValue(Integer value)
	{
		this.value = value;
	}
	
	
}
