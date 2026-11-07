# Architecture Decision Records (ADRs)

## ADR-0009: Entidades hijas persistidas con su propia PersistenceEntity y mapeadas por el assembler del agregado

**Estado:** Aceptado

### Contexto
El agregado HvofSystem contiene entidades hijas como HvofSubsystem y Controller.
Estas entidades tienen información propia y necesitan persistirse en tablas independientes,
pero su ciclo de vida depende del agregado HvofSystem.

### Decisión
Cada entidad hija tendrá su propia clase PersistenceEntity en infraestructura,
dentro de `infrastructure.persistence.jpa.entities`.

La relación con HvofSystem se representa mediante una relación JPA ManyToOne,
mientras que HvofSystem mantiene las colecciones de sus entidades hijas.

El assembler de persistencia del agregado será responsable de convertir
las entidades del dominio y sus entidades de persistencia.

La persistencia de las entidades hijas se realizará mediante cascada desde
el agregado HvofSystem.

### Consecuencias
- Se mantiene la separación entre dominio e infraestructura.
- Las entidades del dominio no dependen de anotaciones JPA.
- Las entidades hijas se guardan junto con el agregado.
- El agregado conserva el control de las operaciones sobre sus entidades hijas.

---

## ADR-0010: Reglas de bandas en el Value Object ParameterBands compartido vía ACL con Process Monitoring

**Estado:** Aceptado

### Contexto
Los parámetros de una receta contienen valores que representan límites de seguridad,
advertencia y operación nominal.

Process Monitoring necesita interpretar estas bandas para supervisar
los valores del proceso.

Duplicar las reglas de validación en diferentes contextos puede generar
inconsistencias.

### Decisión
Las reglas de orden y consistencia de las bandas se encapsulan en el
Value Object `ParameterBands`.

El contexto Equipment mantiene la responsabilidad sobre la definición
y validación de las bandas de los parámetros de las recetas.

Cuando Process Monitoring necesite utilizar esta información,
la comunicación entre ambos contextos se realizará mediante una
Anti-Corruption Layer (ACL).

La ACL adapta la información entre los modelos de ambos contextos,
evitando que uno dependa directamente del modelo interno del otro.

### Consecuencias
- Las reglas de las bandas se mantienen centralizadas en el Value Object.
- Se evita duplicar las reglas de validación.
- Process Monitoring utiliza la información mediante una adaptación explícita.
- Se conserva la independencia de los modelos de ambos bounded contexts.

---

## ADR-0011: Transiciones como sub-recursos (/publication) con 422 para violaciones de regla

**Estado:** Aceptado

### Contexto
Una receta tiene un ciclo de vida con estados DRAFT, ACTIVE y RETIRED.
La publicación de una receta representa una transición de estado
que debe respetar reglas de negocio.

Modificar directamente el atributo status desde un endpoint genérico
no expresa claramente la operación de negocio.

### Decisión
La publicación de una receta se expone como un sub-recurso:

`PATCH /api/v1/recipes/{id}/publication`

La operación no necesita un body: el endpoint representa explícitamente
la intención de publicar la receta.

Cuando una transición no cumple las reglas de negocio,
la API responde con HTTP 422 (Unprocessable Entity).

Los errores de validación de datos y los recursos inexistentes
mantienen sus códigos HTTP correspondientes.

### Consecuencias
- Las operaciones de negocio se expresan mediante endpoints específicos.
- Se evita modificar directamente el estado desde el cliente.
- Las violaciones de reglas de negocio se diferencian de los errores de validación.
- La API comunica claramente las transiciones permitidas.