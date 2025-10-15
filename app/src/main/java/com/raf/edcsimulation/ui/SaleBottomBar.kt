package com.raf.edcsimulation.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.raf.edcsimulation.navigation.SaleBottomBarData
import com.raf.edcsimulation.navigation.listSaleBottomBarMenu
import com.raf.edcsimulation.navigation.routes.SaleRoutes

@Composable
fun SaleBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: SaleRoutes = SaleRoutes.Sale,
    onNavigate: (SaleBottomBarData) -> Unit = {},
) {
    val localDensity = LocalDensity.current
    val menuSize = 48
    val currentRouteIndex = listSaleBottomBarMenu.indexOfFirst { it.route == currentRoute }
    val selectedOffset by animateFloatAsState(
        targetValue = with(localDensity) { (currentRouteIndex * menuSize).dp.toPx() },
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = selectedOffset
                    }
                    .size(menuSize.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                listSaleBottomBarMenu.forEach { menu ->
                    IconButton(
                        onClick = {
                            onNavigate(menu)
                        },
                        modifier = Modifier.size(menuSize.dp)
                    ) {
                        Icon(
                            imageVector = if (currentRoute == menu.route) menu.activeIcon else menu.icon,
                            contentDescription = null,
                            tint = if (currentRoute == menu.route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}