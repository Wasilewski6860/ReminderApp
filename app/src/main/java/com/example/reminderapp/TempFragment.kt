package com.example.reminderapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.reminderapp.databinding.ReminderCreaterScreenBinding

class TempFragment(private val actContext: Context) : Fragment() {

    private lateinit var binding: ReminderCreaterScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReminderCreaterScreenBinding.inflate(inflater, container, false)

        val testColors = listOf(
            SpinnerColor(ContextCompat.getColor(actContext, R.color.red)),
            SpinnerColor(ContextCompat.getColor(actContext, R.color.green)),
            SpinnerColor(ContextCompat.getColor(actContext, R.color.blue))
        )

        val testTime = listOf(
            SpinnerTimeText("5 минут"),
            SpinnerTimeText("10 минут"),
            SpinnerTimeText("15 минут"),
            SpinnerTimeText("30 минут"),
            SpinnerTimeText("1 час"),
            SpinnerTimeText("2 часа")
        )

        binding.apply {

            val colorSpinnerAdapter = createColorSpinnerAdapter(testColors)
            reminderColorSpinner.adapter = colorSpinnerAdapter

            val timeSpinnerAdapter = createTimeSpinnerAdapter(testTime)
            reminderTimePickerSpinner.adapter = timeSpinnerAdapter

            reminderTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                reminderSpinnerOrButtonPlaceHolder.visibility = View.VISIBLE
                val checkedButton = binding.root.findViewById<RadioButton>(checkedId)
                when (checkedButton) {
                    periodicReminderRadioButton -> {
                        reminderDateAndTimePickersButton.visibility = View.GONE
                        reminderTimeSpinnerHolder.visibility = View.VISIBLE
                    }
                    onetimeReminderRadioButton -> {
                        reminderTimeSpinnerHolder.visibility = View.GONE
                        reminderDateAndTimePickersButton.visibility = View.VISIBLE
                    }
                }
            }

        }

        return binding.root
    }

    private fun createTimeSpinnerAdapter(
        timesList: List<SpinnerTimeText>
    ): ArrayAdapter<SpinnerTimeText> {
        return object : ArrayAdapter<SpinnerTimeText>(
            actContext,
            R.layout.spinner_time_item,
            timesList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater
                    .from(actContext)
                    .inflate(R.layout.spinner_time_item, parent, false)
                val timeItem = getItem(position)

                val timeView = view.findViewById<TextView>(R.id.spinnerTimeItemView)
                timeView.text = timeItem?.time ?: actContext.getString(R.string.stub)

                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return getView(position, convertView, parent)
            }
        }
    }

    private fun createColorSpinnerAdapter(
        colorsList: List<SpinnerColor>
    ): ArrayAdapter<SpinnerColor> {
        return object : ArrayAdapter<SpinnerColor>(
            actContext,
            R.layout.spinner_color_item,
            colorsList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater
                    .from(actContext)
                    .inflate(R.layout.spinner_color_item, parent, false)
                val colorItem = getItem(position)

                val colorView = view.findViewById<View>(R.id.spinnerItemColorView)
                colorView.setBackgroundColor(colorItem?.color ?: 0)

                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return getView(position, convertView, parent)
            }
        }
    }

}

data class SpinnerColor(
    val color: Int
)

data class SpinnerTimeText(
    val time: String
)