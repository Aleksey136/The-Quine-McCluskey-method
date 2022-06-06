package MainProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainForm extends JFrame{
    private JPanel outputTable;
    private JPanel inputValues;
    private JPanel tableOfValues;
    private JTextField inputHexadecimal;
    private JTextField inputBinary;
    private JTextArea inputTableText;
    private JTextArea tableOfValuesText;
    private JTextField outputAnswer;
    private JLabel outputTextAnswer;
    private JLabel outputRequest;
    private JPanel rootPanel;
    private JButton buttonStart;
    private JScrollPane inputTableTextScroll;

    private JRadioButton flagHexadecimal;
    private JRadioButton flagBinary;

    private String valueInput;
    private final Map<Integer, ArrayList<String>> tableTruthValue = new HashMap<Integer, ArrayList<String>>();
    private final ArrayList<String> tableOfValuesWithOne = new ArrayList<>();

    public MainForm(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);                                     // ctrl+o
        setSize(700,600);
        setContentPane(rootPanel);
        setVisible(true);
        rootPanel.addComponentListener(new ComponentAdapter() {
        });

        ButtonGroup choiceButton = new ButtonGroup();
        choiceButton.add(flagHexadecimal);
        choiceButton.add(flagBinary);
        inputHexadecimal.setText("7e8bd7b8");

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTableText.setText("");

                if (flagHexadecimal.isSelected()){
                    valueInput = Long.toBinaryString(Long.parseLong(inputHexadecimal.getText(), 16));
                    for (int i = 0; i<valueInput.length()%4; i++)
                        valueInput = "0" + valueInput;
                }
                else{
                    valueInput = Integer.toBinaryString(Integer.parseInt(inputBinary.getText(), 2));
                }

                int valueDegreeOfTwo = CheckingForTheDegreeOfTwo(valueInput);
                int valueQuantityBinaryDegreeOfTwo = Integer.toBinaryString(valueDegreeOfTwo-1).length();

                outputAnswer.setText(String.valueOf(valueDegreeOfTwo));
                int quantityMissingZeros = valueDegreeOfTwo - valueInput.length();
                for (int i = 0; i<quantityMissingZeros; i++)
                    valueInput = valueInput + "0";

                for (int i = 0; i<valueDegreeOfTwo;i++){
                    String inputNumber = "";
                    for (int j = 0; j<valueQuantityBinaryDegreeOfTwo-Integer.toBinaryString(i).length();j++){
                        inputNumber += "0";
                    }
                    inputNumber+=Integer.toBinaryString(i);
                    ArrayList<String> forEx = new ArrayList<String>();
                    forEx.add(inputNumber);
                    forEx.add("" + valueInput.charAt(i));
                    tableTruthValue.put(i, new ArrayList<>(forEx));
                }
                TableOutputFunction(valueInput);

                for (int i = 0; i<valueDegreeOfTwo;i++) {
                    if (tableTruthValue.get(i).get(1).equals("1"))
                        tableOfValuesWithOne.add(tableTruthValue.get(i).get(0));
                }

                Map<Integer, ArrayList<String>> tableOfValuesWithAnyOne = new HashMap<Integer, ArrayList<String>>();
                for (int i = 0; i <= valueQuantityBinaryDegreeOfTwo; i++){
                    ArrayList<String> tableOfValuesWithAnyOneList = new ArrayList<>();
                    ProcessingTableOfValuesWithAnyOne(tableOfValuesWithAnyOneList,i,tableOfValuesWithOne);
                    tableOfValuesWithAnyOne.put(i,tableOfValuesWithAnyOneList);
                    tableOfValuesText.append(String.valueOf(tableOfValuesWithAnyOne.get(i))+"\n");
                }

                Map<Integer, ArrayList<String>> tableForTest = new HashMap<Integer, ArrayList<String>>(tableTruthValue);
                for (int i = 0; i<valueDegreeOfTwo;i++) {                                                               //???
                    tableForTest.get(i).set(1, "0");
                }

                Map<Integer, ArrayList<String>> tableOfValuesIntersectionsInAny = new HashMap<Integer, ArrayList<String>>();
                for (int i = 0; i < valueQuantityBinaryDegreeOfTwo; i++){
                    ArrayList<String> tableOfValuesIntersectionsInAnyList = new ArrayList<>();
                    ProcessingTableOfValuesIntersectionsInAny(tableOfValuesIntersectionsInAnyList, tableForTest,
                            tableOfValuesWithAnyOne.get(i), tableOfValuesWithAnyOne.get(i+1));
                    tableOfValuesIntersectionsInAny.put(i,tableOfValuesIntersectionsInAnyList);
                    tableOfValuesText.append(String.valueOf(tableOfValuesIntersectionsInAny.get(i))+"\n");
                }
                ProcessingRepeatTOVIIA(tableOfValuesIntersectionsInAny, tableForTest);
                tableOfValuesText.append("\n");
                for (int i = 0; i < valueQuantityBinaryDegreeOfTwo; i++) {
                    tableOfValuesText.append(String.valueOf(tableOfValuesIntersectionsInAny.get(i)) + "\n");
                }
            }
        });
    }

    public void ProcessingRepeatTOVIIA(Map<Integer, ArrayList<String>> tableOfValuesIntersectionsInAny,
                                       Map<Integer, ArrayList<String>> tableForTest){
        for (int i = 0; i < tableOfValuesIntersectionsInAny.size()-1; i++){
            ArrayList<String> tableOfValuesPart1 = new ArrayList<>(tableOfValuesIntersectionsInAny.get(i));
            ArrayList<String> tableOfValuesPart2 = new ArrayList<>(tableOfValuesIntersectionsInAny.get(i+1));
            ArrayList<String> tableOfValuesOutput = new ArrayList<>();
            ProcessingTableOfValuesIntersectionsInAny(tableOfValuesOutput,tableForTest,tableOfValuesPart1,tableOfValuesPart2);
            tableOfValuesIntersectionsInAny.put(i,tableOfValuesOutput);
        }
    }

    public void ProcessingTableOfValuesIntersectionsInAny(ArrayList<String> tableOfValuesIntersectionsInAny,
                                                          Map<Integer, ArrayList<String>> tableForTest,
                                                          ArrayList<String> tableOfValuesWithPart1One,
                                                          ArrayList<String> tableOfValuesWithPart2One) {

        for (int i = 0; i < tableOfValuesWithPart1One.size(); i++) {
            int quantityIntersections = 0;
            int placeIntersections = 0;
            int place1 = -1;
            for (int l = 0; l < tableForTest.size(); l++)
                if (tableForTest.get(l).get(0).equals(tableOfValuesWithPart1One.get(i)))
                    place1 = l;
            for (int j = 0; j < tableOfValuesWithPart2One.size(); j++) {
                for (int k = 0; k < tableOfValuesWithPart1One.get(i).length(); k++) {
                    if (tableOfValuesWithPart1One.get(i).charAt(k) != tableOfValuesWithPart2One.get(j).charAt(k)) {
                        quantityIntersections++;
                        placeIntersections = k;
                    }
                }
                if (quantityIntersections == 1) {
                    String inputText = "";
                    for (int l = 0; l < tableOfValuesWithPart1One.get(i).length(); l++)
                        if (l == placeIntersections)
                            inputText += "*";
                        else inputText += tableOfValuesWithPart1One.get(i).charAt(l);
                    tableOfValuesIntersectionsInAny.add(inputText);
                    for (int l = 0; l < tableForTest.size(); l++)
                        if (tableForTest.get(l).get(0).equals(tableOfValuesWithPart1One.get(i)))
                            tableForTest.get(l).set(1, "1");
                    for (int l = 0; l < tableForTest.size(); l++)
                        if (tableForTest.get(l).get(0).equals(tableOfValuesWithPart2One.get(j)))
                            tableForTest.get(l).set(1, "1");
                    quantityIntersections = 0;
                    placeIntersections = 0;
                }
            }
            if (tableForTest.get(place1).get(1).equals("0")) {
                tableOfValuesIntersectionsInAny.add(tableOfValuesWithPart1One.get(i));
            }
        }
    }

    public void ProcessingTableOfValuesWithAnyOne(ArrayList<String> tableOfValuesWithAnyOne,
                                                  int anyOne, ArrayList<String> tableOfValuesWithOne){
        for (int i=0; i<tableOfValuesWithOne.size();i++){
            int quantityOne = 0;
            for (int j=0; j<tableOfValuesWithOne.get(i).length();j++){
                if (tableOfValuesWithOne.get(i).charAt(j) == '1')
                    quantityOne++;
            }
            if (quantityOne == anyOne)
                tableOfValuesWithAnyOne.add(tableOfValuesWithOne.get(i));
        }
    }

    public void TableOutputFunction(String valueInput) {
        inputTableText.setText("");
        int valueDegreeOfTwo = CheckingForTheDegreeOfTwo(valueInput);
        int valueQuantityBinaryDegreeOfTwo = Integer.toBinaryString(valueDegreeOfTwo-1).length();
        inputTableText.append("");
        for (int i = valueQuantityBinaryDegreeOfTwo; i>0; i--){
            inputTableText.append("x"+ i + " ");
        }
        inputTableText.append("F\n");
        for (int i = valueQuantityBinaryDegreeOfTwo; i>0; i--) {
            inputTableText.append("----");
        }
        inputTableText.append("\n");
        for (int i = 0; i < CheckingForTheDegreeOfTwo(valueInput); i++){
            inputTableText.append(tableTruthValue.get(i).get(0) + " " + tableTruthValue.get(i).get(1) + "\n");
        }
    }
    public int CheckingForTheDegreeOfTwo(String valueInput){
        int valueDegreeOfTwo = 1;
        while (valueDegreeOfTwo < Long.toBinaryString(Long.parseLong(valueInput, 2)).length()) {
            valueDegreeOfTwo*=2;
        }
        return valueDegreeOfTwo;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainForm();
            }
        });
    }
}
