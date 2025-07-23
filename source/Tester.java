import jxa.*;

class Error
{
	public static void handleWordError (final JxaFlag<?> flag, final String given)
	{
		final String msg = String.format(
			"Here you can handle your error: '--%s' expects a <%s> type\n" +
			"sadly <%s> is not that type\n",
			flag.getLongname(),
			flag.getArgument().getClass().getCanonicalName(),
			given
		);
		System.err.print(msg);
		System.exit(0);
	}
}

public class Tester
{
	static JxaFlag<?> flags[] =
	{
		new JxaFlag<String>  ("document", "document to work with",    'd', JxaFlag.arg.yes, "", ""),
		new JxaFlag<Integer> ("pages",    "number of pages",          'p', JxaFlag.arg.may, 1, 0),
		new JxaFlag<Integer> ("words",    "number of words per page", 'w', JxaFlag.arg.may, 256, Error::handleWordError),
		new JxaFlag<Integer> ("rgb",      "RGB format color number",  'R', JxaFlag.arg.may, 0xffaabb),
		new JxaFlag<Void>    ("usage",    "print usage",              'u', JxaFlag.arg.non)
	};
	
	public static void main(String[] args)
	{
		Jxa.parse(flags, args);	
		JxaDoc.printUsage(flags);
		JxaDoc.debugInfo(flags);
	}
}