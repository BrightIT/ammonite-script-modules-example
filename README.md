# Scripting remote services with Ammonite

This repo showcases how to script remote services with Ammonite based on
a Northwind-like database. [Insert link to blog post when it's published]

Sample interaction with the repository (note that scripts handle environment
setup/teardown by themselves):

```bash
> git clone $REPO && cd $REPONAME
> # we will now bring up a Docker container with a DB and insert data into it
> cmd/setup.sc
Docker container is up and running
Inserted:
	10 employees
	92 customers
	101 orders
> # Check the last order in the database
> cmd/order.sc last
Last order: Order(10348,5,FAMIA,Familia Arquibaldo,Sao Paulo)
> # Add a new one and see if it was inserted
> cmd/order.sc place --employee 2 --customer ANTON --shipName Boat --shipCity London
> cmd/order.sc compare
Last local order (10348) is 2 order(s) behind
> cmd/order.sc last
Last order: Order(10349,2,ANTON,Boat,London)
> # Finally, check the status of the Docker container and bring it down
> cmd/docker.sc status
Docker container is up and running
> cmd/docker.sc down
```

`cmd/repl` can be used to start an Ammonite REPL with all scripts imported:

```
> cmd/repl
Welcome to the Ammonite Repl 1.1.2
(Scala 2.12.6 Java 1.8.0_144)
If you like Ammonite, please support our development at www.patreon.com/lihaoyi
@ order.place(
    employee = 2, customer = "ANTON", shipName = "Boat", shipCity = "London"
  )

@ order.compare()
Last local order (10347) is 1 order(s) behind

@ order.last()
Last order: Order(10348,2,ANTON,Boat,London)
```

`bin/try-auth.sc` shows how to add custom repositories with the help of
[coursier+auth](https://github.com/BrightIT/coursier-plus-auth):

```
> bin/try-auth.sc
bash-4.4$ bin/is-prime.sc 5
true
bash-4.4$ cat bin/is-prime.sc
#!/usr/bin/env amm
import $ivy.`org.apache.commons:commons-math3:3.4.1.redhat-3`

@main def check(n: Int) =
  org.apache.commons.math3.primes.Primes.isPrime(n)
bash-4.4$ ^D
```

# Notable points

1. All scripts are executable

    They begin with `#!/usr/bin/env amm` shebang. This makes running them simple
    and also makes `bin/try-auth.sh` possible.

2. Every script has at least one `@main` entrypoint

    The alternative would be to keep the main script code as top-level code.
    This would make it impossible to re-run the scripts inside REPL or to use
    the scripts from other scripts, so instead `@main` is used everywhere.

3. All dependencies are kept in a single file

    `cmd/modules/dependencies.sc` contains all the dependencies. This makes
		resolving dependency conflicts work exactly the same between all scripts
		and prevents including the same library twice in a different version.

		Also, since the dependencies are kept in a variable, they can be used
		from scripts to, for example, generate a POM file.
