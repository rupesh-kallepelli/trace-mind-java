================================================================================
APPLICATION CONTEXT
================================================================================

Application Name

Spring PetClinic Microservices

Repository

https://github.com/rupesh-kallepelli/petclinic-devops

Repository Owner

rupesh-kallepelli

Repository Name

petclinic-devops

Primary Branch

main

Architecture

Spring Boot Microservices Architecture

Business Domain

Veterinary Clinic Management System

================================================================================
APPLICATION PURPOSE
================================================================================

Spring PetClinic is a veterinary clinic management application.

The application allows veterinary clinics to manage pet owners, pets,
veterinarians and medical visits.

The application follows a Microservices Architecture where every business
capability is implemented as an independent Spring Boot microservice.

Each microservice owns its business domain and communicates with other
services through REST APIs.

================================================================================
REPOSITORY STRUCTURE
================================================================================

Business Services

• spring-petclinic-customers-service

• spring-petclinic-vets-service

• spring-petclinic-visits-service

Platform Services

• spring-petclinic-api-gateway

• spring-petclinic-config-server

• spring-petclinic-discovery-server

• spring-petclinic-admin-server

Infrastructure

• spring-petclinic-chart

• docs

• .github

================================================================================
MICROSERVICE RESPONSIBILITIES
================================================================================

spring-petclinic-customers-service

Business Capability

Owner and Pet Management

Owns

• Owner

• Pet

• PetType

Primary Responsibilities

• Register Owner

• Update Owner

• Search Owner

• View Owner Details

• Register Pet

• Update Pet

• View Pet Details

Does NOT own

• Veterinarians

• Visits

--------------------------------------------------------------------------------

spring-petclinic-vets-service

Business Capability

Veterinarian Management

Owns

• Veterinarian

• Specialty

Primary Responsibilities

• List Veterinarians

• View Veterinarian

• Update Veterinarian

• Manage Specialties

Does NOT own

• Owners

• Pets

• Visits

--------------------------------------------------------------------------------

spring-petclinic-visits-service

Business Capability

Visit Management

Owns

• Visit

Primary Responsibilities

• Create Visit

• Update Visit

• View Visit History

• Store Visit Notes

Depends On

• Existing Pet

• Existing Veterinarian

--------------------------------------------------------------------------------

spring-petclinic-api-gateway

Business Capability

API Routing

Responsibilities

• Accept incoming client requests

• Route requests

• Forward requests

Business logic should NOT exist here.

--------------------------------------------------------------------------------

spring-petclinic-config-server

Business Capability

Centralized Configuration

Responsibilities

• Configuration Management

• Externalized Configuration

No business logic.

--------------------------------------------------------------------------------

spring-petclinic-discovery-server

Business Capability

Service Discovery

Responsibilities

• Service Registration

• Service Discovery

No business logic.

--------------------------------------------------------------------------------

spring-petclinic-admin-server

Business Capability

Application Monitoring

Responsibilities

• Service Monitoring

• Health Monitoring

No business logic.

================================================================================
BUSINESS CAPABILITY MAPPING
================================================================================

Owner Registration

→ spring-petclinic-customers-service

--------------------------------------------------------------------------------

Owner Search

→ spring-petclinic-customers-service

--------------------------------------------------------------------------------

Owner Update

→ spring-petclinic-customers-service

--------------------------------------------------------------------------------

Pet Registration

→ spring-petclinic-customers-service

--------------------------------------------------------------------------------

Pet Search

→ spring-petclinic-customers-service

--------------------------------------------------------------------------------

Pet Update

→ spring-petclinic-customers-service

--------------------------------------------------------------------------------

Veterinarian Listing

→ spring-petclinic-vets-service

--------------------------------------------------------------------------------

Veterinarian Management

→ spring-petclinic-vets-service

--------------------------------------------------------------------------------

Visit Creation

→ spring-petclinic-visits-service

--------------------------------------------------------------------------------

Visit Update

→ spring-petclinic-visits-service

--------------------------------------------------------------------------------

Visit History

→ spring-petclinic-visits-service

--------------------------------------------------------------------------------

Configuration Loading

→ spring-petclinic-config-server

--------------------------------------------------------------------------------

Service Discovery

→ spring-petclinic-discovery-server

--------------------------------------------------------------------------------

API Routing

→ spring-petclinic-api-gateway

--------------------------------------------------------------------------------

Application Monitoring

→ spring-petclinic-admin-server

================================================================================
APPLICATION REQUEST FLOW
================================================================================

External Client

↓

API Gateway

↓

Business Controller

↓

Business Service

↓

Repository

↓

Database

Business logic exists ONLY inside the business services.

The API Gateway should only perform routing.

================================================================================
SERVICE DEPENDENCIES
================================================================================

External Client

↓

spring-petclinic-api-gateway

↓

spring-petclinic-customers-service

↓

Database

------------------------------------------------------------

External Client

↓

spring-petclinic-api-gateway

↓

spring-petclinic-vets-service

↓

Database

------------------------------------------------------------

External Client

↓

spring-petclinic-api-gateway

↓

spring-petclinic-visits-service

↓

Database

--------------------------------------------------------------------------------

Business services depend on:

• spring-petclinic-config-server

• spring-petclinic-discovery-server

for configuration and service registration.

================================================================================
APPLICATION OWNERSHIP
================================================================================

Owners

Owned By

spring-petclinic-customers-service

------------------------------------------------------------

Pets

Owned By

spring-petclinic-customers-service

------------------------------------------------------------

Pet Types

Owned By

spring-petclinic-customers-service

------------------------------------------------------------

Veterinarians

Owned By

spring-petclinic-vets-service

------------------------------------------------------------

Specialties

Owned By

spring-petclinic-vets-service

------------------------------------------------------------

Visits

Owned By

spring-petclinic-visits-service

================================================================================
INVESTIGATION GUIDELINES
================================================================================

Always identify the affected business capability first.

Then determine the responsible microservice.

Investigations should focus on the responsible microservice before
investigating dependent platform services.

Platform services should only be considered if evidence indicates
routing, configuration or service discovery failures.

Never investigate unrelated services.

Example

Issue

"Unable to register pet"

Business Capability

Pet Registration

Responsible Service

spring-petclinic-customers-service

Do not investigate:

• spring-petclinic-vets-service

• spring-petclinic-visits-service

unless evidence proves they are involved.

================================================================================
GENERAL INVESTIGATION PRINCIPLES
================================================================================

Always investigate the service responsible for the reported business capability.

Always prioritize evidence from the responsible service before investigating dependencies.

Avoid reporting failures from unrelated services.

Only correlate evidence when a dependency relationship is established.

Every conclusion must be supported by evidence collected by the investigating agent.

Never guess.

Never hallucinate.

Never fabricate evidence.