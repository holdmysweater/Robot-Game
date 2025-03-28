package game.model.field.cell_objects;

import game.model.field.NormalCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.field.Cell;
import game.model.field.CellTestModel;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest {

    private static final int DEFAULT_TEST_BATTERY_CHARGE = 10;

    private Battery battery;

    @BeforeEach
    public void testSetup() {
        battery = new Battery();
        Robot robot = new Robot(battery);
    }

    @Test
    public void test_Battery_createAndGetCharge() {
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeAmountLessCharge() {
        int chargeAmount = 5;
        assertTrue(battery.releaseCharge(chargeAmount));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - chargeAmount, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeEqualsCharge() {
        int chargeAmount = DEFAULT_TEST_BATTERY_CHARGE;
        assertTrue(battery.releaseCharge(chargeAmount));
        assertEquals(0, battery.getCharge());
    }

    @Test
    public void test_canLocateAtPosition_inEmptyCell() {
        NormalCell cellWithPowerSupply = new NormalCell();

        boolean result = battery.canLocateAtPosition(cellWithPowerSupply);

        assertTrue(result);
    }

    @Test
    public void test_canLocateAtPosition_inCellWithBattery() {
        Battery anotherBattery = new Battery();
        NormalCell cellWithPowerSupply = new NormalCell();
        cellWithPowerSupply.setSmallObject(anotherBattery);

        boolean result = battery.canLocateAtPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_alreadyHavePosition() {
        NormalCell cellWithPowerSupply = new NormalCell();
        cellWithPowerSupply.setSmallObject(battery);

        boolean result = battery.canLocateAtPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_inNotCellWithPowerSupply() {
        Cell cell = new CellTestModel();

        boolean result = battery.canLocateAtPosition(cell);

        assertFalse(result);
    }

    @Test
    public void test_releaseCharge_whenChargeAmountMoreThanCharge() {
        int chargeAmount = 11;
        assertFalse(battery.releaseCharge(chargeAmount));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, battery.getCharge());
    }
}