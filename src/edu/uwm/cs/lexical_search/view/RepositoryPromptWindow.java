package edu.uwm.cs.lexical_search.view;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import edu.uwm.cs.lexical_search.util.CommandPrompt;
import edu.uwm.cs.lexical_search.util.DialogUtils;
import edu.uwm.cs.lexical_search.util.SWTResourceManager;

public class RepositoryPromptWindow extends Dialog {

	protected String[] result ={"http://", "1", "1", "20"};
	protected Shell shlRepositoryPrompt;
	private Text urlTextBox;
	private Text firstRevText;
	private Text lastRevText;
	private Button btnOk;
	private Label lblHeadRevision;
	private Text headRevText;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RepositoryPromptWindow(Shell parent, int style) {
		super(parent, style);
		shlRepositoryPrompt = parent;
		setText("Repository Prompt");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String[] open(String repoURL) {
		createContents();
		DialogUtils.centerScreen(shlRepositoryPrompt);
		
		Label lblFirstRevision = new Label(shlRepositoryPrompt, SWT.NONE);
		lblFirstRevision.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblFirstRevision.setBounds(353, 74, 117, 25);
		lblFirstRevision.setText("First Revision");
		
		Label lblLastRevision = new Label(shlRepositoryPrompt, SWT.NONE);
		lblLastRevision.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblLastRevision.setBounds(354, 105, 114, 27);
		lblLastRevision.setText("Last Revision");
		
		lblHeadRevision = new Label(shlRepositoryPrompt, SWT.NONE);
		lblHeadRevision.setText("Head Revision");
		lblHeadRevision.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblHeadRevision.setBounds(10, 74, 126, 25);
		
		headRevText = new Text(shlRepositoryPrompt, SWT.BORDER | SWT.CENTER);
		headRevText.setEditable(false);
		headRevText.setBounds(10, 108, 126, 21);		
		
		urlTextBox = new Text(shlRepositoryPrompt, SWT.BORDER);
		urlTextBox.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		urlTextBox.setText(repoURL);
		urlTextBox.setBounds(10, 41, 457, 27);
		urlTextBox.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR | e.keyCode == SWT.KEYPAD_CR){
					btnOk.notifyListeners(SWT.Selection, new Event());
				}
			}
		});
		
		firstRevText = new Text(shlRepositoryPrompt, SWT.BORDER | SWT.CENTER);
		firstRevText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		firstRevText.setBounds(285, 76, 50, 21);
		firstRevText.setText("1");

		lastRevText = new Text(shlRepositoryPrompt, SWT.BORDER | SWT.CENTER);
		lastRevText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lastRevText.setBounds(285, 108, 50, 21);
		lastRevText.setText(result[3]);
		
		btnOk = new Button(shlRepositoryPrompt, SWT.NONE);
		btnOk.setEnabled(false);
		btnOk.setBounds(311, 154, 75, 25);
		btnOk.setText("OK");
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					int headRev = Integer.parseInt(headRevText.getText());
					int firstRev = Integer.parseInt(firstRevText.getText()); 
					int lastRev = Integer.parseInt(lastRevText.getText());
					
					if(firstRev <= lastRev && headRev >= firstRev && headRev >= lastRev && firstRev > 0 && lastRev > 0)
					{
						result[0] = urlTextBox.getText();
						result[1] = headRevText.getText(); 
						result[2] = firstRevText.getText();
						result[3] = lastRevText.getText();
						shlRepositoryPrompt.close();
					}
					else
					{
						DialogUtils.displayErrorMsgWindow("The repository range is invalid", shlRepositoryPrompt);
					}
				} catch (NumberFormatException e1) {
					DialogUtils.displayErrorMsgWindow("The repository range is invalid", shlRepositoryPrompt);
				}
			}
		});
		
		Button btnCancel = new Button(shlRepositoryPrompt, SWT.NONE);
		btnCancel.setBounds(392, 154, 75, 25);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlRepositoryPrompt.close();
			}
		});
		
		Button btnValidate = new Button(shlRepositoryPrompt, SWT.NONE);
		btnValidate.setBounds(230, 154, 75, 25);
		btnValidate.setText("Validate");
		btnValidate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String command = "svn log -r head " + urlTextBox.getText();
				CommandPrompt cmd = new CommandPrompt();
				
				try {
					
					String results = cmd.commandLine(command);
					
					if(cmd.getExitValue() == 0)
					{
						btnOk.setEnabled(true);				
						Pattern p = Pattern.compile("\\nr(\\d+)");
						Matcher m = p.matcher(results);
						
						if(m.find())
						{
							headRevText.setText(m.group(1));
						}
					}
					else
					{
						DialogUtils.displayErrorMsgWindow("Error while running the process, exit code: " + cmd.getExitValue(), shlRepositoryPrompt);
						System.out.println("Error while running the process, exit code: " + cmd.getExitValue());
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		

		
		shlRepositoryPrompt.open();
		shlRepositoryPrompt.layout();
		
		Display display = getParent().getDisplay();
		while (!shlRepositoryPrompt.isDisposed()) {
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
		shlRepositoryPrompt = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlRepositoryPrompt.setSize(483, 217);
		shlRepositoryPrompt.setText("Repository Prompt");
		
		Label lblRepositoryUrl = new Label(shlRepositoryPrompt, SWT.NONE);
		lblRepositoryUrl.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblRepositoryUrl.setBounds(10, 10, 457, 25);
		lblRepositoryUrl.setText("Repository URL:");

	}
}
