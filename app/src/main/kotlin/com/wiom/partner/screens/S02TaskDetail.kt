@file:OptIn(ExperimentalFoundationApi::class)
package com.wiom.partner.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage

private val DarkBg = Color(0xFF201726)

@Composable
fun S02TaskDetail(onStartFlow: () -> Unit, onBack: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // ── Dark header: 56dp ──
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).background(Header)
                .shadow(3.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.ArrowBack, "Back", tint = Color(0xFFFAF9FC),
                modifier = Modifier.size(24.dp).clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("मेरे काम (2)", color = Color(0xFFFAF9FC), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // ── Dark bg body with curved bottom ──
        Box(modifier = Modifier.fillMaxSize().background(Bg)) {
            // Dark bg with convex curved bottom (dark bulges downward)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .drawBehind {
                        // Main dark rectangle
                        drawRect(color = DarkBg, size = Size(size.width, size.height - 48.dp.toPx()))
                        // Convex bottom: dark ellipse extending below the rectangle
                        drawOval(
                            color = DarkBg,
                            topLeft = Offset(-size.width * 0.15f, size.height - 96.dp.toPx()),
                            size = Size(size.width * 1.3f, 96.dp.toPx())
                        )
                    }
            )

            // ── Vertical pager (snap-scroll cards) ──
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp),
                pageSpacing = 24.dp
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (page == 0) {
                        TaskCard(
                            title = "नया सेटअप",
                            name = "Himanshu Singh",
                            address = "G 69, Mata mandir, 3rd street, Ghaziabad Uttar Pradesh",
                            timeLabel = "10:00am",
                            deadlineLabel = "12 January तक काम पूरा करें",
                            ctaText = "काम पूरा करें",
                            isLate = false,
                            onCta = onStartFlow
                        )
                    } else {
                        TaskCard(
                            title = "नया सेटअप",
                            name = "Sanjay Singh Chauhan",
                            address = "B 12, Shiv Colony, Sector 5, Noida Uttar Pradesh",
                            timeLabel = "10:00am",
                            deadlineLabel = "आप लेट हो गए हो",
                            ctaText = "जल्दी काम पूरा करें",
                            isLate = true,
                            onCta = onStartFlow
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    title: String, name: String, address: String,
    timeLabel: String, deadlineLabel: String, ctaText: String,
    isLate: Boolean, onCta: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.2f))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFAF9FC))
            .border(1.dp, Brand, RoundedCornerShape(16.dp))
    ) {
        // ── Pink header: title + step ring ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandLight)
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Pri, lineHeight = 28.sp)
            // Step ring
            Box(
                modifier = Modifier.size(36.dp).border(2.5.dp, BrandSec, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("0/8", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Brand)
            }
        }
        // Divider
        Box(Modifier.fillMaxWidth().height(1.dp).background(Brand))

        // ── Content ──
        Column(modifier = Modifier.padding(16.dp)) {
            // Name row
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Person, null, tint = Hint, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text(name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri, modifier = Modifier.weight(1f))
                Icon(Icons.Rounded.Call, null, tint = Brand, modifier = Modifier.size(24.dp))
            }

            // Address row
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Rounded.LocationOn, null, tint = Hint, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text(address, fontSize = 16.sp, color = Pri, lineHeight = 24.sp, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            // ── Cricket timeline ──
            Box(modifier = Modifier.fillMaxWidth().height(32.dp)) {
                if (!isLate) {
                    // Fresh: ball at left, bat at right
                    AssetImage("img/cricket_ball.webp", modifier = Modifier.size(24.dp).align(Alignment.BottomStart))
                    AssetImage("img/cricket_bat.webp", modifier = Modifier.size(32.dp).align(Alignment.BottomEnd))
                } else {
                    // Late: bat(left) ball(mid) wicket(right)
                    AssetImage("img/bat_only.webp", modifier = Modifier.size(32.dp).align(Alignment.BottomStart).offset(x = 0.dp))
                    AssetImage("img/cricket_ball.webp", modifier = Modifier.size(24.dp).align(Alignment.BottomEnd).offset(x = (-40).dp, y = (-4).dp))
                    AssetImage("img/wicket.webp", modifier = Modifier.size(24.dp, 32.dp).align(Alignment.BottomEnd))
                }
            }
            Spacer(Modifier.height(4.dp))

            // Progress bar
            Box(
                modifier = Modifier.fillMaxWidth().height(6.dp)
                    .clip(RoundedCornerShape(3.dp)).background(BorderDivider)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(if (!isLate) 1f else 0.18f)
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (!isLate) Pos else Neg)
                )
            }
            Spacer(Modifier.height(8.dp))

            // Time labels
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(timeLabel, fontSize = 12.sp, color = Pri)
                Text(
                    deadlineLabel, fontSize = 12.sp, color = Pri,
                    fontWeight = if (isLate) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }

        // ── CTA bar: full-width pink ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brand)
                .clickable { onCta() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(ctaText, color = Color(0xFFFAF9FC), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
