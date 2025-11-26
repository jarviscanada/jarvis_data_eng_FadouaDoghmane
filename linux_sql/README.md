# Linux Cluster Monitoring Agent

## Introduction

This project implements a lightweight Linux cluster monitoring agent that collects hardware specifications and real-time resource usage from multiple Linux hosts. All collected data is stored in a centralized PostgreSQL database running inside a Docker container.

Target users include system administrators and DevOps engineers who need visibility into cluster-wide CPU, memory, and disk performance.

The solution uses **Bash, Docker, psql, Git, and crontab**.  
Development/testing were performed on a **GCP Compute Engine** VM accessed through **VNC Viewer** using the `rocky` user.

This project includes automated scripts for:
- Database setup  
- Hardware information collection  
- Real-time resource monitoring  

---

## Quick Start

### 1. Start the PostgreSQL Instance
```bash
./scripts/psql_docker.sh create
./scripts/psql_docker.sh start
```

### 2. Create Database Tables
```bash
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```

### 3. Insert Static Hardware Specs (one-time)
```bash
./scripts/host_info.sh localhost 5432 host_agent postgres password
```

### 4. Insert Real-Time Hardware Usage (manual test)
```bash
./scripts/host_usage.sh localhost 5432 host_agent postgres password
```

### 5. Crontab Setup (runs usage script every minute)
```bash
crontab scripts/crontab.txt
```

---

## Crontab File (scripts/crontab.txt)

```bash
* * * * * linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password >> /tmp/host_usage.log 2>&1
```

---

## Implementation

### Architecture

The system includes:
- Multiple Linux hosts (monitoring agents)
- One PostgreSQL database in Docker
- Two Bash scripts for data collection  
  - `host_info.sh`
  - `host_usage.sh`

---

## Scripts Overview

### psql_docker.sh
Manages PostgreSQL container lifecycle.

```bash
./psql_docker.sh create
./psql_docker.sh start
./psql_docker.sh stop
```

### host_info.sh
Collects static system hardware specs.

Usage:
```bash
./host_info.sh localhost 5432 host_agent postgres password
```

### host_usage.sh
Collects live resource usage (CPU, memory, disk).

Usage:
```bash
./host_usage.sh localhost 5432 host_agent postgres password
```

---

## SQL (queries.sql)

Includes SQL queries for analyzing:

- Hosts grouped by CPU number and memory
- Average memory usage per 5-minute intervals
- Detecting host failures or missing data points

Example query:

- Which host has the highest average memory usage in a given interval?


---

## Database Schema

### host_info Table
| Column Name        | Description                   |
|--------------------|-------------------------------|
| id                 | Unique host ID                |
| hostname           | Hostname                      |
| cpu_number         | Number of CPU cores           |
| cpu_architecture   | CPU architecture              |
| cpu_model          | CPU model                     |
| cpu_mhz            | CPU frequency (MHz)           |
| l2_cache           | L2 cache size                 |
| total_mem          | Total memory (MB)             |
| timestamp          | Record timestamp              |

### host_usage Table
| Column Name       | Description                        |
|-------------------|------------------------------------|
| timestamp         | Time of measurement                |
| host_id           | FK to host_info.id                 |
| memory_free       | Free memory (MB)                   |
| cpu_idle          | CPU idle %                         |
| cpu_kernel        | CPU kernel %                       |
| disk_io           | Disk I/O metric                    |
| disk_available    | Available disk space (MB)          |

---

# Testing

- `psql_docker.sh` successfully created and managed the DB container.
- `host_info.sh` inserted correct hardware specs.
- `host_usage.sh` was run repeatedly to confirm timestamps and FK integrity.
- `ddl.sql` executed cleanly with all constraints working as intended.

---

# Deployment

This project uses:

- **GitHub** for version control
- **Docker** for PostgreSQL
- **crontab** for scheduled monitoring
- **GCP VM** for environment hosting
- **VNC Viewer** for GUI access

---

# Project Structure

| Folder / File | Description |
|---------------|-------------|
| `linux_sql/`  | Root project folder |
| `linux_sql/scripts/` | Bash scripts for DB setup and monitoring |
| `psql_docker.sh` | Creates and manages PostgreSQL Docker container |
| `host_info.sh`  | Collects hardware specifications and inserts into DB |
| `host_usage.sh` | Collects usage metrics and inserts into DB |
| `crontab.txt`   | Example crontab configuration for scheduled scripts |
| `linux_sql/sql/` | SQL scripts folder |
| `ddl.sql`       | Database schema and constraints |
| `queries.sql`   | Sample queries to retrieve collected data |
| `README.md`     | Project documentation |

---

# Improvements

Future enhancements:

1. Auto-detect hardware changes (CPU/RAM upgrades)
2. Add retry logic for DB connection failures
3. Integrate email/Slack alerts for abnormal metrics
4. Convert Bash scripts into systemd services

---
