# Ticket Shop: Testing Workshop

A small ticket shop written in plain Java 25 and Maven. The production code is finished. Your job is to write the tests.

> The code may contain bugs. If one of your tests turns red, check the specification below first. If the specification confirms your expectation, you have found a bug. Tell the trainer; fixing it is optional.

## Getting started

1. Open the repository in GitHub Codespaces. The container installs Java 25, Maven and the VS Code extensions, then runs a first build.
2. Open the **Testing** view (flask icon) to run single tests, or use the terminal:
    - `mvn test` runs the unit tests (`*Test`)
    - `mvn verify` runs the unit tests and the integration tests (`*IT`)
3. Optional: start the application by running `Main` (Run link above `main`) and send requests from `requests.http` (click "Send Request").

Tests marked with `@Disabled("Exercise x.y")` and a `// TODO` comment are your exercises. Remove the annotation and the comment, then write the test.

## Specification

### Ticket price

- An order is for one event and contains a list of attendees. Each attendee has an `age` and a `student` flag.
- An order contains **1 to 20 attendees**; anything else is rejected with an `IllegalArgumentException`.
- Every attendee pays the base price of the event, minus their personal discount.
- Ticket prices are rounded to cents (half up) per ticket.
- A **service fee of 2.00 EUR** is added once per order. It is never discounted.

### Personal discounts

- Students get **20 %**.
- Seniors (age **65 and older**) get **30 %**.
- If both apply, only the **higher** discount is granted. Discounts are never added up.

### Bulk discount

- Orders with **10 or more attendees** get **10 %** off the subtotal.
- The subtotal is the sum of all ticket prices after personal discounts, without the service fee.

### REST endpoint

`POST /tickets`

```json
{
    "eventId": "rock-night",
    "attendees": [
        { "age": 30, "student": false },
        { "age": 22, "student": true }
    ]
}
```

| Situation | Status |
|---|---|
| Purchase successful | `201 Created` with purchase id, ticket count and price breakdown |
| Invalid request (malformed JSON, missing fields, invalid number of attendees) | `400 Bad Request` |
| Unknown event | `404 Not Found` |

Available events:

| Event id | Base price |
|---|---|
| `rock-night` | 49.90 |
| `jazz-brunch` | 24.50 |
| `comedy-evening` | 19.99 |

## Workshop flow

| Block | Time | Test class |
|---|---|---|
| 1. Classic unit tests | 15 min | `TicketPriceCalculatorTest` |
| 2. Personal discounts | 15 min | `PersonalDiscountPolicyTest` |
| 3. Bulk discount | 15 min | `BulkOrderPricingTest` |
| 4. Coverage is not quality (trainer demo) | 10 min | `CoverageIsNotQualityTest` |
| 5. Integration test | 20 min | `TicketPurchaseIT` |
| 6. Test doubles | 15 min | `TicketPurchaseServiceTest` |

Each test class contains one finished example test. Use it as a template.

### 1. Classic unit tests

Class under test: `TicketPriceCalculator`

- 1.1 `chargesServiceFeeOncePerOrder`: several adults, the fee is added only once.
- 1.2 `acceptsTwentyAttendees`
- 1.3 `rejectsMoreThanTwentyAttendees`: use `assertThrows`.
- 1.4 `rejectsOrderWithoutAttendees`

Things to look at:

- Arrange, Act, Assert: three blocks separated by a blank line.
- Test names describe behavior, not methods.
- Money is a `BigDecimal`. `new BigDecimal("2.0").equals(new BigDecimal("2.00"))` is `false`. That is why the tests use `assertAmount`, which compares with `compareTo`.
- Boundary values: what are the interesting attendee counts?

### 2. Personal discounts

Class under test: `PersonalDiscountPolicy` (2.1 to 2.3) and `TicketPriceCalculator` (2.4)

- 2.1 `studentGetsTwentyPercentDiscount`
- 2.2 `seniorDiscountStartsAtAge65`: test the ages around the boundary. Turn it into a `@ParameterizedTest` with `@CsvSource` instead of writing one test per age.
- 2.3 `seniorStudentGetsOnlyTheHigherDiscount`
- 2.4 `roundsDiscountedTicketPriceToCents` (in `TicketPriceCalculatorTest`): pick a base price whose discounted price has more than two decimals.

Things to look at:

- Equivalence classes: which groups of attendees behave the same? One test per class is enough, plus the boundaries.
- Boundary value analysis: bugs live at `<` versus `<=`.

### 3. Bulk discount

Class under test: `TicketPriceCalculator`

- 3.1 `tenAttendeesGetTenPercentBulkDiscount`
- 3.2 `serviceFeeIsNotDiscounted`
- 3.3 `bulkDiscountAppliesOnTopOfPersonalDiscounts`: a group of 10 with students and seniors.

Things to look at:

- Ten `new Attendee(...)` lines hide what the test is about. Write a small helper (test data builder) such as `adults(8)` or `students(2)` and combine the results.
- Calculate expected values by hand from the specification, never by copying the current output of the code.

### 4. Coverage is not quality

The trainer presents this block live. There is nothing to implement.

- The trainer runs only `CoverageIsNotQualityTest` with coverage. Almost every line of the `pricing` package turns green.
- Look at the test. What does it cover, and what does it actually check?
- The trainer then changes the production code live. Which tests turn red: `CoverageIsNotQualityTest`, or the tests from blocks 1 to 3?

Things to look at:

- Coverage shows which code ran, not which behavior is checked.
- A better question than "is this line covered?" is "which test turns red if this line breaks?". Mutation testing tools such as PIT automate exactly that.

### 5. Integration test

Class under test: the whole application via `POST /tickets`. The server starts on a random free port before the tests (`TicketServer.start(0)`); `postTickets` and `toJson` help you send requests.

- 5.1 `purchasesTicketsForKnownEvent`: check status code, `Content-Type` and the total. Read the response with `JSON_MAPPER.readTree(response.body())` and navigate with `.at("/price/total")`.
- 5.2 `returnsNotFoundForUnknownEvent`
- 5.3 `returnsBadRequestForOrderWithoutAttendees`
- 5.4 `returnsBadRequestForMalformedJson`

Things to look at:

- What does this test find that the unit tests cannot?
- Why port `0` instead of `8080`?
- Compare the runtime with the unit tests. Why are integration tests run separately (`mvn verify`)?

### 6. Test doubles

Class under test: `TicketPurchaseService`, isolated from its repositories with Mockito.

- 6.1 `storesThePurchase`: `verify` that the purchase is saved.
- 6.2 `rejectsUnknownEvent`: stub `findById` to return `Optional.empty()`.
- 6.3 `storesNothingForUnknownEvent`: `verifyNoInteractions`.
- 6.4 `storedPurchaseCanBeFoundWithInMemoryFakes`: build the service with `InMemoryEventRepository` and `InMemoryPurchaseRepository` instead of mocks.

Things to look at:

- Stub (returns prepared answers), mock (verifies interactions), fake (simple working implementation).
- Stub queries, verify commands. Verifying `findById` adds nothing.
