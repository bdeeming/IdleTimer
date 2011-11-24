package idletimer.ui;

import idletimer.Task;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AllocateIdleTimePopup extends JDialog implements TimeAllocationChooser {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AllocateIdleTimePopup() {
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("Allocate Idle Time");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
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
