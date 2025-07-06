/*
 * jxa - Java executable arguments parser
 * Jul 5, 2025
 * Error handling
 */

package jxa;

public final class JxaFatal
{
	public static void duplicatedId (int first, int second, JxaFlag[] flags)
	{
		final String msg = String.format(
			"jxa::error: duplicated id!\n" +
			"  both '%s' and '%s' flags share same id '%c'\n",
			flags[first].getLongName(),
			flags[second].getLongName(),
			flags[second].getShortName()
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void invalidId (JxaFlag flag)
	{
		final String msg = String.format(
			"jxa::error: invalid id provided\n" +
			"  the '%s' flag has an invalid id (%c)\n",
			flag.getLongName(),
			flag.getShortName()
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void undefinedFlag (String given)
	{
		final String msg = String.format(
			"jxa::error: undefined flag provided\n" +
			"  the program cannot recognize '--%s' as a flag ,':|\n",
			given
		);
		System.err.print(msg);
		System.exit(1);
	}

	public static void undefinedFlag (char given)
	{
		final String msg = String.format(
			"jxa::error: undefined flag provided\n" +
			"  the program cannot recognize '-%c' as a flag ,':|\n",
			given
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void groupedArgedFlags (String given, char first, char second)
	{
		final String msg = String.format(
			"jxa::error: more than one flag needs argument in this group\n" +
			"  the program found '%s'; Both '-%c' and '-%c' takes argument\n" +
			"  better do: -%c <arg> -%c <arg> ...\n",
			given,
			first,
			second,
			first,
			second
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void missingArgument (JxaFlag flag)
	{
		final String msg = String.format(
			"jxa::error: flag is missing its argument\n" +
			"  the program found that '--%s' has no argument, please provide it\n",
			flag.getLongName()
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void duplicatedName (JxaFlag flag)
	{	
		final String msg = String.format(
			"jxa::error: longname appears more than once\n" +
			"  the program found that '--%s' longname is repeated\n",
			flag.getLongName()
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void noNeedOfArg (String flagname)
	{	
		final String msg = String.format(
			"jxa::error: giving unnecessary argument\n" +
			"  the program found that '--%s' is taking an argument when it does not ask for it\n",
			flagname
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void unsupportedStdin ()
	{
		System.out.println("jxa::error: '-' UNIX option is not supported, use a shell command instead! Sorry");
		System.exit(1);
	}
}