package net.ftlines.metagen.processor.model;

public enum Visibility
{
	PUBLIC(40, "public"), PROTECTED(30, "protected"), DEFAULT(20, ""), PRIVATE(10, "private");

	private final int rank;
	private final String keyword;

	private Visibility(int rank, String keyword)
	{
		this.rank = rank;
		this.keyword = keyword;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public static Visibility max(Visibility a, Visibility b)
	{
		return (a.rank > b.rank) ? a : b;
	}
}
