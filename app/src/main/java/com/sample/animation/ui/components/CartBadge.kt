package com.sample.animation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                    AnimatedContent(
                        targetState = count,
                        transitionSpec = {
                            (slideInVertically { it } + fadeIn(animationSpec = tween(300)) + scaleIn()) togetherWith
                                (slideOutVertically { -it } + fadeOut(animationSpec = tween(300)) + scaleOut())
                        },
                        label = "cart_badge_counter"
                    ) { targetCount ->
                        Box(modifier = Modifier.size(16.dp), contentAlignment = Alignment.Center) {
                            Text(text = "$targetCount", style = MaterialTheme.typography.labelSmall)
                        }
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