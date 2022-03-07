package life;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameOfLife extends JFrame {

    Controller controller;
    JLabel generationLabel;
    JLabel aliveLabel;
    JToggleButton playToggleButton;
    JSlider slider;
    State state;
    final int borderWidth = 5;
    private ImageIcon startIcon;
    private ImageIcon pauseIcon;
    private ImageIcon resetIcon;
    private final int buttonSize = 25;

    public GameOfLife() {
        super("Game of life");
        SwingUtilities.invokeLater(this::createGUI);
        controller = new Controller(this);
        controller.newGame();
    }

    public void startUpdate(State state) {
        this.state = state;
        SwingUtilities.invokeLater(this::updateField);
    }

    private void createGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        prepareIcons();
        createHeader();
        createBoard();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateField() {
        generationLabel.setText("Generation #" + state.getGeneration());
        aliveLabel.setText("Alive: " + state.getAliveCount());
        repaint();
    }

    private void createHeader() {
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        generationLabel = new JLabel("Generation: 1");
        generationLabel.setName("GenerationLabel");
        aliveLabel = new JLabel("Alive: 1");
        aliveLabel.setName("AliveLabel");

        statsPanel.add(generationLabel);
        statsPanel.add(aliveLabel);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));


        playToggleButton = new JToggleButton(pauseIcon);
        playToggleButton.setName("PlayToggleButton");
        playToggleButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        playToggleButton.addActionListener(this::handleStartStopButton);

        JButton resetButton = new JButton(resetIcon);
        resetButton.setName("ResetButton");
        resetButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        resetButton.addActionListener(this::handleResetGameButton);

        toolBarPanel.add(playToggleButton);
        toolBarPanel.add(resetButton);

        slider = new JSlider(1, 50);
        slider.addChangeListener(this::handleSizeSliderChange);
        toolBarPanel.add(slider);


        topPanel.add(statsPanel);
        topPanel.add(toolBarPanel);
        add(topPanel, BorderLayout.NORTH);
    }

    private void createBoard() {
        BoardPanel boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);
    }

    private void handleStartStopButton(ActionEvent e) {
        playToggleButton.setIcon(playToggleButton.getIcon() == startIcon ? pauseIcon : startIcon);
        controller.startStop();
    }

    private void handleResetGameButton(ActionEvent e) {
        controller.resetGame();
    }

    private void handleSizeSliderChange(ChangeEvent e) {
        controller.setSize(slider.getValue());
    }

    private void prepareIcons() {
        ImageIcon icon = new ImageIcon("./start.png");
        startIcon = new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));

        icon = new ImageIcon("./pause.png");
        pauseIcon = new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));

        icon = new ImageIcon("./reset.png");
        resetIcon = new ImageIcon(icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
    }

    private class BoardPanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (state == null) {
                return;
            }

            boolean[][] world = state.getCurrent();
            int size = state.getSize();
            int smallerSide = Math.min(getWidth(), getHeight()) - borderWidth * 2;
            int cellSize = smallerSide / size;
            int horOffset = (getWidth() - smallerSide) / 2;
            int verOffset = (getHeight() - smallerSide) / 2;


            for (int i = 0; i <= size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i < size && world[i][j]) {
                        g.fillRect(horOffset + cellSize * j,
                                verOffset + cellSize * i,
                                cellSize,
                                cellSize);
                    }
                }
                g.drawLine(horOffset + cellSize * i,
                        verOffset,
                        horOffset + cellSize * i,
                        verOffset + cellSize * size);
                g.drawLine(horOffset,
                        verOffset + cellSize * i,
                        horOffset + cellSize * size,
                        verOffset + cellSize * i);
            }

        }
    }
}

