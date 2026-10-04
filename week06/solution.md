## Exercise 6.1) 

---

File: CasHistogram

### 6.1.1) 

1. Our class state is essentially our array bins, which is secured behind private and getters, so it can't escape.
2. We initialize the histogram with default values set in the constructor to address potential visibility issues. Also, access to the mutable bins is secured behind lock-free CAS operations. We have span and the array object bins being immutable, with the content of bins being mutable.

---

### 6.1.2) 

Yes, we execute getAndClear() in the same way (CAS operation) as with increment(), thus ensuring atomicity.

---

### 6.1.3)

See TestHistogram.java

---

## Exercise 6.2¨

### 6.2.1)



---


