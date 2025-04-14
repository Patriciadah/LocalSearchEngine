# Lab 03
> 2025-03-10

> **Main goal**: Component-based thinking
---

## Hour 1:
- Intro to DBMS history
- ORM vs SQL
    - Explain mappings, like 1d light switches to 2d floor map
    - Link to Dependency Inversion
- "Tech stack" is a bad phrase. Learn about layer above and below you.
    - So if the abstraction fails, you can fix your way around it
    - https://www.joelonsoftware.com/2002/11/11/the-law-of-leaky-abstractions/
    - Don't be a React developer. Be a web developer.
        - LAMP (Linux, Apache, MySQL, PHP)
        - MEAN (MongoDB, Express, Angular, Node)
        - etc.
    - Learn about web standards. Learn about the browser platform.


## Hour 2:
- Show Quirks.cpp
    - Link: https://github.com/WebKit/WebKit/blob/main/Source/WebCore/page/Quirks.cpp#L1130
- Chaos Monkey
    - Link to Antifragile
    - Exercise: how much is 5-nines? 4-nines? 3-nines?
- Snippet indent exercise. Explain the real power of Haskell types (runnable UML!)
    - "functional core, imperative shell" architecture


## Hour 3:
- Write Fizz Buzz in 3 ways (simple, 1 buffer, buffered writer)
- Explain the N+1 problem


#### Sources:
- https://www.martinfowler.com/bliki/BlueGreenDeployment.html
- https://googleblog.blogspot.com/2008/08/search-experiments-large-and-small.html
- https://www.slideshare.net/slideshow/culture-1798664/1798664#23
- https://youtu.be/jeRWyYIgiU8?t=176
- https://0.30000000000000004.com
- https://martinfowler.com/bliki/OrmHate.html
- https://matklad.github.io/2021/02/06/ARCHITECTURE.md.html

- Books mentioned:
    - https://en.wikipedia.org/wiki/Antifragile_(book)
    - https://en.wikipedia.org/wiki/Hacker%27s_Delight
    - https://www.amazon.com/Understanding-Software-Addison-Wesley-Professional-Computing/dp/0137589735


# Lab 02
> 2025-03-03

> **Main goal**: Understand GRASP
---


## Hour 1:
- Go through what 'Clean Code' promotes:
    - Link: https://gist.github.com/wojteklu/73c6914cc446146b8b533c0988cf8d29
    - Show counterexamples
- Write a `pluralize` function
    - Rust version
    - TypeScript version
- Show `gap` command
- Refactor method from the lab guide


## Hour 2:
- GRASP exercise


## Hour 3:
- GRASP exercise


#### Sources:
- https://sandimetz.com/blog/2016/1/20/the-wrong-abstraction
- https://qntm.org/clean
- https://www.codecentric.de/en/knowledge-hub/blog/curly-braces
- https://github.com/tigerbeetle/tigerbeetle/blob/fe681fc34729e7afb3ae4ead33d1093ceb68d164/src/constants.zig#L14
- Style:
    - https://cbea.ms/git-commit/
    - https://go.dev/doc/contribute#commit_messages
    - https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716
    - http://number-none.com/blow/john_carmack_on_inlined_code.html
    - https://google.github.io/styleguide/cppguide.html#Exceptions
    - https://github.com/torvalds/linux/blob/master/Documentation/process/coding-style.rst
    - https://testing.googleblog.com/2023/10/improve-readability-with-positive.html
- Big functions:
    - https://github.com/torvalds/linux/blob/master/kernel/fork.c#L2147-L2705
    - https://github.com/gcc-mirror/gcc/blob/master/gcc/c/c-parser.cc#L2306-L3175
- Big comments:
    - https://github.com/golang/go/blob/master/src/math/big/natdiv.go


# Lab 01
> 2025-02-24

> **Main goal**: Understand SOLID
---


## Hour 1: Warm up, important concepts from software engineering
- "Software engineering is programming integrated over time"
- Explained the origins of OOP
    - Simula vs Smalltalk
    - Alan Kay's version
    - Steve Jobs selling objects for the NeXT operating system
- These principles of software design aren't generally applicable
- Style:
    - coding styles
    - why 80 columns
    - case formats
    - why code formatters exist (explain what gofmt does)
    - commit styles
    - version control systems
- Explained why Rust/Golang don't have classes (prefer composition over inheritance)
- Don't try to be Netflix or Google (design for the problem)


## Hour 2:
- Self-check test
- Explained a few items from the test


## Hour 3:
- SOLID live demos
    - S = Single Responsibility Principle
    - O = Open/Closed Principle (OCP)
    - L = Liskov Substitution Principle (LSP)
    - I = Integration Segregation Principle (ISP)
    - D = Dependency Inversion Principle (DIP)


#### Sources:
- https://abseil.io/resources/swe-book/html/ch01.html
- https://overreacted.io/goodbye-clean-code/
- https://www.youtube.com/watch?v=tD5NrevFtbU
- https://www.youtube.com/watch?v=oKg1hTOQXoY (10:00)
- https://www.youtube.com/watch?v=Gk-9Fd2mEnI
- https://simonwillison.net/2024/Jun/17/russ-cox/

- Books mentioned:
    - https://en.wikipedia.org/wiki/Understanding_Media
    - https://en.wikipedia.org/wiki/The_medium_is_the_message
