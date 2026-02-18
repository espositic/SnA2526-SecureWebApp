# SecureWebApp - Guida all'Installazione

## 1. Generazione Keystore SSL

Tomcat richiede un certificato per abilitare HTTPS. Creiamo una cartella dedicata e generiamo un certificato autofirmato.

Dalla cartella principale del progetto:

```bash
# 1. Crea la cartella per i certificati
mkdir -p certs

# 2. Genera il keystore
keytool -genkey -alias tomcat -keyalg RSA -keystore certs/keystore.jks -storepass changeit -keypass changeit -validity 3650 -dname "CN=localhost, OU=Dev, O=SecureApp, L=Bari, C=IT"

```

---

## 2. Configurazione Variabili d'Ambiente (.env)

Crea un file chiamato **`.env`** nella cartella principale.
Viene riportato di seguito un esempio del contenuto del file .env.

**File:** `.env`

```ini
# ==========================================
# CONFIGURAZIONE DATABASE
# ==========================================
# Password di root per MySQL
DB_ROOT_PASSWORD=CambiaQuestaPasswordRoot123!

# Nome del database
DB_NAME=secure_app_db

# Utente applicativo
DB_USER=app_user

# Password utente applicativo
DB_PASSWORD=CambiaQuestaPasswordUser123!

# ==========================================
# CONFIGURAZIONE TOMCAT
# ==========================================
TOMCAT_HTTP_PORT=8080
TOMCAT_HTTPS_PORT=8443

```

---

## 3. Compilazione del Progetto

Prima di avviare i container, è necessario compilare il codice Java per creare il file `.war`.

```bash
# 1. Entra nella cartella del codice sorgente
cd SecureWebApp

# 2. Pulisci e compila il progetto
sudo mvn clean package -DskipTests

# 3. Torna alla cartella principale
cd ..

```

---

## 4. Avvio con Docker Compose

Una volta compilato il progetto e configurato il file `.env`, avvia l'infrastruttura.

```bash
docker compose up -d --build

```

---

## 5. Accesso

* **Web App (HTTP):** [http://localhost:8080/SecureWebApp]()
* **Web App (HTTPS):** [https://localhost:8443/SecureWebApp]()
* *Nota: Accetta il rischio di sicurezza del browser (certificato autofirmato).*



### Comandi Utili

* **Visualizza Log:** `docker compose logs -f`
* **Stop Container:** `docker compose down`
