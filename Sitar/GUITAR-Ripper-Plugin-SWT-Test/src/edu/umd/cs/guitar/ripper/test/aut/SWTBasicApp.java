/*	
 *  Copyright (c) 2011-@year@. The GUITAR group at the University of Maryland. Names of owners of this group may
 *  be obtained by sending an e-mail to atif@cs.umd.edu
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 *  documentation files (the "Software"), to deal in the Software without restriction, including without 
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *	the Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 *	conditions:
 * 
 *	The above copyright notice and this permission notice shall be included in all copies or substantial 
 *	portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 *	LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO 
 *	EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 *	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 *	THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package edu.umd.cs.guitar.ripper.test.aut;

//Credit to http://www.vogella.de/articles/SWT/article.html

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * Sample application that's a little bigger than the others.
 */
public class SWTBasicApp {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		// Create a new Gridlayout with 2 columns where the 2 column do no need
		// to be same size
		GridLayout layout = new GridLayout(2, false);
		// set the layout of the shell
		shell.setLayout(layout);
		// Create a label and a button
		Label label = new Label(shell, SWT.NONE);
		label.setText("A lable");
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Press Me");
		
		// Create a new label that will spam two columns
		label = new Label(shell, SWT.BORDER);
		label.setText("This is a label");
		// Create new layout data
		GridData data = new GridData(GridData.FILL, GridData.BEGINNING, true,
				false, 2, 1);
		label.setLayoutData(data);
		
		// Create a new label which is used as a separator
		label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		// Create new layout data
		data = new GridData(GridData.FILL, GridData.BEGINNING, true,
				false, 2, 1);
		data.horizontalSpan=2;
		label.setLayoutData(data);
		
		// Create a right aligned button
		Button b = new Button(shell, SWT.PUSH);
		b.setText("New Button");
		
		data = new GridData(GridData.END, GridData.BEGINNING, false,
				false, 2, 1);
		b.setLayoutData(data);

		Spinner spinner = new Spinner(shell, SWT.READ_ONLY); // TODO add event for spinners
		spinner.setMinimum(0);
		spinner.setMaximum(1000);
		spinner.setSelection(500);
		spinner.setIncrement(1);
		spinner.setPageIncrement(100);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.widthHint = SWT.DEFAULT;
		gridData.heightHint = SWT.DEFAULT;
		gridData.horizontalSpan=2;
		spinner.setLayoutData(gridData);
		
		Composite composite = new Composite(shell, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan= 2;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));
		
		// test that SitarEditableTextAction doesn't mark read-only Texts as replayable
		Text text = new Text(composite, SWT.READ_ONLY);
		text.setText("Read only");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		text.setLayoutData(gridData);
		
		text = new Text(composite, SWT.NONE);
		text.setText("Another test");
//		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
//		text.setLayoutData(gridData);
		Group group = new Group(shell, SWT.NONE);
		group.setText("This is my group");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan= 2;
		group.setLayoutData(gridData);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		text = new Text(group, SWT.NONE);
		text.setText("Another test");
		
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}