package org.example
import java.util.Date

abstract class PaymentMethod
{
    abstract fun pay(fee: Double): Boolean
}

class CashPayment(private var availableAmount: Double) : PaymentMethod(){

    override fun pay(fee: Double): Boolean{
        return if(availableAmount >= fee)
        {
            availableAmount -= fee
            true
        } else {
            false
        }
    }
}

interface Amount{
    fun getAmount(): Double
}

interface AccountUpdate{
    fun updateAmount(value: Double): Boolean
}

class BankAccount(
    var availableAmount: Double = 0.0,
    var carNumber: String = "",
    var expirationDate: Date = Date(),
    var cvvCode: Int = 0,
    var userName: String = ""

) : Amount, AccountUpdate {
    override fun updateAmount(value: Double): Boolean {
        return if (availableAmount + value >= 0) {
            availableAmount += value
            true
        } else {
            false
        }
    }

    override fun getAmount(): Double {
        return availableAmount
    }

    class CardPayment(private var bankAccount: Amount) : PaymentMethod() {
        override fun pay(fee: Double): Boolean {
            return if (bankAccount.getAmount() >= fee) {
                (bankAccount as AccountUpdate).updateAmount(-fee)
            } else {
                false
            }
        }
    }
}

//Rponsabilitate unica: separam datele contului bancar si updatarea sumei in doua clase diferite
//Deschis/Inschis: prin transformarea interfetei PaymentMethod in clasa abstracta pt ca atunci putem adauga si alte metode de pplata de exemplu fara a modifica codul existent
//Dependenta inversa: prin abstractizarea clasei AccountUpdate, deoarece in felul asta deoarece clasele nu mai depind acum de o clasa concreta cum era inainte AccountUpdate
//Liskov: prin faptul ca CardPayment nu mai depinde de BankAccount ci de interfata AccountUpdate -> CardPayment poate functiona cu orice clasa care implementeaza interfata
//Separarea Interfetelor: prin separea functiilor getAmount si updateAmount in dou interfete diferite, astfel clase care implementeaza doar una dintre functii nu vor fi obligate sa o implementeze si pe a doua
