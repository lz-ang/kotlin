package test

actual class Foo actual constructor() {
    constructor(s: String): this(0)
}

fun test() {
    Foo("1")
    Foo(s = "1")
    Foo()
    Foo()
}