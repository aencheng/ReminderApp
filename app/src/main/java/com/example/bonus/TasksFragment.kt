package com.example.bonus
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bonus.databinding.FragmentTasksBinding
import com.google.android.material.appbar.MaterialToolbar



class TasksFragment : Fragment() {

    val TAG = "TasksFragment"
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = ReminderViewModelFactory(dao)
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(ReminderViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        fun reminderClicked(reminderId: Long) {
            viewModel.onReminderClicked(reminderId)
        }

        fun yesPressed(reminderId: Long) {
            Log.d(TAG, "in yesPressed(): reminderId = $reminderId")
        }

        fun deleteClicked(reminderId: Long) {
            ConfirmDeleteDialogFragment(reminderId, ::yesPressed).show(
                childFragmentManager,
                ConfirmDeleteDialogFragment.TAG
            )
        }

        val adapter = ReminderItemAdapter(::reminderClicked, ::deleteClicked)

        binding.remindersList.adapter = adapter

        viewModel.reminders.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToReminder.observe(viewLifecycleOwner, Observer { reminderId ->
            reminderId?.let {
                val action = TasksFragmentDirections.actionTasksFragmentToEditFragment(reminderId)
                this.findNavController().navigate(action)
                viewModel.onReminderNavigated()
            }
        })

        val toolbar: MaterialToolbar = view.findViewById(R.id.materialToolbar)
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)


        return view
    }

    // send email process
    private val sendEmail =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Feedback", "sent")
            } else {
                Log.d("Feedback", "failed")
            }
        }

    // send email and set up texts
    private fun sendFeedback() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"

        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("cheng15@iu.edu"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Enter feedback here")

        val chooser = Intent.createChooser(emailIntent, "Send email with ")
        sendEmail.launch(chooser)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    private fun aboutPage() {
        val url = "https://luddy.indiana.edu/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val viewModel: ReminderViewModel by activityViewModels()

        when (item.itemId) {
            R.id.feedback -> {
                sendFeedback()
                return true
            }
            R.id.add -> {
                val action = TasksFragmentDirections.actionTasksFragmentToReminderFragment()
                this.findNavController().navigate(action)
                Log.d("add", "button clicked")
                return true
            }
            R.id.about -> {
                aboutPage()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}