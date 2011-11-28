package idletimer.ui;

import idletimer.Task;

import java.util.Date;

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

public class AllocateIdleTimePopup extends JDialog implements TimeAllocationChooser {

	private static final long serialVersionUID = 1L;
	private final JTextField textField = new JTextField();
	private JTextField txtOriginaltaskdetails;
	private JTextField txtOthertaskdetails;
	private final ButtonGroup grpKeepPortionChoice = new ButtonGroup();
	private final ButtonGroup grpAssignChoice = new ButtonGroup();
	private JTextField txtAssignedtotaskdetails;
	private JTextField txtOriginaltaskdetails_1;
	private JTextField txtContinuewithothertask;
	private final ButtonGroup grpContinueChoice = new ButtonGroup();

	/**
	 * Create the dialog.
	 */
	public AllocateIdleTimePopup() {
		setAlwaysOnTop(true);
		setTitle("Allocate Idle Time");
		setBounds(100, 100, 450, 426);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		{
			Component verticalStrut = Box.createVerticalStrut(10);
			getContentPane().add(verticalStrut);
		}
		{
			JPanel idlePeriodPane = new JPanel();
			getContentPane().add(idlePeriodPane);
			idlePeriodPane.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JLabel lblIdleperiod = new JLabel("idlePeriod");
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
			gbl_panel.columnWidths = new int[]{0, 305, 0};
			gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
				JRadioButton rdbtnAllOfIdle = new JRadioButton("All of idle period");
				grpKeepPortionChoice.add(rdbtnAllOfIdle);
				GridBagConstraints gbc_rdbtnAllOfIdle = new GridBagConstraints();
				gbc_rdbtnAllOfIdle.anchor = GridBagConstraints.WEST;
				gbc_rdbtnAllOfIdle.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnAllOfIdle.gridx = 0;
				gbc_rdbtnAllOfIdle.gridy = 1;
				panel.add(rdbtnAllOfIdle, gbc_rdbtnAllOfIdle);
			}
			{
				JRadioButton rdbtnSomeOfIt = new JRadioButton("Some of it:");
				grpKeepPortionChoice.add(rdbtnSomeOfIt);
				GridBagConstraints gbc_rdbtnSomeOfIt = new GridBagConstraints();
				gbc_rdbtnSomeOfIt.anchor = GridBagConstraints.WEST;
				gbc_rdbtnSomeOfIt.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnSomeOfIt.gridx = 0;
				gbc_rdbtnSomeOfIt.gridy = 2;
				panel.add(rdbtnSomeOfIt, gbc_rdbtnSomeOfIt);
			}
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 0, 15);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 2;
			panel.add(textField, gbc_textField);
			textField.setColumns(20);
			{
				JRadioButton rdbtnNoneOfIt = new JRadioButton("None of it");
				grpKeepPortionChoice.add(rdbtnNoneOfIt);
				GridBagConstraints gbc_rdbtnNoneOfIt = new GridBagConstraints();
				gbc_rdbtnNoneOfIt.anchor = GridBagConstraints.WEST;
				gbc_rdbtnNoneOfIt.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnNoneOfIt.gridx = 0;
				gbc_rdbtnNoneOfIt.gridy = 3;
				panel.add(rdbtnNoneOfIt, gbc_rdbtnNoneOfIt);
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
				JRadioButton rdbtnCurrent = new JRadioButton("Current:");
				grpAssignChoice.add(rdbtnCurrent);
				GridBagConstraints gbc_rdbtnCurrent = new GridBagConstraints();
				gbc_rdbtnCurrent.anchor = GridBagConstraints.WEST;
				gbc_rdbtnCurrent.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnCurrent.gridx = 0;
				gbc_rdbtnCurrent.gridy = 6;
				panel.add(rdbtnCurrent, gbc_rdbtnCurrent);
			}
			{
				txtOriginaltaskdetails = new JTextField();
				txtOriginaltaskdetails.setEditable(false);
				txtOriginaltaskdetails.setHorizontalAlignment(SwingConstants.TRAILING);
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
				JRadioButton rdbtnOther = new JRadioButton("Other:");
				grpAssignChoice.add(rdbtnOther);
				GridBagConstraints gbc_rdbtnOther = new GridBagConstraints();
				gbc_rdbtnOther.anchor = GridBagConstraints.WEST;
				gbc_rdbtnOther.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnOther.gridx = 0;
				gbc_rdbtnOther.gridy = 7;
				panel.add(rdbtnOther, gbc_rdbtnOther);
			}
			{
				txtOthertaskdetails = new JTextField();
				txtOthertaskdetails.setEditable(false);
				txtOthertaskdetails.setHorizontalAlignment(SwingConstants.TRAILING);
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
				JLabel lblContinueTimingWith = new JLabel("Continue timing with task:");
				GridBagConstraints gbc_lblContinueTimingWith = new GridBagConstraints();
				gbc_lblContinueTimingWith.anchor = GridBagConstraints.WEST;
				gbc_lblContinueTimingWith.insets = new Insets(0, 15, 0, 5);
				gbc_lblContinueTimingWith.gridx = 0;
				gbc_lblContinueTimingWith.gridy = 9;
				panel.add(lblContinueTimingWith, gbc_lblContinueTimingWith);
			}
			{
				JRadioButton rdbtnTaskTimeWas = new JRadioButton("Task time was assigned to:");
				grpContinueChoice.add(rdbtnTaskTimeWas);
				GridBagConstraints gbc_rdbtnTaskTimeWas = new GridBagConstraints();
				gbc_rdbtnTaskTimeWas.anchor = GridBagConstraints.WEST;
				gbc_rdbtnTaskTimeWas.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnTaskTimeWas.gridx = 0;
				gbc_rdbtnTaskTimeWas.gridy = 10;
				panel.add(rdbtnTaskTimeWas, gbc_rdbtnTaskTimeWas);
			}
			{
				txtAssignedtotaskdetails = new JTextField();
				txtAssignedtotaskdetails.setEditable(false);
				txtAssignedtotaskdetails.setHorizontalAlignment(SwingConstants.TRAILING);
				txtAssignedtotaskdetails.setText("AssignedToTaskDetails");
				GridBagConstraints gbc_txtAssignedtotaskdetails = new GridBagConstraints();
				gbc_txtAssignedtotaskdetails.insets = new Insets(0, 0, 0, 15);
				gbc_txtAssignedtotaskdetails.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtAssignedtotaskdetails.gridx = 1;
				gbc_txtAssignedtotaskdetails.gridy = 10;
				panel.add(txtAssignedtotaskdetails, gbc_txtAssignedtotaskdetails);
				txtAssignedtotaskdetails.setColumns(10);
			}
			{
				JRadioButton rdbtnOriginalTask = new JRadioButton("Original task:");
				grpContinueChoice.add(rdbtnOriginalTask);
				GridBagConstraints gbc_rdbtnOriginalTask = new GridBagConstraints();
				gbc_rdbtnOriginalTask.anchor = GridBagConstraints.WEST;
				gbc_rdbtnOriginalTask.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnOriginalTask.gridx = 0;
				gbc_rdbtnOriginalTask.gridy = 11;
				panel.add(rdbtnOriginalTask, gbc_rdbtnOriginalTask);
			}
			{
				txtOriginaltaskdetails_1 = new JTextField();
				txtOriginaltaskdetails_1.setEditable(false);
				txtOriginaltaskdetails_1.setHorizontalAlignment(SwingConstants.TRAILING);
				txtOriginaltaskdetails_1.setText("OriginalTaskDetails");
				GridBagConstraints gbc_txtOriginaltaskdetails_1 = new GridBagConstraints();
				gbc_txtOriginaltaskdetails_1.insets = new Insets(0, 0, 0, 15);
				gbc_txtOriginaltaskdetails_1.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtOriginaltaskdetails_1.gridx = 1;
				gbc_txtOriginaltaskdetails_1.gridy = 11;
				panel.add(txtOriginaltaskdetails_1, gbc_txtOriginaltaskdetails_1);
				txtOriginaltaskdetails_1.setColumns(10);
			}
			{
				JRadioButton rdbtnOther_1 = new JRadioButton("Other:");
				grpContinueChoice.add(rdbtnOther_1);
				GridBagConstraints gbc_rdbtnOther_1 = new GridBagConstraints();
				gbc_rdbtnOther_1.anchor = GridBagConstraints.WEST;
				gbc_rdbtnOther_1.insets = new Insets(0, 15, 0, 5);
				gbc_rdbtnOther_1.gridx = 0;
				gbc_rdbtnOther_1.gridy = 12;
				panel.add(rdbtnOther_1, gbc_rdbtnOther_1);
			}
			{
				txtContinuewithothertask = new JTextField();
				txtContinuewithothertask.setEditable(false);
				txtContinuewithothertask.setHorizontalAlignment(SwingConstants.TRAILING);
				txtContinuewithothertask.setText("Click to select");
				GridBagConstraints gbc_txtContinuewithothertask = new GridBagConstraints();
				gbc_txtContinuewithothertask.insets = new Insets(0, 0, 0, 15);
				gbc_txtContinuewithothertask.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtContinuewithothertask.gridx = 1;
				gbc_txtContinuewithothertask.gridy = 12;
				panel.add(txtContinuewithothertask, gbc_txtContinuewithothertask);
				txtContinuewithothertask.setColumns(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane);
			buttonPane.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JButton btnCommit = new JButton("Commit");
				buttonPane.add(btnCommit);
			}
			{
				JButton btnIgnore = new JButton("Ignore Idle Period");
				buttonPane.add(btnIgnore);
			}
		}
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	@Override
	public void RequestUsersChoice(Task currentTask, Date idleStartTime,
			Date idleEndTime) {
		this.setVisible(true);
	}

	@Override
	public double GetAmountOfTimeToAllocate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Task GetTaskToAllocateTimeTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task GetTaskToContinueTimingWith() {
		// TODO Auto-generated method stub
		return null;
	}

}
