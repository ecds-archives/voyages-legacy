package edu.emory.library.tast.master;

public class HelpLinks
{
	
	private static HelpLink[] helpLinks = {
		new HelpLink("Sitemap", "help/sitemap.faces", true),
		new HelpLink("FAQs", "help/faq.faces", true),
		new HelpLink("Demos", "help/demo-overview.faces", true),
		new HelpLink("Glossary", "help/glossary.faces", true),
		new HelpLink("Legal", "help/legal.faces", true) };

	public static HelpLink[] getHelpLinks()
	{
		return helpLinks;
	}

}
