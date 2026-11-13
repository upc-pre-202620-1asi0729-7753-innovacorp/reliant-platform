# Reliant Platform — Deploy en Azure (App Service + MySQL Flexible Server)

Resultado: la API corriendo en `https://reliant-platform-<sufijo>.azurewebsites.net` con MySQL en Azure, desplegada automáticamente desde GitHub (`main`) con GitHub Actions. La app ya está preparada: el perfil `prod` lee todo de variables de entorno y Hibernate crea el esquema al arrancar.

Requisitos: cuenta Azure (Azure for Students), el repo `reliant-platform` en GitHub con `main` actualizado (release 1.0.0).

---

## 1. Grupo de recursos

1.1. https://portal.azure.com → **Resource groups** → **Create**.

| Campo | Valor |
|---|---|
| Subscription | Azure for Students |
| Resource group | `rg-reliant` |
| Region | `East US` (misma en todo) |

1.2. **Review + create** → **Create**.

---

## 2. Base de datos — Azure Database for MySQL Flexible Server

2.1. Buscador → **Azure Database for MySQL flexible servers** → **Create** → **Flexible server**.

2.2. **Basics**:

| Campo | Valor |
|---|---|
| Resource group | `rg-reliant` |
| Server name | `reliant-mysql` (si está ocupado: `reliant-mysql-ic`) |
| Region | `East US` |
| MySQL version | `8.0` |
| Workload type | **For development or hobby projects** |
| Compute + storage | **Configure server** → Burstable, **Standard_B1ms**, 20 GiB, backup 7 días → Save |
| Authentication method | MySQL authentication only |
| Admin username | `reliant_admin` |
| Password | una contraseña fuerte (anótala: paso 4) |

Si aparece la oferta gratuita de 12 meses (750 h B1ms + 32 GB), acéptala.

2.3. **Networking**:

| Campo | Valor |
|---|---|
| Connectivity method | **Public access (allowed IP addresses)** |
| Allow public access from any Azure service within Azure to this server | ✅ |
| Firewall rules | **Add current client IP address** |

2.4. **Review + create** → **Create**. Espera 5–10 min.

2.5. En el servidor → **Databases** → **Add**:

| Campo | Valor |
|---|---|
| Name | `reliant-os` |
| Character set | `utf8mb4` |
| Collation | `utf8mb4_0900_ai_ci` |

→ **Save**.

2.6. En **Overview** copia el **Server name** (`reliant-mysql.mysql.database.azure.com`). Es tu `DATABASE_URL`.

> TLS: el servidor exige SSL y la URL JDBC del perfil `prod` ya lleva `useSSL=true`. No hay que cambiar nada.

---

## 3. Web App — App Service

3.1. Buscador → **App Services** → **Create** → **Web App**.

3.2. **Basics**:

| Campo | Valor |
|---|---|
| Resource group | `rg-reliant` |
| Name | `reliant-platform` (Azure agrega un sufijo al dominio; cópialo del Overview al final) |
| Publish | **Code** |
| Runtime stack | **Java 21** |
| Java web server stack | **Java SE (Embedded Web Server)** |
| Operating System | **Linux** |
| Region | `East US` |
| Linux Plan | **Create new** → `plan-reliant` |
| Pricing plan | **Free F1** (demo) o **Basic B1** |

3.3. **Deployment**: deja **GitHub Actions: Disable** por ahora.

3.4. **Review + create** → **Create**.

---

## 4. Variables de entorno de la Web App

4.1. Web App `reliant-platform` → **Settings → Environment variables** → **App settings** → **Add** una por una:

| Name | Value |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DATABASE_URL` | `reliant-mysql.mysql.database.azure.com` (paso 2.6) |
| `DATABASE_PORT` | `3306` |
| `DATABASE_NAME` | `reliant-os` |
| `DATABASE_USER` | `reliant_admin` |
| `DATABASE_PASSWORD` | la contraseña del paso 2.2 |
| `JWT_SECRET` | cadena aleatoria de **mínimo 32 caracteres** (PowerShell: `-join ((48..57)+(65..90)+(97..122) \| Get-Random -Count 48 \| % {[char]$_})`) |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:4200` por ahora; luego la URL del webapp en Azure, p. ej. `https://reliant-webapp-xxxx.azurewebsites.net` (sin `/` final). Varias se separan con coma |
| `PORT` | `8080` |
| `WEBSITES_PORT` | `8080` |

4.2. **Apply** → **Confirm**.

---

## 5. Deploy automático desde GitHub

5.1. Web App → **Deployment → Deployment Center**.

| Campo | Valor |
|---|---|
| Source | **GitHub** |
| Organization | `upc-pre-202620-1asi0729-7753-innovacorp` |
| Repository | `reliant-platform` |
| Branch | `main` |
| Authentication type | **User-assigned identity** (o Basic authentication si está habilitada) |

5.2. **Save**. Azure crea `.github/workflows/main_reliant-platform.yml` con un commit en `main` (build con Maven y Java 21, deploy del `.jar`).

5.3. Si el workflow falla en el build por la versión de Java, edita el yml y deja `java-version: '21'` y `distribution: 'microsoft'`; el proyecto compila con `--release 21`.

5.4. GitHub → **Actions**: espera el workflow en verde (4–6 min).

5.5. Trae el commit del workflow a tu PC y a `develop`:

```powershell
cd C:\Users\LENOVO\Documents\UPC\UPC-202602\OpenSource\reliant-platform
git checkout main
git pull origin main
git checkout develop
git merge --no-ff -m "Merge branch 'main' into develop" main
git push origin develop
```

---

## 6. Verificar

6.1. `https://<tu-dominio>.azurewebsites.net/swagger-ui.html` → debe cargar Swagger (en F1 el primer arranque tarda 1–2 min).

6.2. Web App → **Monitoring → Log stream**: debe aparecer `Hibernate: create table ...` (20+ tablas), `Tomcat started on port 8080` y `Roles seeding verification finished`.

6.3. Prueba:

```powershell
curl -X POST https://<tu-dominio>.azurewebsites.net/api/v1/users -H "Content-Type: application/json" -d "{\"organizationName\":\"Fesa\",\"ruc\":\"20100027292\",\"organizationType\":\"RECUPERATION_SUPPLIER\",\"fullName\":\"Carolina Navarro\",\"email\":\"admin@fesa.pe\",\"password\":\"Reliant2026!\"}"
```

Respuesta esperada: `201 Created`. Luego `GET /api/v1/roles` → 7 roles.

---

## 7. Conectar el webapp

7.1. En `reliant-webapp/src/environments/environment.ts` pon `platformProviderApiBaseUrl: 'https://<tu-dominio>.azurewebsites.net/api/v1'`, commit y push a `main` (el workflow de Azure del webapp lo redespliega).

7.2. En la Web App del platform actualiza `CORS_ALLOWED_ORIGINS` con la URL del webapp desplegado.

---

## 8. Problemas típicos

| Síntoma | Causa | Solución |
|---|---|---|
| Log: `Communications link failure` | Firewall de MySQL | Paso 2.3: "Allow public access from any Azure service" marcado |
| `Access denied for user` | credenciales | `DATABASE_USER` = `reliant_admin` sin sufijo `@servidor` |
| `The specified key byte array is 0 bits` / error JWT | `JWT_SECRET` ausente o corto | mínimo 32 caracteres |
| CORS error en el navegador | origen no coincide | `CORS_ALLOWED_ORIGINS` exacto, sin `/` final |
| `Unsupported class file major version` | runtime Java menor a 21 o build con otra versión | Runtime Java 21 en App Service y `java-version: '21'` en el workflow |
| 503 un rato | plan F1 dormido | esperar 1–2 min o pasar a B1 |
