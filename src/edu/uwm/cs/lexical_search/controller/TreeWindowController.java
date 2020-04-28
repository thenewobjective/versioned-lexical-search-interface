package edu.uwm.cs.lexical_search.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import edu.uwm.cs.lexical_search.model.MsgType;
import edu.uwm.cs.lexical_search.util.FileManagement;
import edu.uwm.cs.lexical_search.util.Pair;
import edu.uwm.cs.lexical_search.view.CompareFilesDialog;
import edu.uwm.cs.lexical_search.view.MainScreen;
import edu.uwm.cs.lexical_search.view.TabWindowComposite;
import edu.uwm.cs.lexical_search.view.TabWindowComposite.GraphNodes;

public class TreeWindowController extends SelectionAdapter implements
		MouseListener {

	private MainScreen view;

	public TreeWindowController(MainScreen v) {
		view = v;
	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0) {

		if (arg0.getSource() instanceof Tree) {
			Tree tree = (Tree) arg0.getSource();

			// Check if the double click event is on one of the items
			if (tree.getSelection().length == 1) {
				TreeItem selection = tree.getSelection()[0];
				openTreeFile(tree, selection);
			}

		}
	}

	/**
	 * Method implementation to open the selected item from the tree composite
	 * 
	 * @param tree
	 * @param selection
	 */
	private void openTreeFile(Tree tree, TreeItem selection) {

		FileManagement fileManager = new FileManagement();

		// Check if the selected item is a search result
		if (!isSelectionRootOrFileNode(tree, selection)) {
			// Selection is a lineNumber
			if (selection.getData() instanceof Pair<?, ?>) {

				try {
					@SuppressWarnings("unchecked")
					Pair<File, ArrayList<Integer>> pairFileLineNum = (Pair<File, ArrayList<Integer>>) selection
							.getData();
					File file = pairFileLineNum.getLeft();
					int lineNum = pairFileLineNum.getRight().get(
							selection.getParentItem().indexOf(selection));
					String text;

					text = fileManager.openFile(file.getPath());
					String tabText = selection.getParentItem().getParentItem()
							.getText()
							+ " - " + file.getName();
					// Create new text editor
					view.getTabwindowComposite().createNewEditorTab(tabText,
							text, lineNum, pairFileLineNum.getRight());
				} catch (IOException e) {
					view.getTabwindowComposite()
							.setOutputWindowText(
									MsgType.ERROR.getTag()
											+ "IOException when opening the result file");
				}
			} else {
				System.out.println("The is no Pair<File, Integer>");
			}
		}
	}

	/**
	 * Method implementation used to check if the selected item is either a
	 * result line number or a file name
	 * 
	 * @param tree
	 * @param selection
	 * @return
	 */
	private boolean isSelectionRootOrFileNode(Tree tree, TreeItem selection) {
		if (selection.getParentItem() == null)
			return true;
		TreeItem[] items = tree.getItems();

		for (int i = 0; i < items.length; ++i) {
			TreeItem tempItem = selection.getParentItem();
			if (items[i].equals(tempItem))
				return true;
		}

		return false;
	}

	/**
	 * 
	 */
	public void openCompareFiles() {

		Tree tree = view.getTreeComposite().getTree();

		if (tree.getSelection().length == 2) {

			if (tree.getSelection()[0].getData() instanceof File) {

				File fileLeft = (File) tree.getSelection()[0].getData();
				File fileRight = (File) tree.getSelection()[1].getData();
				new CompareFilesDialog(view.getMainWindow(), SWT.CLOSE
						| SWT.BORDER | SWT.MAX | SWT.RESIZE, fileLeft,
						fileRight).open();
			} else {
				displayMessage(MsgType.WARNING.getTag(),
						"Please select only file from the tree file browser");
			}
		} else {
			displayMessage(MsgType.WARNING.getTag(),
					"Please select two files from the tree file browser");
		}
	}

	/**
	 * Method implementation to display a message on the output window
	 * 
	 * @param type
	 * @param msg
	 */
	private void displayMessage(String type, String msg) {
		view.getTabwindowComposite().setOutputWindowText(type + msg);
	}

	/**
	 * Method implementation to handle the click events from the context menu
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		Tree tree = view.getTreeComposite().getTree();
		if (tree.getSelection().length >= 1) {
			TreeItem selected = tree.getSelection()[0];
			if (e.getSource() instanceof MenuItem) {
				MenuItem menu = (MenuItem) e.getSource();
				switch (menu.getText()) {
				case "Open":
					openTreeFile(tree, selected);
					break;
				case "Remove":
					if (selected.getData() instanceof GraphNodes)
						((GraphNodes) selected.getData())
								.setBackground(TabWindowComposite.nodeImage);
					selected.dispose();
					break;
				case "Compare":
					openCompareFiles();
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void mouseDown(MouseEvent arg0) {
	}

	@Override
	public void mouseUp(MouseEvent arg0) {
	}
}
