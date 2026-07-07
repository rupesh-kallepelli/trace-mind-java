================================================================================
DATABASE CONTEXT
================================================================================

Database Name

petclinic-db

Database Alias

petclinic-db

Database Type

MySQL

The Database MCP Server is configured to access ONLY this database.

All database investigations MUST use this database.

Never investigate any other database.

================================================================================
DATABASE PURPOSE
================================================================================

The petclinic database stores all business data for the Spring PetClinic
Microservices application.

Although the application is implemented as multiple microservices, this Proof
of Concept uses a single shared MySQL database.

Business ownership of tables is still defined by the owning microservice.

================================================================================
MICROSERVICE TO TABLE OWNERSHIP
================================================================================

customers-service

Owns

• owners
• pets
• types

------------------------------------------------------------

vets-service

Owns

• vets
• specialties
• vet_specialties

------------------------------------------------------------

visits-service

Owns

• visits

================================================================================
TABLE DEFINITIONS
================================================================================

TABLE

owners

Owned By

customers-service

Columns

id (Primary Key)

first_name

last_name

address

city

telephone

Indexes

last_name

Business Purpose

Stores pet owner information.

------------------------------------------------------------

TABLE

types

Owned By

customers-service

Columns

id (Primary Key)

name

Indexes

name

Business Purpose

Stores pet categories.

------------------------------------------------------------

TABLE

pets

Owned By

customers-service

Columns

id (Primary Key)

name

birth_date

type_id

owner_id

Indexes

name

Foreign Keys

owner_id → owners.id

type_id → types.id

Business Purpose

Stores pet information.

Business Rules

Every pet must belong to an existing owner.

Every pet must reference an existing pet type.

------------------------------------------------------------

TABLE

vets

Owned By

vets-service

Columns

id (Primary Key)

first_name

last_name

Indexes

last_name

Business Purpose

Stores veterinarian information.

------------------------------------------------------------

TABLE

specialties

Owned By

vets-service

Columns

id (Primary Key)

name

Indexes

name

Business Purpose

Stores veterinarian specialties.

------------------------------------------------------------

TABLE

vet_specialties

Owned By

vets-service

Columns

vet_id

specialty_id

Foreign Keys

vet_id → vets.id

specialty_id → specialties.id

Unique Constraint

(vet_id, specialty_id)

Business Purpose

Maps veterinarians to specialties.

------------------------------------------------------------

TABLE

visits

Owned By

visits-service

Columns

id (Primary Key)

pet_id

visit_date

description

Foreign Keys

pet_id → pets.id

Business Purpose

Stores pet visit history.

Business Rules

Every visit must reference an existing pet.

================================================================================
DATABASE RELATIONSHIPS
================================================================================

owners (1)

↓

pets (N)

------------------------------------------------------------

types (1)

↓

pets (N)

------------------------------------------------------------

vets (N)

↓

vet_specialties

↓

specialties (N)

------------------------------------------------------------

pets (1)

↓

visits (N)

================================================================================
BUSINESS CAPABILITY TO TABLE MAPPING
================================================================================

Owner Registration

↓

owners

------------------------------------------------------------

Owner Search

↓

owners

------------------------------------------------------------

Owner Update

↓

owners

------------------------------------------------------------

Pet Registration

↓

owners

pets

types

------------------------------------------------------------

Pet Update

↓

pets

------------------------------------------------------------

Pet Lookup

↓

pets

owners

types

------------------------------------------------------------

Veterinarian Listing

↓

vets

specialties

vet_specialties

------------------------------------------------------------

Veterinarian Management

↓

vets

specialties

vet_specialties

------------------------------------------------------------

Visit Creation

↓

visits

pets

------------------------------------------------------------

Visit History

↓

visits

pets

================================================================================
COMMON DATABASE RCA PATTERNS
================================================================================

Foreign Key Constraint Failure

pets.owner_id

↓

owners.id

Likely Cause

Owner does not exist.

------------------------------------------------------------

Foreign Key Constraint Failure

pets.type_id

↓

types.id

Likely Cause

Pet type does not exist.

------------------------------------------------------------

Foreign Key Constraint Failure

visits.pet_id

↓

pets.id

Likely Cause

Pet does not exist.

------------------------------------------------------------

Duplicate Veterinarian Specialty

Unique Constraint

(vet_id, specialty_id)

Likely Cause

Duplicate specialty assignment.

------------------------------------------------------------

Missing Owner

Investigate

owners

------------------------------------------------------------

Missing Pet

Investigate

pets

------------------------------------------------------------

Missing Pet Type

Investigate

types

------------------------------------------------------------

Missing Visit

Investigate

visits

================================================================================
DATABASE INVESTIGATION GUIDELINES
================================================================================

Always identify the affected business capability first.

Determine the responsible microservice using the Application Context.

Use the ownership mapping above to determine the relevant tables.

Investigate ONLY the relevant tables.

Ignore unrelated tables unless database evidence establishes a dependency.

Example

Issue

Unable to register pet.

Responsible Service

customers-service

Relevant Tables

owners

pets

types

Do NOT investigate

vets

specialties

vet_specialties

visits

unless evidence proves they are involved.

================================================================================
STRICT RULES
================================================================================

The configured database is

petclinic

Always investigate ONLY this database.

Never investigate another database.

Never fabricate schema.

Never fabricate relationships.

Never infer columns.

Use the schema defined in this document as authoritative.

Every conclusion must be supported by Database MCP evidence.