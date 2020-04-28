package edu.uwm.cs.lexical_search.controller;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import edu.uwm.cs.lexical_search.view.MainScreen;
import edu.uwm.cs.lexical_search.view.TabWindowComposite;
import edu.uwm.cs.lexical_search.view.TabWindowComposite.GraphNodes;

public class TabWindowController extends SelectionAdapter implements
		MouseListener {

	private MainScreen view;

	public TabWindowController(MainScreen v) {
		view = v;
	}

	@Override
	public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {

		if (e.getSource() instanceof GraphNodes) {
			GraphNodes node = (GraphNodes) e.getSource();
			node.setBackground(TabWindowComposite.nodeSelectedImage);
			view.getTreeComposite().addSubTreeItems(node);
		}

	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		StyledText textEditor;
		String selected;

		if (e.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) e.getSource();
			textEditor = (StyledText) menuItem.getParent().getData();
			selected = menuItem.getText();
		} else if ((e.getSource() instanceof ToolItem)) {
			ToolItem toolItem = (ToolItem) e.getSource();
			textEditor = (StyledText) toolItem.getParent().getData();
			selected = toolItem.getText();
		} else {
			return;
		}

		switch (selected) {
		case "Cut":
			textEditor.cut();
			break;
		case "Copy":
			textEditor.copy();
			break;
		case "Select All":
			textEditor.selectAll();
			break;
		case "Stop":
			break;
		default:
			break;
		}
	}

	// Methods without definition
	@Override
	public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
	}

	@Override
	public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
	}
}
