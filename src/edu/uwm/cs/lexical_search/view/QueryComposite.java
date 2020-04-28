package edu.uwm.cs.lexical_search.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import edu.uwm.cs.lexical_search.controller.QueryController;
import edu.uwm.cs.lexical_search.model.QueryResult;
import edu.uwm.cs.lexical_search.util.DialogUtils;
import edu.uwm.cs.lexical_search.util.SWTResourceManager;

public class QueryComposite extends Composite implements Observer {
	private Text firstRevTextBox;
	private Text lastRevTextBox;
	private Text queryTextBox;
	private Button btnQuery;
	@SuppressWarnings("unused")
	private QueryResult queryResult;
	private CLabel lblRepoDir;
	private CLabel lblHeadRevision;
	private Text txtHeadrevtextbox;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QueryComposite(Composite parent, int style) {
		super(parent, SWT.BORDER);
		
		CLabel lblQuery = new CLabel(this, SWT.CENTER);
		lblQuery.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblQuery.setAlignment(SWT.LEFT);
		lblQuery.setBounds(10, 129, 40, 21);
		lblQuery.setText("Query");
		
		CLabel lblRepositoryDirectory = new CLabel(this, SWT.CENTER);
		lblRepositoryDirectory.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblRepositoryDirectory.setBounds(10, 10, 167, 21);
		lblRepositoryDirectory.setText("Repository Directory");
		
		lblRepoDir = new CLabel(this, SWT.BORDER);
		lblRepoDir.setBounds(10, 37, 167, 21);
		lblRepoDir.setText("");
		
		lblHeadRevision = new CLabel(this, SWT.NONE);
		lblHeadRevision.setText("Head Revision");
		lblHeadRevision.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblHeadRevision.setBounds(10, 64, 85, 21);
		
		CLabel lblRevision = new CLabel(this, SWT.NONE);
		lblRevision.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblRevision.setBounds(10, 102, 53, 21);
		lblRevision.setText("Revision");
		
		txtHeadrevtextbox = new Text(this, SWT.BORDER | SWT.CENTER);
		txtHeadrevtextbox.setEditable(false);
		txtHeadrevtextbox.setBounds(101, 64, 76, 21);
		
		firstRevTextBox = new Text(this, SWT.BORDER | SWT.CENTER);
		firstRevTextBox.setBounds(66, 102, 50, 21);
		
		lastRevTextBox = new Text(this, SWT.BORDER | SWT.CENTER);
		lastRevTextBox.setBounds(127, 102, 50, 21);
		
		queryTextBox = new Text(this, SWT.BORDER | SWT.RIGHT);
		queryTextBox.setBounds(56, 129, 121, 21);
		queryTextBox.setText("'static'");
		queryTextBox.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR | e.keyCode == SWT.KEYPAD_CR){
					btnQuery.notifyListeners(SWT.Selection, new Event());
				}
			}
		});

		btnQuery = new Button(this, SWT.NONE);
		btnQuery.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		btnQuery.setBounds(102, 156, 75, 25);
		btnQuery.setText("Query");

	}
	
	public int getHeadRevTextBox() {
		return Integer.parseInt(txtHeadrevtextbox.getText());
	}

	/**
	 * Get the repository start number if not a number generate a Message Window 
	 * if empty return -1
	 * @return
	 */
	public int getTextStartRev() {
		String repStart = firstRevTextBox.getText();
		
		if (!repStart.isEmpty())
		{
			int repEndNum;
			try {
				repEndNum = Integer.parseInt(repStart);
				return repEndNum;
			} 
			catch (NumberFormatException e) {
				DialogUtils.displayErrorMsgWindow("Please enter a valid starting repository number", getShell());	
			}
		}
		return -1;
	}
	
	/**
	 * Get the repository end number if not a number generate a Message Window 
	 * if empty return -1
	 * @return
	 */
	public int getTextEndRev() {
		
		String repEnd = lastRevTextBox.getText();
		
		if (!repEnd.isEmpty())
		{
			int repEndNum;
			try {
				repEndNum = Integer.parseInt(repEnd);
				return repEndNum;
			} catch (NumberFormatException e) {
				DialogUtils.displayErrorMsgWindow("Please enter a valid ending repository number", getShell());
			}
		}
		return -1;
	}

	public String getLblRepoDir() {
		return lblRepoDir.getText();
	}

	public String getTextQuery() {
		return queryTextBox.getText();
	}

	public Button getBtnQuery() {
		return btnQuery;
	}

	public void setHeadRevTextBox(String revNum) {
		txtHeadrevtextbox.setText(revNum);
		btnQuery.setFocus();
	}

	public void setLblRepoDir(String repo) {
		lblRepoDir.setText(repo);
	}
	
	public void setFirstRevTextBox(String firstRev) {
		firstRevTextBox.setText(firstRev);
	}

	public void setLastRevTextBox(String lastRev) {
		lastRevTextBox.setText(lastRev);
	}

	public void addController(QueryController controller){
		btnQuery.addSelectionListener(controller);
	} 
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof QueryResult){
			queryResult = (QueryResult) arg;
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
