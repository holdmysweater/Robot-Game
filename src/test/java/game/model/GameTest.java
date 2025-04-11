package game.model;

import game.model.field.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.events.GameActionEvent;
import game.model.events.GameActionListener;
import game.model.field.core.Robot;
import game.model.labyrinths.TestLabyrinth;
import game.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    private enum Event {ROBOT_MOVED, ROBOT_TELEPORTED}

    private List<Pair<Event, Robot>> events = new ArrayList<>();
    private List<Pair<Event, Robot>> expectedEvents = new ArrayList<>();

    private class EventListener implements GameActionListener {

        @Override
        public void robotIsMoved(@NotNull GameActionEvent event) {
            events.add(new Pair<>(Event.ROBOT_MOVED, event.getRobot()));
        }

        @Override
        public void robotIsTeleported(@NotNull GameActionEvent event) {
            events.add(new Pair<>(Event.ROBOT_TELEPORTED, event.getRobot()));
        }

        @Override
        public void gameStatusChanged(@NotNull GameActionEvent event) {

        }
    }

    @BeforeEach
    public void testSetup() {
        events.clear();
        expectedEvents.clear();

        game = new Game(new TestLabyrinth());
        game.addGameActionListener(new EventListener());
    }

    @Test
    public void test_finishGame() {
        game.abort();

        assertEquals(GameStatus.GAME_ABORTED, game.getStatus());
    }

    @Test
    public void test_robotMoved_success() {
        Robot robot = game.getRobot();
        expectedEvents.add(new Pair<>(Event.ROBOT_MOVED, robot));

        game.getRobot().move(Direction.EAST);

        assertEquals(expectedEvents, events);
        assertEquals(GameStatus.GAME_IS_ON, game.getStatus());
    }

    @Test
    public void test_robotMoved_incorrectDirection() {
        Robot robot = game.getRobot();
        game.getRobot().move(Direction.WEST);

        assertEquals(robot, game.getRobot());
        assertTrue(robot.isUnfrozen());
        assertEquals(expectedEvents, events);
        assertEquals(GameStatus.GAME_IS_ON, game.getStatus());
    }

    @Test
    public void test_robotTeleported() {
        Robot robot = game.getRobot();

        game.getRobot().move(Direction.EAST);
        expectedEvents.add(new Pair<>(Event.ROBOT_MOVED, robot));

        game.getRobot().move(Direction.EAST);
        expectedEvents.add(new Pair<>(Event.ROBOT_TELEPORTED, robot));

        expectedEvents.add(new Pair<>(Event.ROBOT_MOVED, robot));

        assertEquals(expectedEvents, events);
        assertFalse(robot.isUnfrozen());
        assertEquals(GameStatus.WIN, game.getStatus());
    }

    @Test
    public void test_robotHasNoCharge() {
        Robot robot = game.getRobot();

        for(int i = 0; i < 5; i++) {
            game.getRobot().move(Direction.EAST);
            expectedEvents.add(new Pair<>(Event.ROBOT_MOVED, robot));
            game.getRobot().move(Direction.WEST);
            expectedEvents.add(new Pair<>(Event.ROBOT_MOVED, robot));
        }

        assertEquals(0,  robot.getCharge());
        assertFalse(robot.isUnfrozen());
        assertEquals(GameStatus.LOSS, game.getStatus());
    }

    @Test
    public void test_winnerFound() {
        Robot robot = game.getRobot();

        game.getRobot().move(Direction.EAST);
        expectedEvents.add(new Pair<>(Event.ROBOT_MOVED, robot));

        game.getRobot().move(Direction.EAST);
        expectedEvents.add(new Pair<>(Event.ROBOT_TELEPORTED, robot));

        expectedEvents.add(new Pair<>(Event.ROBOT_MOVED, robot));

        assertEquals(expectedEvents, events);
        assertFalse(robot.isUnfrozen());
        assertEquals(GameStatus.WIN, game.getStatus());
    }
}