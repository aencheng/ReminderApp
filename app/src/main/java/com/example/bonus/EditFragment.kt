package com.example.bonus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.bonus.databinding.FragmentEditBinding
import java.util.Calendar

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val view = binding.root
        val reminderId = EditFragmentArgs.fromBundle(requireArguments()).reminderId

        val application = requireNotNull(this.activity).application
        val dao = ReminderDatabase.getInstance(application).reminderDao

        val viewModelFactory = EditViewModelFactory(reminderId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.time.text != "Pick Time" && binding.date.text != "Pick Date") {
                        viewModel.newDate = binding.date.text.toString()
                        viewModel.newTime = binding.time.text.toString()
                    }
                    viewModel.updateReminder()
                }
            })


        // navigates back to taskFragment on data change.
        viewModel.navigateToReminder.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_editFragment_to_tasksFragment)
                viewModel.onNavigatedToReminder()
            }
        })

        val pickTimeBtn = binding.time
        pickTimeBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

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

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    pickDateBtn.text = "${selectedMonth + 1}/$selectedDayOfMonth/$selectedYear"
                },
                year,
                month,
                dayOfMonth
            )

            datePickerDialog.show()
        }

        viewModel.reminder.observe(viewLifecycleOwner) { reminder ->
            pickDateBtn.text = if (reminder.date.isNullOrEmpty()) {
                "Pick Date"
            } else {
                reminder.date
            }
        }

        viewModel.reminder.observe(viewLifecycleOwner) { reminder ->
            pickTimeBtn.text = if (reminder.time.isNullOrEmpty()) {
                "Pick Time"
            } else {
                reminder.time
            }
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}