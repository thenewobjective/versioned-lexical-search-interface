package edu.uwm.cs.lexical_search.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MenuItem;

import edu.uwm.cs.lexical_search.model.MsgType;
import edu.uwm.cs.lexical_search.util.FileManagement;
import edu.uwm.cs.lexical_search.view.HelpDialog;
import edu.uwm.cs.lexical_search.view.MainScreen;
import edu.uwm.cs.lexical_search.view.RepositoryPromptWindow;

public class MainScreenController extends SelectionAdapter {

	private MainScreen view;

	/**
	 * Constructor used to install the controller and set a reference to the
	 * view
	 * 
	 * @param v
	 */
	public MainScreenController(MainScreen v) {
		view = v;
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {

		if (arg0.getSource() instanceof MenuItem) {

			MenuItem menuSelected = (MenuItem) arg0.getSource();

			switch (menuSelected.getText()) {
			case "Open":
				FileDialog fd = new FileDialog(view.getMainWindow(), SWT.OPEN);
				fd.setText("Open");
				fd.setFilterPath("C:\\");
				String selected = fd.open();
				FileManagement file = new FileManagement();
				try {
					view.getTabwindowComposite().createNewEditorTab(selected,
							file.openFile(selected), 0, null);
				} catch (IOException e1) {
					view.getTabwindowComposite()
							.setOutputWindowText(
									MsgType.ERROR.getTag()
											+ "IOException opening file");
				}

				break;
			case "Change Repository":
				try {
					String repoURL = view.getQueryComposite().getLblRepoDir();
					view.createModel(new RepositoryPromptWindow(view
							.getMainWindow(), SWT.DIALOG_TRIM).open(repoURL));
				} catch (MalformedURLException e) {
					view.getTabwindowComposite()
							.setOutputWindowText(
									MsgType.ERROR.getTag()
											+ "MalformedURLException when creating the new repository");
				}
				break;
			case "Help":
				new HelpDialog(view.getMainWindow(), SWT.DIALOG_TRIM).open();
				break;

			default:
				Runtime.getRuntime().exit(0);
				break;
			}
		}
	}

}
