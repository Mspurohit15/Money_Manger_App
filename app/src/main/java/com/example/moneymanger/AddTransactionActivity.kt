package com.example.moneymanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddTransactionActivity : AppCompatActivity() {

    val addTransactionbtn=findViewById<Button>(R.id.addTransactionBtn)as Button
    val labelInput=findViewById<View>(R.id.labelInput)as TextInputEditText
    val amountInput=findViewById<View>(R.id.amountInput)as TextInputEditText
    val descriptionInput=findViewById<View>(R.id.descriptionInput)as TextInputEditText
    val labelLayout=findViewById<View>(R.id.labelLayout)as TextInputEditText
    val amountLayout=findViewById<View>(R.id.amountLayout)as TextInputEditText
    val closebtn =findViewById<Button>(R.id.closebtn) as ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        labelInput.addTextChangedListener {
            if (it!!.count()>0)
                labelLayout.error=null
        }
        amountInput.addTextChangedListener {
            if (it!!.count()>0)
                amountLayout.error=null
        }

        addTransactionbtn.setOnClickListener{
            val label=labelInput.text.toString()
            val description=descriptionInput.text.toString()
            val amount =amountInput.text.toString().toDoubleOrNull()
            if(label.isEmpty()){
                labelLayout.error="Please enter a valid label "
            }
            else if(amount==null){
                amountLayout.error="Please enter a valid amount"
            }
            else {
                val transaction=Transaction(0,label,amount,description)
                insert(transaction)
            }
        }
        closebtn.setOnClickListener{
            finish()
        }
    }
    private fun insert(transaction: Transaction){
        val  db:AppDatabase = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transaction").build()
        GlobalScope.launch{
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}