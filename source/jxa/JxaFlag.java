/*
 * jxa - Java executable arguments parser
 * Jul 4, 2025
 * Defines a flag and its components
 */

package jxa;

public final class JxaFlag
{
	public static enum FlagArg
	{
		YES,
		NON,
		MAY
	};
	
	private final String   longName;
	private final char     shortName;
	private final FlagArg needs;
	
	public JxaFlag (String longName, char shortName, FlagArg needs)
	{
		this.longName  = longName;
		this.needs     = needs;
		this.shortName = shortName;
	}
	
	public String getLongName () { return this.longName; }
	public char getShortName ()  { return this.shortName; }
	public FlagArg getNeeds ()   { return this.needs; }
}
