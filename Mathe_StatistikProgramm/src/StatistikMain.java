import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import java.awt.Toolkit;

public class StatistikMain extends JFrame {

	private JPanel contentPane;
	private JTextField txtKz;
	private JTextField txtZz;
	private JTextField textField_2;
	private JTextField textField_3;
	private JFreeChart chart2;
	private ChartPanel chartPanel;
	public ArrayList<Simuliert> archivSimuliert = new ArrayList<Simuliert>();
	public ArrayList<String> simuliert = new ArrayList<String>();
	public ArrayList<String> möglichkeiten = new ArrayList<String>();
	public ArrayList<String> zuSuchen = new ArrayList<String>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StatistikMain frame = new StatistikMain();
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
	public StatistikMain() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(StatistikMain.class.getResource("/resources/MatheStatistikProgrammLogo.png")));
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int w = (int)bounds().getWidth();
				int h = (int)bounds().getHeight();
				chartPanel.setBounds(217, 30, (w - 243), (h - 80));	//Damit man das Fenster auch vergrößern kann
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 906, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Simulation");
		
		JLabel lblMglichkeiten = new JLabel("M\u00F6glichkeiten:");
		lblMglichkeiten.setBounds(10, 30, 82, 14);
		contentPane.add(lblMglichkeiten);
		
		txtKz = new JTextField();
		txtKz.setText("K,Z");
		txtKz.setToolTipText("Bitte geben Sie hier alle M\u00F6glichen Zeichen mit Kommata getrennt ein (z.B. \"K,Z\" f\u00FCr das Werfen einer M\u00FCnze).");
		txtKz.setBounds(99, 27, 86, 20);
		contentPane.add(txtKz);
		txtKz.setColumns(10);
		
		JLabel lblZuSuchen = new JLabel("Zu suchen:");
		lblZuSuchen.setToolTipText("");
		lblZuSuchen.setBounds(10, 58, 82, 14);
		contentPane.add(lblZuSuchen);
		
		txtZz = new JTextField();
		txtZz.setText("KZ");
		txtZz.setToolTipText("Geben Sie hier die Anordnung einzelner Elemente aus \"M\u00F6glichkeiten\" an, die Sie simulieren wollen (bei mehreren durch ein Komma trennen)!");
		txtZz.setColumns(10);
		txtZz.setBounds(99, 55, 86, 20);
		contentPane.add(txtZz);
		
		JButton btnSimulieren = new JButton("Simulieren!");
		btnSimulieren.setToolTipText("Startet die Simulation (neu)");
		btnSimulieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				simuliert = new ArrayList<String>();
				möglichkeiten = new ArrayList<String>();
				String[] mgl = txtKz.getText().split(",");
				for (int i = 0; i < mgl.length; i++) {	//Fügt alle Möglichkeiten einer ArrayList hinzu
					möglichkeiten.add(mgl[i]);
				}
				zuSuchen = new ArrayList<String>();
				String[] zSuch = txtZz.getText().split(",");
				for (int i = 0; i < zSuch.length; i++) {	//Fügt alle Strings, die gesucht werden sollen ein
					zuSuchen.add(zSuch[i]);
				}
				
				a = new MyThread();	//Damit kann der Thread neu gestartet werden
				if (a.isAlive()) {
					a.stop();
				}
				a.start();
			}
		});
		btnSimulieren.setBounds(10, 150, 175, 23);
		contentPane.add(btnSimulieren);
		
		JLabel lblIterationen = new JLabel("Iterationen:");
		lblIterationen.setToolTipText("");
		lblIterationen.setBounds(10, 92, 82, 14);
		contentPane.add(lblIterationen);
		
		textField_2 = new JTextField();
		textField_2.setText("1500");
		textField_2.setToolTipText("Die Anzahl, wie oft die Simulation wiederholt werden soll!");
		textField_2.setColumns(10);
		textField_2.setBounds(99, 89, 86, 20);
		contentPane.add(textField_2);
		
		JLabel lblUpdateListener = new JLabel("Listener:");
		lblUpdateListener.setToolTipText("");
		lblUpdateListener.setBounds(10, 120, 82, 14);
		contentPane.add(lblUpdateListener);
		
		chart2 = new JFreeChart(plot);
		
		chartPanel = new ChartPanel(chart2);
		chartPanel.setMouseZoomable(true);
		chartPanel.setDisplayToolTips(true);
		chartPanel.setBounds(217, 30, 663, 388);
		contentPane.add(chartPanel);
		chartPanel.setMouseWheelEnabled(true);
		
		textField_3 = new JTextField();
		textField_3.setText("10");
		textField_3.setToolTipText("Geben Sie hier den Wert ein, nach wie vielen Iterationen Sie wollen, dass der Graf geupdatet wird!");
		textField_3.setColumns(10);
		textField_3.setBounds(99, 117, 86, 20);
		contentPane.add(textField_3);
		
		btnExport = new JButton("Export");
		btnExport.setToolTipText("Exportiert alle einzelnen \"Spiele\" in einer .csv Datei (mit einigen n\u00FCtzlichen Daten).");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	//Exportiert alle Daten als .csv
				if (simuliert.size() > 0) {
					String pfad = saveAll(null) + ".csv";
					File file = new File(pfad);
					try {
						FileWriter fw = new FileWriter(file);
						
						fw.write("Ergebnisraum:");
						for (int i = 0; i < möglichkeiten.size(); i++) {
							fw.write(";" + möglichkeiten.get(i));
						}
						
						fw.write(System.lineSeparator() + "Gesuchte Folgen:");
						for (int i = 0; i < zuSuchen.size(); i++) {
							fw.write(";" + zuSuchen.get(i));
						}
						
						fw.write(System.lineSeparator() + System.lineSeparator() + "Simulationsnummer;Datenstring;Stelle;relative Häufigkeit;absolute Häufigkeit");	//TODO
						
						ArrayList<String> zuSpeichern = new ArrayList<String>();
						zuSpeichern = archivSimuliert.get(getIndexOf(Integer.parseInt(txtBuffer.getText()))).simuliert;	//Holt sich den zu exportierenden Plot
						
						for (int i = 0; i < zuSpeichern.size(); i++) {	//Schreibt alle einzelnen Ergebnisse in eine Datei
							fw.write(System.lineSeparator() + zuSpeichern.get(i));
						}
						fw.flush();
						fw.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		btnExport.setBounds(96, 270, 89, 23);
		contentPane.add(btnExport);
		
		btnStop = new JButton("STOP!");
		btnStop.setToolTipText("Stoppt den Thread, in welchem die Simulation l\u00E4uft.");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a.stop();
//				double[][] a = new double[2][2];
//				a[0][0] = 5;
//				a[1][0] = 3.5;
//				a[0][1] = 7.83;
//				a[1][1] = 1.7;
//				intialisieren("Toll",a,true);	
			}
		});
		btnStop.setBounds(96, 236, 89, 23);
		contentPane.add(btnStop);
		
		lblIteratione = new JLabel("Iterationen: -");
		lblIteratione.setToolTipText("Anzahl insgesamter Iterationen (also \"ganzer Spiele\")");
		lblIteratione.setBounds(10, 200, 175, 14);
		contentPane.add(lblIteratione);
		
		lblWahrscheinlichkeit = new JLabel("rel. H\u00E4ufigkeit: -");
		lblWahrscheinlichkeit.setToolTipText("Die insgsamte relative H\u00E4ufigkeit zum momentanen Zeitpunkt");
		lblWahrscheinlichkeit.setBounds(10, 334, 175, 14);
		contentPane.add(lblWahrscheinlichkeit);
		
		lblAbsHufigkeit = new JLabel("ges. Z\u00FCge: -");
		lblAbsHufigkeit.setToolTipText("Die Anzahl an insgesamten Z\u00FCgen w\u00E4hrend der Simulation");
		lblAbsHufigkeit.setBounds(10, 350, 175, 14);
		contentPane.add(lblAbsHufigkeit);
		
		lblSpiellnge = new JLabel("Spiell\u00E4nge: -");
		lblSpiellnge.setToolTipText("Gibt die durchschnittliche Spiell\u00E4nge an");
		lblSpiellnge.setBounds(10, 366, 175, 14);
		contentPane.add(lblSpiellnge);
		
		JLabel lblBuffer = new JLabel("Buffer:");
		lblBuffer.setBounds(10, 236, 46, 14);
		contentPane.add(lblBuffer);
		
		txtBuffer = new JTextField();
		txtBuffer.setToolTipText("Geben Sie hier eine Integerzahl als Nummerierung f\u00FCr den Plot ein.");
		txtBuffer.setText("1");
		txtBuffer.setBounds(20, 253, 46, 20);
		contentPane.add(txtBuffer);
		txtBuffer.setColumns(10);
		
		JButton btnClear = new JButton("clear");
		btnClear.setToolTipText("Der oben ausgew\u00E4hlte Plot wird gel\u00F6scht.");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dataSet.removeSeries("Plot " + txtBuffer.getText());
			}
		});
		btnClear.setBounds(10, 284, 66, 23);
		contentPane.add(btnClear);
	}
	
	XYSplineRenderer dot = new XYSplineRenderer();
	DefaultXYDataset dataSet = new DefaultXYDataset();
	NumberAxis yax = new NumberAxis("realitve Häufigkeit [%]");
	NumberAxis xax = new NumberAxis("Iterationen (ganze Spiele)");
	XYPlot plot = new XYPlot(dataSet,xax,yax,dot);
	
	private JButton btnExport;
	private MyThread a;	//Damit der Thread beliebig beendet und gestartet werden kann
	private JButton btnStop;
	private JLabel lblIteratione;
	private JLabel lblWahrscheinlichkeit;
	private JLabel lblAbsHufigkeit;
	private JLabel lblSpiellnge;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtBuffer;
	
	
	class MyThread extends java.lang.Thread {
		public void run() {
			ArrayList<String> datenReihe = new ArrayList<String>();	//Hier werden die Ergebnisse gespeichert
//			dataSet = new DefaultXYDataset();
//			datasetWindg.addSeries("Irgend so eine Statistik", datenReihe);

			int absH = 0;
			double zeichenInsg = 0;
			Simuliert temp = new Simuliert();
			temp.name = Integer.parseInt(txtBuffer.getText());
			
			for (int i = 1; i <= Integer.parseInt(textField_2.getText()); i++) {	//Geht die ganzen Iterationen durch
				
				String simulation = "";	//Hier werden dann immer zufällige angehängt
				while (zuSuchVorhanden(simulation) == -1) {	//Fügt so lange Zahlen hinzu, bis das Muster gefunde wurde
					simulation += möglichkeiten.get(zufallszahl(möglichkeiten.size()));
				}
				
//				System.out.println(i + " - " + simulation);
				zeichenInsg += simulation.length();
				absH++;
				simuliert.add(i + ";" + simulation + ";" + simulation.length() + ";" + String.valueOf((absH / zeichenInsg)).replace(".", ",") + ";" + (int)zeichenInsg);	//TODO
				
//				datenReihe.add(i + ";" + i*i);
				if (i%Integer.parseInt(textField_3.getText()) == 0) {	//Damit jedes Mal ein Update geschieht

					datenReihe.add(i + ";" + ((absH / zeichenInsg) * 100));
					lblWahrscheinlichkeit.setText("rel. Häufigkeit: " + (((int)((absH / zeichenInsg) * 10000)) / 100d) + "%");
					lblAbsHufigkeit.setText("ges. Züge: " + (int)zeichenInsg);
//					System.out.println("rel. Häufigkeit: " + (((absH / i) * 100) / 100));
					lblIteratione.setText("Iterationen: " + i);
					lblSpiellnge.setText("Spiellänge: " + ((int)((zeichenInsg/i)*100))/100d);	//TODO
//					if (checkBox.isSelected()) {
						intialisieren("Plot " + Integer.parseInt(txtBuffer.getText()), toDoubleAr(datenReihe, ";"), true);
//					} else {
//						intialisieren2("Plot 2", toDoubleAr(datenReihe, ";"), true);
//					}
				}
			}
			
			int ind = getIndexOf(temp.name);
			if (ind == -1) {
				temp.simuliert = kop(simuliert);
				archivSimuliert.add(temp);
			} else {
				temp.simuliert = kop(simuliert);
				archivSimuliert.set(ind, temp);
			}
			
		}
	}
	
	public int getIndexOf (int zz) {
		for (int i = 0; i < archivSimuliert.size(); i++) {
			if (archivSimuliert.get(i).name == zz) {
				return i;
			}
		}
		return -1;	//Fehler
	}
	   
	public int zuSuchVorhanden (String sim) {	//Sollte der String vorhanden sein, gibt der true zurück
		for (int i = 0; i < zuSuchen.size(); i++) {
			if (sim.indexOf(zuSuchen.get(i)) != -1) {
				return sim.indexOf(zuSuchen.get(i));
			}
		}
		return -1;
	}
	
	public ArrayList<String> kop (ArrayList<String> a) {
		ArrayList<String> nA = new ArrayList<String>();
		for (int i = 0; i < a.size(); i++) {
			String tt = a.get(i);
			nA.add(tt);
		}
		return nA;
	}
	
	public String saveAll (String pfad) {
		JFileChooser chooser;
		chooser = new JFileChooser(".");
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		FileNameExtensionFilter alexTab = new FileNameExtensionFilter(
				"Datenbank (.csv)", "csv");
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		chooser.setFileFilter(alexTab);
		chooser.setDialogTitle("Alle Daten als CSV-Datei speichern!");
		chooser.setVisible(true);
		int result = chooser.showSaveDialog(this);
		return chooser.getSelectedFile().toString();
	} 
	
	public int zufallszahl(int bis) {
		return (int)(Math.random()*bis);
	}
	
	public static int zufallszahl(int von, int bis) {
		return (int)(Math.random()*(bis-(von-1)) + (von)); //Zufallszahlen von inklusive "von", bis inklusive "bis"
	}
	
	public double[][] toDoubleAr (ArrayList<String> a, String trennzeichen) {
		double[][] ret = new double[2][a.size()];	
		for (int i = 0; i < a.size(); i++) {
			String[] temp = a.get(i).split(trennzeichen);
			ret[0][i] = Double.parseDouble(temp[0]);
			ret[1][i] = Double.parseDouble(temp[1]);
//			System.out.println(temp[0] + " - " + temp[1]);
		}
//		System.out.println(ret[0][0] + " - " + ret[1][0]);
		return ret;
	}
	
	public void intialisieren (String name, double[][] datenReihe, boolean hinzufügen) {	//hinzufügen (==true) fügt die Datenreihe in ein Diagramm hinzu, false lässt sie als einzelne
		if (hinzufügen == true) {
			dataSet.addSeries(name, datenReihe);
		} else {
			dataSet = new DefaultXYDataset();
			dataSet.addSeries(name, datenReihe);
		}
	}
}
