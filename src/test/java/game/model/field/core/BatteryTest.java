package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertTrue(battery.drainCharge(chargeAmount));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - chargeAmount, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeEqualsCharge() {
        int chargeAmount = DEFAULT_TEST_BATTERY_CHARGE;
        assertTrue(battery.drainCharge(chargeAmount));
        assertEquals(0, battery.getCharge());
    }

    @Test
    public void test_canLocateAtPosition_isConnected() {
        NormalCell cellWithPowerSupply = new NormalCell();

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_disconnected() {
        NormalCell cellWithPowerSupply = new NormalCell();
        Battery battery = new Battery();

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertTrue(result);
    }


    @Test
    public void test_canLocateAtPosition_inCellWithBattery() {
        Battery anotherBattery = new Battery();
        NormalCell cellWithPowerSupply = new NormalCell();
        cellWithPowerSupply.setSmallObject(anotherBattery);

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_alreadyHavePosition() {
        NormalCell cellWithPowerSupply = new NormalCell();
        cellWithPowerSupply.setSmallObject(battery);

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_inNotCellWithPowerSupply() {
        AbstractCell cell = new NormalCell();

        boolean result = battery.canSetPosition(cell);

        assertFalse(result);
    }

    @Test
    public void test_releaseCharge_whenChargeAmountMoreThanCharge() {
        int chargeAmount = 11;
        assertFalse(battery.drainCharge(chargeAmount));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, battery.getCharge());
    }
}