package com.example.moneymanger


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Transaction

/*
Now we need to find a way to provide the binding from the date set to the views that are displayed within the recyclerview
following the transaction layout mode we just created this is a job of recycle view adapter
 */
/*
So the adapter class needs to implement three Methods for it to work in properly

      The recyclerview invoke it to create a new view-holder but does not fill in the data
      we have the onbindviewholder the recyclerview calls it to associate a viewholder with
      a data instance and fill in each field and the get item account which return size of data set of recycler
      view uses it to determine when to stop displaying  items

 */
class TransactionAdapter(private var transaction: List<com.example.moneymanger.Transaction>):
    RecyclerView.Adapter<TransactionAdapter.TransacationHolder>() {
    /*
    This class needs to extend the RecyclerView.Adapter class
    So this class needs a ViewHolder so we need to create this ViewHolder to be able to use it
    wait what is a ViewHolder ??

    -->So ViewHolder is kotlin class object that is used to map the transaction Layout we build to the recycleview
    so it knows what our transaction layout contains and how to set values like the lable and the amount and how
    to interact with it so let's define a new class
        */
    class TransacationHolder(view: View):RecyclerView.ViewHolder(view) {
        /*
        So this class extend the RecyclerviewHolder and connect these textview to these
        these variable so here we find a variable called lable
         */
        val label: TextView = view.findViewById(R.id.label)
        val amount: TextView = view.findViewById(R.id.amount)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacationHolder {
        /*
         we have the parents and the view type  so we won't use we won't need this variable because we
         only have one viewholder  type so it doesn't matter but if you had multiple view types so each one will have
         a view type number and then the viewholder knows  which viewholder use
         */
        /*
        In this clas we want to inflate our transaction layout and create a transactino view holder and return it

         */
        val view=LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent,false)
        return TransacationHolder(view)
        // Now this function is called to create transaction view holder and this view holder is used to bind these values to the textview
        // in the transaction layout and later we'll use this transaction holder to set the label and amount values and the colors
    }
    override fun onBindViewHolder(holder: TransacationHolder, position: Int) {
        /*
        Which is the position of the item in the transaction list
        So remember combined viewholder is called to associate viewholder with data instance and fill in each field so first of all let's
        get our transaction
         */
        val transaction: com.example.moneymanger.Transaction =transaction[position]
        val context:Context=holder.amount.context // it is basically the current state of the activity or the application it let's new created objects like this one understand what has been done until
        if (transaction.amount>=0){
            holder.amount.text="+$%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green))
        }
        else{
            holder.amount.text="-$%.2f".format(Math.abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.red))
        }
        holder.label.text=transaction.label
    }
    override fun getItemCount(): Int {
        return transaction.size
    }
    fun setData(transactions: List<com.example.moneymanger.Transaction>){
        this.transaction=transactions
        notifyDataSetChanged()
    }

}