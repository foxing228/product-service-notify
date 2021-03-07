Task (BackToStock)
Our customer AmazingCo asked to let users to subscribe products that are out of stock.
The idea is to notify customers when the product back in stock.
At the same time they have already in place premium users and also some users may have
priority for particular product categories.
Rules:
- the premium user has a high priority
- users elder than 70 years old has:
- high priority for medical products
- medium priority for all categories
- FIFO for all the rest
Criteria (from high to low)
Correctness
Code quality, readability, error handling, following the best practice
Unit tests
Flexibility
Pattern
Notes:
you don't need DB
you don't need UI, API, etc
you don't worry about concurrency
focus on domain
You may use:
java
Gradle or maven
Imagine that this is not a test task, but a real production task