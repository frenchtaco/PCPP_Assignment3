## Exercise 5.1) 

Note: Run tests using `$ gradle cleanTest test --tests <package>.<test_class>`

---

### 5.1.1) Implement a functional correctness test that finds concurrency errors in the add(Integer element) method in ConcurrentIntegerSetBuggy. Describe the interleaving that your test finds.
---

File: ConcurrentIntegerSet.java

| Step | T1 `add(3)` | T2 `add(7)` | State |
|------|-------------|-------------|-------|
| 1 | inserts node 3 into its bucket | | `size = 5` |
| 2 | | inserts node 7 into its bucket | `size = 5` |
| 3 | reads `size` (5) | | |
| 4 | | reads `size` (5) | |
| 5 | writes `size = 6` | | `size = 6` |
| 6 | | writes `size = 6` | `size = 6` |

The interleaving above highlights a sizing issue, where two threads, T1 and T2, both add values to the set. When doing this concurrently, T2 does not see that the size has been incremented yet by T1, and so we end up in a situation where they both see the size as 5 and increments to 6. 

---
### 5.1.2) Implement a functional correctness test that finds concurrency errors in the remove(Integer element) method in ConcurrentIntegerSetBuggy. Descrfibe the interleaving that your test finds.

---

Same as above, T1 and T2 will again mutate on the same size, resulting in a data race, i.e. unintended behaviour.

---
### 5.1.3) In the class ConcurrentIntegerSetSync, implement fixes to the errors you found in the previous exercises. Run the tests again to increase your confidence that your updates fixed the problems. In addition, explain why your solution fixes the problems discovered by your tests.
---

We introduced the synchronized keyword on the `add()` and `remove()` to ensure mutual exclusion. We have not done it on `size()`, as it will be called after our threads terminate, and because of the termination rule, we didn't think we had to.  


---
### 5.1.4) Run your tests on the ConcurrentIntegerSetLibrary. Discuss the results
---

Here, the tests... pass. Here, a **SkipList** is used, which behaves in a way that is more atomic than the HashSet. While the code is identical, the underlying data structure's actions are atomic to other threads, and therefore, it does not need the lock to ensure that the thing is thread-safe, or that a data race can occur.

---
### 5.1.5) Do a failure on your tests above prove that the tested collection is not thread-safe? Explain your answer.
---

So, if a test fails, it tells us that the `add` and `remove` are not behaving the way that we want, and thus it tells us that it isn't thread-safe (assuming there isn't a mistake in the test)

---
### 5.1.6) Does passing your tests above prove that the tested collection is thread-safe (when only using add() and remove())? Explain your answer.
---

Even though tests are a way for us to look for thread-**un**safety, we cannot simply conclude that thread-safety has occurred based on our tests cases, as we cannot ensure that all possible interleavings have been experimented with.

But I guess we could conclude that mutual exclusion has been ensured via the `synchronized`keyword, and thus that `add` and `remove` access can only happen one at a time.

---

## Exercise 5.2) 

---
### 5.2.1) Let capacity denote the final field capacity in SemaphoreImp. Then, the property above does not hold for SemaphoreImp. Your task is to provide an interleaving showing a counterexample of the property, and explain why the interleaving violates the property
---

This question is a bit strange as the underlying problem to us, does not seem like an interleaving-issue, but rather a vulnerability in the implementation that can be abused, but as long as the program using the semaphore isn't malicious or faulty, it is thread-safe.

To give an answer to what we assume the question is about: if a thread calls release() without having a lock first, it could decrement the state to < 0, which would mean that the next c+1 calls to acquire would succeed and thus having c+1 threads inside the critical section - one more than the allowed amount.

---
### 5.2.2) Write a functional correctness test that can trigger the interleaving you describe in 1. Explain why your test triggers the interlaving
---

Our first test is calling acquire() and then calling release() twice. Obviously a faulty program, but it's closest we can come to an "interleaving"?
