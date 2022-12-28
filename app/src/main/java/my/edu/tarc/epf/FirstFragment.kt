package my.edu.tarc.epf

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import my.edu.tarc.epf.databinding.FragmentFirstBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    // getter, !! means null
    private val binding get() = _binding!!

    var calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonNavThird.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment)
        }

        binding.buttonDOB.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener{view, year, month, day ->
                // set calendar value
                calendar.set(YEAR, year)
                calendar.set(MONTH, month)
                calendar.set(DAY_OF_MONTH, day)

                // update button text attribute
                val dateFormat = "dd/MM/yyyy"
                val standardFormat = SimpleDateFormat(dateFormat, Locale.UK)
                binding.buttonDOB.text = standardFormat.format(calendar.getTime())
            }

            DatePickerDialog(it.context,
                dateSetListener,
                calendar.get(YEAR),
                calendar.get(MONTH),
                calendar.get(DAY_OF_MONTH)
            ).show()
        }

        binding.buttonCalculate.setOnClickListener {
            val endDate = getInstance()
            val age = daysBetween(calendar, endDate).div(365).toInt()
            val basicSaving = binding.editTextSaving.text.toString().toFloat()

            // determine the allowable investment amount
            val investment = calculateInvestmentAmount(age, basicSaving)
            binding.textViewEligibleAmount.text = "RM" + String.format("%.2f", investment)
        }
    }

    private fun calculateInvestmentAmount(age: Int, basicSaving: Float): Float {
        var amount: Float = 0.0f
        var minBasicSaving: Int = 0

        when (age) {
            in 16..20 -> {
                minBasicSaving = 5000
            }
            in 21..25 -> {
                minBasicSaving = 14000
            }
            in 26..30 -> {
                minBasicSaving = 29000
            }
            in 31..35 -> {
                minBasicSaving = 50000
            }
            in 36..40 -> {
                minBasicSaving = 78000
            }
            in 41..45 -> {
                minBasicSaving = 116000
            }
            in 46..50 -> {
                minBasicSaving = 165000
            }
            in 51..55 -> {
                minBasicSaving = 228000
            }
        }
        if (basicSaving > minBasicSaving) {
            amount = (basicSaving - minBasicSaving) * 0.3.toFloat()
        }

        return amount
    }

    fun daysBetween(startDate: Calendar, endDate: Calendar): Long {
        val date = startDate.clone() as Calendar
        var daysBetween: Long = 0
        while (date.before(endDate)) {
            date.add(DAY_OF_MONTH, 1)
            daysBetween++
        }
        return daysBetween
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}