package com.sample.animation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartBadge(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // =========================================================================
    // TODO: 3.1 Content animation for number transitions with slide, fade, and scale.
    // Wrap the count display inside `AnimatedContent(targetState = count, label = "CartCount")`.
    // Define transitionSpec using `togetherWith`:
    //   (slideInVertically { it } + fadeIn(tween(300)) + scaleIn()) togetherWith
    //   (slideOutVertically { -it } + fadeOut(tween(300)) + scaleOut())
    // Make sure to render the lambda value { value -> Text(text = "$value", ...) }
    // =========================================================================
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                    Box(modifier = Modifier.size(16.dp), contentAlignment = Alignment.Center) {
                        Text(text = "$count", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        },
        modifier = modifier
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart"
            )
        }
    }
}
