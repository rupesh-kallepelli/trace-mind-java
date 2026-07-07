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

• customers-service

• vets-service

• visits-service

Platform Services

• api-gateway

• config-server

• discovery-server

• admin-server

Infrastructure

• chart

• docs

• .github

================================================================================
MICROSERVICE RESPONSIBILITIES
================================================================================

customers-service

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

vets-service

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

visits-service

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

api-gateway

Business Capability

API Routing

Responsibilities

• Accept incoming client requests

• Route requests

• Forward requests

Business logic should NOT exist here.

--------------------------------------------------------------------------------

config-server

Business Capability

Centralized Configuration

Responsibilities

• Configuration Management

• Externalized Configuration

No business logic.

--------------------------------------------------------------------------------

discovery-server

Business Capability

Service Discovery

Responsibilities

• Service Registration

• Service Discovery

No business logic.

--------------------------------------------------------------------------------

admin-server

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

→ customers-service

--------------------------------------------------------------------------------

Owner Search

→ customers-service

--------------------------------------------------------------------------------

Owner Update

→ customers-service

--------------------------------------------------------------------------------

Pet Registration

→ customers-service

--------------------------------------------------------------------------------

Pet Search

→ customers-service

--------------------------------------------------------------------------------

Pet Update

→ customers-service

--------------------------------------------------------------------------------

Veterinarian Listing

→ vets-service

--------------------------------------------------------------------------------

Veterinarian Management

→ vets-service

--------------------------------------------------------------------------------

Visit Creation

→ visits-service

--------------------------------------------------------------------------------

Visit Update

→ visits-service

--------------------------------------------------------------------------------

Visit History

→ visits-service

--------------------------------------------------------------------------------

Configuration Loading

→ config-server

--------------------------------------------------------------------------------

Service Discovery

→ discovery-server

--------------------------------------------------------------------------------

API Routing

→ api-gateway

--------------------------------------------------------------------------------

Application Monitoring

→ admin-server

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

api-gateway

↓

customers-service

↓

Database

------------------------------------------------------------

External Client

↓

api-gateway

↓

vets-service

↓

Database

------------------------------------------------------------

External Client

↓

api-gateway

↓

visits-service

↓

Database

--------------------------------------------------------------------------------

Business services depend on:

• config-server

• discovery-server

for configuration and service registration.

================================================================================
APPLICATION OWNERSHIP
================================================================================

Owners

Owned By

customers-service

------------------------------------------------------------

Pets

Owned By

customers-service

------------------------------------------------------------

Pet Types

Owned By

customers-service

------------------------------------------------------------

Veterinarians

Owned By

vets-service

------------------------------------------------------------

Specialties

Owned By

vets-service

------------------------------------------------------------

Visits

Owned By

visits-service

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

customers-service

Do not investigate:

• vets-service

• visits-service

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