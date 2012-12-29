/*
 *  
 *  Fosstrak LLRP Commander (www.fosstrak.org)
 * 
 *  Copyright (C) 2008 ETH Zurich
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/> 
 *
 */

package org.fosstrak.llrp.commander.dialogs;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * superclass for all the connect dialogs. all subclasses have to instantiate 
 * the two members FIELDS and DEFAULTS as arrays providing the labels and the 
 * default values for the fields available. <br/>
 * <br/>
 * <code>setFieldsLabels(new String[]{ "test", "me" });</code><br/>
 * <code>setFieldsDefaultValues(new String [] { "myTestDefault", "memuuDefault" });</code><br/>
 * will create two fields with labels "test" and "me" with the respective 
 * default values.
 * 
 * @author sawielan
 *
 */
public abstract class ConnectDialog extends org.eclipse.jface.dialogs.Dialog {
	
	/** the label of the fields. */
	private String [] fieldsLabels;
	
	/** the default values for the fields. */
	private String [] fieldsDefaultValues;
	
	/** the values collected from the fields. */
	private String [] values;
	
	/** the text fields. */
	private Text [] txts;
	
	/** the caption. */
	private final String caption;
	
	/** the grid settings for the label fields. */
	private GridData gridLabel = new GridData(GridData.FILL_BOTH);
	
	/** the grid settings for the text fields. */
	private GridData gridText = new GridData(GridData.FILL_BOTH);
	
	/** the grid settings for a horizontal filler. */
	private GridData gridAll = new GridData(GridData.FILL_BOTH);
	
	/**
	 * create a new connect dialog.
	 * @param shell the parent shell.
	 * @param caption the caption for the dialog.
	 */
	public ConnectDialog(Shell shell, String caption) {
		super(shell);
		this.caption = caption;
	}
	
	/**
	 * sets the layout for the dialog.
	 * @param parent the parent where to set the layout.
	 */
	protected void setLayout(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		
		getGridLabel().verticalSpan = 1;
		getGridLabel().horizontalSpan = 1;
		getGridLabel().widthHint=100;
		getGridLabel().heightHint = 20;

		getGridText().verticalSpan = 1;
		getGridText().horizontalSpan = 2;
		getGridText().widthHint=200;	
		getGridText().heightHint = 20;

		getGridAll().verticalSpan = 1;
		getGridAll().horizontalSpan = 3;
		getGridAll().heightHint = 20;
		
		parent.getShell().setLayout(layout);
		parent.getShell().setText(caption);
	}
	
	/**
	 * registers listeners to the text fields with the OK button as the parent.
	 * @param btnOK the ok button to use as parent.
	 */
	protected void registerTextFieldListeners(Button btnOK) {
		// add the selection listeners.
		for (int i=0; i<getFieldsDefaultValues().length; i++) {
			Listener listener = getListener(getTxts()[i], i, btnOK);
			if (null != listener) {
				// add a listener
				getTxts()[i].addListener(SWT.Modify, listener);
			}
		}
	}
	
	/**
	 * adds the text fields.
	 * @param parent the parent where to add.
	 */
	protected void addTextFields(Composite parent) {
		setValues(new String[getFieldsDefaultValues().length]);
		setTxts(new Text[getFieldsDefaultValues().length]);
		for (int i=0; i<getFieldsLabels().length; i++) {
			Label label = new Label(parent, SWT.NONE);
			label.setText(getFieldsLabels()[i]);
			label.setLayoutData(getGridLabel());
			
			getTxts()[i] = new Text(parent, SWT.BORDER);
			getTxts()[i].setText(getFieldsDefaultValues()[i]);
			getTxts()[i].setLayoutData(getGridText());
		}
	}
	
	/**
	 * adds a Cancel button.
	 * @param parent the parent where to add.
	 */
	protected void addCancelButton(Composite parent) {		
		final Button btnCancel = new Button(parent, SWT.PUSH);
		btnCancel.setText("Cancel");
		btnCancel.setLayoutData(getGridLabel());
		btnCancel.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		    	  setReturnCode(Window.CANCEL);
		    	  close();
		      }
		    });
	}
	
	/**
	 * adds a OK button and installs the necessary listeners.
	 * @param parent the parent where to add.
	 */
	protected void addOKButton(Composite parent) {
		final Button btnOK = new Button(parent, SWT.PUSH);
		btnOK.setText("OK");
		btnOK.setLayoutData(getGridLabel());
		
		btnOK.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		    	  for (int i=0; i<getFieldsDefaultValues().length; i++) {
		    		  getValues()[i] = getTxts()[i].getText();
		    	  }
				
		    	  setReturnCode(Window.OK);
		    	  close();
		      }
		    });
		
		// add the selection listeners.
		for (int i=0; i<getFieldsDefaultValues().length; i++) {
			Listener listener = getListener(getTxts()[i], i, btnOK);
			if (null != listener) {
				// add a listener
				getTxts()[i].addListener(SWT.Modify, listener);
			}
		}
	}
	
	/**
	 * adds an invisible button to the grid to keep the alignment.
	 * @param parent the parent where to add.
	 */
	protected void addInvisibleButton(Composite parent) {
		// empty invisible button to make nice alignment
		final Button none = new Button(parent, SWT.NONE);
		none.setVisible(false);
		none.setLayoutData(getGridLabel());
	}
	
	/**
	 * Create GUI elements in the dialog.
	 */
	protected Control createContents(Composite parent) {
		
		setLayout(parent);
		
		addTextFields(parent);
		addInvisibleButton(parent);
		addOKButton(parent);
		addCancelButton(parent);

		parent.pack();	
		return parent;
	}
	
	/**
	 * this method allows the subclasses to put constraints via listeners 
	 * on the content of the value fields. you can use the offset to determine 
	 * the field.
	 * @param txt the field holding the changed text.
	 * @param offset the offset of the field. 
	 * @param ok the OK button.
	 * @return null if no constraint, otherwise the listener.
	 */
	public abstract Listener getListener(final Text txt, int offset, final Button ok);

	/**
	 * @return access to the fields labels.
	 */
	protected String [] getFieldsLabels() {
		return fieldsLabels;
	}

	/**
	 * set the new fields labels.
	 * @param fieldsLabels the new labels.
	 */
	protected void setFieldsLabels(String [] fieldsLabels) {
		this.fieldsLabels = fieldsLabels;
	}

	/**
	 * @return access to the fields default values.
	 */
	protected String [] getFieldsDefaultValues() {
		return fieldsDefaultValues;
	}

	/**
	 * set the fields new default values.
	 * @param fieldsDefaultValues the new default values.
	 */
	protected void setFieldsDefaultValues(String [] fieldsDefaultValues) {
		this.fieldsDefaultValues = fieldsDefaultValues;
	}

	/**
	 * @return handle to the values.
	 */
	protected String [] getValues() {
		return values;
	}

	/**
	 * set the new values.
	 * @param values the new values.
	 */
	protected void setValues(String [] values) {
		this.values = values;
	}

	/**
	 * @return handle to the texts.
	 */
	protected Text [] getTxts() {
		return txts;
	}

	/**
	 * sets all the texts.
	 * @param txts the new texts to be used.
	 */
	protected void setTxts(Text [] txts) {
		this.txts = txts;
	}

	/**
	 * @return access to the grid label.
	 */
	protected GridData getGridLabel() {
		return gridLabel;
	}

	/**
	 * sets the grid label.
	 * @param gridLabel the new grid label.
	 */
	protected void setGridLabel(GridData gridLabel) {
		this.gridLabel = gridLabel;
	}

	/**
	 * @return access to the grid text.
	 */
	protected GridData getGridText() {
		return gridText;
	}

	/**
	 * set a new grid text.
	 * @param gridText the new grid text.
	 */
	protected void setGridText(GridData gridText) {
		this.gridText = gridText;
	}

	/**
	 * @return access to the grid alignment.
	 */
	protected GridData getGridAll() {
		return gridAll;
	}

	/**
	 * sets the grid alignment.
	 * @param gridAll the new grid alignment to use.
	 */
	protected void setGridAll(GridData gridAll) {
		this.gridAll = gridAll;
	}
}
