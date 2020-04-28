package edu.uwm.cs.lexical_search.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import edu.uwm.cs.lexical_search.model.MsgType;
import edu.uwm.cs.lexical_search.model.Query;
import edu.uwm.cs.lexical_search.util.DialogUtils;
import edu.uwm.cs.lexical_search.view.MainScreen;

public class QueryController extends SelectionAdapter {

	private final Query queryModel;
	private MainScreen view;
	// This boolean variable can be set to false to use the fake data in the
	// query class
	private boolean useRealData = true;

	/**
	 * Constructor used to install the controller and set a reference to the
	 * view
	 * 
	 * @param m
	 * @param v
	 */
	public QueryController(Query m, MainScreen v) {
		queryModel = m;
		view = v;
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {

		if (arg0.getSource() instanceof Button) {
			Button btn = (Button) arg0.getSource();

			if (btn.getText().equals("Query")) {
				view.getTabwindowComposite().selectOutputWindowTab();
				// Close all opened text editors
				view.getTabwindowComposite().resetTabWindow();
				// Remove all entries in the tree viewer
				view.getTreeComposite().getTree().removeAll();

				view.getMainWindow().update();

				if (!view.getQueryComposite().getLblRepoDir().isEmpty()) {
					if (!view.getQueryComposite().getTextQuery().isEmpty()) {
						final int headRev = view.getQueryComposite()
								.getHeadRevTextBox();
						final int firstRev = view.getQueryComposite()
								.getTextStartRev();
						final int lastRev = view.getQueryComposite()
								.getTextEndRev();

						view.getTabwindowComposite().setEndingRev(lastRev);
						view.getTabwindowComposite().clearOutputWindow();

						if (useRealData) {
							if (!(firstRev <= lastRev && headRev >= firstRev && headRev >= lastRev)) {
								logErrorMessage("The repository revision range is invalid");
								DialogUtils
										.displayErrorMsgWindow(
												"Please enter a valid repository revision range",
												view.getMainWindow());
								return;
							}

							new Thread(new Runnable() {
								@Override
								public void run() {
									Display.getDefault().asyncExec(
											new Runnable() {
												public void run() {
													try {
														view.getTabwindowComposite()
																.setOutputWindowText(
																		"\nSTART\n");
														String query = view
																.getQueryComposite()
																.getTextQuery();
														queryModel
																.executeQuery(
																		firstRev,
																		lastRev,
																		query);
														view.getTabwindowComposite()
																.setOutputWindowText(
																		"\nDONE\n");
													} catch (MalformedURLException e) {
														logErrorMessage("Invalid repository URL");
													} catch (IOException e) {
														logErrorMessage("IOException when executing the query");
													}
												}
											});
								}
							}).start();
						} else {
							queryModel.createFakeData();
						}
					} else {
						logErrorMessage("Please enter a valid query");
						DialogUtils.displayErrorMsgWindow(
								"Please enter a valid query",
								view.getMainWindow());
					}
				} else {
					logErrorMessage("Please select a valid repository URL");
					DialogUtils.displayErrorMsgWindow(
							"Please enter a valid repository URL",
							view.getMainWindow());
				}
			}
		}
	}

	/**
	 * Method used to write to the log output window
	 * 
	 * @param msg
	 */
	private void logErrorMessage(String msg) {
		view.getTabwindowComposite().setOutputWindowText(
				MsgType.ERROR.getTag() + msg);
	}
}