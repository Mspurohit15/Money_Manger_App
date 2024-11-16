package com.example.moneymanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
/*
        The recyclerview needs a few things it needs an adapter to know how it should look and behave and what kind of display
        Layout Manager to tell it how it should position items and when to recycle one of them that are off screen
        in our case we want linear layout manager and by default it's vertical
       Let's created data list
       lateinit = That don't worry kotlin this variable will be initialized but later not now
         */
class MainActivity : AppCompatActivity() {

    private lateinit var deletedTransaction: Transaction
    private lateinit var oldtransactions :List<Transaction>
    private lateinit var transactions :List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var LinearLayoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Need to initialize the transaction adapter so transaction
        transactions= arrayListOf()
        transactionAdapter= TransactionAdapter(transactions)
        LinearLayoutManager= LinearLayoutManager(this )

        db= Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transaction").build()
        // Now i need you to remember the id that we gave to out recycle view
        val recyclerview=findViewById<View>(R.id.recyclerview)as RecyclerView
        recyclerview.apply {
            adapter=transactionAdapter
            layoutManager=LinearLayoutManager
        }
        //swipe to remove
        val itemTouchHelper=object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }
        val swipeHelper= ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerview)

        val addbtn=findViewById<Button>(R.id.addBtn)as Button
        addbtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchAll(){
        GlobalScope.launch {

            transactions=db.transactionDao().getAll()
            /*
            We need to update recyclerview and Dashboard but this two function interact with UI  Thread
           and we can't change ui thread from background thread . so we should call them UI thread
             */
            runOnUiThread{
                updatedDashboard()
                transactionAdapter.setData(transactions)
            }
        }
    }
    private fun updatedDashboard(){
        /*
        Here  transactions.map { it.amount }.sum() --> Basically what this code here does it goes over all the elements in the
        transaction and only takes the amount and added to empty list . A new list with only the amount
         */
        val totalAmount = transactions.map { it.amount }.sum()
        /*
        transactions.filter { it.amount>0}.map { it.amount }.sum()
        it's little bit trickier so i want only the amount that are greater or equals to zero actually , greater than zero
        therefore we have to filter them then map and sum filtering is getting only the
        values wr want so let's use
         */
        val budgetAmount =transactions.filter { it.amount>0}.map { it.amount }.sum()
        val expenseAmount=totalAmount-budgetAmount
        //Now we have value to map them to the Layout

        val balance =findViewById<View>(R.id.balance) as TextView
        balance.text="$ %.2f".format(totalAmount)
        val budget =findViewById<View>(R.id.budget) as TextView
        budget.text="$ %.2f".format(budgetAmount)
        val expense =findViewById<View>(R.id.expense) as TextView
        expense.text="$ %.2f".format(expenseAmount)
    }
    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction=transaction
        oldtransactions=transactions
        GlobalScope.launch {
            db.transactionDao().delete(transaction)

            transactions=transactions.filter { it.id!=transaction.id }
            runOnUiThread{
                updatedDashboard()
            }

        }
    }
    override fun onResume() {
        super.onResume()
        fetchAll()
    }

}