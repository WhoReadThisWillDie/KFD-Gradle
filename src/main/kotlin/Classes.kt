sealed class A {
    open class A1 : A()
    class A2 : A()
    data class A3(val property: String) : A()
}

abstract class Test() {
    fun method1() {

    }

    fun method2() {

    }

    fun method3() {

    }
}

class Class1 : Test() {

}

final class Class2 : Test() {

}