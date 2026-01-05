# KYC Mobile API

## Overview

`kycMobileApi` is a **Spring Boot microservice** that acts as the **Mobile layer** in the KYC (Know Your Customer) system.

Its main responsibilities are:

* Receive KYC submissions from frontend/mobile clients
* Validate and orchestrate the request
* Forward the submission to the **BackOffice (Office MS)** for persistence and processing
* Delegate **document extraction, liveness verification, and comparison with official ID** to the **FastAPI MS**

This service **does not own the KYC domain or database**. It acts as a **gateway / orchestration layer**.

---

## Architecture Position

```
Frontend / Mobile App
        ↓
   kycMobileApi  (this service)
       ↙       ↘
FastAPI MS   BackOffice MS
(document processing,    (KYC core logic & persistence)
liveness & ID comparison)
```

Key principles:

* Processes KYC submissions efficiently
* Handles orchestration between FastAPI MS and BackOffice MS
* No shared domain models across services
* Clear API ownership

---

## Tech Stack

* Java 21
* Spring Boot
* WebClient for service-to-service HTTP calls
* Lombok
* Jackson (JSON serialization)

---

## API Responsibilities

### 1. Receive KYC submissions

* Exposes REST endpoints for frontend/mobile clients
* Accepts JSON payloads
* Performs request validation

### 2. Forward submissions

* Calls FastAPI MS for document extraction and biometric verification
* Calls BackOffice MS for persistence
* Applies timeout and retry policies

### 3. Handle document and biometric processing

* Delegates extraction, liveness checks, and ID comparison to **FastAPI MS**
* Receives processed results and forwards them to BackOffice MS


## License

This project is internal and intended for KYC system integration.
