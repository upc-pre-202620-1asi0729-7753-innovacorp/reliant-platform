package com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import static io.github.encryptorcode.pluralize.Pluralize.pluralize;

public class SnakeCasePhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment context) { return toSnakeCase(identifier); }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment context) { return toSnakeCase(identifier); }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment context) { return toSnakeCase(toPlural(identifier)); }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment context) { return toSnakeCase(identifier); }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment context) { return toSnakeCase(identifier); }

    private Identifier toSnakeCase(Identifier identifier) {
        if (identifier == null) return null;
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
        return Identifier.toIdentifier(newName);
    }

    private Identifier toPlural(Identifier identifier) {
        final String newName = pluralize(identifier.getText());
        return Identifier.toIdentifier(newName);
    }
}