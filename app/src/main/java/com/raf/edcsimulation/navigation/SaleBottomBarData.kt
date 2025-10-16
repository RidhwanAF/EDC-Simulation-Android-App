package com.raf.edcsimulation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.raf.edcsimulation.navigation.routes.SaleRoutes

data class SaleBottomBarData(
    val title: String,
    val icon: ImageVector,
    val activeIcon: ImageVector,
    val route: SaleRoutes,
)

val listSaleBottomBarMenu = listOf(
    SaleBottomBarData(
        title = "Sale",
        icon = Icons.Outlined.PointOfSale,
        activeIcon = Icons.Default.PointOfSale,
        route = SaleRoutes.Sale
    ),
    SaleBottomBarData(
        title = "History",
        icon = Icons.Outlined.History,
        activeIcon = Icons.Default.History,
        route = SaleRoutes.History
    ),
    SaleBottomBarData(
        title = "Settlement",
        icon = Icons.Outlined.NoteAlt,
        activeIcon = Icons.Default.NoteAlt,
        route = SaleRoutes.Settlement
    ),
)