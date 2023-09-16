import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class centrifuge_decontamination extends JFrame {
    private JTextField samplesFolderField;
    private JTextField contaminantsFolderField;
    private JTextField metadataFileField;
    private JComboBox<String> taxLevelComboBox;
    private JTextArea outputTextArea;

    private static final String AUTHOR_INFO = "Author: Dr. Andrew Tedder\nUniversity of Bradford";

    // Define the tax level options
    private String[] taxLevelOptions = {"total", "genus", "species"};

    public centrifuge_decontamination() {
        setTitle("centrifuge_decontamination");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        JPanel inputPanel = new JPanel(new GridLayout(6, 3));

        JButton browseSamplesButton = new JButton("Browse");
        browseSamplesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseSamples();
            }
        });

        JButton browseContaminantsButton = new JButton("Browse");
        browseContaminantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseContaminants();
            }
        });

        JButton browseMetadataButton = new JButton("Browse");
        browseMetadataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseMetadata();
            }
        });

        samplesFolderField = new JTextField();
        contaminantsFolderField = new JTextField();
        metadataFileField = new JTextField();

        taxLevelComboBox = new JComboBox<>(taxLevelOptions);

        JButton decontaminateButton = new JButton("Decontaminate");
        decontaminateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decontaminate();
            }
        });

        JButton startAgainButton = new JButton("Start Again");
        startAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton authorInfoButton = new JButton("Author Info");
        authorInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAuthorInfo();
            }
        });

        inputPanel.add(new JLabel("Samples Folder:"));
        inputPanel.add(samplesFolderField);
        inputPanel.add(browseSamplesButton);
        inputPanel.add(new JLabel("Contaminants Folder:"));
        inputPanel.add(contaminantsFolderField);
        inputPanel.add(browseContaminantsButton);
        inputPanel.add(new JLabel("Metadata File:"));
        inputPanel.add(metadataFileField);
        inputPanel.add(browseMetadataButton);
        inputPanel.add(new JLabel("Tax Level:"));
        inputPanel.add(taxLevelComboBox);
        inputPanel.add(decontaminateButton);
        inputPanel.add(startAgainButton);
        inputPanel.add(exitButton);
        inputPanel.add(authorInfoButton);

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void browseSamples() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            samplesFolderField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void browseContaminants() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            contaminantsFolderField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void browseMetadata() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            metadataFileField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void decontaminate() {
        String samplesFolder = samplesFolderField.getText();
        String contaminantsFolder = contaminantsFolderField.getText();
        String metadataFile = metadataFileField.getText();
        String taxLevel = (String) taxLevelComboBox.getSelectedItem();

        // Construct the command to execute the Python script
        String pythonScript = "python";  // Assuming python is in the system PATH
        String scriptPath = "centrifuge_env_decontam.py";  // Adjust this to your script's path
        String[] command = {pythonScript, scriptPath, samplesFolder, contaminantsFolder, metadataFile, taxLevel};

        try {
            // Execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Display the output in the outputTextArea
            outputTextArea.setText(output.toString());

            // Display the location of the output files
            String outputLocation = samplesFolder + File.separator + taxLevel + "_env_decontam_centrifugeReport.txt";
            outputTextArea.append("\nOutput files location: " + outputLocation);

        } catch (IOException e) {
            e.printStackTrace();
            outputTextArea.setText("Error: Failed to execute Python script.");
        }
    }

    private void showAuthorInfo() {
        JOptionPane.showMessageDialog(this, AUTHOR_INFO, "Author Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        samplesFolderField.setText("");
        contaminantsFolderField.setText("");
        metadataFileField.setText("");
        outputTextArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new centrifuge_decontamination().setVisible(true);
            }
        });
    }
}
