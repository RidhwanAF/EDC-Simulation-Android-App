package com.raf.edcsimulation.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raf.edcsimulation.core.data.utils.CurrencyFormatter.toRupiah
import com.raf.edcsimulation.core.presentation.theme.approvedColor
import com.raf.edcsimulation.core.presentation.theme.onApprovedColor
import com.raf.edcsimulation.history.domain.models.HistoryData

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    historyData: HistoryData,
) {
    val isApproved = historyData.status.equals("approved", true)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        HistoryItem(
            label = historyData.id.toString(),
            showStartDivider = false,
            showEndDivider = true,
            centerAligned = true,
            modifier = Modifier.weight(0.15f)
        )
        HistoryItem(
            label = historyData.merchant,
            showStartDivider = false,
            modifier = Modifier.weight(0.2f)
        )
        HistoryItem(
            label = historyData.amount.toRupiah(),
            modifier = Modifier.weight(0.25f)
        )
        HistoryItem(
            label = historyData.cardNumber,
            modifier = Modifier.weight(0.2f)
        )
        HistoryItem(
            label = historyData.status,
            color = if (isApproved) approvedColor else Color.Unspecified,
            textColor = if (isApproved) onApprovedColor else Color.Unspecified,
            centerAligned = true,
            modifier = Modifier.weight(0.2f)
        )
    }
}


@Composable
private fun HistoryItem(
    modifier: Modifier = Modifier,
    label: String = "",
    color: Color = Color.Unspecified,
    textColor: Color = Color.Unspecified,
    showStartDivider: Boolean = true,
    showEndDivider: Boolean = false,
    centerAligned: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(color)
            .height(IntrinsicSize.Max)
    ) {
        VerticalDivider(color = if (showStartDivider) MaterialTheme.colorScheme.primary else Color.Transparent)
        Text(
            text = label.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = if (centerAligned || label.isBlank()) TextAlign.Center else TextAlign.Start,
            maxLines = 1,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .basicMarquee()
        )
        VerticalDivider(color = if (showEndDivider) MaterialTheme.colorScheme.primary else Color.Transparent)
    }
}