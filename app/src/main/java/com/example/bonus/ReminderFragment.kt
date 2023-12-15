package com.example.bonus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bonus.databinding.FragmentReminderBinding
import java.util.Calendar


// Fragment for creating Reminder
class ReminderFragment : Fragment() {
    val TAG = "ReminderFragment"
    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    // create view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = ReminderViewModelFactory(dao)
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(ReminderViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // handler for resetting time button values for new Reminders
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
//                    val name = binding.reminderName.text.toString()
//                    val description = binding.description.text.toString()
//                    viewModel.newReminderName = name
//                    viewModel.newDesc = description
                    if (binding.time.text != "Pick Time" && binding.date.text != "Pick Date") {
                        viewModel.newDate = binding.date.text.toString()
                        viewModel.newTime = binding.time.text.toString()
                    }
                    viewModel.addReminder() // Execute your update logic here
                    findNavController().navigateUp() // Navigate up
                }
            })

        val pickTimeBtn = binding.time
        pickTimeBtn.setOnClickListener {
            val c = Calendar.getInstance()
            // get hour and minute of the day
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            // selected hour/minute
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    if (selectedMinute.toString().length == 1) {
                        pickTimeBtn.text = "$selectedHour:0$selectedMinute"

                    } else {
                        pickTimeBtn.text = "$selectedHour:$selectedMinute"
                    }

                },
                hour,
                minute,
                true // 24-hour time format
            )

            timePickerDialog.show()
        }

        val pickDateBtn = binding.date
        pickDateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            // update text
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    pickDateBtn.text = "${selectedMonth + 1}/$selectedDayOfMonth/$selectedYear"
                },
                year,
                month,
                dayOfMonth
            )
            // show picked date
            datePickerDialog.show()
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}