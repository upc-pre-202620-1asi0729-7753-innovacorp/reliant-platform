
package com.innovacorp.reliant.platform;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.Controller;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.HvofSubsystem;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.*;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class F4FunctionalTests {

    private HvofSystem hvofSystem;

    @BeforeEach
    void setUp() {
        hvofSystem = new HvofSystem(
                1L,
                new OrganizationId(1L),
                new HvofSystemCode("HVOF-01"),
                "JP5000-2019-0421",
                "Praxair",
                "JP-5000",
                FuelType.KEROSENE,
                HvofSystemStatus.ACTIVE,
                List.of(),
                List.of()
        );
    }

    // SUBSYSTEM TESTS

    @Test
    void shouldRegisterSubsystemSuccessfully() {
        HvofSubsystem subsystem = hvofSystem.addSubsystem(
                SubsystemType.GAS_CONSOLE,
                "Gas console",
                "GC1",
                List.of("fuel_gas_flow", "oxygen_flow")
        );

        assertNotNull(subsystem);
        assertEquals("Gas console", subsystem.getName());
        assertEquals("GC1", subsystem.getAlias());
        assertEquals(2, hvofSystem.getSubsystems().size());
    }

    @Test
    void shouldRejectDuplicateSubsystemName() {
        hvofSystem.addSubsystem(
                SubsystemType.GAS_CONSOLE,
                "Gas console",
                "GC1",
                List.of("fuel_gas_flow")
        );

        assertThrows(IllegalStateException.class, () ->
                hvofSystem.addSubsystem(
                        SubsystemType.GAS_CONSOLE,
                        "Gas console",
                        "GC2",
                        List.of("oxygen_flow")
                )
        );
    }

    @Test
    void shouldRejectInvalidSubsystemParameter() {
        assertThrows(IllegalArgumentException.class, () ->
                hvofSystem.addSubsystem(
                        SubsystemType.GAS_CONSOLE,
                        "Gas console",
                        "GC1",
                        List.of("FuelGas")
                )
        );
    }

    @Test
    void shouldRejectInvalidSubsystemType() {
        assertThrows(IllegalArgumentException.class, () ->
                SubsystemType.fromString("INVALID_TYPE")
        );
    }

    // CONTROLLER TESTS

    @Test
    void shouldRegisterControllerSuccessfully() {
        Controller controller = hvofSystem.addController(
                1,
                DeviceType.PLC,
                "CLX-5580-001",
                "Rockwell",
                "1756-L83E",
                "1756-L83E",
                new NetworkEndpoint("192.168.10.21", 44818, 0, 0),
                null,
                null,
                List.of(Protocol.ETHERNET_IP)
        );

        assertNotNull(controller);
        assertEquals(1, controller.getControllerNumber());
        assertEquals("Rockwell", controller.getManufacturer());
        assertEquals(1, hvofSystem.getControllers().size());
    }

    @Test
    void shouldRejectDuplicateControllerNumber() {
        hvofSystem.addController(
                1,
                DeviceType.PLC,
                "CLX-5580-001",
                "Rockwell",
                "1756-L83E",
                "1756-L83E",
                new NetworkEndpoint("192.168.10.21", 44818, 0, 0),
                null,
                null,
                List.of(Protocol.ETHERNET_IP)
        );

        assertThrows(IllegalStateException.class, () ->
                hvofSystem.addController(
                        1,
                        DeviceType.PLC,
                        "CLX-5580-002",
                        "Rockwell",
                        "1756-L83E",
                        "1756-L83E",
                        new NetworkEndpoint("192.168.10.22", 44818, 0, 0),
                        null,
                        null,
                        List.of(Protocol.ETHERNET_IP)
                )
        );
    }

    @Test
    void shouldRejectInvalidControllerNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                hvofSystem.addController(
                        0,
                        DeviceType.PLC,
                        "CLX-5580-001",
                        "Rockwell",
                        "1756-L83E",
                        "1756-L83E",
                        new NetworkEndpoint("192.168.10.21", 44818, 0, 0),
                        null,
                        null,
                        List.of(Protocol.ETHERNET_IP)
                )
        );
    }
}
