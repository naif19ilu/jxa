/*
 * jxa - Java executable arguments parser
 * Jul 5, 2025
 * Self documented programs :)
 */

package jxa;

public class JxaDoc {
	
	private static int longestLongName = 0;
	private static int longestFlagDesc = 0;
	
	private static final String programsDesc = "here your description";
	
	public static void printUsage (String programsName, JxaFlag[] flags)
	{
		getLongss(flags);
		longestFlagDesc += 2;
		longestLongName += 2;
		
		System.out.println("    \nUsage: " + programsName + " - " + programsDesc + "\n");
		System.out.println("    flags:");
		for (int i = 0; i < flags.length; i++)
		{
			final JxaFlag f = flags[i];
			final String fmt = String.format(
				"      -%c or --%-" + longestLongName + "s" + "%-" + longestFlagDesc + "s - %s",
				f.getShortName(),
				f.getLongName(),
				f.getDesc(),
				getEnumAsStr(f.getNeeds())
			);
			System.out.println(fmt);
		}
		System.out.println("");
	}
	
	private static String getEnumAsStr (JxaFlag.FlagArg a)
	{
		switch (a)
		{
			case YES: return "needs argument";
			case NON: return "does not require argument";
			default:  return "argument is optional";
		}
	}
	
	private static void getLongss (JxaFlag[] flags)
	{	
		for (int i = 0; i < flags.length; i++)
		{
			final int nameLen = flags[i].getLongName().length();
			final int descLen = flags[i].getDesc().length();
			
			longestLongName = (longestLongName > nameLen) ? longestLongName : nameLen;
			longestFlagDesc = (longestFlagDesc > descLen) ? longestFlagDesc : descLen;
		}
	}	
}

