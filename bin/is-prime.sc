#!/usr/bin/env amm
import $ivy.`org.apache.commons:commons-math3:3.4.1.redhat-3`

@main def check(n: Int) =
  org.apache.commons.math3.primes.Primes.isPrime(n)
