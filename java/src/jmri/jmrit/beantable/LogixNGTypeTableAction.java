package jmri.jmrit.beantable;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

import jmri.InstanceManager;
import jmri.Manager;
import jmri.util.FileUtil;
import jmri.util.JmriJFrame;


import jmri.jmrit.logixng.Type;
import jmri.jmrit.logixng.TypeManager;
import jmri.jmrit.logixng.tools.swing.AbstractLogixNGEditor;
import jmri.jmrit.logixng.tools.swing.TableEditor;

/**
 * Swing action to create and register a LogixNG Type.
 <p>
 * Most of the text used in this GUI is in BeanTableBundle.properties, accessed
 * via Bundle.getMessage().
 * <p>
 *
 * @author Dave Duchamp Copyright (C) 2007 (LogixTableAction)
 * @author Pete Cressman Copyright (C) 2009, 2010, 2011 (LogixTableAction)
 * @author Matthew Harris copyright (c) 2009 (LogixTableAction)
 * @author Dave Sand copyright (c) 2017 (LogixTableAction)
 * @author Daniel Bergqvist copyright (c) 2019
 * @author Dave Sand copyright (c) 2021
 * @author Daniel Bergqvist copyright (c) 2021
 */
public class LogixNGTypeTableAction extends AbstractLogixNGTableAction<Type> {

    JRadioButton _typeExistingType = new JRadioButton(Type.TypeEnum.ExistingClass.toString());
    JRadioButton _typeScript = new JRadioButton(Type.TypeEnum.Script.toString());
    ButtonGroup _buttonGroup = new ButtonGroup();
    JTextField _csvFileName = new JTextField(50);

    /**
     * Create a LogixNGTableAction instance.
     *
     * @param s the Action title, not the title of the resulting frame. Perhaps
     *          this should be changed?
     */
    public LogixNGTypeTableAction(String s) {
        super(s);
    }

    /**
     * Create a LogixNGTableAction instance with default title.
     */
    public LogixNGTypeTableAction() {
        this(Bundle.getMessage("TitleLogixNGTypeTable"));
    }

    @Override
    protected void setTitle() {
        f.setTitle(Bundle.getMessage("TitleLogixNGTypeTable"));
    }

    @Override
    public String getClassDescription() {
        return Bundle.getMessage("TitleLogixNGTypeTable");        // NOI18N
    }

    @Override
    protected AbstractLogixNGEditor<Type> getEditor(BeanTableFrame<Type> f, BeanTableDataModel<Type> m, String sName) {
        throw new UnsupportedOperationException("Not implemented yet");
//        return new TableEditor(m, sName);
    }

    @Override
    protected Manager<Type> getManager() {
        return InstanceManager.getDefault(TypeManager.class);
    }

    @Override
    protected void enableAll(boolean enable) {
        // Not used by the types table
    }

    @Override
    protected void setEnabled(Type bean, boolean enable) {
        // Not used by the types table
    }

    @Override
    protected boolean isEnabled(Type bean) {
        return true;
    }

    @Override
    protected Type createBean(String userName) {
        String systemName = InstanceManager.getDefault(TypeManager.class).getAutoSystemName();
        return createBean(systemName, userName);
    }

    @Override
    protected Type createBean(String systemName, String userName) {
        throw new UnsupportedOperationException("Not implemented yet");
/*        
        if (_typeExistingType.isSelected()) {
            String fileName = _csvFileName.getText();
            if (fileName == null || fileName.isEmpty()) {
                JOptionPane.showMessageDialog(addLogixNGFrame,
                        Bundle.getMessage("LogixNGError2"), Bundle.getMessage("ErrorTitle"), // NOI18N
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return InstanceManager.getDefault(TypeManager.class)
                    .newCSVTable(systemName, userName, fileName);
        } else if (_typeScript.isSelected()) {
            // Open table editor
        } else {
            log.error("No table type selected");
            throw new RuntimeException("No table type selected");
        }

//        InstanceManager.getDefault(TypeManager.class).loadTableFromCSV(file, systemName, userName);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
*/        
    }

    @Override
    protected void deleteBean(Type bean) {
        InstanceManager.getDefault(TypeManager.class).deleteType(bean);
    }

    @Override
    protected boolean browseMonoSpace() {
        return true;
    }

    @Override
    protected String getBeanText(Type bean) {
        return "Not implemented yet";
/*        
        int maxColumnWidth = 0;
        int columnWidth[] = new int[bean.numColumns()];
        String[][] cells = new String[bean.numRows()][];
        for (int row=0; row < bean.numRows(); row++) {
            cells[row] = new String[bean.numColumns()];
            for (int col=0; col < bean.numColumns(); col++) {
                Object value = bean.getCell(row, col);
                cells[row][col] = value != null ? value.toString() : "<null>";
                columnWidth[col] = Math.max(columnWidth[col], cells[row][col].length());
                maxColumnWidth = Math.max(maxColumnWidth, columnWidth[col]);
            }
        }
        StringBuilder columnLine = new StringBuilder();
        while (columnLine.length()+2 < maxColumnWidth) {
            columnLine.append("----------------------");
        }
        String columnPadding = String.format("%"+Integer.toString(maxColumnWidth)+"s", "");
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        for (int col=0; col < bean.numColumns(); col++) {
            sb.append(columnLine.substring(0,columnWidth[col]+2));
            sb.append("+");
            if (col+1 == bean.numColumns()) sb.append(String.format("%n"));
        }
        for (int row=0; row < bean.numRows(); row++) {
            sb.append("|");
            for (int col=0; col < bean.numColumns(); col++) {
                sb.append(" ");
                sb.append((cells[row][col]+columnPadding).substring(0,columnWidth[col]));
                sb.append(" |");
                if (col+1 == bean.numColumns()) sb.append(String.format("%n"));
            }
            sb.append("+");
            for (int col=0; col < bean.numColumns(); col++) {
                sb.append(columnLine.substring(0,columnWidth[col]+2));
                sb.append("+");
                if (col+1 == bean.numColumns()) sb.append(String.format("%n"));
            }
        }
        return sb.toString();
*/        
    }

    @Override
    protected String getAddTitleKey() {
        return "TitleLogixNGTypeTable";
    }

    @Override
    protected String getCreateButtonHintKey() {
        return "LogixNGTableCreateButtonHint";
    }

    @Override
    protected String helpTarget() {
        return "package.jmri.jmrit.beantable.LogixNGTypeTable";  // NOI18N
    }

    private JButton createFileChooser() {
        JButton selectFileButton = new JButton("..."); // "File" replaced by ...
        selectFileButton.setMaximumSize(selectFileButton.getPreferredSize());
        selectFileButton.setToolTipText(Bundle.getMessage("LogixNG_FileButtonHint"));  // NOI18N
        selectFileButton.addActionListener((ActionEvent e) -> {
            JFileChooser csvFileChooser = new JFileChooser(FileUtil.getUserFilesPath());
            csvFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv", "txt")); // NOI18N
            csvFileChooser.rescanCurrentDirectory();
            int retVal = csvFileChooser.showOpenDialog(null);
            // handle selection or cancel
            if (retVal == JFileChooser.APPROVE_OPTION) {
                // set selected file location
                try {
                    _csvFileName.setText(FileUtil.getPortableFilename(csvFileChooser.getSelectedFile().getCanonicalPath()));
                } catch (java.io.IOException ex) {
                    log.error("exception setting file location: {}", ex);  // NOI18N
                    _csvFileName.setText("");
                }
            }
        });
        return selectFileButton;
    }

    /**
     * Create or copy bean frame.
     *
     * @param titleId   property key to fetch as title of the frame (using Bundle)
     * @param startMessageId part 1 of property key to fetch as user instruction on
     *                  pane, either 1 or 2 is added to form the whole key
     * @return the button JPanel
     */
    @Override
    protected JPanel makeAddFrame(String titleId, String startMessageId) {
        addLogixNGFrame = new JmriJFrame(Bundle.getMessage(titleId));
        addLogixNGFrame.addHelpMenu(
                "package.jmri.jmrit.beantable.LogixNGTypeTable", true);     // NOI18N
        addLogixNGFrame.setLocation(50, 30);
        Container contentPane = addLogixNGFrame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel p;
        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridheight = 1;

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;

        p.add(_sysNameLabel, c);
        _sysNameLabel.setLabelFor(_systemName);
        c.gridy = 1;
        p.add(_userNameLabel, c);
        _userNameLabel.setLabelFor(_addUserName);
        c.gridy = 2;
        p.add(new JLabel(Bundle.getMessage("LogixNG_CsvFileName")), c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;  // text field will expand
        p.add(_systemName, c);
        c.gridy = 1;
        p.add(_addUserName, c);

        c.gridy = 2;
        createFileChooser();
        p.add(createFileChooser(), c);

        c.gridx = 2;        // make room for file selector
        c.gridwidth = GridBagConstraints.REMAINDER;
        p.add(_csvFileName, c);

        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;  // text field will expand
        c.gridy = 0;
        p.add(_autoSystemName, c);


        _buttonGroup.add(_typeExistingType);
        _buttonGroup.add(_typeScript);

        _addUserName.setToolTipText(Bundle.getMessage("LogixNGUserNameHint"));    // NOI18N
        _systemName.setToolTipText(Bundle.getMessage("LogixNGSystemNameHint"));   // NOI18N
        contentPane.add(p);

        JPanel panel98 = new JPanel();
        panel98.setLayout(new FlowLayout());
        JPanel panel99 = new JPanel();
        panel99.setLayout(new BoxLayout(panel99, BoxLayout.Y_AXIS));
        panel99.add(_typeExistingType, c);
        panel99.add(_typeScript, c);
        panel98.add(panel99);
        contentPane.add(panel98);

        // set up message
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        JPanel panel31 = new JPanel();
        panel31.setLayout(new FlowLayout());
        JLabel message1 = new JLabel(Bundle.getMessage(startMessageId + "LogixNGTableMessage1"));  // NOI18N
        panel31.add(message1);
        JPanel panel32 = new JPanel();
        JLabel message2 = new JLabel(Bundle.getMessage(startMessageId + "LogixNGTableMessage2"));  // NOI18N
        panel32.add(message2);
        panel3.add(panel31);
        panel3.add(panel32);
        contentPane.add(panel3);

        // set up create and cancel buttons
        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout());
        // Cancel
        JButton cancel = new JButton(Bundle.getMessage("ButtonCancel"));    // NOI18N
        panel5.add(cancel);
        cancel.addActionListener(this::cancelAddPressed);
        cancel.setToolTipText(Bundle.getMessage("CancelLogixNGButtonHint"));      // NOI18N

        addLogixNGFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelAddPressed(null);
            }
        });
        contentPane.add(panel5);

        _autoSystemName.addItemListener((ItemEvent e) -> {
            autoSystemName();
        });
        return panel5;
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogixNGTypeTableAction.class);

}
