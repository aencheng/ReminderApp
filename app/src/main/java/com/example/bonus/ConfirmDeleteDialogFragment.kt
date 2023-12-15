package com.example.bonus

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

class ConfirmDeleteDialogFragment(val reminderId: Long, val clickListener: (reminderId: Long) -> Unit) :
    DialogFragment() {
    val TAG = "ConfirmDeleteDialogFragment"

    interface myClickListener {
        fun yesPressed()
    }


    var listener: myClickListener? = null

    /**
     * Creates the dialog box for the yes no box.
     * Also contains the viewmodel and dao information allowing the viewmodel to delete
     * a specific item marked with taskID.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete the reminder?")
            .setPositiveButton("Yes") { _, _ ->
                clickListener(reminderId)
                val application = requireNotNull(this.activity).application
                val dao = ReminderDatabase.getInstance(application).reminderDao
                val viewModelFactory = ReminderViewModelFactory(dao)
                val viewModel =
                    ViewModelProvider(this, viewModelFactory)[ReminderViewModel::class.java]
                viewModel.deleteNote(reminderId)

            }
            .setNegativeButton("No") { _, _ -> }

            .create()

    companion object {
        const val TAG = "ConfirmDeleteDialogFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as myClickListener
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

}