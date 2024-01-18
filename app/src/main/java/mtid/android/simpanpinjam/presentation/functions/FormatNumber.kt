package mtid.android.simpanpinjam.presentation.functions

import java.text.DecimalFormat

fun formatNumber(number: Long): String {
    try {
        // Convert the input string to a Long and then format it with commas
        val formatter = DecimalFormat("#,###")
        val formattedNumber = formatter.format(number)
        // Replace commas with dots
        return formattedNumber.replace(",", ".")
    } catch (e: NumberFormatException) {
        // Handle the case where the input is not a valid number
        return number.toString()
    }
}