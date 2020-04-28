package edu.uwm.cs.lexical_search.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import edu.uwm.cs.lexical_search.util.CommandPrompt;

public class OutputComposite extends Composite {
	public Text outputConsole;
	private Text repositoryTextBox;
	private Text projectTextBox;
	private CommandPrompt cmd = new CommandPrompt();
	private Text queryTextBox;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OutputComposite(Composite parent, int style) {
		super(parent, SWT.BORDER);
		setLayout(null);
		
		outputConsole = new Text(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		outputConsole.setBounds(10, 118, 616, 237);
		
		Button btnLexeme = new Button(this, SWT.NONE);
		btnLexeme.setBounds(10, 361, 75, 25);
		btnLexeme.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	           
				outputConsole.setText(cmd.commandLine("DMSLexemeExtractor.cmd Java~Java1_6 @D:\\Capstone\\LexemeExtractor.lxe"));
			}
		});
		btnLexeme.setText("Lexeme");
		
		Button btnDatabase = new Button(this, SWT.NONE);
		btnDatabase.setBounds(94, 361, 75, 25);
		btnDatabase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				outputConsole.setText(cmd.commandLine("DMSSearchEngineIndex.cmd D:\\Capstone\\SearchIndexer.prj"));
			}
		});
		btnDatabase.setText("Database");
		
		repositoryTextBox = new Text(this, SWT.BORDER);
		repositoryTextBox.setBounds(123, 64, 422, 21);
		repositoryTextBox.setText("https://uwmcs536weddingplanner.googlecode.com/svn/");
		
		projectTextBox = new Text(this, SWT.BORDER);
		projectTextBox.setBounds(123, 91, 422, 21);
		
		Label projectDirLabel = new Label(this, SWT.NONE);
		projectDirLabel.setBounds(10, 94, 88, 15);
		projectDirLabel.setText("Project Directory");
		
		Label repositoryLabel = new Label(this, SWT.NONE);
		repositoryLabel.setBounds(10, 67, 107, 15);
		repositoryLabel.setText("Repository Directory");
		
		Button browseNewButton1 = new Button(this, SWT.NONE);
		browseNewButton1.setBounds(551, 62, 75, 25);
		browseNewButton1.setText("Browse");
		
		Button browseNewButton_1 = new Button(this, SWT.NONE);
		browseNewButton_1.setBounds(551, 87, 75, 25);
		browseNewButton_1.setText("Browse");
		
		queryTextBox = new Text(this, SWT.BORDER);
		queryTextBox.setBounds(400, 365, 142, 21);
		
		Button queryButton = new Button(this, SWT.NONE);
		queryButton.setBounds(548, 361, 75, 25);
		queryButton.setText("New Button");
		
		
	}

	public Text getOutputConsole() {
		return outputConsole;
	}

	public void setOutputConsole(Text outputConsole) {
		this.outputConsole = outputConsole;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
