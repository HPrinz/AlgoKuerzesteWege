package de.bht.algo.dijkstra;

import graph.Edge;
import graph.Graph;
import graph.GraphLesen;
import graph.Vertex;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Diese Klasse erstellt eine GUI.
 * 
 * @author Hala Basali
 * @author Hanna Prinz
 * @author Jan Zimmermann
 * 
 */
@SuppressWarnings("serial")
public class DijkstraGUI extends JFrame {
  private final JTextField textField;
  private final JFileChooser chooser;
  private final JTextArea textArea;
  private final JButton btnStartGraph;
  private final JButton btnClose;
  private final JButton fileChooserButton;
  private final JPanel richtungsAuswahlPanel;
  private final JPanel startClosePanel;
  private final JRadioButton rdbtnGerichtet;
  private final JRadioButton rdbtnUngerichtet;
  private final JPanel methodenAuswahlPanel;
  private final JComboBox<String> startComboBox;
  private final JLabel lblStartknoten;
  private final JPanel startEndPanel;
  private Graph<Vertex, Edge<Vertex>> graph;
  private final JLabel lblZielknoten;
  private final JComboBox<String> zielComboBox;

  /**
   * Erstellt die GUI
   * 
   */
  public DijkstraGUI() {
    setTitle("Dijkstra");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(600, 450);
    getContentPane().setLayout(new GridLayout(2, 2, 10, 10));

    chooser = new JFileChooser("3._BeispieleGewichtet");

    JPanel panel = new JPanel();
    getContentPane().add(panel);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JPanel fileChooserPanel = new JPanel();
    panel.add(fileChooserPanel);
    fileChooserPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    textField = new JTextField();
    textField.setColumns(40);
    fileChooserPanel.add(textField);

    fileChooserButton = new JButton("Datei...");
    fileChooserPanel.add(fileChooserButton);

    JPanel buttonPanel = new JPanel();
    panel.add(buttonPanel);

    textArea = new JTextArea("");
    JScrollPane textScrollPane = new JScrollPane(textArea);
    getContentPane().add(textScrollPane);
    buttonPanel.setLayout(new GridLayout(0, 1, 0, 0));

    methodenAuswahlPanel = new JPanel();
    methodenAuswahlPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Einstellungen",
        TitledBorder.LEADING, TitledBorder.TOP, null, null));
    buttonPanel.add(methodenAuswahlPanel);

    ButtonGroup methodenAuswahlGroup = new ButtonGroup();
    methodenAuswahlPanel.setLayout(new BoxLayout(methodenAuswahlPanel, BoxLayout.Y_AXIS));

    startEndPanel = new JPanel();
    methodenAuswahlPanel.add(startEndPanel);
    FlowLayout flowLayout = (FlowLayout) startEndPanel.getLayout();

    lblStartknoten = new JLabel("Startknoten");
    startEndPanel.add(lblStartknoten);

    startComboBox = new JComboBox<String>();
    startEndPanel.add(startComboBox);

    lblZielknoten = new JLabel("Zielknoten");
    startEndPanel.add(lblZielknoten);

    zielComboBox = new JComboBox<String>();
    startEndPanel.add(zielComboBox);

    richtungsAuswahlPanel = new JPanel();
    methodenAuswahlPanel.add(richtungsAuswahlPanel);
    richtungsAuswahlPanel.setBorder(null);

    rdbtnGerichtet = new JRadioButton("gerichtet", true);
    ButtonGroup richtungsAuswahlGroup = new ButtonGroup();
    richtungsAuswahlGroup.add(rdbtnGerichtet);
    richtungsAuswahlPanel.add(rdbtnGerichtet);

    rdbtnUngerichtet = new JRadioButton("ungerichtet", false);
    richtungsAuswahlGroup.add(rdbtnUngerichtet);
    richtungsAuswahlPanel.add(rdbtnUngerichtet);

    startClosePanel = new JPanel();
    panel.add(startClosePanel);

    btnStartGraph = new JButton("Starte Suche");
    startClosePanel.add(btnStartGraph);

    btnClose = new JButton("Beenden");
    startClosePanel.add(btnClose);

    initActionListeners();
  }

  /**
   * initialisiert die Buttonlistener (zur besseren Uebersicht)
   */
  private void initActionListeners() {

    // wird aufgerufen, wenn der Datei...-Button geklickt wird
    // das Textfeld wird dann mit dem Dateipfad versehen
    fileChooserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (chooser.showOpenDialog(chooser.getParent()) == chooser.APPROVE_OPTION) {
          textField.setText(chooser.getSelectedFile().toString());
        }
      }
    });

    // wird aufgerufen, wenn sich im Textfeld was aendert
    // die Comboboxeintraege werden dadurch aktualisiert
    textField.addCaretListener(new CaretListener() {

      @Override
      public void caretUpdate(CaretEvent e) {
        startComboBox.removeAllItems();
        zielComboBox.removeAllItems();

        if (textField.getText().trim().length() > 0) {
          graph = GraphLesen.FileToWeightedGraph(textField.getText(), rdbtnGerichtet.isSelected());

          zielComboBox.addItem("alle");
          for (Vertex v : graph.getVertices()) {
            startComboBox.addItem(v.toString());
            zielComboBox.addItem(v.toString());
          }

        }
      }
    });

    // wird aufgerufen, der Start-Button geklickt wird
    // der Graph wird dann erzeugt und der Weg gesucht
    btnStartGraph.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        callDijkstra();
      }
    });

    // wird aufgerufen wenn der Close-Button geklickt wird
    // das Fenster wird geschlossen und das Programm beendet
    btnClose.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        end();
      }
    });
  }

  /**
   * Diese Methode liest die Konfiguration des Benutzes (Dateiname, Startwert)
   * aus und startet den Dijkstra-Algorithmus
   */
  private void callDijkstra() {

    String fileName = textField.getText();

    if (textField.getText().trim().length() > 0) {
      graph = GraphLesen.FileToWeightedGraph(fileName, rdbtnGerichtet.isSelected());
    } else {
      return;
    }

    // Ausgabe der Informationen im Fenster
    textArea.append("DATEI: " + new File(fileName).getName() + "\n");

    // die Zahl aus der Combobox extrahieren
    int startpoint = Integer.parseInt((String) startComboBox.getSelectedItem());

    Dijkstra dijkstra = new Dijkstra(graph, startpoint);
    String dijkstraReturn = dijkstra.startDijkstra((String) zielComboBox.getSelectedItem());

    textArea.append(dijkstraReturn + "\n");
    textArea.append("ENDE \n \n");

  }

  /**
   * Schliesst das Fenster und beendet das Programm
   */
  private void end() {
    this.dispose();
  }

  /**
   * Oeffnet das JFrame
   * 
   * @param args
   *          keine
   */
  public static void main(String[] args) {
    new DijkstraGUI().setVisible(true);
  }
}
