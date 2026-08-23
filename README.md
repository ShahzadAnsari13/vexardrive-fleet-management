# VexarDrive Fleet Management Platform

A native Android fleet management application developed as part of the VexarDrive Technologies SDE Technical Assessment.

## Overview

The application provides fleet managers with a centralized interface to manage vehicles, drivers, vehicle-driver assignments, and fleet-level information.

The Android application is built natively using Kotlin with a layered architecture and REST API integration.

## Screenshots

### Manager Dashboard

![VexarDrive Dashboard](VexarDrive-Dashboard.jpeg)

### Vehicle Management

![Vehicles](VexarDrive-Vehicles.jpeg)

![Vehicle Details](VexarDrive-VehicleDetails.jpeg)

### Driver Management

![Drivers](VexarDrive-Drivers.jpeg)

![Driver Details](VexarDrive-DriverDetails.jpeg)

### Authentication

![Login](VexarDrive-Login.jpeg)

![Register](VexarDrive-Register.jpeg)

### Splash Screen

![Splash Screen](VexarDrive-Splash.jpeg)

### Driver Management

![Drivers](screenshots/VexarDrive-Drivers.jpg)

![Driver Details](screenshots/VexarDrive-DriverDetails.jpg)
## Implemented Features

### Authentication

* User registration
* User login
* Authenticated session handling
* JWT-based authentication
* Refresh-token handling
* Role-based application flow

### Fleet Manager

#### Dashboard

* Fleet overview
* Total vehicles
* Available vehicles
* Vehicles on trip
* Maintenance-related fleet information
* Active fleet information

#### Vehicle Management

* Vehicle listing
* Vehicle details
* Add vehicle
* Edit vehicle
* Update vehicle status
* Vehicle information display

#### Driver Management

* Driver listing
* Driver details
* Add driver
* Edit driver
* Activate/deactivate driver
* Driver information display

#### Vehicle–Driver Assignment

* Assignment listing
* Assignment details
* Create vehicle-driver assignment
* Delete assignment
* Assignment date range handling

## Android Architecture

The application follows a layered architecture with separation between presentation, domain, and data layers.

```text
Presentation
    |
    |-- Activities
    |-- Fragments
    |-- ViewModels
    |-- UI States
    |
Domain
    |
    |-- Repository Interfaces
    |
Data
    |
    |-- Repository Implementations
    |-- Retrofit APIs
    |-- DTOs
    |-- DataStore
    |-- Network Interceptors
```

### Technology Stack

* Kotlin
* Android SDK
* XML-based UI
* MVVM
* Clean Architecture principles
* Hilt / Dependency Injection
* Retrofit
* OkHttp
* Kotlin Coroutines
* StateFlow
* DataStore
* Navigation Component

## Backend Integration

The Android application communicates with a RESTful backend through Retrofit APIs.

Implemented API modules include:

* Authentication
* Vehicles
* Drivers
* Assignments
* Dashboard
* Trip API integration layer

Authentication tokens are handled through network interceptors and refresh-token authentication.

## Project Structure

```text
app/
└── src/main/java/com/vexardrive/fleetmanager/

    ├── data/
    │   ├── local/
    │   ├── remote/
    │   └── repository/

    ├── domain/
    │   └── repository/

    ├── presentation/
    │   ├── auth/
    │   ├── driver/
    │   ├── manager/
    │   │   ├── assignment/
    │   │   ├── dashboard/
    │   │   ├── driver/
    │   │   └── vehicle/
    │   └── splash/

    └── di/
```

## Setup

### Requirements

* Android Studio
* Android SDK
* JDK compatible with the project configuration
* Running VexarDrive backend/API

### Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Configure the backend/API base URL.
4. Sync Gradle dependencies.
5. Build and run the application on an Android device or emulator.

## Environment Configuration

Do not commit production secrets or credentials to the repository.

Configure environment-specific values locally before running the application.

## Testing

The project contains Android unit-test and instrumentation-test structure.

Further automated coverage can be extended for:

* Authentication
* Vehicle creation/update
* Duplicate registration validation
* Assignment conflicts
* Trip state transitions
* Location handling
* Maintenance workflows

## Screenshots

Screenshots can be added to this section as part of the final documentation.

## Known Limitations

Due to the limited assessment implementation time, the following areas were not fully completed:

* Complete Trip management workflow
* Complete GPS/location tracking workflow
* Maintenance management UI
* Incident/issue reporting workflow
* Push notification workflows
* Complete secondary iOS implementation
* Advanced fleet analytics
* Full offline synchronization
* Complete automated test coverage
* Production cloud deployment documentation

The current implementation focuses on the core Android fleet-manager experience, authentication, vehicle management, driver management, assignments, dashboard, and API architecture.

## Design Decisions

The application uses a layered architecture to keep UI, business contracts, and data/network concerns separated.

Repository interfaces are defined in the domain layer while their implementations remain in the data layer. Retrofit is used for REST API communication, DataStore is used for local session/preferences storage, and network interceptors handle authentication concerns.

## Future Improvements

* Complete driver trip workflow
* Real-time GPS tracking
* WebSocket-based fleet tracking
* Maintenance and incident modules
* Push notifications
* Offline-first synchronization
* Automated test expansion
* Cloud deployment
* iOS secondary-platform implementation

## Assessment Context

This project was developed for the VexarDrive Technologies SDE Technical Assessment and demonstrates native Android development, REST API integration, architecture, authentication, fleet-management workflows, and software engineering practices.
