package silence;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import java.awt.Toolkit;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Insets;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JComboBox;

public class HushAlexaGUI extends JFrame {

	private static AudioFilePlayer player = new AudioFilePlayer();
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmExit;
	private JLabel lblIntervalInSeconds;
	private JTextField textField;
	private JButton btnStart;
	private JButton btnStop;
	private JLabel lblCurrentStatus_1;
	private JTextField textCurrentStatus;
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private JLabel lblSoundFile;
	private JComboBox comboBox;
	private String boxString;
	private JMenuItem mntmAbout;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HushAlexaGUI frame = new HushAlexaGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HushAlexaGUI() {
		initGUI();
	}

	private void initGUI() {
		setResizable(false);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(HushAlexaGUI.class.getResource("/silence/apple-touch-icon.png")));
		setTitle("HushAlexa");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 417, 231);

		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);

		this.mnFile = new JMenu("File");
		this.menuBar.add(this.mnFile);

		this.mntmExit = new JMenuItem("Exit");
		this.mntmExit.addActionListener(e -> do_mntmExit_actionPerformed(e));

		this.mntmAbout = new JMenuItem("About");
		this.mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_mntmAbout_actionPerformed(e);
			}
		});
		this.mnFile.add(this.mntmAbout);
		this.mnFile.add(this.mntmExit);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		this.contentPane.setLayout(gbl_contentPane);

		this.lblIntervalInSeconds = new JLabel("Interval in Seconds :");
		GridBagConstraints gbc_lblIntervalInSeconds = new GridBagConstraints();
		gbc_lblIntervalInSeconds.anchor = GridBagConstraints.EAST;
		gbc_lblIntervalInSeconds.insets = new Insets(0, 0, 5, 5);
		gbc_lblIntervalInSeconds.gridx = 0;
		gbc_lblIntervalInSeconds.gridy = 0;
		this.contentPane.add(this.lblIntervalInSeconds, gbc_lblIntervalInSeconds);

		this.textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.WEST;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		this.contentPane.add(this.textField, gbc_textField);
		this.textField.setColumns(10);

		this.btnStart = new JButton("Start");
		this.btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnStart_actionPerformed(e);
			}
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.anchor = GridBagConstraints.EAST;
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 1;
		this.contentPane.add(this.btnStart, gbc_btnStart);

		this.btnStop = new JButton("Stop");
		this.btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnStop_actionPerformed(e);
			}
		});
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.anchor = GridBagConstraints.WEST;
		gbc_btnStop.insets = new Insets(0, 0, 5, 0);
		gbc_btnStop.gridx = 1;
		gbc_btnStop.gridy = 1;
		this.contentPane.add(this.btnStop, gbc_btnStop);

		this.lblCurrentStatus_1 = new JLabel("Current Status :");
		GridBagConstraints gbc_lblCurrentStatus_1 = new GridBagConstraints();
		gbc_lblCurrentStatus_1.anchor = GridBagConstraints.EAST;
		gbc_lblCurrentStatus_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentStatus_1.gridx = 0;
		gbc_lblCurrentStatus_1.gridy = 2;
		this.contentPane.add(this.lblCurrentStatus_1, gbc_lblCurrentStatus_1);

		this.textCurrentStatus = new JTextField();
		this.textCurrentStatus.setEditable(false);
		GridBagConstraints gbc_textCurrentStatus = new GridBagConstraints();
		gbc_textCurrentStatus.anchor = GridBagConstraints.WEST;
		gbc_textCurrentStatus.insets = new Insets(0, 0, 5, 0);
		gbc_textCurrentStatus.fill = GridBagConstraints.VERTICAL;
		gbc_textCurrentStatus.gridx = 1;
		gbc_textCurrentStatus.gridy = 2;
		this.contentPane.add(this.textCurrentStatus, gbc_textCurrentStatus);
		this.textCurrentStatus.setColumns(10);
		this.textCurrentStatus.setText("Stopped");
		this.textCurrentStatus.setForeground(Color.RED);

		this.lblSoundFile = new JLabel("Sound File :");
		GridBagConstraints gbc_lblSoundFile = new GridBagConstraints();
		gbc_lblSoundFile.anchor = GridBagConstraints.EAST;
		gbc_lblSoundFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblSoundFile.gridx = 0;
		gbc_lblSoundFile.gridy = 3;
		this.contentPane.add(this.lblSoundFile, gbc_lblSoundFile);

		String[] comboStrings = { "clock.wav", "ding.mp3", "quiet.mp3" };
		this.comboBox = new JComboBox(comboStrings);
		this.comboBox.setSelectedIndex(2);
		this.boxString = comboStrings[2];
		this.comboBox.addActionListener(e -> do_comboBox_actionPerformed(e));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		this.contentPane.add(this.comboBox, gbc_comboBox);
	}

	protected void do_mntmExit_actionPerformed(ActionEvent e) {
		System.out.println("Exiting application...");
		System.exit(0);
	}

	protected void do_btnStart_actionPerformed(ActionEvent e) {
		double timeDelay = 0.0;
		boolean isValid = true;
		try {
			timeDelay = Double.parseDouble(this.textField.getText());
		} catch (Exception e2) {
			// this.consoleText.append("Please enter a valid time interval! (Recommended
			// value is 300)\n");
			JOptionPane.showInternalMessageDialog(null,
					"Please enter a Valid time interval!\n(Only numbers are allowed)", "Check Input!", 0);
			isValid = false;
		}
		if (isValid) {
			this.executorService.scheduleAtFixedRate(() -> {
				if (this.boxString == "clock.wav") {
					URL url = this.getClass().getClassLoader().getResource(this.boxString);
					try {
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
						Clip clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
						System.gc();
					} catch (UnsupportedAudioFileException | IOException e4) {
						e4.printStackTrace();
					} catch (LineUnavailableException e4) {
						e4.printStackTrace();
					}
				} else
					try {
						player.play(this.boxString);
						System.gc();
					} catch (URISyntaxException e5) {
						e5.printStackTrace();
					}
			}, 0, (long) timeDelay, TimeUnit.SECONDS);
			this.textCurrentStatus.setText("Running");
			this.textCurrentStatus.setForeground(Color.GREEN);
		}
	}

	protected void do_btnStop_actionPerformed(ActionEvent e) {
		this.executorService.shutdownNow();
		this.textCurrentStatus.setText("Stopped");
		this.textCurrentStatus.setForeground(Color.RED);
	}

	protected void do_comboBox_actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String comboString = (String) cb.getSelectedItem();
		this.boxString = comboString;
	}

	protected void do_mntmAbout_actionPerformed(ActionEvent e) {
		JOptionPane.showInternalMessageDialog(this.mntmAbout,
				"This is a simple application that plays \n some stuff in the background", "About", 1);
	}
}
