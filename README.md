# Vinay PharmaMES – Enterprise Pharmaceutical Manufacturing Execution System

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)
[![21 CFR Part 11](https://img.shields.io/badge/Compliance-21%20CFR%20Part%2011%20Ready-orange.svg)]()
[![Permanent Storage](https://img.shields.io/badge/Storage-Permanent%20Disk%20DB-emerald.svg)]()

**Vinay PharmaMES** is an enterprise-grade Pharmaceutical Manufacturing Execution System (MES) web application engineered to digitize pharmaceutical manufacturing workflows, maintain Electronic Batch Records (EBR), enforce 21 CFR Part 11 regulatory compliance, streamline analytical quality assurance releases, and ensure **permanent transactional record persistence on disk**.

---

## 1. Technology Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 17+** | Backend core programming & JVM runtime |
| **Spring Boot 3** | Enterprise framework (Spring MVC, Spring Data JPA, Bean Validation) |
| **Maven** | Dependency management, build automation & test execution |
| **Thymeleaf + Chart.js + CSS3** | Clean clinical MES web frontend with real-time analytics & charts |
| **H2 File Database** | **Permanent transactional disk storage** (`./data/pharmames_db.mv.db`) |
| **Docker & Docker Compose** | Multi-stage containerization with non-root security execution |
| **Git & GitHub** | Source code version control hosted on GitHub |
| **GitHub Actions** | Automated CI/CD (automated testing, build JAR, Docker image publish) |

---

## 2. Core MES Modules

### 1. Production Dashboard
- **Executive KPIs**: Real-time fleet metrics (Total Batches, Active In-Progress, Completed & Released, Pending Schedule, On Hold / Quarantined).
- **Interactive Floor Analytics**: Live Chart.js doughnut chart for lifecycle status distribution and bar chart for 7-stage manufacturing unit operations.
- **Permanent Storage Active Badge**: Continuous disk persistence indicator.
- **Live Regulatory Stream**: Real-time feed of operator sign-offs, stage advancements, and QA dispositions.

### 2. Batch Management
- **Master Formula Initiation**: Create new batches with target quantities, dosage forms (Tablets, Capsules, Suspensions, Injectables, Vials), manufacturing & expiration dates, assigned lines, and responsible scientists.
- **Lifecycle Advancement**: Step-by-step stage transitions (`Dispensing` &rarr; `Granulation` &rarr; `Compression` &rarr; `Coating` &rarr; `Packaging` &rarr; `QC Review` &rarr; `Released`).
- **Operational Controls**: Capability to place batches on hold, investigate deviations, or resume production.

### 3. Electronic Batch Records (EBR)
- **Digital Operation Sign-Off**: Capture step numbers, operation names, and equipment parameters.
- **Critical Process Parameters (CPPs)**: Compare target parameters vs actual measured values (e.g., temperatures, impeller RPM, compression force).
- **Dual Electronic Signatures**: Operator execution signature and supervisor verification sign-off.
- **Deviation Flagging**: Explicitly record parameter excursions with root cause comments.

### 4. Quality Control (QC) & QA Release
- **Analytical Laboratory Testing**: Record Active Ingredient Assay %, Dissolution Rate %, Moisture Content / Loss on Drying, and Microbial Bioburden.
- **QA Disposition Workflow**:
  - `APPROVED`: Authorizes formal commercial release and flags batch as `COMPLETED`.
  - `UNDER_REVIEW`: Keeps batch in analytical review stage.
  - `QUARANTINED`: Places batch on hold for Out-of-Specification (OOS) investigation.
  - `REJECTED`: Permanently rejects non-conforming batch.

### 5. Regulatory Audit Trail (21 CFR Part 11)
- **Tamper-Evident Chronological History**: Computer-generated timestamps, entity IDs, performed actions, operator credentials, roles, change reasons, and pre/post change details.
- **Search & Filtering**: Search by batch number, operator, action keyword, or details.
- **Regulatory Print/Export View**: Print-ready Certificate of Analysis and Batch Dossier format for simulated health authority inspections.

---

## 3. Permanent Record Persistence Guarantee

All records created in Vinay PharmaMES are **permanently saved on disk**:
- **Database Engine**: H2 Persistent Embedded Engine (`AUTO_SERVER=TRUE`)
- **Database File Location**: `./data/pharmames_db.mv.db`
- **Integrity**: Every new batch, stage advance, EBR parameter measurement, QC approval, and audit trail record is written directly to physical disk. Restarting the server or machine preserves 100% of the manufacturing history and audit logs.

---

## 4. Quick Start & Local Execution

### Run with Maven
```bash
# Navigate to the project directory
cd /Users/apple/vinay/pharmames

# Compile, test, and run the application
mvn spring-boot:run
```

Once started, open your web browser:
- **MES Dashboard**: [http://localhost:8080](http://localhost:8080)
- **H2 Permanent Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:file:./data/pharmames_db`
  - User Name: `sa`
  - Password: *(leave blank)*

---

## 5. Visual Studio Code Setup

To open the project in VS Code:

```bash
open -a "Visual Studio Code" /Users/apple/vinay/pharmames
# or if 'code' CLI is installed:
code /Users/apple/vinay/pharmames
```

- **Run & Debug**: Press `F5` and select **"Launch PharmaMES Application"**.
- Pre-configured with `.vscode/launch.json` and `.vscode/settings.json`.

---

## 6. Docker Containerization

```bash
cd /Users/apple/vinay/pharmames

# Build Docker image
docker build -t pharmames-dashboard:latest .

# Run container
docker run -d -p 8080:8080 -v $(pwd)/data:/app/data --name pharmames-app pharmames-dashboard:latest
```

---

## 7. GitHub Repository & CI/CD

- **GitHub Repository**: [https://github.com/vinay0716yadav-ops/pharmames](https://github.com/vinay0716yadav-ops/pharmames)
- **GitHub Actions**: Automated pipeline defined in `.github/workflows/ci-cd.yml` automatically verifies code, executes all test suites, and publishes Docker containers.
