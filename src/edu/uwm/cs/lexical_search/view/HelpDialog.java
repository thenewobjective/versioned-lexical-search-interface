package edu.uwm.cs.lexical_search.view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.swt.browser.Browser;

import edu.uwm.cs.lexical_search.util.DialogUtils;

public class HelpDialog extends Dialog {

	protected Object result;
	protected Shell shellHelp;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public HelpDialog(Shell parent, int style) {
		super(parent, style);
		setText("Lexical Search - Help");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		DialogUtils.centerScreen(shellHelp);
		shellHelp.open();
		shellHelp.layout();
		Display display = getParent().getDisplay();
		while (!shellHelp.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shellHelp = new Shell(getParent(), getStyle());
		shellHelp.setSize(801, 464);
		shellHelp.setText(getText());

		Button btnOk = new Button(shellHelp, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shellHelp.dispose();
			}
		});
		btnOk.setBounds(710, 404, 75, 25);
		btnOk.setText("OK");

		Composite composite = new Composite(shellHelp, SWT.NONE);
		composite.setBounds(10, 10, 775, 388);

		final Browser browser = new Browser(composite, SWT.BORDER);
		browser.setBounds(224, 10, 551, 378);

		Tree treeHelpMenu = new Tree(composite, SWT.BORDER);
		treeHelpMenu.setHeaderVisible(true);
		treeHelpMenu.setLinesVisible(true);
		treeHelpMenu.setBounds(0, 10, 218, 378);

		TreeItem trtmHelpContent = new TreeItem(treeHelpMenu, SWT.NONE);
		trtmHelpContent.setText("1. Help Content");
		trtmHelpContent.setData(HelpMenu.QUERY_EXPLANATION);

		TreeItem trtmTokenNames = new TreeItem(trtmHelpContent, SWT.NONE);
		trtmTokenNames.setText("1.1 Token Names");
		trtmTokenNames.setData(HelpMenu.TOKEN_NAMES);

		TreeItem trtmValueConstraints = new TreeItem(trtmHelpContent, SWT.NONE);
		trtmValueConstraints.setText("1.2 Value Constraints");
		trtmValueConstraints.setData(HelpMenu.VALUE_CONSTRAINTS);

		TreeItem trtmTokenSequence = new TreeItem(trtmHelpContent, SWT.NONE);
		trtmTokenSequence.setText("1.3 Token Sequence");
		trtmTokenSequence.setData(HelpMenu.TOKEN_SEQUENCES);

		TreeItem trtmCombinators = new TreeItem(trtmHelpContent, SWT.NONE);
		trtmCombinators.setText("1.4 Combinators");
		trtmCombinators.setData(HelpMenu.COMBINATORS);

		TreeItem trtmGrepSearch = new TreeItem(trtmHelpContent, SWT.NONE);
		trtmGrepSearch.setText("1.5 Grep-Style Searches");
		trtmGrepSearch.setData(HelpMenu.GREP_SEARCHES);

		TreeItem trtmPrevQueries = new TreeItem(trtmHelpContent, SWT.NONE);
		trtmPrevQueries.setText("1.6 Retrieving Previous Queries");
		trtmPrevQueries.setData(HelpMenu.PREVIOUS_QUERIES);

		TreeItem trtmOtherCmds = new TreeItem(trtmHelpContent, SWT.NONE);
		trtmOtherCmds.setText("1.7 Other Commands");
		trtmOtherCmds.setData(HelpMenu.OTHER_COMMANDS);

		TreeItem trtmLogging = new TreeItem(treeHelpMenu, SWT.NONE);
		trtmLogging.setText("2. Session Logging");
		trtmLogging.setData(HelpMenu.SESSION_LOGGING);

		TreeItem trtmExamples = new TreeItem(treeHelpMenu, SWT.NONE);
		trtmExamples.setText("3. Examples");
		trtmExamples.setData(HelpMenu.EXAMPLES);

		treeHelpMenu.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {

				if (e.item instanceof TreeItem) {
					TreeItem selected = (TreeItem) e.item;
					HelpMenu data = (HelpMenu) selected.getData();
					switch (data) {

					case QUERY_EXPLANATION:
						browser.setText("<h1>Query Explanation</h1>"
								+ "Queries mainly define searches over the files indexed by the "
								+ "current project file, but also have a few uses in modifying the state of the Search Engine. "
								+ "A search defines a sequence of tokens in the current domain dialect; executing a search causes "
								+ "the engine to find all places in the database that match the query, modulo file restriction. "
								+ "The Search Engine has many query options, including sequences, wildcards, and file restriction. "
								+ "The following sections give two presentations of the query format and meaning: an exploratory "
								+ "overview and a grammar with examples.");
						break;
					case VALUE_CONSTRAINTS:
						browser.setText("<h1>Value Constraints</h1>");

						break;
					case COMBINATORS:
						browser.setText("<h1>Combinators</h1>");
						break;
					case EXAMPLES:
						browser.setText("<h1>Examples</h1>");
						break;
					case GREP_SEARCHES:
						browser.setText("<h1>Grep-Style Search</h1>");
						break;
					case OTHER_COMMANDS:
						browser.setText("<h1>Other Commands</h1>");
						break;
					case PREVIOUS_QUERIES:
						browser.setText("<h1>Retrieve Previous Queries</h1>");
						break;

					case SESSION_LOGGING:
						browser.setText("<h1>Session Logging</h1>");
						break;
					case TOKEN_NAMES:
						browser.setText("<h1>Token Names</h1>");
						break;
					case TOKEN_SEQUENCES:
						browser.setText("<h1>Token Sequence</h1>");
						break;
					default:
						break;
					}
				}
			}
		});

		trtmHelpContent.setExpanded(true);
	}

	private enum HelpMenu {
		QUERY_EXPLANATION, TOKEN_NAMES, VALUE_CONSTRAINTS, TOKEN_SEQUENCES, COMBINATORS, GREP_SEARCHES, PREVIOUS_QUERIES, OTHER_COMMANDS, SESSION_LOGGING, EXAMPLES
	}
}
