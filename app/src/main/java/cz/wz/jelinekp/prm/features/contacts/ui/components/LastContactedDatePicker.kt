package cz.wz.jelinekp.prm.features.contacts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LastContactedDatePicker(
    initialDateTime: LocalDateTime,
    onValueChange: (lastContacted: LocalDateTime) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Pre-select a date - converting initialDateTime to milliseconds
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateTime.toEpochSecond(
                ZoneOffset.UTC
            ) * 1000
        )
        DatePicker(
            state = datePickerState,
            modifier = Modifier,
        )
        val date = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
            ZoneId.systemDefault()
        )
        onValueChange(date)
    }
}
