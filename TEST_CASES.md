# Test Cases Documentation

This document provides comprehensive information about the test cases implemented for the Airline Application.

## Overview

The test suite includes **33 test cases** covering different layers of the application:
- **Unit Tests**: Testing individual components in isolation
- **Integration Tests**: Testing components working together

## Test Structure

```
src/test/java/com/airline/
├── AirlineApplicationTests.java       (1 test)
├── controller/
│   └── FlightControllerTest.java      (5 tests)
├── model/
│   └── FlightModelTest.java           (18 tests)
└── service/
    └── FlightServiceImplTest.java     (9 tests)
```

## Test Details

### 1. Application Context Tests (1 test)

**File**: `AirlineApplicationTests.java`

| Test Name | Description |
|-----------|-------------|
| `contextLoads()` | Verifies that the Spring application context loads successfully |

### 2. Controller Layer Tests (5 tests)

**File**: `FlightControllerTest.java`

Tests the REST API endpoints using mocked service layer.

| Test Name | Description | HTTP Method | Endpoint |
|-----------|-------------|-------------|----------|
| `testAddFlight()` | Tests creating a new flight | POST | `/flight/` |
| `testGetAllFlight()` | Tests retrieving all flights | GET | `/flight/` |
| `testGetFlight()` | Tests retrieving a specific flight by ID | GET | `/flight/{flightId}` |
| `testDeleteFlight()` | Tests deleting a flight by ID | DELETE | `/flight/{flightId}` |
| `testUpdateFlight()` | Tests updating an existing flight | PUT | `/flight/{flightId}` |

**Coverage**: All CRUD operations on the Flight REST API

### 3. Service Layer Tests (9 tests)

**File**: `FlightServiceImplTest.java`

Tests business logic layer with mocked repository.

| Test Name | Description | Expected Result |
|-----------|-------------|-----------------|
| `testAddFlight_Success()` | Tests adding a new flight | Flight is saved successfully |
| `testGetAllFlight_Success()` | Tests retrieving all flights | Returns list of all flights |
| `testGetAllFlight_EmptyList()` | Tests when no flights exist | Returns empty list |
| `testGetFlight_Success()` | Tests retrieving flight by valid ID | Returns the flight |
| `testGetFlight_NotFound()` | Tests retrieving non-existent flight | Throws Exception with message |
| `testDeleteFlight_Success()` | Tests deleting a flight | Returns true |
| `testUpdateFlight_Success()` | Tests updating a flight | Returns updated flight |
| `testAddFlight_NullFlight()` | Tests adding null flight | Throws IllegalArgumentException |
| `testDeleteFlight_VerifyRepositoryCall()` | Verifies repository method is called | Repository method invoked once |

**Coverage**: All service layer business logic including error handling

### 4. Model Layer Tests (18 tests)

**File**: `FlightModelTest.java`

Tests the Flight entity/model class.

| Test Name | Description | Test Type |
|-----------|-------------|-----------|
| `testNoArgsConstructor()` | Tests no-args constructor | Constructor test |
| `testAllArgsConstructor()` | Tests all-args constructor | Constructor test |
| `testSetAndGetFlightId()` | Tests flightId getter/setter | Property test |
| `testSetAndGetFlightName()` | Tests flightName getter/setter | Property test |
| `testSetAndGetSource()` | Tests source getter/setter | Property test |
| `testSetAndGetDestination()` | Tests destination getter/setter | Property test |
| `testSetAndGetTicketPrice()` | Tests ticketPrice getter/setter | Property test |
| `testNullFlightName()` | Tests null flight name | Null handling |
| `testNullSource()` | Tests null source | Null handling |
| `testNullDestination()` | Tests null destination | Null handling |
| `testNullTicketPrice()` | Tests null ticket price | Null handling |
| `testZeroTicketPrice()` | Tests zero price | Edge case |
| `testNegativeTicketPrice()` | Tests negative price | Edge case |
| `testLargeTicketPrice()` | Tests large price value | Edge case |
| `testEmptyFlightName()` | Tests empty string for name | Edge case |
| `testEmptySource()` | Tests empty string for source | Edge case |
| `testEmptyDestination()` | Tests empty string for destination | Edge case |
| `testSetAllProperties()` | Tests setting all properties | Integration test |

**Coverage**: Complete coverage of all model properties including edge cases and null handling

## Test Configuration

### Dependencies

The following dependencies are used for testing:

- **spring-boot-starter-test**: Core Spring Boot testing support including JUnit 5
- **Mockito**: For mocking dependencies in unit tests
- **H2 Database**: In-memory database for integration tests
- **JaCoCo**: Code coverage reporting

### Configuration Files

- **application-test.properties**: Test-specific configuration with H2 in-memory database

## Running the Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=FlightControllerTest
mvn test -Dtest=FlightServiceImplTest
mvn test -Dtest=FlightModelTest
```

### Run with Code Coverage
```bash
mvn clean test
```

Code coverage report will be available at: `target/site/jacoco/index.html`

## Test Results

All **33 tests** pass successfully:
- ✅ 1 Application Context Test
- ✅ 5 Controller Tests  
- ✅ 9 Service Tests
- ✅ 18 Model Tests

## Best Practices Followed

1. **Descriptive Test Names**: Using `@DisplayName` annotations for clear test descriptions
2. **AAA Pattern**: Tests follow Arrange-Act-Assert structure
3. **Mocking**: Using Mockito to isolate units under test
4. **Edge Cases**: Testing null values, empty strings, boundary conditions
5. **Verification**: Verifying interactions with mocked dependencies
6. **Independence**: Each test is independent and can run in isolation

## Future Enhancements

Potential areas for additional test coverage:

1. **Repository Integration Tests**: Testing JPA repository with actual H2 database
2. **End-to-End Integration Tests**: Testing complete request/response cycles
3. **Exception Handling Tests**: More comprehensive error scenario coverage
4. **Performance Tests**: Load testing for the API endpoints
5. **Security Tests**: Testing authentication and authorization if implemented

## Code Coverage

The test suite provides comprehensive coverage of:
- ✅ Controllers (All REST endpoints)
- ✅ Services (Business logic)
- ✅ Models (Entity classes)
- ⚠️ Repository (Basic JPA operations - requires database setup)

## Notes

- Tests use in-memory H2 database for isolation
- Mockito is used to mock dependencies and verify interactions
- All tests are designed to run quickly without external dependencies
- Tests can be run in any order (no dependencies between tests)
