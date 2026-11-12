package com.innovacorp.reliant.platform.equipment.domain.model.entities;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.DeviceType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.NetworkEndpoint;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.Protocol;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Controller {
    private Long id;
    private final int controllerNumber;
    private final DeviceType deviceType;
    private final String serialNumber;
    private final String manufacturer;
    private final String model;
    private final String partNumber;
    private final NetworkEndpoint endpoint;
    private final String endpointUrl;
    private final String tagCatalogId;
    private final List<Protocol> supportedProtocols;

    public Controller(int controllerNumber, DeviceType deviceType, String serialNumber, String manufacturer, String model, String partNumber,
                      NetworkEndpoint endpoint, String endpointUrl, String tagCatalogId, List<Protocol> supportedProtocols) {
        if (controllerNumber <= 0) throw new IllegalArgumentException("Controller number must be positive");
        if (serialNumber == null || serialNumber.isBlank()) throw new IllegalArgumentException("Controller serial number must not be blank");
        if (manufacturer == null || manufacturer.isBlank()) throw new IllegalArgumentException("Controller manufacturer must not be blank");
        if (model == null || model.isBlank()) throw new IllegalArgumentException("Controller model must not be blank");
        this.controllerNumber = controllerNumber;
        this.deviceType = Objects.requireNonNull(deviceType, "deviceType must not be null");
        this.serialNumber = serialNumber.trim();
        this.manufacturer = manufacturer.trim();
        this.model = model.trim();
        this.partNumber = partNumber == null ? "" : partNumber.trim();
        this.endpoint = Objects.requireNonNull(endpoint, "endpoint must not be null");
        this.endpointUrl = endpointUrl;
        this.tagCatalogId = tagCatalogId;
        this.supportedProtocols = (supportedProtocols == null ? List.<Protocol>of() : supportedProtocols).stream().distinct().toList();
    }

    public static Controller reconstitute(Long id, int controllerNumber, DeviceType deviceType, String serialNumber, String manufacturer, String model,
                                          String partNumber, NetworkEndpoint endpoint, String endpointUrl, String tagCatalogId, List<Protocol> supportedProtocols) {
        var controller = new Controller(controllerNumber, deviceType, serialNumber, manufacturer, model, partNumber, endpoint, endpointUrl, tagCatalogId, supportedProtocols);
        controller.id = id;
        return controller;
    }
}