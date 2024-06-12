//    private fun getKeyByValue(map: Map<Long, String>, value: String): Long? {
//        return map.entries.firstOrNull { it.value == value }?.key
//    }
//
//    /** Use that in new creator fragment */
//    // Temp date and time picker variant for one time reminder
//    private fun showDateAndTimePickers() {
//        val formatDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
//        val getDate = Calendar.getInstance()
//
//        // Date Picker
//        val datePicker = DatePickerDialog(
//            requireContext(),
//            { _, year, monthOfYear, dayOfMonth ->
//                val selectedDate = Calendar.getInstance()
//                selectedDate.set(Calendar.YEAR, year)
//                selectedDate.set(Calendar.MONTH, monthOfYear)
//                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//                // Time Picker
//                val timePicker = TimePickerDialog(
//                    requireContext(),
//                    { _, hourOfDay, minute ->
//                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                        selectedDate.set(Calendar.MINUTE, minute)
//
//                        // Format and display the selected date-time
//                    },
//                    getDate.get(Calendar.HOUR_OF_DAY),
//                    getDate.get(Calendar.MINUTE),
//                    true // set true for 24-hour format
//                )
//                timePicker.show()
//            },
//            getDate.get(Calendar.YEAR),
//            getDate.get(Calendar.MONTH),
//            getDate.get(Calendar.DAY_OF_MONTH)
//        )
//        datePicker.show()
//    }
//
//}