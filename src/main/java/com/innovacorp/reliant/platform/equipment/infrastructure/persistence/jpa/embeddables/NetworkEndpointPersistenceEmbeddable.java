package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NetworkEndpointPersistenceEmbeddable {

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "port", nullable = false)
    private int port;

    @Column(name = "rack", nullable = false)
    private int rack;

    @Column(name = "slot", nullable = false)
    private int slot;
}
