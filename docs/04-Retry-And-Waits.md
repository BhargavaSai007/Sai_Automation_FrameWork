# 04 – Retry and Waits

## Policy
- Max retry attempts: **3**
- Log each attempt: **Yes** (built into `RetryUtils`)

## Where retry is applied
In `BasePage`:
- wait for visible
- wait for clickable
- click
- type
- read text

So you satisfy the requirement: “retry for all waits (and core UI actions)”.

## What retry solves
- flakiness
- intermittent DOM refresh
- timing delays

## Wait style
- Explicit waits: `WebDriverWait` + `ExpectedConditions`
- **Note:** `DriverManager` sets an implicit wait too. In general, projects prefer implicit wait = 0 and rely only on explicit waits, but your current design is documented.

