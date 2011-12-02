package idletimer.ui;

import idletimer.LiveTaskTimer;
import idletimer.Task;

import java.util.Date;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AllocateIdleTimePopup extends JDialog implements
		TimeAllocationChooser {

	private static final long serialVersionUID = 1L;

	private Task originalTask;
	private double maxTimeThatCanBeAllocated;
	
	private JLabel lblIdleperiod;
	private final JTextField txtTimeToAllocate = new JTextField();
	private JTextField txtOriginaltaskdetails;
	private JTextField txtOthertaskdetails;
	private JTextField txtAssignedtotaskdetails;
	private JTextField txtOriginaltaskdetails_1;
	private JTextField txtContinuewithothertask;
	private final ButtonGroup grpKeepPortionChoice = new ButtonGroup();
	private final ButtonGroup grpAssignChoice = new ButtonGroup();
	private final ButtonGroup grpContinueChoice = new ButtonGroup();
	private JRadioButton rdbtnSomeOfIdle;
	private JRadioButton rdbtnAllOfIdle;
	private JRadioButton rdbtnNoneOfIdle;
	private JRadioButton rdbtnAssignToCurrent;
	private JRadioButton rdbtnAssignToOther;
	private JRadioButton rdbtnContWithAssignedTask;
	private JRadioButton rdbtnContWithOriginalTask;
	private JRadioButton rdbtnContWithOtherTask;

	/**
	 * Create the dialog.
	 */
	public AllocateIdleTimePopup() {
		setModal(true);
		setAlwaysOnTop(true);
		setTitle("Allocate Idle Time");
		setBounds(100, 100, 450, 426);
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		{
			Component verticalStrut = Box.createVerticalStrut(10);
			getContentPane().add(verticalStrut);
		}
		{
			JPanel idlePeriodPane = new JPanel();
			getContentPane().add(idlePeriodPane);
			idlePeriodPane.setLayout(new GridLayout(0, 1, 0, 0));
			{
				lblIdleperiod = new JLabel("idlePeriod");
				lblIdleperiod.setFont(new Font("Tahoma", Font.PLAIN, 17));
				lblIdleperiod.setHorizontalAlignment(SwingConstants.CENTER);
				idlePeriodPane.add(lblIdleperiod);
			}
		}
		{
			Component verticalStrut = Box.createVerticalStrut(10);
			getContentPane().add(verticalStrut);
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 0, 305, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				JLabel lblKeep = new JLabel("Keep:");
				lblKeep.setVerticalAlignment(SwingConstants.BOTTOM);
				lblKeep.setHorizontalAlignment(SwingConstants.LEFT);
				GridBagConstraints gbc_lblKeep = new GridBagConstraints();
				gbc_lblKeep.anchor = GridBagConstraints.WEST;
				gbc_lblKeep.insets = new Insets(0, 15, 0, 5);
				gbc_lblKeep.gridx = 0;
				gbc_lblKeep.gridy = 0;
				panel.add(lblKeep, gbc_lblKeep);
			}
			{
				rdbtnAllOfIdle = new JRadioButton(
						"All of idle period");
				grpKeepPortionChoice.add(rdbtnAllOfIdle);
				GridBagConstraints gbc_rdbtnAllOfIdle = new GridBagConstraints();
				gbc_rdbtnAllOfIdle.anchor = GridBagConstraints.WEST;
				gbc_rdbtnAllOfIdle.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnAllOfIdle.gridx = 0;
				gbc_rdbtnAllOfIdle.gridy = 1;
				panel.add(rdbtnAllOfIdle, gbc_rdbtnAllOfIdle);
			}
			{
				rdbtnSomeOfIdle = new JRadioButton("Some of it:");
				grpKeepPortionChoice.add(rdbtnSomeOfIdle);
				GridBagConstraints gbc_rdbtnSomeOfIdle = new GridBagConstraints();
				gbc_rdbtnSomeOfIdle.anchor = GridBagConstraints.WEST;
				gbc_rdbtnSomeOfIdle.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnSomeOfIdle.gridx = 0;
				gbc_rdbtnSomeOfIdle.gridy = 2;
				panel.add(rdbtnSomeOfIdle, gbc_rdbtnSomeOfIdle);
			}
			GridBagConstraints gbc_txtTimeToAllocate = new GridBagConstraints();
			gbc_txtTimeToAllocate.insets = new Insets(0, 0, 0, 15);
			gbc_txtTimeToAllocate.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTimeToAllocate.gridx = 1;
			gbc_txtTimeToAllocate.gridy = 2;
			panel.add(txtTimeToAllocate, gbc_txtTimeToAllocate);
			txtTimeToAllocate.setColumns(20);
			{
				rdbtnNoneOfIdle = new JRadioButton("None of it");
				rdbtnNoneOfIdle.setSelected(true);
				grpKeepPortionChoice.add(rdbtnNoneOfIdle);
				GridBagConstraints gbc_rdbtnNoneOfIdle = new GridBagConstraints();
				gbc_rdbtnNoneOfIdle.anchor = GridBagConstraints.WEST;
				gbc_rdbtnNoneOfIdle.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnNoneOfIdle.gridx = 0;
				gbc_rdbtnNoneOfIdle.gridy = 3;
				panel.add(rdbtnNoneOfIdle, gbc_rdbtnNoneOfIdle);
			}
			{
				JLabel lblAssignTo = new JLabel("Assign To Task:");
				GridBagConstraints gbc_lblAssignTo = new GridBagConstraints();
				gbc_lblAssignTo.anchor = GridBagConstraints.WEST;
				gbc_lblAssignTo.insets = new Insets(0, 15, 0, 5);
				gbc_lblAssignTo.gridx = 0;
				gbc_lblAssignTo.gridy = 5;
				panel.add(lblAssignTo, gbc_lblAssignTo);
			}
			{
				rdbtnAssignToCurrent = new JRadioButton("Current:");
				rdbtnAssignToCurrent.setSelected(true);
				grpAssignChoice.add(rdbtnAssignToCurrent);
				GridBagConstraints gbc_rdbtnAssignToCurrent = new GridBagConstraints();
				gbc_rdbtnAssignToCurrent.anchor = GridBagConstraints.WEST;
				gbc_rdbtnAssignToCurrent.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnAssignToCurrent.gridx = 0;
				gbc_rdbtnAssignToCurrent.gridy = 6;
				panel.add(rdbtnAssignToCurrent, gbc_rdbtnAssignToCurrent);
			}
			{
				txtOriginaltaskdetails = new JTextField();
				txtOriginaltaskdetails.setEditable(false);
				txtOriginaltaskdetails
						.setHorizontalAlignment(SwingConstants.TRAILING);
				txtOriginaltaskdetails.setText("OriginalTaskDetails");
				GridBagConstraints gbc_txtOriginaltaskdetails = new GridBagConstraints();
				gbc_txtOriginaltaskdetails.insets = new Insets(0, 0, 0, 15);
				gbc_txtOriginaltaskdetails.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtOriginaltaskdetails.gridx = 1;
				gbc_txtOriginaltaskdetails.gridy = 6;
				panel.add(txtOriginaltaskdetails, gbc_txtOriginaltaskdetails);
				txtOriginaltaskdetails.setColumns(10);
			}
			{
				rdbtnAssignToOther = new JRadioButton("Other:");
				rdbtnAssignToOther.setEnabled(false);
				grpAssignChoice.add(rdbtnAssignToOther);
				GridBagConstraints gbc_rdbtnAssignToOther = new GridBagConstraints();
				gbc_rdbtnAssignToOther.anchor = GridBagConstraints.WEST;
				gbc_rdbtnAssignToOther.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnAssignToOther.gridx = 0;
				gbc_rdbtnAssignToOther.gridy = 7;
				panel.add(rdbtnAssignToOther, gbc_rdbtnAssignToOther);
			}
			{
				txtOthertaskdetails = new JTextField();
				txtOthertaskdetails.setEditable(false);
				txtOthertaskdetails
						.setHorizontalAlignment(SwingConstants.TRAILING);
				txtOthertaskdetails.setText("Click to select");
				GridBagConstraints gbc_txtOthertaskdetails = new GridBagConstraints();
				gbc_txtOthertaskdetails.insets = new Insets(0, 0, 0, 15);
				gbc_txtOthertaskdetails.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtOthertaskdetails.gridx = 1;
				gbc_txtOthertaskdetails.gridy = 7;
				panel.add(txtOthertaskdetails, gbc_txtOthertaskdetails);
				txtOthertaskdetails.setColumns(10);
			}
			{
				JLabel lblContinueTimingWith = new JLabel(
						"Continue timing with task:");
				GridBagConstraints gbc_lblContinueTimingWith = new GridBagConstraints();
				gbc_lblContinueTimingWith.anchor = GridBagConstraints.WEST;
				gbc_lblContinueTimingWith.insets = new Insets(0, 15, 0, 5);
				gbc_lblContinueTimingWith.gridx = 0;
				gbc_lblContinueTimingWith.gridy = 9;
				panel.add(lblContinueTimingWith, gbc_lblContinueTimingWith);
			}
			{
				rdbtnContWithAssignedTask = new JRadioButton(
						"Task time was assigned to:");
				rdbtnContWithAssignedTask.setSelected(true);
				grpContinueChoice.add(rdbtnContWithAssignedTask);
				GridBagConstraints gbc_rdbtnContWithAssignedTask = new GridBagConstraints();
				gbc_rdbtnContWithAssignedTask.anchor = GridBagConstraints.WEST;
				gbc_rdbtnContWithAssignedTask.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnContWithAssignedTask.gridx = 0;
				gbc_rdbtnContWithAssignedTask.gridy = 10;
				panel.add(rdbtnContWithAssignedTask, gbc_rdbtnContWithAssignedTask);
			}
			{
				txtAssignedtotaskdetails = new JTextField();
				txtAssignedtotaskdetails.setEditable(false);
				txtAssignedtotaskdetails
						.setHorizontalAlignment(SwingConstants.TRAILING);
				txtAssignedtotaskdetails.setText("AssignedToTaskDetails");
				GridBagConstraints gbc_txtAssignedtotaskdetails = new GridBagConstraints();
				gbc_txtAssignedtotaskdetails.insets = new Insets(0, 0, 0, 15);
				gbc_txtAssignedtotaskdetails.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtAssignedtotaskdetails.gridx = 1;
				gbc_txtAssignedtotaskdetails.gridy = 10;
				panel.add(txtAssignedtotaskdetails,
						gbc_txtAssignedtotaskdetails);
				txtAssignedtotaskdetails.setColumns(10);
			}
			{
				rdbtnContWithOriginalTask = new JRadioButton(
						"Original task:");
				grpContinueChoice.add(rdbtnContWithOriginalTask);
				GridBagConstraints gbc_rdbtnContWithOriginalTask = new GridBagConstraints();
				gbc_rdbtnContWithOriginalTask.anchor = GridBagConstraints.WEST;
				gbc_rdbtnContWithOriginalTask.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnContWithOriginalTask.gridx = 0;
				gbc_rdbtnContWithOriginalTask.gridy = 11;
				panel.add(rdbtnContWithOriginalTask, gbc_rdbtnContWithOriginalTask);
			}
			{
				txtOriginaltaskdetails_1 = new JTextField();
				txtOriginaltaskdetails_1.setEditable(false);
				txtOriginaltaskdetails_1
						.setHorizontalAlignment(SwingConstants.TRAILING);
				txtOriginaltaskdetails_1.setText("OriginalTaskDetails");
				GridBagConstraints gbc_txtOriginaltaskdetails_1 = new GridBagConstraints();
				gbc_txtOriginaltaskdetails_1.insets = new Insets(0, 0, 0, 15);
				gbc_txtOriginaltaskdetails_1.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtOriginaltaskdetails_1.gridx = 1;
				gbc_txtOriginaltaskdetails_1.gridy = 11;
				panel.add(txtOriginaltaskdetails_1,
						gbc_txtOriginaltaskdetails_1);
				txtOriginaltaskdetails_1.setColumns(10);
			}
			{
				rdbtnContWithOtherTask = new JRadioButton("Other:");
				rdbtnContWithOtherTask.setEnabled(false);
				grpContinueChoice.add(rdbtnContWithOtherTask);
				GridBagConstraints gbc_rdbtnContWithOtherTask = new GridBagConstraints();
				gbc_rdbtnContWithOtherTask.anchor = GridBagConstraints.WEST;
				gbc_rdbtnContWithOtherTask.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnContWithOtherTask.gridx = 0;
				gbc_rdbtnContWithOtherTask.gridy = 12;
				panel.add(rdbtnContWithOtherTask, gbc_rdbtnContWithOtherTask);
			}
			{
				txtContinuewithothertask = new JTextField();
				txtContinuewithothertask.setEditable(false);
				txtContinuewithothertask
						.setHorizontalAlignment(SwingConstants.TRAILING);
				txtContinuewithothertask.setText("Click to select");
				GridBagConstraints gbc_txtContinuewithothertask = new GridBagConstraints();
				gbc_txtContinuewithothertask.insets = new Insets(0, 0, 0, 15);
				gbc_txtContinuewithothertask.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtContinuewithothertask.gridx = 1;
				gbc_txtContinuewithothertask.gridy = 12;
				panel.add(txtContinuewithothertask,
						gbc_txtContinuewithothertask);
				txtContinuewithothertask.setColumns(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane);
			buttonPane.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JButton btnCommit = new JButton("Commit");
				btnCommit.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						AllocateIdleTimePopup.this.CommitClicked();
					}
				});
				buttonPane.add(btnCommit);
			}
			{
				JButton btnIgnore = new JButton("Ignore Idle Period");
				buttonPane.add(btnIgnore);
			}
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	protected void CommitClicked() {
		// Hide the dialogue
		this.setVisible(false);
	}

	@Override
	public void RequestUsersChoice(Task currentTask, Date idleStartTime,
			Date idleEndTime) {
		
		// Setup the text to display the current info
		SetIdleperiodText(idleStartTime, idleEndTime);
		SetOriginalTask(currentTask);
		
		// Get the duration of idle time in seconds
		this.maxTimeThatCanBeAllocated = ((double)(idleEndTime.getTime() - idleStartTime.getTime())) / 1000.0;
		
		do {
			// Display it to the user
			this.setVisible(true);

			// Check the user input for correctness, if not display again
		} while (!AllInputsAreValid());	
		
		// Finished with it now
		this.dispose();
	}

	private boolean AllInputsAreValid() {
		
		// Check the user time portion
		if (rdbtnSomeOfIdle.isSelected()) {
			// Check its a number
			if (txtTimeToAllocate.getText().matches("(\\d{1,2})(\\:(\\d{1,2})){0,2}")) {
				double timeToAllocate = GetPartialAmountOfTimeToAllocate();

				// Check that it doesn't exceed the limit allowed
				if (timeToAllocate > this.maxTimeThatCanBeAllocated || timeToAllocate < 0.0) {
					return false;
				}

			} else {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public double GetAmountOfTimeToAllocate() {
		// Check the radio buttons for the source
		ButtonModel selectedPortionChoice = grpKeepPortionChoice.getSelection();
		
		if (selectedPortionChoice == rdbtnAllOfIdle) {
			return maxTimeThatCanBeAllocated;
		} else if (selectedPortionChoice == rdbtnSomeOfIdle) {
			return GetPartialAmountOfTimeToAllocate();
		} else if (selectedPortionChoice == rdbtnNoneOfIdle) {
			return 0.0;
		} else {
			// TODO Log the event (should never happen since defaults are set)
			// Default to returning all of it
			return maxTimeThatCanBeAllocated;
		}
	}
	
	protected double GetPartialAmountOfTimeToAllocate() {
		String timeValues[] = txtTimeToAllocate.getText().split(":");
		
		// Convert to longs
		int mins = (timeValues.length > 0) ? new Integer(timeValues[0])
				: 0;
		int hours = (timeValues.length > 1) ? new Integer(timeValues[1])
				: 0;
		int days = (timeValues.length > 2) ? new Integer(timeValues[2])
				: 0;

		// Accumulate to a local total (seconds)
		double timeToAllocate = 0;
		timeToAllocate += mins * 60;
		timeToAllocate += hours * 60 * 60;
		timeToAllocate += days * 24 * 60 * 60;
		return timeToAllocate;
	}

	@Override
	public Task GetTaskToAllocateTimeTo() {
		// Only support original task currently
		return originalTask;
	}

	@Override
	public Task GetTaskToContinueTimingWith() {
		// Check the radio buttons for the source
		ButtonModel selectedContinueChoice = grpContinueChoice.getSelection();
		
		if (selectedContinueChoice == rdbtnContWithAssignedTask) {
			return GetTaskToAllocateTimeTo();
		} else if (selectedContinueChoice == rdbtnContWithOriginalTask) {
			return originalTask;
		} else {
			// TODO Log (should never happen as we set defaults)
			// Default to original task
			return originalTask;
		}
	}

	protected void SetIdleperiodText(Date idleStartTime, Date idleEndTime) {
		// Form the string
		String labelText = idleStartTime + " to " + idleEndTime;
		
		// Set the GUI text
		lblIdleperiod.setText(labelText);
	}
	
	protected void SetOriginalTask(Task originalTask) {
		this.originalTask = originalTask;
		
		// Update the text on screen
		String originalTaskDetails = originalTask.GetName() + " Total time: " + originalTask.GetTotalTime();
		txtOriginaltaskdetails.setText(originalTaskDetails);
		txtOriginaltaskdetails_1.setText(originalTaskDetails);
	}
	
	/**
	 * @param args
	 *            Command line args
	 */
	public static void main(String[] args) {

		// Create a popup
		AllocateIdleTimePopup ui = new AllocateIdleTimePopup();
		
		// Create a task and idle period
		Task defaultTask = new Task("Default", 0.0);
		Date start = new Date();
		Date end = new Date(start.getTime() + (1000*60*5));
		
		// Show the task to the user
		ui.RequestUsersChoice(defaultTask, start, end);
		
		// Print out the results
		System.out.println("Keep: " + ui.GetAmountOfTimeToAllocate());
		System.out.println("Assign to: " + ui.GetTaskToAllocateTimeTo());
		System.out.println("Continue with: " + ui.GetTaskToContinueTimingWith());
		
	}
}
