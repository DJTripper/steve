package de.rwth.idsg.steve.utils;

import de.rwth.idsg.steve.repository.dto.TransactionDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransactionStopServiceHelperTest {

    @Test
    public void testFloatingStringToIntString() {
        String actual = TransactionStopServiceHelper.floatingStringToIntString("11.01");

        Assertions.assertEquals("12", actual);
    }

    @Test
    public void testFloatingStringToIntString2() {
        String actual = TransactionStopServiceHelper.floatingStringToIntString("234.678");

        Assertions.assertEquals("235", actual);
    }

    @Test
    public void testKWhStringToWhString() {
        String actual = TransactionStopServiceHelper.kWhStringToWhString("12");

        Assertions.assertEquals("12000.0", actual);
    }

    @Test
    public void testIsEnergy_empty() {
        var value = TransactionDetails.MeterValues.builder()
            .build();

        Assertions.assertFalse(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_onlyValue() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22")
            .build();

        Assertions.assertTrue(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_onlyValueDecimal() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22.5")
            .build();

        Assertions.assertTrue(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_signedData() {
        var value = TransactionDetails.MeterValues.builder()
            .value("some gibberish that is not an energy value")
            .format("SignedData")
            .build();

        Assertions.assertFalse(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_notEnergyUnit() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22")
            .format("Raw")
            .unit("Celsius")
            .build();

        Assertions.assertFalse(TransactionStopServiceHelper.isEnergyValue(value));
    }


    @Test
    public void testIsEnergy_notActiveImportMeasurand() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22")
            .format("Raw")
            .unit("Wh")
            .measurand("Current.Export")
            .build();

        Assertions.assertFalse(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_nullFormat() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22")
            .format(null)
            .unit("Wh")
            .measurand("Energy.Active.Import.Register")
            .build();

        Assertions.assertTrue(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_rawFormat() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22")
            .format("Raw")
            .unit("Wh")
            .measurand("Energy.Active.Import.Register")
            .build();

        Assertions.assertTrue(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_kWhUnit() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22")
            .format("Raw")
            .unit("kWh")
            .measurand("Energy.Active.Import.Register")
            .build();

        Assertions.assertTrue(TransactionStopServiceHelper.isEnergyValue(value));
    }

    @Test
    public void testIsEnergy_nonNumericValue() {
        var value = TransactionDetails.MeterValues.builder()
            .value("22a819()b")
            .format("Raw")
            .unit("Wh")
            .measurand("Energy.Active.Import.Register")
            .build();

        Assertions.assertFalse(TransactionStopServiceHelper.isEnergyValue(value));
    }
}
