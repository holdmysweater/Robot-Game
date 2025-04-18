package game;

import game.model.Game;
import game.model.GameStatus;
import game.model.events.GameActionEvent;
import game.model.events.GameActionListener;
import game.model.labyrinths.SmallLabyrinth;
import game.ui.FieldWidget;
import game.ui.WidgetFactory;
import game.ui.cell.RobotWidget;
import game.ui.utils.GameWidgetsUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GamePanel::new);
    }

    static class GamePanel extends JFrame {

        private Game game;
        private WidgetFactory widgetFactory;
        private int SHOW_MESSAGE_DELAY = 10;

        public GamePanel() throws HeadlessException {
            setVisible(true);
            startGame();
            setResizable(false);

            JMenuBar menuBar = new JMenuBar();
            menuBar.add(createGameMenu());
            setJMenuBar(menuBar);

            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        private JMenu createGameMenu() {
            JMenu gameMenu = new JMenu("Игра");
            JMenuItem newGameMenuItem = new JMenuItem(new NewGameAction());
            JMenuItem exitMenuItem = new JMenuItem(new ExitAction());
            gameMenu.add(newGameMenuItem);
            gameMenu.add(exitMenuItem);
            return gameMenu;
        }

        private void startGame() {
            widgetFactory = new WidgetFactory();
            game = new Game(new SmallLabyrinth());

            game.addGameActionListener(new GameController());

            JPanel content = (JPanel) this.getContentPane();
            content.removeAll();
            content.add(new FieldWidget(game.getGameField(), widgetFactory));
            widgetFactory.getWidget(game.getRobot()).requestFocus();

            pack();
        }

        private class NewGameAction extends AbstractAction {

            public NewGameAction() {
                putValue(NAME, "Новая");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(GamePanel.this,
                        "Начать новую игру?", "Новая игра", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) startGame();
            }
        }

        private static class ExitAction extends AbstractAction {

            public ExitAction() {
                putValue(NAME, "Выход");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }

        private final class GameController implements GameActionListener {

            @Override
            public void robotIsMoved(@NotNull GameActionEvent event) {

            }

            @Override
            public void robotIsTeleported(@NotNull GameActionEvent event) {

            }

            @Override
            public void gameStatusChanged(@NotNull GameActionEvent event) {
                GameStatus status = event.getStatus();
                if (status != GameStatus.GAME_IS_ON) {
                    String message = "";
                    switch (status) {
                        case WIN:
                            message = "Выиграл робот: " + GameWidgetsUtils.colorName(
                                    ((RobotWidget) widgetFactory.getWidget(game.getRobot())).getColor()
                            );
                            break;
                        case GAME_ABORTED:
                            message = "Игра завершена досрочно";
                            break;
                        case LOSS:
                            message = "Робот имеет нулевой заряд";
                            break;
                    }

                    String finalMessage = message;
                    Timer timer = new Timer(SHOW_MESSAGE_DELAY, e -> showMessage(finalMessage));
                    timer.setRepeats(false);
                    timer.start();
                }
            }

            /**
             * Отображает диалогово окно.
             *
             * @param message - сообщение.
             */
            private void showMessage(String message) {
                String[] options = {"ok"};
                JOptionPane.showOptionDialog(
                        GamePanel.this,
                        message,
                        "Игра окончена",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
            }
        }
    }
}