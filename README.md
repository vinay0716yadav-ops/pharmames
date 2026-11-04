# PharmaMES – Pharmaceutical Manufacturing Execution System Dashboard

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)
[![21 CFR Part 11](https://img.shields.io/badge/Compliance-21%20CFR%20Part%2011%20Ready-orange.svg)]()
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)]()

**PharmaMES** is an educational, production-style Pharmaceutical Manufacturing Execution System (MES) web application designed to digitize pharmaceutical manufacturing operations, maintain Electronic Batch Records (EBR), enforce 21 CFR Part 11 regulatory audit trails, streamline quality assurance dispositions, and automate deployment via Docker and GitHub Actions.

---

## 1. Technology Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 17+** | Backend core programming & JVM runtime |
| **Spring Boot 3** | Enterprise framework (Spring MVC, Spring Data JPA, Bean Validation) |
| **Maven** | Dependency management, build automation & test execution |
| **Thymeleaf + HTML/CSS** | Modern clinical MES web frontend with real-time UI indicators |
| **H2 Database** | Fast in-memory & persistent transactional store for batch dossiers |
| **Docker & Docker Compose** | Multi-stage containerization with non-root security execution |
| **Git & GitHub** | Source code version control |
| **GitHub Actions** | Automated CI/CD (lint, test, build JAR, Docker image publish) |

---

## 2. Core MES Modules

### 1. Production Dashboard
- **Real-time Plant Metrics**: Total batches, active in-progress lines, completed & released lots, pending orders, and quarantined lots.
- **Stage Progression Tracker**: Visual overview of batch distribution across pharmaceutical unit operations.
- **Live Audit Stream**: Instant feed of operator sign-offs and state transitions.

### 2. Batch Management
- **Master Formula Initiation**: Create new batches with targeted quantities, dosage forms (Tablets, Capsules, Suspensions, Injectables, Vials), manufacturing & expiration dates, assigned lines, and responsible scientists.
- **Lifecycle Advancement**: Step-by-step stage transitions (`Dispensing` &rarr; `Granulation` &rarr; `Compression` &rarr; `Coating` &rarr; `Packaging` &rarr; `QC Review` &rarr; `Released`).
- **Operational Status Controls**: Instant capability to place batches on hold, investigate deviations, or resume production.

### 3. Electronic Batch Records (EBR)
- **Digital Operation Sign-Off**: Capture step numbers, operation names, and equipment parameters.
- **Critical Process Parameters (CPPs)**: Record and compare target parameters vs actual measured values (e.g., temperatures, impeller RPM, compression force).
- **Dual Electronic Signatures**: Operator execution signature and supervisor verification sign-off.
- **Deviation Flagging**: Explicitly record parameter excursions with root cause comments.

### 4. Quality Management & QA Release
- **Analytical Laboratory Testing**: Record Active Ingredient Assay %, Dissolution Rate %, Moisture Content / Loss on Drying, and Microbial Bioburden.
- **QA Disposition Workflow**:
  - `APPROVED`: Authorizes formal commercial release and flags batch as `COMPLETED`.
  - `UNDER_REVIEW`: Keeps batch in analytical review stage.
  - `QUARANTINED`: Places batch on hold for Out-of-Specification (OOS) investigation.
  - `REJECTED`: Permanently rejects non-conforming batch.

### 5. Audit Trail (21 CFR Part 11)
- **Tamper-Evident Chronological History**: Computer-generated timestamps, entity IDs, performed actions, operator credentials, roles, change reasons, and pre/post change details.
- **Search & Filtering**: Search by batch number, operator, action keyword, or details.
- **Regulatory Print/Export View**: Print-ready format for simulated health authority inspections.

---

## 3. Quick Start & Local Execution

### Prerequisites
- **JDK 17 or later** (`java -version`)
- **Apache Maven 3.8+** (`mvn -version`)
- **Docker** (optional, for containerization)

### Run with Maven
```bash
# Clone or navigate to the project directory
cd /Users/apple/.gemini/antigravity/scratch/pharmames

# Compile, test, and run the application
mvn spring-boot:run
```

Once started, open your browser and navigate to:
- **MES Dashboard**: [http://localhost:8080](http://localhost:8080)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:pharmamesdb`
  - User Name: `sa`
  - Password: *(leave blank)*

---

## 4. Visual Studio Code Setup

To open and run the project in VS Code:

```bash
# Open directly in VS Code
open -a "Visual Studio Code" /Users/apple/.gemini/antigravity/scratch/pharmames
# or if 'code' CLI is installed:
code /Users/apple/.gemini/antigravity/scratch/pharmames
```

The repository includes pre-configured `.vscode/` settings:
- **Run & Debug**: Press `F5` or go to the Run & Debug tab and select **"Launch PharmaMES Application"**.
- **Extensions**: Automatically recommends the **Extension Pack for Java** and **Spring Boot Tools**.

---

## 5. Docker Containerization

### Build & Run with Docker
```bash
# Build Docker container image
docker build -t pharmames-dashboard:latest .

# Run containerized MES application
docker run -d -p 8080:8080 --name pharmames-app pharmames-dashboard:latest
```

### Run with Docker Compose
```bash
docker compose up --build -d
```
Access the application at [http://localhost:8080](http://localhost:8080).

---

## 6. GitHub & GitHub Actions CI/CD Setup

### Initialize and Push to GitHub
```bash
cd /Users/apple/.gemini/antigravity/scratch/pharmames

# Initialize git repository (if not already done)
git init
git add .
git commit -m "feat: Initial commit for PharmaMES - Pharmaceutical MES Dashboard"

# Create a new repository on GitHub using GitHub CLI
gh repo create pharmames --public --source=. --remote=origin --push

# Or push to an existing remote:
# git remote add origin https://github.com/<your-username>/pharmames.git
# git branch -M main
# git push -u origin main
```

### GitHub Actions Pipeline
The file [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml) automates:
1. **Continuous Integration**: Triggers on push to `main` and Pull Requests to run `mvn clean verify` and execute automated unit/integration tests.
2. **Container Build**: Builds the Docker image.
3. **Docker Hub Publishing**: If repository secrets `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` are configured in GitHub Settings &rarr; Secrets, the pipeline automatically publishes the image to Docker Hub.

---

## 7. Educational Notice
> **Important**: This application is an educational prototype created to demonstrate digital manufacturing workflows, Good Manufacturing Practices (GMP) concepts, 21 CFR Part 11 audit trails, and CI/CD container automation. It is **not** a validated pharmaceutical production system.
