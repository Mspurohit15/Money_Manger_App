package com.example.moneymanger

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
By default it's class so we change into data class
Data class : Just hold data and does not encapsulate  any  additional logic

In kotlin we have data class so we use
data class it's take parameters
 */
@Entity(tableName = "transactions") // If we don't give name that table name is equal to data class
data class Transaction(
    @PrimaryKey(autoGenerate =true) val id: Int,
    val label: String,
    val amount: Double,
    val description: String) {


    }

    /*
we're done we don't need yo specify anything else no copy or to-string method the
data class is taking care of that

--> Let's create a layout for transaction item
*/
