package edu.uwm.cs.lexical_search.view;

import java.io.File;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.RGB;
import edu.uwm.cs.lexical_search.util.DialogUtils;
import edu.uwm.cs.lexical_search.util.FileComparator;
import edu.uwm.cs.lexical_search.util.JavaLineStyler;
import edu.uwm.cs.lexical_search.util.SWTResourceManager;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Group;

public class CompareFilesDialog extends Dialog {

	protected Object result;
	protected Shell shlFileCompareTool;
	private File original;
	private File revised;
	private StyledText leftTextWindow;
	private StyledText rightTextWindow;
	private Group group;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CompareFilesDialog(Shell parent, int style, File leftFile, File rightFile) {
		super(parent, SWT.DIALOG_TRIM | SWT.MAX | SWT.APPLICATION_MODAL);
		setText("Compare Tool");
		original = leftFile;
		revised = rightFile;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		DialogUtils.centerScreen(shlFileCompareTool);
		
		shlFileCompareTool.open();
		shlFileCompareTool.layout();
		setTextEditorWindows();
		Display display = getParent().getDisplay();
		while (!shlFileCompareTool.isDisposed()) {
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
		shlFileCompareTool = new Shell(getParent(), getStyle());
		shlFileCompareTool.setSize(830, 544);
		shlFileCompareTool.setText("File Comparison Tool");
		shlFileCompareTool.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		group = new Group(shlFileCompareTool, SWT.NONE);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		leftTextWindow = new StyledText(group, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		leftTextWindow.addLineStyleListener(new JavaLineStyler());
		leftTextWindow.addLineBackgroundListener(new LineBackground());
		leftTextWindow.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		leftTextWindow.addLineStyleListener(new LineStyleListener()
		{
		    public void lineGetStyle(LineStyleEvent e)
		    {
		        //Set the line number
		        e.bulletIndex = leftTextWindow.getLineAtOffset(e.lineOffset);

		        //Set the style, 12 pixles wide for each digit
		        StyleRange style = new StyleRange();
		        style.fontStyle = SWT.ITALIC | SWT.BOLD;
		        style.metrics = new GlyphMetrics(0, 0, Integer.toString(leftTextWindow.getLineCount()+1).length()*12);

		        //Create and set the bullet
		        e.bullet = new Bullet(ST.BULLET_NUMBER,style);
		    }
		});
		
		rightTextWindow = new StyledText(group, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		rightTextWindow.addLineStyleListener(new JavaLineStyler());
		rightTextWindow.addLineBackgroundListener(new LineBackground());
		rightTextWindow.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		rightTextWindow.addLineStyleListener(new LineStyleListener()
		{
		    public void lineGetStyle(LineStyleEvent e)
		    {
		        //Set the line number
		        e.bulletIndex = rightTextWindow.getLineAtOffset(e.lineOffset);

		        //Set the style, 12 pixles wide for each digit
		        StyleRange style = new StyleRange();
		        style.fontStyle = SWT.ITALIC | SWT.BOLD;
		        style.metrics = new GlyphMetrics(0, 0, Integer.toString(rightTextWindow.getLineCount()+1).length()*12);

		        //Create and set the bullet
		        e.bullet = new Bullet(ST.BULLET_NUMBER,style);
		    }
		});
		
		// Sync the scroll bars
		final ScrollBar leftScrollBar = leftTextWindow.getVerticalBar();
		final ScrollBar rightScrollBar = rightTextWindow.getVerticalBar();
		
		leftScrollBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rightTextWindow.setTopIndex(leftTextWindow.getTopIndex());
			}
		});

		rightScrollBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				leftTextWindow.setTopIndex(rightTextWindow.getTopIndex());
			}
		});
	}
	
	private void setTextEditorWindows(){
		FileComparator fileComparator = new FileComparator(original, revised);
		StringBuilder[] fileDiff = fileComparator.comparableDiff();
		leftTextWindow.setText(fileDiff[0].toString());
		rightTextWindow.setText(fileDiff[1].toString());

	}
	
	private class LineBackground implements LineBackgroundListener {
		final static int INSERT = 0;
		final static int DELETE = 1;
		final static int CHANGE = 2;
		
		@Override
		public void lineGetBackground(LineBackgroundEvent event) {
			Display display = Display.getDefault();
			Color[] colors = new Color[] {
											 new Color(display, new RGB(222, 222, 222)),	 // Gray
											 new Color(display, new RGB(255, 255, 0)),	 // Yellow
											 new Color(display, new RGB(135, 206, 250)), // Skyblue
										 };

			
		    if (event.lineText.length() > 0)
		    {
		    	char firstChar = event.lineText.charAt(0);
		        switch (firstChar)
			    {
			    	case '+':
			    		event.lineBackground = colors[INSERT];
			    		break;
			    	case '-':
			    		event.lineBackground = colors[DELETE];
			    		break;
			    	case ' ':
			    		//Don't change the background color when the lines are equal
			    		break;
			    	case '!':
			    		event.lineBackground = colors[CHANGE];
			    		break;
			    }
		    }
		}
		
	}
	
}
