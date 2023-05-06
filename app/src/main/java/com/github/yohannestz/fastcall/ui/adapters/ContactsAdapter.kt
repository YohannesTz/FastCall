package com.github.yohannestz.fastcall.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.yohannestz.fastcall.R
import com.github.yohannestz.fastcall.data.model.db.Contact


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactNameTv: TextView = itemView.findViewById(R.id.contacts_name)
        val phoneNumTv: TextView = itemView.findViewById(R.id.phone_num)
    }


    private val differCallback = object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contacts_item_layout,
            parent, false
        )

        return ContactsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.contactNameTv.text = differ.currentList[position].name
        holder.phoneNumTv.text = differ.currentList[position].phoneNumber

        holder.itemView.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${differ.currentList[position].phoneNumber}")
            startActivity(it.context, callIntent, null)
        }
    }

}